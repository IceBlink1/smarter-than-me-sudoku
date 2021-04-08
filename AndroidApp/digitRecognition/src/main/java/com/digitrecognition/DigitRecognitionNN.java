package com.digitrecognition;

import org.deeplearning4j.nn.conf.layers.Convolution2D;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.File;
import java.io.IOException;

public class DigitRecognitionNN {

    private final MultiLayerNetwork recognitionModel;

    public DigitRecognitionNN(String filename) throws IOException {
        recognitionModel = MultiLayerNetwork.load(new File(filename), true);
    }

    /**
     * Возвращает предсказание модели на переданных входных данных.
     * @param input Многомерный массив размерности (1, 28, 28, 1).
     * @return Предсказанное значение цифры в клетке.
     */
    public int predict(INDArray input) {
        INDArray predictions = recognitionModel.output(input);

        int prediction = 0;
        double maxProbability = predictions.getDouble(0, 0);
        for(int i = 1; i < 9; ++i) {
            double probability = predictions.getDouble(0, i);
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
