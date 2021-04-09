package com.digitrecognition;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.*;

public class DigitRecogniser {

    private final DigitRecognitionNN network;

    public DigitRecogniser(String filename) throws IOException {
        network = new DigitRecognitionNN(filename);
    }

    /**
     * Распознает поле судоку и возвращает цифры в клетках.
     * @param image Матрица изображения.
     * @return Двумерный массив цифр в клектках. Если клетка пуста, элемент
     * равен 0.
     */
    public ArrayList<ArrayList<Integer>> recognise(Mat image) {
        // Получаем матрицы клеток поля.
        ArrayList<ArrayList<Mat>> cells = getCellsFromImage(image);
        ArrayList<NormalizedCell> normalizedCells = new ArrayList<>();

        // Получаем массив нормализованных клеток.
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                Mat cell = cells.get(i).get(j);
                removeNoise(cell);
                cell = removeEmptySpace(cell);
                normalizedCells.add(new NormalizedCell(i, j, cell));
            }
        }

        // Сортируем изображения клеток по размеру.
        normalizedCells = new ArrayList<>(Arrays.asList(
                normalizedCells.stream().sorted(
                        Comparator.comparingInt((NormalizedCell normalizedCell) ->
                                normalizedCell.size)).toArray(NormalizedCell[]::new)));

        // Индекс первой клетки с цифрой.
        int border = -1;
        // Максимальное отношение размеров двух соседних клеток.
        double maxRatio = 0.0;
        // Ищем максимальное отношение.
        for(int i = 1; i < normalizedCells.size(); ++i) {
            if(normalizedCells.get(i - 1).size != 0) {
                maxRatio = Math.max(maxRatio,
                        (double)normalizedCells.get(i).size /
                                (double)normalizedCells.get(i - 1).size);
            }
        }
        // Минимальное значение отношения для разграничения пустой клетки и
        // клетки с цифрой.
        final double MIN_RATIO = 3.0;
        // Получаем индекс первой клетки с цифрой (border == -1, если все
        // клетки не пустые).
        for(int i = 1; i < normalizedCells.size(); ++i) {
            if(normalizedCells.get(i - 1).size != 0 &&
                    ((double)normalizedCells.get(i).size /
                            (double)normalizedCells.get(i - 1).size ==
                            maxRatio) &&
                    maxRatio >= MIN_RATIO) {
                border = i;
                break;
            }
        }

        ArrayList<ArrayList<Integer>> result = new ArrayList<>(9);
        for(int i = 0; i < 9; ++i) {
            result.add(new ArrayList<>());
            for(int j = 0; j < 9; ++j) {
                result.get(i).add(0);
            }
        }

        // Минимальной размер изображения клетки, чтобы считаться цифрой.
        final int MIN_SIZE = 80;
        // Получаем прогнозы сети для клеток с цифрами.
        for(int i = 0; i < normalizedCells.size(); ++i) {
            NormalizedCell cell = normalizedCells.get(i);
            if(i < border || cell.size < MIN_SIZE) {
                result.get(cell.row).set(cell.column, 0);
            }
            else {
                int digit = network.predict(
                        getInputForNN(cell.cell));
                result.get(cell.row).set(cell.column, digit);
            }
        }

        return result;
    }

    /**
     * Конвертирует матрицу клетку в формат для подачи модели.
     * @param cell Матрица распознаваемой клетки.
     * @return Многомерный массив размерности (1, 28, 28, 1).
     */
    private double[][][] getInputForNN(Mat cell) {
        Mat resizedCell = new Mat(28, 28, CvType.CV_32F);
        Imgproc.resize(cell, resizedCell, resizedCell.size());

        double[][][] input = new double[28][28][1];
        for(int i = 0; i < 28; ++i) {
            for(int j = 0; j < 28; ++j) {
                input[i][j][0] = resizedCell.get(i, j)[0] / 255.0;
            }
        }
        return input;
    }

    /**
     * Удаляет шум в матрице изображения. Выделяется наибольшая компонента
     * связности ненулевых пикселей, остальные пиксели зануляются.
     * @param mat Матрица, в которой удаляется шум.
     */
    private static void removeNoise(Mat mat) {
        // Матрица, хранящая цвет каждого пикселя в раскраске bfs'ом.
        Mat bfsMat = new Mat(mat.rows(), mat.cols(), CvType.CV_32S);
        for(int i = 0; i < bfsMat.rows(); ++i) {
            for(int j = 0; j < bfsMat.cols(); ++j) {
                bfsMat.put(i, j, 0);
            }
        }

        // Поиском в ширину выделяем компоненты связности ненулевых пикселей.
        // Пиксели могут находится в одной компоненте, если имееют общую точку,
        // то есть у каждого пикселя 8 соседних.
        int currColor = 1;
        int maxColorIndex = 0;
        int maxColorCount = 0;
        for(int i = 0; i < bfsMat.rows(); ++i) {
            for(int j = 0; j < bfsMat.cols(); ++j) {
                if(bfsMat.get(i, j)[0] != 0 || mat.get(i, j)[0] == 0)
                    continue;

                Queue<IntPair> bfsQueue = new ArrayDeque<>();
                bfsQueue.add(new IntPair(i, j));
                int currColorCount = 0;
                while(!bfsQueue.isEmpty()) {
                    ++currColorCount;
                    int currI = bfsQueue.peek().first;
                    int currJ = bfsQueue.poll().second;
                    for (int di = -1; di < 2; ++di) {
                        for (int dj = -1; dj < 2; dj += 2 - Math.abs(di)) {
                            if (currI + di < bfsMat.rows() && currI + di >= 0 &&
                                    currJ + dj < bfsMat.cols() && currJ + dj >= 0 &&
                                    mat.get(currI + di, currJ + dj)[0] != 0.0 &&
                                    bfsMat.get(currI + di, currJ + dj)[0] == 0) {
                                bfsMat.put(currI + di, currJ + dj, currColor);
                                bfsQueue.add(new IntPair(currI + di, currJ + dj));
                            }
                        }
                    }
                }
                if(currColorCount > maxColorCount) {
                    maxColorIndex = currColor;
                    maxColorCount = currColorCount;
                }
                ++currColor;
            }
        }

        for(int i = 0; i < mat.rows(); ++i) {
            for(int j = 0; j < mat.cols(); ++j) {
                if(bfsMat.get(i, j)[0] != maxColorIndex) {
                    mat.put(i, j, 0.0);
                }
            }
        }
    }

    /**
     * Обрезает пустые места в матрице изображения.
     * @param mat Исходная матрица.
     * @return Матрица изображения с удаленными пустыми столбцами
     * справа и слева и строками сверху и снизу.
     */
    private static Mat removeEmptySpace(Mat mat) {
        int firstNotEmptyRow = -1;
        int lastNotEmptyRow = -1;
        for(int i = 0; i < mat.rows(); ++i) {
            for(int j = 0; j < mat.cols(); ++j) {
                if(mat.get(i, j)[0] > 0.0) {
                    if(firstNotEmptyRow == -1) {
                        firstNotEmptyRow = i;
                    }
                    lastNotEmptyRow = i;
                }
            }
        }

        int firstNotEmptyColumn = -1;
        int lastNotEmptyColumn = -1;
        for(int i = 0; i < mat.cols(); ++i) {
            for(int j = 0; j < mat.rows(); ++j) {
                if(mat.get(j, i)[0] > 0.0) {
                    if(firstNotEmptyColumn == -1) {
                        firstNotEmptyColumn = i;
                    }
                    lastNotEmptyColumn = i;
                }
            }
        }

        Mat result = new Mat(lastNotEmptyRow - firstNotEmptyRow + 1,
                lastNotEmptyColumn - firstNotEmptyColumn + 1, CvType.CV_8U);
        for(int i = firstNotEmptyRow; i <= lastNotEmptyRow; ++i) {
            for(int j = firstNotEmptyColumn; j <= lastNotEmptyColumn; ++j) {
                result.put(i - firstNotEmptyRow, j - firstNotEmptyColumn,
                        mat.get(i, j)[0]);
            }
        }
        return result;
    }

    /**
     * Вычисляет количество ненулевых пикселей в матрице.
     * @param image Матрица изображения.
     * @return Количество ненулевых пикселей.
     */
    private int getImageSize(Mat image) {
        int size = 0;
        for(int i = 0; i < image.height(); ++i) {
            for(int j = 0; j < image.width(); ++j) {
                if(image.get(i, j)[0] != 0) {
                    ++size;
                }
            }
        }
        return size;
    }

    /**
     * Выделяет поле судоку на изображении и разбивает на клетки.
     * @param imageMatrix Матрица изображения с полем.
     * @return Матрица матриц клеток.
     */
    private ArrayList<ArrayList<Mat>> getCellsFromImage(Mat imageMatrix) {
        // Получаем чб картинку.
        Mat grayMatrix = new Mat();
        if (imageMatrix.channels() == 3) {
            Imgproc.cvtColor(imageMatrix, grayMatrix, Imgproc.COLOR_BGR2GRAY);
        } else {
            grayMatrix = imageMatrix;
        }

        // Выделяем поле.
        Mat gridMat = GridExtractor.contourGridExtract(grayMatrix);

        // Получаем бинарную матрицу. 255 - черный, 0 - белый.
        Mat processedImage = ImageProcessor.processImage(gridMat, false);

        // Удаляем линии сетки поля.
        Mat lines = new Mat();
        Imgproc.HoughLinesP(processedImage, lines,
                1.0, Math.PI / 180.0, 100, 50.0, 5.0);
        Mat gridWOLines = GridLinesRemover.removeGridLines(processedImage, lines);

        // Получаем массив клеток.
        return CellExtractor.splitGrid(gridWOLines, 9, 9);
    }

    class NormalizedCell {
        public final int row;
        public final int column;
        public final int size;
        public final Mat cell;

        public NormalizedCell(int row, int column, Mat cell) {
            this.row = row;
            this.column = column;
            this.cell = cell;
            size = getImageSize(this.cell);
        }
    }
}
