package com.digitrecognition.digitrecognitioncnn;

public class DenseLayer implements Layer {

    private final double[][] weights;
    private final double[] biases;
    private final ActivationFunction activationFunction;

    public DenseLayer(double[][] weights, double[] biases,
                      ActivationFunction activationFunction) {
        this.weights = weights;
        this.biases = biases;
        this.activationFunction = activationFunction;
    }

    @Override
    public double[][][] forward(double[][][] input) {
        double[][][] output = new double[1][1][weights[0].length];

        for(int i = 0; i < weights[0].length; ++i) {
            for(int j = 0; j < weights.length; ++j) {
                output[0][0][i] += input[0][0][j] * weights[j][i];
            }
            output[0][0][i] += biases[i];
        }
        activationFunction.activate(output);

        return output;
    }
}
