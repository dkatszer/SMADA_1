package net.layers;

import data.DataRow;
import net.neurons.NetNeuron;
import net.neurons.Neuron;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by doka on 2018-03-17.
 */
public class NetLayer {
    List<NetNeuron> netNeurons;

    public NetLayer(List<NetNeuron> netNeurons) {
        this.netNeurons = netNeurons;
    }

    public void stimulateAllNeurons() {
        netNeurons.forEach(NetNeuron::stimulate);
    }

    public void calculateErrorsForAllNeurons() {
        netNeurons.forEach(NetNeuron::calculateError);
    }
    public void calculateErrorsForAllNeurons(DataRow expectedResult) {
        IntStream.range(0,expectedResult.size()).forEach(i->netNeurons.get(i).calculateError(expectedResult.getValue(i)));
    }

    public boolean isConnectedToNextLayer(){
        final Optional<NetNeuron> any = netNeurons.stream().filter(NetNeuron::isConnectedToAnyNextNeuron).findAny();
        return any.isPresent();
    }

    public DataRow getResult() {
        List<Double> result = new ArrayList<>();
        for (NetNeuron netNeuron : netNeurons) {
            result.add(netNeuron.getResult());
        }
        return new DataRow(result);
    }

    public void removeAllNeurons(){
        netNeurons.forEach(NetNeuron::clean);
        netNeurons.clear();
    }

    public void removeAllConnectionsToNextLayer() {
        netNeurons.forEach(NetNeuron::removeConnectionWithAllNext);
    }

    public void trainAllNeurons(){
        netNeurons.forEach(NetNeuron::trainInputs);
    }

    public void finishTrainingForAllNeurons() {
        netNeurons.forEach(NetNeuron::finishTraining);
    }
}
