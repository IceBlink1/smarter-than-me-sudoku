package digitRecognition;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.*;

public class NeuralNetwork {
    private final Mat inputHiddenWeights;

    private final Mat hiddenOutputWeights;

    /**
     * Считывает веса сети из файла.
     * @param filename Путь к файлу с параметрами нейронной сети.
     * @throws IOException
     */
    public NeuralNetwork(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        reader.readLine();
        String[] params = reader.readLine().split(" ");
        int inputNodes = Integer.parseInt(params[0]);
        int hiddenNodes = Integer.parseInt(params[1]);
        int outputNodes = Integer.parseInt(params[2]);

        inputHiddenWeights = new Mat(hiddenNodes, inputNodes, CvType.CV_32F);
        hiddenOutputWeights = new Mat(outputNodes, hiddenNodes, CvType.CV_32F);

        for (int i = 0; i < inputHiddenWeights.height(); ++i) {
            String[] values = reader.readLine().split(" ");
            for (int j = 0; j < inputHiddenWeights.width(); ++j) {
                inputHiddenWeights.put(i, j, Float.parseFloat(values[j]));
            }
        }
        reader.readLine();
        for (int i = 0; i < hiddenOutputWeights.height(); ++i) {
            String[] values = reader.readLine().split(" ");
            for (int j = 0; j < hiddenOutputWeights.width(); ++j) {
                hiddenOutputWeights.put(i, j, Float.parseFloat(values[j]));
            }
        }
    }

    /**
     * Вычисляет результат для входных данных.
     * @param inputs Массив входных данных.
     * @return Массив выходных данных.
     */
    public Mat predict(Mat inputs) {
        Core.transpose(inputs, inputs);
        Mat hiddenInputs = new Mat();
        Core.gemm(inputHiddenWeights, inputs, 1, new Mat(), 0, hiddenInputs);
        Mat hiddenOutputs = applyActivationFunction(hiddenInputs);
        Mat finalInputs = new Mat();
        Core.gemm(hiddenOutputWeights, hiddenOutputs, 1, new Mat(), 0, finalInputs);
        return applyActivationFunction(finalInputs);
    }

    private Mat applyActivationFunction(Mat signals) {
        Mat result = signals.clone();
        for(int i = 0; i < signals.height(); ++i) {
            for(int j = 0; j < signals.width(); ++j) {
                result.put(i, j, 1.0 / (1.0 + Math.exp(-signals.get(i, j)[0])));
            }
        }
        return result;
    }
}
