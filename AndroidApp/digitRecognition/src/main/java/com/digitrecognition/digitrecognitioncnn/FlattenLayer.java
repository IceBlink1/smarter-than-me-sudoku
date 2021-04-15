package com.digitrecognition.digitrecognitioncnn;

public class FlattenLayer implements Layer {

    @Override
    public double[][][] forward(double[][][] input) {
        final int height = input.length;
        final int width = input[0].length;
        final int depth = input[0][0].length;
        final int totalSize = height * width * depth;

        double[][][] output = new double[1][1][totalSize];

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                for(int k = 0; k < depth; ++k) {
                    output[0][0][i * width * depth + j * depth + k] =
                            input[i][j][k];
                }
            }
        }

        return output;
    }
}
