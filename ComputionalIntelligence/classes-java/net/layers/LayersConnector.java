package net.layers;

import net.learning.LearningRateRandomRange;
import net.neurons.Synapse;
import net.neurons.FixedNeuron;
import net.neurons.NetNeuron;
import net.neurons.Neuron;

/**
 * Created by doka on 2018-03-17.
 */
public class LayersConnector {
    public static void connectAllToAll(InputLayer inputLayer, NetLayer netLayer,  LearningRateRandomRange learningRateRandomRange){
        for(FixedNeuron fixedNeuron : inputLayer.neurons){
            for(NetNeuron netNeuron : netLayer.netNeurons){
                Synapse synapse = new Synapse(fixedNeuron, netNeuron, learningRateRandomRange);
                netNeuron.addInputConnection(synapse);
            }
        }
    }

    public static void connectAllToAll(NetLayer netLayer, NetLayer nextNetLayer,  LearningRateRandomRange learningRateRandomRange){
        for(NetNeuron netNeuron : netLayer.netNeurons){
            for(NetNeuron nextNetNeuron : nextNetLayer.netNeurons){
                Synapse synapse = new Synapse(netNeuron, nextNetNeuron, learningRateRandomRange);
                netNeuron.addOutputConnection(synapse);
                nextNetNeuron.addInputConnection(synapse);
            }
        }
    }

    public static void connectNeuronToAllInLayer(Neuron neuron, NetLayer netLayer, LearningRateRandomRange learningRateRandomRange){
        for(NetNeuron netNeuron : netLayer.netNeurons){
            Synapse synapse = new Synapse(neuron, netNeuron, learningRateRandomRange);
            netNeuron.addInputConnection(synapse);
        }
    }
}
