package net.activationfunctions;

/**
 * Created by doka on 2018-03-20.
 */
public class SigmPositive implements ActivationStrategy {
    public static double BETA = 1;

    private static SigmPositive instance = new SigmPositive();

    private SigmPositive() {
    }

    public static SigmPositive getInstance() {
        return instance;
    }

    @Override
    public double activation(double sumOfInputs) {
        return 1.0 / (1.0 + Math.exp((-1)*BETA * sumOfInputs));
    }

    @Override
    public double derivative(double activationResult) {
        return (1d - activationResult) * activationResult;
    }

    @Override
    public boolean isDataNormalizedRequired() {
        return true;
    }
}
