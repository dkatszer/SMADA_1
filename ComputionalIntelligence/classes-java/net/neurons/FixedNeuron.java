package net.neurons;

import java.util.List;

/**
 * Created by doka on 2018-03-17.
 */
public class FixedNeuron implements Neuron {
    private double fixedResult;

    public FixedNeuron(double fixedResult) {
        this.fixedResult = fixedResult;
    }

    public FixedNeuron() {
    }

    public void setFixedResult(double fixedResult) {
        this.fixedResult = fixedResult;
    }

    @Override
    public double getResult() {
        return fixedResult;
    }
}
