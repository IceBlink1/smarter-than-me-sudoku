package solver;

import algorithms.dancingLink.SudokuDlx;
import board.SudokuField;
import exception.SudokuException;

import java.util.*;

import javafx.util.Pair;

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
        try {
            List<List<Integer>> finalSudoku = solve(sudoku, version);
        } catch (SudokuException ex) {
            System.out.println(ex.getMessage());
            for (Pair<Integer, Integer> elem : ex.getErrors()) {
                System.out.println(elem.getKey() + " " + elem.getValue() + "; ");
            }
        }
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

    public static List<List<Integer>> solve(List<Integer> data, int version) throws SudokuException {
        List<Integer> zeroIndexes = new ArrayList<>(0);
        SudokuField sudokuField = new SudokuField(data);

        System.out.println("Initial sudoku:");
        printSudoku(sudokuField.toMatrix());
        System.out.println();

        HashSet<Pair<Integer, Integer>> errors = correctnessCheck(sudokuField.toMatrix());
        if (errors.size() != 0) {
//            System.out.println("Invalid sudoku was entered");
//            for (Pair<Integer, Integer> elem : errors) {
//                System.out.println(elem.getKey() + " " + elem.getValue() + "; ");
//            }
            throw new SudokuException("Invalid sudoku was entered.", errors);
        }

        SudokuDlx dlxAlg = new SudokuDlx();
        dlxAlg.solve(sudokuField.toMatrix());

        int[][] solvedSudoku = dlxAlg.getRes();

        if (version == 1) {
            System.out.println("Final Sudoku:");
            printSudoku(solvedSudoku);
            return convertToListList(solvedSudoku);
        } else {
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
            System.out.println("Final Sudoku:");
            printSudoku(sudokuField.toMatrix());
            return convertToListList(sudokuField.toMatrix());
        }
    }


    public static int difficultyEstimation() {
        return 1 + rnd.nextInt(5);
    }

    public static HashSet<Pair<Integer, Integer>> correctnessCheck(int[][] sudoku) {
        HashSet<Pair<Integer, Integer>> errors = new HashSet<>();
        LinkedList<Pair<Integer, Integer>> forCheck = new LinkedList<>(); //1 param - value, 2 - index
        boolean indic = true;

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (sudoku[i][j] != 0) {
                    for (Pair<Integer, Integer> elem : forCheck) {
                        if (elem.getKey().equals(sudoku[i][j])) { //сравнение ключа - цифры из судоку и элемента судоку
                            errors.add(new Pair<>(elem.getValue() / 9, elem.getValue() % 9));
                            errors.add(new Pair<>(i, j));
                            indic = false;
                        }
                    }
                    if (indic)
                        forCheck.add(new Pair<>(sudoku[i][j], i * 9 + j));
                    else
                        indic = true;
                }
            }
            forCheck.clear();
        }

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (sudoku[j][i] != 0) {
                    for (Pair<Integer, Integer> elem : forCheck) {
                        if (elem.getKey().equals(sudoku[j][i])) { //сравнение ключа - цифры из судоку и элемента судоку
                            errors.add(new Pair<>(elem.getValue() / 9, elem.getValue() % 9));
                            errors.add(new Pair<>(j, i));
                            indic = false;
                        }
                    }
                    if (indic)
                        forCheck.add(new Pair<>(sudoku[j][i], j * 9 + i));
                    else
                        indic = true;
                }
            }
            forCheck.clear();
        }

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                errors.addAll(sectionCheck(i * 3, i * 3 + 2, j * 3, j * 3 + 2, sudoku));
            }
        }

        return errors;
    }

    public static HashSet<Pair<Integer, Integer>> sectionCheck(int sHor, int eHor,
                                                               int sVert, int eVert, int[][] sudoku) {
        HashSet<Pair<Integer, Integer>> errors = new HashSet<>();
        LinkedList<Pair<Integer, Integer>> forCheck = new LinkedList<>(); //1 param - value, 2 - index
        boolean indic = true;

        for (int i = sHor; i < eHor; ++i) {
            for (int j = sVert; j < eVert; ++j) {
                if (sudoku[i][j] != 0) {
                    for (Pair<Integer, Integer> elem : forCheck) {
                        if (elem.getKey().equals(sudoku[i][j])) { //сравнение ключа и элемента судоку
                            errors.add(new Pair<>(elem.getValue() / 9, elem.getValue() % 9));
                            errors.add(new Pair<>(i, j));
                            indic = false;
                        }
                    }
                    if (indic)
                        forCheck.add(new Pair<>(sudoku[i][j], i * 9 + j));
                    else
                        indic = true;
                }
            }
        }

        return errors;
    }

    public static List<List<Integer>> convertToListList(int[][] arr) {
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

    public static void printSudoku(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                System.out.print(board[row][column] + " ");
            }
            System.out.println();
        }
    }
}