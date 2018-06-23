package net.trainings;

import net.neurons.Synapse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by doka on 2018-03-26.
 */
public class OnlineTraining implements TrainingStrategy {
    private List<Synapse> synapses = new ArrayList<>();

    @Override
    public void train() {
        synapses.forEach(e->e.updateWeight(e.calculateDeltaWeight()));
    }

    @Override
    public void finished() {

    }

    @Override
    public List<Synapse> getSynapses() {
        return synapses;
    }

    @Override
    public TrainingType getType() {
        return TrainingType.ONLINE;
    }

    @Override
    public void addSynapseToTrain(Synapse synapse) {
        synapses.add(synapse);
    }
}
