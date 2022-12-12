package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

public class Day12 extends Day2022 {
    public Day12() {
        super(12);
    }

    private char[][] charArray;
    private int[][] stepArray;

    @Override
    public Object part1() {
        String input = getTodaysInput();

        charArray = convertInputToArray(input);
        stepArray = new int[charArray.length][charArray[0].length];
        GridPosition startPoint = findStartingPoint(charArray);
        GridPosition endPoint = findEndPoint(charArray);

        charArray[endPoint.row()][endPoint.column()] = 'z';
        charArray[startPoint.row()][startPoint.column()] = 'a';
        Deque<GridPosition> cellsToCheck = new ArrayDeque<>();
        cellsToCheck.addLast(endPoint);

        while (!cellsToCheck.isEmpty()) {
            final GridPosition nextCell = cellsToCheck.removeFirst();
            cellsToCheck.addAll(findNextSpace(nextCell));
        }
        return Integer.parseInt(String.valueOf(stepArray[startPoint.row][startPoint.column]));
    }

    private Collection<GridPosition> findNextSpace(GridPosition cellToCheck) {
        List<GridPosition> neighborCells = new ArrayList<>();
        neighborCells.add(new GridPosition(cellToCheck.row() + 1, cellToCheck.column));
        neighborCells.add(new GridPosition(cellToCheck.row() - 1, cellToCheck.column));
        neighborCells.add(new GridPosition(cellToCheck.row(), cellToCheck.column + 1));
        neighborCells.add(new GridPosition(cellToCheck.row(), cellToCheck.column - 1));

        neighborCells = neighborCells.stream().filter(cell -> cell.row >= 0 && cell.column() >= 0 && cell.row() < stepArray.length && cell.column < stepArray[0].length && stepArray[cell.row()][cell.column] == 0).toList();

        List<GridPosition> setCells = new ArrayList<>();
        for (GridPosition cell : neighborCells) {
            if (charArray[cell.row()][cell.column()] + 1 >= charArray[cellToCheck.row()][cellToCheck.column]) {
                int nextStepValue = stepArray[cellToCheck.row()][cellToCheck.column] + 1;
                stepArray[cell.row()][cell.column()] = nextStepValue;
                setCells.add(cell);
            }
        }
        return setCells;
    }

    @Override
    public Object part2() {

        String input = getTodaysInput();

        charArray = convertInputToArray(input);
        stepArray = new int[charArray.length][charArray[0].length];
        GridPosition startPoint = findStartingPoint(charArray);
        GridPosition endPoint = findEndPoint(charArray);

        charArray[endPoint.row()][endPoint.column()] = 'z';
        charArray[startPoint.row()][startPoint.column()] = 'a';
        Deque<GridPosition> cellsToCheck = new ArrayDeque<>();
        cellsToCheck.addLast(endPoint);

        while (!cellsToCheck.isEmpty() /*&& stepArray[startPoint.row()][endPoint.column()] == 0*/) {
            final GridPosition nextCell = cellsToCheck.removeFirst();
            cellsToCheck.addAll(findNextSpace(nextCell));
        }

        List<GridPosition> aCells = new ArrayList<>();
        for (int i = 0; i < stepArray.length; i++) {
            for (int j = 0; j < stepArray[0].length; j++) {
                if (charArray[i][j] == 'a') {
                    aCells.add(new GridPosition(i, j));
                }
            }
        }

        return aCells.stream().map(gridPosition -> Integer.parseInt(String.valueOf(stepArray[gridPosition.row][gridPosition.column]))).filter(x -> x > 0).sorted().toList().get(0);
    }

    private char[][] convertInputToArray(String input) {
        final String[] split = input.split("\n");
        int numRows = split.length;
        int numColumns = split[0].length();
        char[][] chars = new char[numRows][numColumns];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                chars[i][j] = split[i].charAt(j);
            }
        }
        return chars;
    }

    private GridPosition findStartingPoint(char[][] charArray) {
        return findCharacter(charArray, 'S');
    }

    private GridPosition findEndPoint(char[][] charArray) {
        return findCharacter(charArray, 'E');
    }

    private GridPosition findCharacter(char[][] charArray, char find) {
        for (int row = 0; row < charArray.length; row++) {
            for (int column = 0; column < charArray[0].length; column++) {
                if (charArray[row][column] == find) {
                    return new GridPosition(row, column);
                }
            }
        }
        throw new IllegalStateException();
    }

    private record GridPosition(int row, int column) {
    }
}