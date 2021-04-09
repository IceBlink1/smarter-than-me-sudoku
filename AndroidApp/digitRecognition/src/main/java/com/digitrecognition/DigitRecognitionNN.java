package com.digitrecognition;


import com.digitrecognition.digitrecognitioncnn.*;
import org.opencv.core.Mat;

import java.io.*;

public class DigitRecognitionNN {

    private final Sequential model;

    public DigitRecognitionNN(String filename) throws IOException {
        model = new Sequential();
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        double[][][][] kernels = new double[5][5][1][32];
        double[] biases = new double[32];
        readMat(reader, kernels, biases);
        model.add(new ConvolutionalLayer(kernels, biases, new ReLU()));

        kernels = new double[5][5][32][32];
        biases = new double[32];
        readMat(reader, kernels, biases);
        model.add(new ConvolutionalLayer(kernels, biases, new ReLU()));

        model.add(new MaxPoolingLayer(2, 2));

        kernels = new double[3][3][32][64];
        biases = new double[64];
        readMat(reader, kernels, biases);
        model.add(new ConvolutionalLayer(kernels, biases, new ReLU()));

        kernels = new double[3][3][64][64];
        biases = new double[64];
        readMat(reader, kernels, biases);
        model.add(new ConvolutionalLayer(kernels, biases, new ReLU()));

        model.add(new MaxPoolingLayer(2, 2));
        model.add(new FlattenLayer());

        double[][] weights = new double[7 * 7 * 64][256];
        biases = new double[256];
        readMat2(reader, weights, biases);
        model.add(new DenseLayer(weights, biases, new ReLU()));

        weights = new double[256][9];
        biases = new double[9];
        readMat2(reader, weights, biases);
        model.add(new DenseLayer(weights, biases, new Softmax()));

        reader.close();
    }

    private void readMat(BufferedReader reader, double[][][][] weights, double[] biases)
            throws IOException {
        String[] string = reader.readLine().split(" ");

        final int height = weights.length;
        final int width =weights[0].length;
        final int depth = weights[0][0].length;
        final int depth1 = weights[0][0][0].length;

        int counter = 0;
        for(int i = 0; i < height; ++i) {
            for(int j  =0; j < width; ++j) {
                for(int k = 0; k < depth; ++k) {
                    for(int l = 0; l < depth1; ++l) {
                        weights[i][j][k][l] = Double.parseDouble(string[counter++]);
                    }
                }
            }
        }

        for(int i = 0; i < biases.length; ++i) {
            biases[i] = Double.parseDouble(string[counter++]);
        }
    }

    private void readMat2(BufferedReader reader, double[][] weights, double[] biases)
            throws IOException {
        String[] string = reader.readLine().split(" ");

        final int height = weights.length;
        final int width =weights[0].length;

        int counter = 0;
        for(int i = 0; i < height; ++i) {
            for(int j  =0; j < width; ++j) {
                weights[i][j] = Double.parseDouble(string[counter++]);
            }
        }

        for(int i = 0; i < biases.length; ++i) {
            biases[i] = Double.parseDouble(string[counter++]);
        }
    }

    /**
     * Возвращает предсказание модели на переданных входных данных.
     * @param input Многомерный массив размерности (1, 28, 28, 1).
     * @return Предсказанное значение цифры в клетке.
     */
    public int predict(double[][][] input) {
        double[][][] predictions = model.forward(input);

        int prediction = 0;
        double maxProbability = predictions[0][0][0];
        for(int i = 1; i < 9; ++i) {
            double probability = predictions[0][0][i];
            if(maxProbability < probability) {
                prediction = i;
                maxProbability = probability;
            }
        }
        // Т.к. модель предсказывает числа от 0 до 8, предсказание надо
        // увеличить на 1.
        return prediction + 1;
    }
}
