package net.activationfunctions;

/**
 * Created by doka on 2018-03-20.
 */
public class Sigm implements ActivationStrategy {
    public static double BETA = 1;

    private static Sigm instance = new Sigm();

    private Sigm() {
    }

    public static Sigm getInstance() {
        return instance;
    }

    @Override
    public double activation(double sumOfInputs) {
        return 2.0 / (1.0 + Math.exp((-1)*BETA * sumOfInputs)) - 1 ;
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
