package net.trainings;

/**
 * Created by doka on 2018-03-25.
 */
public enum TrainingType {
    OFFLINE() {
        @Override
        public TrainingStrategy createStrategy() {
            return new OfflineTraining();
        }
    },
    ONLINE() {
        @Override
        public TrainingStrategy createStrategy() {
            return new OnlineTraining();
        }
    };

    public abstract TrainingStrategy createStrategy();

}
