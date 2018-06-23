package net.neurons;

import data.DataRow;
import net.activationfunctions.ActivationStrategy;
import net.trainings.TrainingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by doka on 2018-03-17.
 */
public class NetNeuron implements Neuron {
    List<Synapse> inputs = new ArrayList<>();
    List<Synapse> outputs = new ArrayList<>();

    final TrainingStrategy trainingStrategy;
    final ActivationStrategy activationFunction;

    private double result;
    private double error;

    public NetNeuron(TrainingStrategy trainingStrategy, ActivationStrategy activationFunction) {
        this.trainingStrategy = trainingStrategy;
        this.activationFunction = activationFunction;
    }

    public void stimulate() {
        double sum = sumarizeInputs();
        result = activationFunction.activation(sum);
    }

    private double sumarizeInputs() {
        return inputs.stream().mapToDouble(Synapse::calculateValue).sum();
    }

    public void calculateError(double expectedResult) {
        error = result - expectedResult;
    }

    public void calculateError() {
        error = outputs.stream()
                .mapToDouble(Synapse::calculatePartOfError)
                .sum();
    }

    @Override
    public double getResult() {
        return result;
    }

    public double getError() {
        return error;
    }

    //todo - change it to connect next, then no synapses will be availalbe outside
    public void addInputConnection(Synapse synapse) {
        inputs.add(synapse);
        trainingStrategy.addSynapseToTrain(synapse);
    }
    public void addOutputConnection(Synapse synapse) {
        outputs.add(synapse);
    }
    public boolean isConnectedToAnyNextNeuron(){
        return !outputs.isEmpty();
    }

    public void clean() {
        inputs.clear();
        outputs.clear();
    }

    public void removeConnectionWithAllNext() {
        outputs.clear();
    }
    public void trainInputs(){
        trainingStrategy.train();
    }

    public void finishTraining() {
        trainingStrategy.finished();
    }

    public List<Synapse> getInputs() {
        return inputs;
    }

}
