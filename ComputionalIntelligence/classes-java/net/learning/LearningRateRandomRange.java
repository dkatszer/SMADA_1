package net.learning;

/**
 * Created by doka on 2018-04-03.
 */
public class LearningRateRandomRange {
    public final double randMin;

    public final double randMax;

    public LearningRateRandomRange(double randMin, double randMax) {
        this.randMin = randMin;
        this.randMax = randMax;
    }

    public static LearningRateRandomRange minusFiveToFive = new LearningRateRandomRange(-5,5);
    public static LearningRateRandomRange somNormalizedData = new LearningRateRandomRange(0,0.1);


}
