package net.neurons;

import net.learning.LearningRateRandomRange;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by doka on 2018-03-17.
 */
public class Synapse {
    private final Neuron from;
    private final NetNeuron to;
    private double weight;

    private static double learningRate;

    public Synapse(Neuron from, NetNeuron to, LearningRateRandomRange learningRateRandomRange) {
        this.from = from;
        this.to = to;
        this.weight = ThreadLocalRandom.current().nextDouble(learningRateRandomRange.randMin, learningRateRandomRange.randMax);
    }

    public void updateWeight(double update) {
        weight += update;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double calculateDeltaWeight() {
        return (-1d) * learningRate * to.getError() * to.activationFunction.derivative(to.getResult()) * from.getResult();
    }

    public double calculatePartOfError() {
        return to.getError() * weight * to.activationFunction.derivative(to.getResult());
    }

    public double calculateValue() {
        return from.getResult() * weight;
    }

    public static void setLearningRate(double learningRate) {
        Synapse.learningRate = learningRate;
    }

    public double getWeight() {
        return weight;
    }

    public static double getLearningRate() {
        return learningRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Synapse synapse = (Synapse) o;
        return Objects.equals(from, synapse.from) &&
                Objects.equals(to, synapse.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
