package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

public class Day8 extends Day2022 {
    public Day8() {
        super(8);
    }

    @Override
    public Object part1() {
        int[][] grid = getGridFromInput();
        int numVisible = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (isVisible(grid, i, j)) {
                    numVisible++;
                }
            }
        }
        return numVisible;
    }

    @Override
    public Object part2() {
        int[][] grid = getGridFromInput();
        int maxScenicScore = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int scenicScore = calculateScenicScore(grid, i, j);
                maxScenicScore = Math.max(maxScenicScore, scenicScore);
            }
        }
        return maxScenicScore;
    }

    private int calculateScenicScore(int[][] grid, int row, int column) {
        int left = getScoreLeft(grid, row, column);
        int right = getScoreRight(grid, row, column);
        int up = getScoreUp(grid, row, column);
        int down = getScoreDown(grid, row, column);
        return left * right * up * down;
    }

    private int getScoreDown(int[][] grid, int row, int column) {
        int numVisible = 0;
        int height = grid[row][column];
        for (int y = row + 1; y < grid.length; y++) {
            numVisible++;
            if (height <= grid[y][column]) {
                break;
            }
        }
        return numVisible;
    }

    private int getScoreUp(int[][] grid, int row, int column) {
        int numVisible = 0;
        int height = grid[row][column];
        for (int y = row - 1; y >= 0; y--) {
            numVisible++;
            if (height <= grid[y][column]) {
                break;
            }
        }
        return numVisible;
    }

    private int getScoreRight(int[][] grid, int row, int column) {
        int numVisible = 0;
        int height = grid[row][column];
        for (int x = column + 1; x < grid[column].length; x++) {
            numVisible++;
            if (height <= grid[row][x]) {
                break;
            }
        }
        return numVisible;
    }

    private int getScoreLeft(int[][] grid, int row, int column) {
        int numVisible = 0;
        int height = grid[row][column];

        for (int x = column - 1; x >= 0; x--) {
            numVisible++;
            if (height <= grid[row][x]) {
                break;
            }
        }
        return numVisible;
    }

    private boolean isVisible(int[][] grid, int row, int column) {
        int height = grid[row][column];
        if (row == 0 || column == 0) {
            return true;
        } else if (row == grid.length - 1 || column == grid[row].length - 1) {
            return true;
        } else {
            boolean isVisibleUp = true;
            boolean isVisibleDown = true;
            boolean isVisibleLeft = true;
            boolean isVisibleRight = true;
            //up
            for (int y = row - 1; y >= 0; y--) {
                if (grid[y][column] >= height) {
                    isVisibleUp = false;
                    break;
                }
            }

            //down
            for (int y = row + 1; y < grid.length; y++) {
                if (grid[y][column] >= height) {
                    isVisibleDown = false;
                    break;
                }
            }
            //left
            for (int x = column - 1; x >= 0; x--) {
                if (grid[row][x] >= height) {
                    isVisibleLeft = false;
                    break;
                }
            }
            //right
            for (int x = column + 1; x < grid[column].length; x++) {
                if (grid[row][x] >= height) {
                    isVisibleRight = false;
                    break;
                }
            }
            return isVisibleUp || isVisibleDown || isVisibleLeft || isVisibleRight;
        }
    }

    private int[][] getGridFromInput() {
        String input = getTodaysInput();
        final String[] lines = input.split("\n");
        int[][] grid = new int[lines.length][lines[0].length()];

        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                grid[i][j] = Integer.parseInt(String.valueOf(lines[i].charAt(j)));
            }
        }
        return grid;
    }
}