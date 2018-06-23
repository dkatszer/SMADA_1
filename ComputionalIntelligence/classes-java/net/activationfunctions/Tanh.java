package net.activationfunctions;

/**
 * Created by doka on 2018-03-20.
 */
public class Tanh implements ActivationStrategy {
    public static double BETA = 1;

    private static Tanh instance = new Tanh();

    private Tanh() {
    }

    public static Tanh getInstance() {
        return instance;
    }

    @Override
    public double activation(double sumOfInputs) {
        return Math.tanh(BETA * sumOfInputs);
    }

    @Override
    public double derivative(double activationResult) {
        return 1d - activationResult * activationResult;
    }

    @Override
    public boolean isDataNormalizedRequired() {
        return false;
    }
}
