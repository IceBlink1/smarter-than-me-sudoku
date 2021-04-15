package solver;

import algoritms.EnumerationAlg;
import board.SudokuField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Solver {
    static Random rnd = new Random();

    public static void main(String[] args) {
        List<Integer> data = new ArrayList<>();
        List<Integer> sudoku;
        try {
            data = readData(args);
        } catch (IllegalArgumentException ex) {
            System.out.println();
            System.out.println(ex.getMessage());
            System.out.println("The program has finished. To start again you have to rerun it with right parameters.");
            System.exit(1);
        }
        sudoku = data.subList(0, data.size() - 1);
        int version = data.get(data.size() - 1);
        List<List<Integer>> finalSudoku = solve(sudoku, version);
    }

    public static List<Integer> readData(String[] args) {
        System.out.println("Input 81 sudoku values divided by gap and 1 in the end to get a full solution " +
                "or 2 for the next step.");
        List<String> listData = new ArrayList<>(0);
        listData.addAll(Arrays.asList(args));
        if (args.length != 82 || !tryParseIntChecked(listData))
            throw new IllegalArgumentException("You have to input 81 integer numbers divided by gap.");
        List<Integer> listIntData = new ArrayList<>(0);
        for (int i = 0; i < 82; ++i) {
            listIntData.add(Integer.parseInt(listData.get(i)));
        }
        return listIntData;
    }

    public static boolean tryParseIntChecked(List<String> list) {
        try {
            for (String s : list) {
                Integer.parseInt(s);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<List<Integer>> solve(List<Integer> data, int version) {
        List<Integer> zeroIndexes = new ArrayList<>(0);
        SudokuField sudokuField = new SudokuField(data);
        EnumerationAlg enAlg = new EnumerationAlg(sudokuField);
        int[][] solvedSudoku = enAlg.getSudoku();
        if (!correctnessCheck(solvedSudoku)) {
            System.out.println("Incorrect sudoku was entered.");
            throw new IllegalArgumentException("Incorrect sudoku was entered.");
        }
        else {
            if (version == 1){
                enAlg.printSudoku(solvedSudoku);
                return convertToListList(solvedSudoku);
            }
            else {
                for (int i = 0; i < sudokuField.size(); ++i) {
                    // Если нет точного значения ячейки
                    if (sudokuField.getSudoku().get(i).getPossibleValues().size() != 1) {
                        zeroIndexes.add(i);
//                        sudokuField.setFigure(i, solvedSudoku[i / 9][i % 9]);
//                        break;
                    }
                }
                int randZerInd = zeroIndexes.get(rnd.nextInt(zeroIndexes.size()));
                sudokuField.setFigure(randZerInd, solvedSudoku[randZerInd / 9][randZerInd % 9]);
                enAlg.printSudoku(enAlg.makeMatrixOfList(sudokuField));
                return convertToListList(enAlg.makeMatrixOfList(sudokuField));
            }
        }
    }

    public static int difficultyEstimation(){
        return 1 + rnd.nextInt(5);
    }

    public static boolean correctnessCheck(int[][] sudoku) {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (sudoku[i][j] == 0)
                    return false;
            }
        }
        return true;
    }

    public static List<List<Integer>> convertToListList (int[][] arr){
        List<List<Integer>> listList = new ArrayList<>();
        for (int[] ints : arr) {
            List<Integer> list = new ArrayList<>();
            for (int anInt : ints) {
                list.add(anInt);
            }
            listList.add(list);
        }
        return listList;
    }
}
