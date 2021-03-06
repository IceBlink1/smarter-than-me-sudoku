package board;

import java.util.ArrayList;
import java.util.List;

public class SudokuField {
    private final List<Cell> sudoku = new ArrayList<>(0);

    public int size() {
        return sudoku.size();
    }

    public SudokuField(List<Integer> sudoku) {
        for (Integer figure : sudoku) {
            if (figure.equals(0))
                this.sudoku.add(new Cell());
            else
                this.sudoku.add(new Cell(figure));
        }
    }

    public int[][] toMatrix() {
        int[][] matrixSudoku = new int[9][9];
        for (int i = 0; i < 9; ++i) {
            for (int g = 0; g < 9; ++g) {
                matrixSudoku[i][g] = sudoku.get(9 * i + g).getPotentialValue();
            }
        }
        return matrixSudoku;
    }

    public List<Cell> getSudoku() {
        return sudoku;
    }

    public void setFigure(int ind, int num) {
        sudoku.get(ind).setValue(num);
    }
}
