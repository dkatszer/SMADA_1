package net.networks;

import data.DataRow;
import data.TrainingRow;
import net.layers.InputLayer;
import net.layers.NetLayer;
import net.neurons.NetNeuron;
import net.neurons.Synapse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by doka on 2018-04-02.
 */
public class SOM extends Network<TrainingRow> {
    List<NetNeuron> grid;
    double radius = 1;
    double alfa = 1000;
    int timeCounter = 1;

    Map<Integer, Map<DataRow, Integer>> winnerCounter = new HashMap<>();

    public SOM(InputLayer inputLayer, List<NetNeuron> grid) {
        //todo - add new type of layer for not backpropagated or remove that it have to be layer here. Remember about uniewerssal mechanism of adding connections between networks.
        super(inputLayer, List.of(new NetLayer(grid)));
        this.grid = grid;
    }

    @Override
    public DataRow processData(DataRow feautreDataRow) {
        //should find the winner
        findTheWinnerNode(feautreDataRow);
        return null;
    }

    public int findWinnerNumber(DataRow dataRow){
        final NetNeuron winner = findTheWinnerNode(dataRow);
        return grid.indexOf(winner) + 1;
    }


    public boolean isLearningRateSmallEnough(){
        return learningRate<0.001;
    }

    @Override
    public void trainNet(List<TrainingRow> trainingData) {
        for (TrainingRow trainingRow : trainingData) {
            inputLayer.setInputData(trainingRow.features);
            final NetNeuron theWinner = findTheWinnerNode(trainingRow.features);
            updateWinnerClassCounter(trainingRow, theWinner);
            train(theWinner, trainingRow.features);
            makeRadiusAndLearningRateSmaller(timeCounter);
            timeCounter++;
        }
    }

    private void updateWinnerClassCounter(TrainingRow trainingRow, NetNeuron theWinner) {
        final int winnerNeuronNo = grid.indexOf(theWinner) + 1;
        Map<DataRow, Integer> classCounter = winnerCounter.computeIfAbsent(winnerNeuronNo, k -> new HashMap<>());
        classCounter.put(trainingRow.result, classCounter.computeIfAbsent(trainingRow.result, k -> 0) + 1);
    }

    private NetNeuron findTheWinnerNode(DataRow trainingRow) {
        final Map<NetNeuron, Double> distances = calculateDistances(trainingRow);
        return getNeuronWithMinimalValue(distances);
    }

    private void makeRadiusAndLearningRateSmaller(int t) {
        radius *= Math.exp(-(t/alfa));
        learningRate *= Math.exp(-(t/alfa));
    }

    private void train(NetNeuron winner, DataRow trainingRow) {
        for (NetNeuron neuron : grid) {
            final List<Synapse> inputs = neuron.getInputs();
            final List<Double> newWeights = calculateNewWeights(neuron, winner, trainingRow);
            IntStream.range(0,newWeights.size())
                    .forEach(i->
                        inputs.get(i).setWeight(newWeights.get(i))
                    );
        }

    }

    private List<Double> calculateNewWeights(NetNeuron neuron, NetNeuron winner, DataRow trainingRow) {
        final DataRow neuronWeights = getNeuronInputWeights(neuron);
        double delta = calculateDelta(neuron,winner, radius);
        List<Double> newWeights = new ArrayList<>();
        for(int i = 0 ; i < neuronWeights.size() ; i ++){
            final Double weight = neuronWeights.getValue(i);
            double newWeight = weight + delta * learningRate * (trainingRow.getValue(i) - weight);
            newWeights.add(newWeight);
        }
        return newWeights;
    }

    private double calculateDelta(NetNeuron neuron, NetNeuron winner, double radius) {
        final Coords neuronCoords = coordsOf(neuron);
        final Coords winnerCoords = coordsOf(winner);

        final double nominator = Math.pow(neuronCoords.calculateDistance(winnerCoords), 2);
        final double denominator = 2 * Math.pow(radius,2);
        return Math.exp(-(nominator/denominator));
    }

    private Coords coordsOf(NetNeuron neuron){
        final int idxOfNeuron = grid.indexOf(neuron);
        final int edgeSize = (int)Math.sqrt(grid.size());

        final int colNo = (idxOfNeuron + 1) % edgeSize;// 1 : edgeSize
        final int rowNo = idxOfNeuron / edgeSize + 1; //1 : edgeSize
        return new Coords(rowNo,colNo);
    }

    private DataRow getNeuronInputWeights(NetNeuron neuron) {
        return new DataRow(neuron.getInputs().stream()
                .map(Synapse::getWeight)
                .collect(Collectors.toList()));
    }

    private NetNeuron getNeuronWithMinimalValue(Map<NetNeuron, Double> distances) {
        final Double min = Collections.min(distances.values());

        final Optional<NetNeuron> winner = distances.entrySet().stream().filter(e -> e.getValue().equals(min)).map(Map.Entry::getKey).findFirst();
        if (winner.isPresent()) {
            return winner.get();
        } else {
            throw new RuntimeException();
        }
    }

    private Map<NetNeuron, Double> calculateDistances(DataRow inputData) {
        Map<NetNeuron, Double> distances = new HashMap<>();
        for (NetNeuron neuron : grid) {
            final double distance = calculateDistance(inputData,getNeuronInputWeights(neuron));
            distances.put(neuron, distance);
        }
        return distances;
    }

    private double calculateDistance(DataRow inputData, DataRow neuronWeights) {
        if (inputData.size() != neuronWeights.size()) {
            throw new IllegalArgumentException();
        }
        final double sum = IntStream.range(0, inputData.size())
                .mapToDouble(i -> inputData.getValue(i) - neuronWeights.getValue(i))
                .sum();
        return Math.sqrt(sum);
    }

    class Coords{
        private final int rowNo;
        private final int colNo;

        public Coords(int rowNo, int colNo) {
            this.rowNo = rowNo;
            this.colNo = colNo;
        }

        public double calculateDistance(Coords other){
            return Math.sqrt(Math.pow((rowNo - other.rowNo),2) + Math.pow((colNo - other.colNo),2));
        }
    }

    public Map<Integer, Map<DataRow, Integer>> getWinnerCounter() {
        return winnerCounter;
    }
}

