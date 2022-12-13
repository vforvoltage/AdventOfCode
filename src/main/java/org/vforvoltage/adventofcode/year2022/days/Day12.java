package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.util.Grid;
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

    @Override
    public Object part1() {
        String input = getTodaysInput();

        Grid<Character> characterGrid = convertInputToGrid(input);
        Grid<Integer> stepGrid = new Grid<>(characterGrid.getSizes());

        int[] startPoint = findStartPoint(characterGrid);
        int[] endPoint = findEndPoint(characterGrid);

        characterGrid.set('z', endPoint);
        characterGrid.set('a', startPoint);

        Deque<int[]> cellsToCheck = new ArrayDeque<>();
        cellsToCheck.add(endPoint);

        while (!cellsToCheck.isEmpty()) {
            int[] nextCell = cellsToCheck.removeFirst();
            cellsToCheck.addAll(findNextSpace(nextCell, characterGrid, stepGrid));
        }
        return stepGrid.get(startPoint).orElseThrow();
    }

    @Override
    public Object part2() {
        String input = getTodaysInput();

        Grid<Character> characterGrid = convertInputToGrid(input);
        Grid<Integer> stepGrid = new Grid<>(characterGrid.getSizes());

        int[] startPoint = findStartPoint(characterGrid);
        int[] endPoint = findEndPoint(characterGrid);

        characterGrid.set('z', endPoint);
        characterGrid.set('a', startPoint);

        Deque<int[]> cellsToCheck = new ArrayDeque<>();
        cellsToCheck.add(endPoint);

        while (!cellsToCheck.isEmpty()) {
            int[] nextCell = cellsToCheck.removeFirst();
            cellsToCheck.addAll(findNextSpace(nextCell, characterGrid, stepGrid));
        }

        List<int[]> aCells = new ArrayList<>();
        for (int i = 0; i < characterGrid.getSizes()[0]; i++) {
            for (int j = 0; j < characterGrid.getSizes()[1]; j++) {
                if (characterGrid.get(i, j).orElseThrow() == 'a') {
                    aCells.add(new int[]{i, j});
                }
            }
        }
        return aCells.stream().map(cell -> stepGrid.get(cell).orElse(0)).filter(x -> x > 0).sorted().findFirst().orElseThrow();
    }

    private Grid<Character> convertInputToGrid(String input) {
        final String[] split = input.split("\n");
        int numRows = split.length;
        int numColumns = split[0].length();

        Grid<Character> characterGrid = new Grid<>(numRows, numColumns);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                characterGrid.set(split[i].charAt(j), i, j);
            }
        }
        return characterGrid;
    }

    private Collection<? extends int[]> findNextSpace(int[] cellToCheck, Grid<Character> characterGrid, Grid<Integer> stepGrid) {
        List<int[]> neighborCells = new ArrayList<>();
        neighborCells.add(new int[]{cellToCheck[0] + 1, cellToCheck[1]});
        neighborCells.add(new int[]{cellToCheck[0] - 1, cellToCheck[1]});
        neighborCells.add(new int[]{cellToCheck[0], cellToCheck[1] + 1});
        neighborCells.add(new int[]{cellToCheck[0], cellToCheck[1] - 1});

        neighborCells = neighborCells.stream()
                .filter(cell ->
                        cell[0] >= 0 &&
                                cell[1] >= 0 &&
                                cell[0] < stepGrid.getSizes()[0] &&
                                cell[1] < stepGrid.getSizes()[1] &&
                                stepGrid.get(cell).orElse(0) == 0)
                .toList();

        List<int[]> setCells = new ArrayList<>();
        for (int[] cell : neighborCells) {
            if (characterGrid.get(cell).orElseThrow() + 1 >= characterGrid.get(cellToCheck).orElseThrow()) {
                int nextStepValue = stepGrid.get(cellToCheck).orElse(0) + 1;
                stepGrid.set(nextStepValue, cell);
                setCells.add(cell);
            }
        }
        return setCells;
    }

    private int[] findStartPoint(Grid<Character> characterGrid) {
        return characterGrid.findValue('S');
    }

    private int[] findEndPoint(Grid<Character> characterGrid) {
        return characterGrid.findValue('E');
    }
}