package net.networks;

import net.activationfunctions.ActivationStrategy;
import net.activationfunctions.SigmPositive;
import net.layers.InputLayer;
import net.layers.NetLayer;
import net.trainings.TrainingStrategy;
import net.trainings.TrainingType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by doka on 2018-03-29.
 */
public class Autoencoder extends BackpropagatedNet {
    private AutoencoderState state = AutoencoderState.FULL_AUTOENCODER;

    Autoencoder(InputLayer inputLayer, List<NetLayer> netLayers) {
        super(inputLayer, netLayers);
    }

    public void convertToEncoder() {
        final int numberOfLayersToRemove = netLayers.size() / 2; // Inputs(5) - 4 - 3 - 4 - 5
        removeLastLayers(numberOfLayersToRemove);
        state = AutoencoderState.ENCODER_ONLY; //it should return encoder object for whiich training is not available
    }

    private void removeLastLayers(int numberOfLayersToRemove) {
        for (int i = this.netLayers.size() - numberOfLayersToRemove; i < this.netLayers.size(); i++) {
            netLayers.get(i).removeAllNeurons();
            netLayers.remove(i);
        }

        final List<NetLayer> netLayersToStay = this.netLayers.subList(0, this.netLayers.size() - numberOfLayersToRemove);
        netLayersToStay.get(netLayersToStay.size() - 1).removeAllConnectionsToNextLayer();
    }
}

