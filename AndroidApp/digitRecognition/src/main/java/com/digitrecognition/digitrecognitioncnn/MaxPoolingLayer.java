package com.digitrecognition.digitrecognitioncnn;

public class MaxPoolingLayer implements Layer {

    private final int poolHeight;
    private final int poolWidth;

    public MaxPoolingLayer(int poolHeight, int poolWidth) {
        this.poolHeight = poolHeight;
        this.poolWidth = poolWidth;
    }

    @Override
    public double[][][] forward(double[][][] input) {
        final int height = input.length;
        final int width = input[0].length;
        final int filters = input[0][0].length;

        double[][][] output =
                new double[height / poolHeight][width / poolWidth][filters];

        for(int i = 0; i < height / poolHeight; ++i) {
            for(int j = 0; j < width / poolWidth; ++j) {
                for(int k = 0; k < filters; ++k) {
                    double max = Double.NEGATIVE_INFINITY;
                    for(int h = 0; h < poolHeight; ++h) {
                        for(int w = 0; w < poolWidth; ++w) {
                            max = Math.max(max,
                                input[i * poolHeight + h][j * poolWidth + w][k]);
                        }
                    }
                    output[i][j][k] = max;
                }
            }
        }

        return output;
    }
}
