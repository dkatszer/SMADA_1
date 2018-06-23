package net.networks;

import net.activationfunctions.ActivationStrategy;
import net.activationfunctions.SigmPositive;
import net.layers.InputLayer;
import net.layers.LayersConnector;
import net.layers.NetLayer;
import net.learning.LearningRateRandomRange;
import net.neurons.FixedNeuron;
import net.neurons.NetNeuron;
import net.trainings.OfflineTraining;
import net.trainings.OnlineTraining;
import net.trainings.TrainingType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by doka on 2018-03-29.
 */
public class NetCreator {
    private static final NetCreator netCreator = new NetCreator();

    private NetCreator() {
    }

    public static NetCreator getInstance() {
        return netCreator;
    }

    /**
     *
     * @param numberOfInputs
     * @param neuronsInLayers
     * @return
     */
    public BackpropagatedNet createOfflinePositiveSigmoidal(int numberOfInputs, List<Integer> neuronsInLayers) {
        return createMLP(numberOfInputs, neuronsInLayers, SigmPositive.getInstance(), TrainingType.ONLINE);
    }

    /**
     *
     * @param numberOfInputs
     * @param neuronsInLayers
     * @param activationFunction
     * @param training
     * @return
     */
    public BackpropagatedNet createMLP(int numberOfInputs, List<Integer> neuronsInLayers, ActivationStrategy activationFunction, TrainingType training) {
        final BackpropagationNetConstrucotrDTO dto = prepareDataForCreation(numberOfInputs, neuronsInLayers, activationFunction, training, LearningRateRandomRange.minusFiveToFive);
        return new BackpropagatedNet(dto.inputLayer, dto.netLayers);
    }

    /**
     *
     * @param neuronsInEncoderLayers
     * @return
     */
    public Autoencoder createOfflineSigmPositive(List<Integer> neuronsInEncoderLayers) {
        return createAutoencoder(neuronsInEncoderLayers, SigmPositive.getInstance(), TrainingType.OFFLINE);
    }

    /**
     * @param neuronsInEncoderLayers - number of neurons in each layer including input layer
     * @param activationFunction
     * @param training
     * @return
     */
    public Autoencoder createAutoencoder(List<Integer> neuronsInEncoderLayers, ActivationStrategy activationFunction, TrainingType training) {
        if (neuronsInEncoderLayers.size() < 2) {
            throw new IllegalArgumentException("Size of layers must be higher or equal 2. One inputs layer and one hidden layer.");
        }
        List<Integer> neuronsInAllLayers = defineNoOfNeuronsInAllLayers(neuronsInEncoderLayers);

        final BackpropagationNetConstrucotrDTO dto = prepareDataForCreation(neuronsInAllLayers.get(0), neuronsInAllLayers.subList(1, neuronsInAllLayers.size()), activationFunction, training, LearningRateRandomRange.minusFiveToFive);
        return new Autoencoder(dto.inputLayer, dto.netLayers);
    }

    public SOM createSom(int numberOfInputs, int numberOfNeuronsOnEdge){
        final int nXn = numberOfNeuronsOnEdge * numberOfNeuronsOnEdge;
        final InputLayer inputLayer = createInputLayer(numberOfInputs);
        final List<NetNeuron> grid = IntStream.range(0, nXn).mapToObj(e -> new NetNeuron(new OnlineTraining(), null)).collect(Collectors.toList());
        final NetLayer netLayer = new NetLayer(grid);
        LayersConnector.connectAllToAll(inputLayer,netLayer,LearningRateRandomRange.somNormalizedData);
        return new SOM(inputLayer,grid);
    }

    private static List<Integer> defineNoOfNeuronsInAllLayers(List<Integer> neuronsInEncoderLayers) {
        List<Integer> decoderPart = new ArrayList<>(extractAllElementsWithoutLast(neuronsInEncoderLayers)); // Integer is passed by reference however Integer is immutable and we dont have to have deep copy
        Collections.reverse(decoderPart);

        List<Integer> neuronsInAllLayers = new ArrayList<>(neuronsInEncoderLayers);
        neuronsInAllLayers.addAll(decoderPart);
        return neuronsInAllLayers;
    }

    private static List<Integer> extractAllElementsWithoutLast(List<Integer> neuronsInEncoderLayers) {
        return neuronsInEncoderLayers.subList(0, neuronsInEncoderLayers.size() - 1);
    }

    private BackpropagationNetConstrucotrDTO prepareDataForCreation(int numberOfInputs, List<Integer> neuronsInLayers, ActivationStrategy activationFunction, TrainingType training, LearningRateRandomRange learningRateRandomRange) {
        if (neuronsInLayers.isEmpty()) {
            throw new IllegalArgumentException("There have to be at least one layer in BackpropagatedNet");
        }
        List<NetLayer> netLayers = createNetLayers(neuronsInLayers, activationFunction, training);
        final InputLayer inputLayer = createInputLayer(numberOfInputs);
        connectLayers(netLayers, inputLayer, learningRateRandomRange);

        addBiasToEachLayer(netLayers, learningRateRandomRange);

        return new BackpropagationNetConstrucotrDTO(inputLayer, netLayers);
    }

    private void addBiasToEachLayer(List<NetLayer> netLayers, LearningRateRandomRange learningRateRandomRange) {
        netLayers.subList(1, netLayers.size())
                .forEach(netLayer ->
                        LayersConnector.connectNeuronToAllInLayer(new FixedNeuron(1), netLayer,learningRateRandomRange));
    }

    private void connectLayers(List<NetLayer> netLayers, InputLayer inputLayer, LearningRateRandomRange learningRateRandomRange) {
        LayersConnector.connectAllToAll(inputLayer, netLayers.get(0), learningRateRandomRange);
        for (int i = 1; i < netLayers.size(); i++) {
            LayersConnector.connectAllToAll(netLayers.get(i - 1), netLayers.get(i), learningRateRandomRange);
        }
    }

    private List<NetLayer> createNetLayers(List<Integer> neuronsInLayers, ActivationStrategy activationFunction, TrainingType training) {
        return neuronsInLayers.stream()
                .map(number -> createNetNeurons(activationFunction, number, training))
                .map(NetLayer::new)
                .collect(Collectors.toList());
    }

    private List<NetNeuron> createNetNeurons(ActivationStrategy activationFunction, Integer neuronsNumber, TrainingType training) {
        return IntStream.rangeClosed(1, neuronsNumber)
                .mapToObj(i -> new NetNeuron(training.createStrategy(), activationFunction))
                .collect(Collectors.toList());
    }

    private InputLayer createInputLayer(int numberOfNeurons) {
        final List<FixedNeuron> fixedNeurons = IntStream.rangeClosed(1, numberOfNeurons)
                .mapToObj(i -> new FixedNeuron())
                .collect(Collectors.toList());
        return new InputLayer(fixedNeurons);
    }

    class BackpropagationNetConstrucotrDTO {
        InputLayer inputLayer;
        List<NetLayer> netLayers;

        public BackpropagationNetConstrucotrDTO(InputLayer inputLayer, List<NetLayer> netLayers) {
            this.inputLayer = inputLayer;
            this.netLayers = netLayers;
        }
    }
}
