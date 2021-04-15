package com.digitrecognition.digitrecognitioncnn;

import java.util.ArrayList;
import java.util.List;

public class Sequential {

    List<Layer> layers = new ArrayList<>();

    public void add(Layer layer) {
        layers.add(layer);
    }

    public double[][][] forward(double[][][] input) {
        for(Layer layer : layers) {
            input = layer.forward(input);
        }
        return input;
    }

}
