package board;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private final Set<Integer> possibleValues;
    private int potentialValue;

    public Cell(){
        possibleValues = new HashSet<>();
        for (int i = 1; i < 10; i++){
            possibleValues.add(i);
        }
        potentialValue = 0;
    }

    public Cell(int num){
        possibleValues = new HashSet<>();
        possibleValues.add(num);
        potentialValue = num;
    }

    public int getPotentialValue() {
        return potentialValue;
    }

    public Set<Integer> getPossibleValues() {
        return possibleValues;
    }

    public void setValue(int figure){
        possibleValues.clear();
        possibleValues.add(figure);
        potentialValue = figure;
    }

    public void removeValue(int num){
        possibleValues.remove(num);
    }
}
