package net.networks;

import data.DataRow;
import data.TrainingRow;
import net.layers.InputLayer;
import net.layers.LayersConnector;
import net.layers.NetLayer;
import net.learning.LearningRateRandomRange;
import net.neurons.Synapse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by doka on 2018-03-29.
 */
public abstract class Network<TrainingDataType> {
    final InputLayer inputLayer;
    final List<NetLayer> netLayers;
    double learningRate = 0.5;

    public Network(InputLayer inputLayer, List<NetLayer> netLayers) {
        this.inputLayer = inputLayer;
        this.netLayers = netLayers;
    }

    public abstract DataRow processData(DataRow feautreDataRow);

    public abstract void trainNet(List<TrainingDataType> trainingData);

    public void connectToNextNetwork(Network<TrainingDataType> nextNetwork, LearningRateRandomRange rateRandomRange){
        final NetLayer lastLayer = netLayers.get(netLayers.size() - 1);
        final NetLayer nextFirstLayer = nextNetwork.netLayers.get(0);
        LayersConnector.connectAllToAll(lastLayer, nextFirstLayer,rateRandomRange);
    }

}
