package com.digitrecognition;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.File;
import java.io.IOException;

public class DigitRecognitionNN {

    private final MultiLayerNetwork recognitionModel;

    public DigitRecognitionNN(String filename) throws IOException {
        recognitionModel = MultiLayerNetwork.load(new File(filename), true);
    }

    public int predict(INDArray input) {
        final double MIN_PROBABILITY_FOR_DIGIT = 0.0;

        INDArray predictions = recognitionModel.output(input);

        int result = 0;
        double resultProbability = predictions.getDouble(0, 0);
        for(int i = 1; i < 9; ++i) {
            double probability = predictions.getDouble(0, i);
            if(probability > MIN_PROBABILITY_FOR_DIGIT &&
                    resultProbability < probability) {
                result = i;
                resultProbability = probability;
            }
        }
        return result + 1;
    }
}
