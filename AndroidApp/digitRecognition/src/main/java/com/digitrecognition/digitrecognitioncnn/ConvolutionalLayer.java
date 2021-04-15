package com.digitrecognition.digitrecognitioncnn;

public class ConvolutionalLayer implements Layer {

    /**
     * [kernelHeight, kernelWidth, inputFilters, outputFilters]
     */
    private final double[][][][] kernels;
    private final double[] biases;
    private final int kernelHeight;
    private final int kernelWidth;
    private final int inputFilters;
    private final int outputFilters;
    private final ActivationFunction activationFunction;

    public ConvolutionalLayer(double[][][][] kernels, double[] biases,
                              ActivationFunction activationFunction) {
        this.kernels = kernels;
        this.biases = biases;
        kernelHeight = kernels.length;
        kernelWidth = kernels[0].length;
        inputFilters = kernels[0][0].length;
        outputFilters = kernels[0][0][0].length;
        this.activationFunction = activationFunction;
    }

    @Override
    public double[][][] forward(double[][][] input) {
        final int height = input.length;
        final int width = input[0].length;
        final int gap = (kernelHeight - 1) / 2;

        double[][][] output = new double[height][width][outputFilters];

        for(int m = 0; m < outputFilters; ++m) {
            for(int i = 0; i < height; ++i) {
                for(int j = 0; j < width; ++j) {
                    double value = 0.0;
                    for(int k = 0; k < kernelHeight; ++k) {
                        for(int l = 0; l < kernelWidth; ++l) {
                            for(int kernel = 0; kernel < inputFilters; ++kernel) {
                                if(i + k - gap >= 0 && j + l - gap >= 0 &&
                                        i + k - gap < height && j + l - gap < width) {
                                    value += input[i + k - gap][j + l - gap][kernel] *
                                            kernels[k][l][kernel][m];
                                }
                            }
                        }
                    }
                    value += biases[m];
                    output[i][j][m] = value;
                }
            }
        }
        activationFunction.activate(output);
        return output;
    }
}
