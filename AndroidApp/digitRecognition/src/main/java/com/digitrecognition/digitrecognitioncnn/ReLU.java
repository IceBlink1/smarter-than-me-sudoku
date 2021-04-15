package com.digitrecognition.digitrecognitioncnn;

public class ReLU implements ActivationFunction {
    @Override
    public void activate(double[][][] input) {
        for(int i = 0; i < input.length; ++i) {
            for(int j = 0; j < input[0].length; ++j) {
                for(int k = 0; k < input[0][0].length; ++k) {
                    input[i][j][k] = Math.max(input[i][j][k], 0.0);
                }
            }
        }
    }
}
