package net.networks;

import data.DataRow;
import data.TrainingRow;
import net.activationfunctions.ActivationStrategy;
import net.activationfunctions.SigmPositive;
import net.layers.InputLayer;
import net.layers.LayersConnector;
import net.layers.NetLayer;
import net.neurons.FixedNeuron;
import net.neurons.Synapse;
import net.trainings.TrainingStrategy;
import net.trainings.TrainingType;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * Created by doka on 2018-03-17.
 */

public class BackpropagatedNet extends Network<TrainingRow> {
    BackpropagatedNet(InputLayer inputLayer, List<NetLayer> netLayers) {
        super(inputLayer,netLayers);
    }

    //todo - add faster learning. After batch (few rows eg.50), not after whole file.
    //        for(int i=0; i<numberOfSubsets; i++) {
    @Override
    public void trainNet(List<TrainingRow> trainingData) {
        Synapse.setLearningRate(learningRate);
        for (TrainingRow trainingRow : trainingData) {
            inputLayer.setInputData(trainingRow.features);
            stimulateNet();
            calculateErrors(trainingRow.result);
            train();
        }
        finishTraining();
        updateLearningRate();
    }

    public void updateLearningRate() {
        learningRate *= 0.9999;
    }

    private void train() {
        netLayers.forEach(NetLayer::trainAllNeurons);
    }

    private void finishTraining() {
        netLayers.forEach(NetLayer::finishTrainingForAllNeurons);
    }

    @Override
    public DataRow processData(DataRow feautreDataRow) {
        inputLayer.setInputData(feautreDataRow);
        return stimulateNet();
    }

    private DataRow stimulateNet() {
        netLayers.forEach(NetLayer::stimulateAllNeurons);
        return netLayers.get(netLayers.size() - 1).getResult();
    }

    private void calculateErrors(DataRow expectedResult) {
        calculateErrorForLastLayer(expectedResult);
        for (ListIterator<NetLayer> it = netLayers.listIterator(netLayers.size() - 1); it.hasPrevious(); ) {
            it.previous().calculateErrorsForAllNeurons();
        }
    }

    private void calculateErrorForLastLayer(DataRow expectedResult) {
        final NetLayer lastLayer = netLayers.get(netLayers.size() - 1);
        lastLayer.calculateErrorsForAllNeurons(expectedResult);
        /*if (lastLayer.isConnectedToNextLayer()) {
            lastLayer.calculateErrorsForAllNeurons();
        } else {
            lastLayer.calculateErrorsForAllNeurons(expectedResult);
        }*/
    }
}
