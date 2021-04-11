package com.digitrecognition.digitrecognitioncnn;

public class Softmax implements ActivationFunction {
    @Override
    public void activate(double[][][] input) {
        final int width = input[0][0].length;

        double sum = 0.0;
        for(int i = 0; i < width; ++i) {
            sum += Math.exp(input[0][0][i]);
        }

        for(int i = 0; i < width; ++i) {
            double value = input[0][0][i];
            value = Math.exp(value) / sum;
            input[0][0][i] = value;
        }
    }
}
