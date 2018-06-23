package net.trainings;

import net.neurons.Synapse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by doka on 2018-03-20.
 */
public class OfflineTraining implements TrainingStrategy {
    private Map<Synapse, Double> deltaWeightSummarization = new HashMap<>();
    private int counter = 0;

    @Override
    public void train() {
        deltaWeightSummarization.forEach((k, v) -> deltaWeightSummarization.put(k, v + k.calculateDeltaWeight()));
        counter++;
    }

    @Override
    public void finished() {
        deltaWeightSummarization.forEach((k, v) -> k.updateWeight(v / counter));
        deltaWeightSummarization.forEach((k, v) -> deltaWeightSummarization.put(k, 0d));
        counter = 0;
    }

    @Override
    public void addSynapseToTrain(Synapse synapse) {
        deltaWeightSummarization.put(synapse,0d);
    }

    @Override
    public List<Synapse> getSynapses() {
        return new ArrayList<>(deltaWeightSummarization.keySet());
    }

    @Override
    public TrainingType getType() {
        return TrainingType.OFFLINE;
    }


}
