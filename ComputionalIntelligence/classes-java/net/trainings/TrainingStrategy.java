package net.trainings;

import net.neurons.Synapse;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by doka on 2018-03-20.
 */
public interface TrainingStrategy {

    void train();

    void finished();

    List<Synapse> getSynapses();

    TrainingType getType();

    void addSynapseToTrain(Synapse synapse);
}
