package net.layers;

import data.DataRow;
import net.neurons.FixedNeuron;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by doka on 2018-03-17.
 */
public class InputLayer{
    List<FixedNeuron> neurons;

    public InputLayer(List<FixedNeuron> neurons) {
        this.neurons = neurons;
    }

    public void setInputData(DataRow data) {
        if(neurons.size()!=data.size()){
            throw new IllegalArgumentException("Number of input neurons ("+neurons.size()+") should be equal dataRow size ("+data.size()+")");
        }
        IntStream.range(0,data.size())
                .forEach( idx -> neurons.get(idx).setFixedResult(data.getValue(idx)));
    }
}
