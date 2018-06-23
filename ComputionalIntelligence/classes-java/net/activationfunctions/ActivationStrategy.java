package net.activationfunctions;

/**
 * Created by doka on 2018-03-20.
 */
public interface ActivationStrategy {
    double activation(double sumOfInputs);
    double derivative(double activationResult);
    boolean isDataNormalizedRequired();
}
