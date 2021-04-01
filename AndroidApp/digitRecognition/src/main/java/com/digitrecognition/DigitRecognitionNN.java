import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.FileInputStream;
import java.io.IOException;

public class DigitRecognitionNN {

    private final MultiLayerNetwork recognitionModel;

    public DigitRecognitionNN() throws IOException,
            UnsupportedKerasConfigurationException,
            InvalidKerasConfigurationException {
        recognitionModel = KerasModelImport
                .importKerasSequentialModelAndWeights(
                        new FileInputStream("model.h5"), false);
    }

    public int predict(INDArray input) {
        final double MIN_PROBABILITY_FOR_DIGIT = 0.0;

        INDArray predictions = recognitionModel.output(input);

        int result = 0;
        double resultProbability = 0.0;
        for(int i = 1; i < 10; ++i) {
            double probability = predictions.getDouble(0, i);
            if(probability > MIN_PROBABILITY_FOR_DIGIT &&
                    (result == 0 || resultProbability < probability)) {
                result = i;
                resultProbability = probability;
            }
        }
        return result;
    }
}
