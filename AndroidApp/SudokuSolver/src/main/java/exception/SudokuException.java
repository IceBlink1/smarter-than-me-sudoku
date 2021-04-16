package exception;

import java.util.HashSet;

public class SudokuException extends IllegalArgumentException {

    private HashSet<Pair<Integer, Integer>> errors;

    public HashSet<Pair<Integer, Integer>> getErrors() {
        return errors;
    }

    public SudokuException(String message) {
        super(message);
    }

    public SudokuException(String message, HashSet<Pair<Integer, Integer>> errors) {
        super(message);
        this.errors = errors;
    }

}
