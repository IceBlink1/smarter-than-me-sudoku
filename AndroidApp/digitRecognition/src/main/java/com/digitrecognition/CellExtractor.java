package com.digitrecognition;

import org.opencv.core.Mat;

import java.util.ArrayList;

public class CellExtractor {
    /**
     * Разрезает изображение поля на массив изображений клеток.
     * @param grid Поле судоку.
     * @param rows Количество строк.
     * @param cols Количество столбцов.
     * @return Массив из rows * cols клеток.
     */
    public static ArrayList<ArrayList<Mat>> splitGrid(Mat grid,
                                                          int rows, int cols) {
        ArrayList<ArrayList<Mat>> result = new ArrayList<>();
        int width = grid.width() / cols;
        int height = grid.height() / rows;
        for(int i = 0; i < rows; ++i) {
            result.add(new ArrayList<>());
            for(int j = 0; j < cols; ++j) {
                Mat cellMat = grid.submat(i * height, (i + 1) * height,
                        j * width, (j + 1) * width);
                result.get(i).add(cellMat);
            }
        }
        return result;
    }
}
