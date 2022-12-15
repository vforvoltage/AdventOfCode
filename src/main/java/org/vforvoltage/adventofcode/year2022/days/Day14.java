package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.util.ArrayCoordinate2D;
import org.vforvoltage.adventofcode.util.ArrayCoordinate2D.ArrayCoordinate2DBuilder;
import org.vforvoltage.adventofcode.util.OffsetArrayCoordinate2D;
import org.vforvoltage.adventofcode.util.OffsetArrayCoordinate2D.OffsetArrayCoordinate2DBuilder;
import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day14 extends Day2022 {
    public Day14() {
        super(14);
    }

    @Override
    public Object part1() {
        String input = getTodaysInput();

        final List<List<ArrayCoordinate2D>> rockPositions = input.lines()
                .map(scan -> Arrays.stream(scan.split("->"))
                        .map(String::trim).map(point -> {
                            final String[] split = point.split(",");
                            return new ArrayCoordinate2DBuilder()
                                    .withRow(Integer.parseInt(split[1]))
                                    .withColumn(Integer.parseInt(split[0]))
                                    .build();
                        }).toList())
                .toList();

        final int columnMin = rockPositions.stream().flatMap(List::stream).map(ArrayCoordinate2D::getColumn).min(Comparator.naturalOrder()).orElseThrow();
        final int columnMax = rockPositions.stream().flatMap(List::stream).map(ArrayCoordinate2D::getColumn).max(Comparator.naturalOrder()).orElseThrow();
        final int rowMin = rockPositions.stream().flatMap(List::stream).map(ArrayCoordinate2D::getRow).min(Comparator.naturalOrder()).orElseThrow();
        final int rowMax = rockPositions.stream().flatMap(List::stream).map(ArrayCoordinate2D::getRow).max(Comparator.naturalOrder()).orElseThrow();

        int wallWidth = columnMax - columnMin + 1;

        ArrayCoordinate2D offset = new ArrayCoordinate2DBuilder()
                .withRow(0)
                .withColumn(-columnMin)
                .build();

        final List<List<OffsetArrayCoordinate2D>> offsetRockPositions = rockPositions.stream().map(coordStream -> coordStream.stream().map(coord -> new OffsetArrayCoordinate2DBuilder().withCoordinates(coord).withOffset(offset).build()).toList()).toList();
        boolean[][] wall = new boolean[rowMax + 1][wallWidth];
        initializeWall(wall, offsetRockPositions);
        char[][] charWall = convertToCharWall(wall);

        OffsetArrayCoordinate2D sandSource = new OffsetArrayCoordinate2DBuilder().withColumn(500).withRow(0).withOffset(offset).build();


        return sandFallsUntilOverflow(wall, sandSource);
    }

    @Override
    public Object part2() {
        String input = getTodaysInput();

        final List<List<ArrayCoordinate2D>> rockPositions = input.lines()
                .map(scan -> Arrays.stream(scan.split("->"))
                        .map(String::trim).map(point -> {
                            final String[] split = point.split(",");
                            return new ArrayCoordinate2DBuilder()
                                    .withRow(Integer.parseInt(split[1]))
                                    .withColumn(Integer.parseInt(split[0]))
                                    .build();
                        }).toList())
                .toList();

        final int columnMin = rockPositions.stream().flatMap(List::stream).map(ArrayCoordinate2D::getColumn).min(Comparator.naturalOrder()).orElseThrow();
        final int columnMax = rockPositions.stream().flatMap(List::stream).map(ArrayCoordinate2D::getColumn).max(Comparator.naturalOrder()).orElseThrow();
        final int rowMin = rockPositions.stream().flatMap(List::stream).map(ArrayCoordinate2D::getRow).min(Comparator.naturalOrder()).orElseThrow();
        final int rowMax = rockPositions.stream().flatMap(List::stream).map(ArrayCoordinate2D::getRow).max(Comparator.naturalOrder()).orElseThrow();

        int wallWidth = columnMax - columnMin + 1;

        ArrayCoordinate2D offset = new ArrayCoordinate2DBuilder()
                .withRow(0)
                .withColumn(-columnMin + 155)
//                .withColumn(0)
                .build();

        final List<List<OffsetArrayCoordinate2D>> offsetRockPositions = rockPositions.stream().map(coordStream -> coordStream.stream().map(coord -> new OffsetArrayCoordinate2DBuilder().withCoordinates(coord).withOffset(offset).build()).toList()).toList();
        boolean[][] wall = new boolean[rowMax + 1 + 2][wallWidth * 4];
        initializeWall(wall, offsetRockPositions);
        for (int i = 0; i < wall[0].length; i++) {
            wall[wall.length - 1][i] = true;
        }
        char[][] charWall = convertToCharWall(wall);

        OffsetArrayCoordinate2D sandSource = new OffsetArrayCoordinate2DBuilder().withColumn(500).withRow(0).withOffset(offset).build();

        return sandFallsUntilBlocked(wall, sandSource);
    }

    private long sandFallsUntilBlocked(boolean[][] wall, OffsetArrayCoordinate2D sandSource) {
        long restingSandUnits = 0;
        boolean lastSandSettled = true;
        boolean isBlocked = false;
        while (lastSandSettled) {
            char[][] charWall = convertToCharWall(wall);
            if (wall[sandSource.getRow()][sandSource.getColumn()]) {
//                System.out.println(charWallToString(wall));
                return restingSandUnits;
            }

            OffsetArrayCoordinate2D sandLocation = new OffsetArrayCoordinate2DBuilder()
                    .withCoordinates(sandSource.getCoordinate())
                    .withOffset(sandSource.getOffset())
                    .build();

            wall[sandSource.getRow()][sandSource.getColumn()] = true;
            boolean canMove = true;
            while (canMove) {
                if (canMoveDown(wall, sandLocation)) {
                    sandLocation = moveDown(wall, sandLocation);
                } else if (canMoveDownLeft(wall, sandLocation)) {
                    sandLocation = moveDownLeft(wall, sandLocation);
                } else if (canMoveDownRight(wall, sandLocation)) {
                    sandLocation = moveDownRight(wall, sandLocation);
                } else {
                    canMove = false;
                }
            }
            if (lastSandSettled) {
                restingSandUnits++;
            }
        }
        return restingSandUnits;
    }

    private String charWallToString(boolean[][] wall) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wall.length; i++) {
            for (int j = 0; j < wall[0].length; j++) {
                sb.append(wall[i][j] ? "#" : "_").append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private long sandFallsUntilOverflow(boolean[][] wall, OffsetArrayCoordinate2D sandSource) {
        long restingSandUnits = 0;
        boolean lastSandSettled = true;
        while (lastSandSettled) {
            if (wall[sandSource.getRow()][sandSource.getColumn()]) {
                throw new IllegalStateException();
            }

            OffsetArrayCoordinate2D sandLocation = new OffsetArrayCoordinate2DBuilder()
                    .withCoordinates(sandSource.getCoordinate())
                    .withOffset(sandSource.getOffset())
                    .build();

            wall[sandSource.getRow()][sandSource.getColumn()] = true;
            boolean canMove = true;
            while (canMove) {
                if (canMoveDown(wall, sandLocation)) {
                    sandLocation = moveDown(wall, sandLocation);
                } else if (canMoveDownLeft(wall, sandLocation)) {
                    sandLocation = moveDownLeft(wall, sandLocation);
                } else if (canMoveDownRight(wall, sandLocation)) {
                    sandLocation = moveDownRight(wall, sandLocation);
                } else {
                    canMove = false;
                    if (isNextFallOutOfBounds(wall, sandLocation)) {
                        lastSandSettled = false;
                    }
                }
            }
            if (lastSandSettled) {
                restingSandUnits++;
            }
        }

        return restingSandUnits;
    }

    private OffsetArrayCoordinate2D moveDownRight(boolean[][] wall, OffsetArrayCoordinate2D sandLocation) {
        wall[sandLocation.getRow()][sandLocation.getColumn()] = false;
        wall[sandLocation.getRow() + 1][sandLocation.getColumn() + 1] = true;
        return new OffsetArrayCoordinate2DBuilder()
                .withOffset(sandLocation.getOffset())
                .withRow(sandLocation.getCoordinate().getRow() + 1)
                .withColumn(sandLocation.getCoordinate().getColumn() + 1)
                .build();
    }

    private OffsetArrayCoordinate2D moveDownLeft(boolean[][] wall, OffsetArrayCoordinate2D sandLocation) {
        wall[sandLocation.getRow()][sandLocation.getColumn()] = false;
        wall[sandLocation.getRow() + 1][sandLocation.getColumn() - 1] = true;
        return new OffsetArrayCoordinate2DBuilder()
                .withOffset(sandLocation.getOffset())
                .withRow(sandLocation.getCoordinate().getRow() + 1)
                .withColumn(sandLocation.getCoordinate().getColumn() - 1)
                .build();
    }

    private OffsetArrayCoordinate2D moveDown(boolean[][] wall, OffsetArrayCoordinate2D sandLocation) {
        wall[sandLocation.getRow()][sandLocation.getColumn()] = false;
        wall[sandLocation.getRow() + 1][sandLocation.getColumn()] = true;
        return new OffsetArrayCoordinate2DBuilder()
                .withOffset(sandLocation.getOffset())
                .withRow(sandLocation.getCoordinate().getRow() + 1)
                .withColumn(sandLocation.getCoordinate().getColumn())
                .build();
    }

    private boolean isNextFallOutOfBounds(boolean[][] wall, OffsetArrayCoordinate2D sandLocation) {
        return sandLocation.getRow() + 1 == wall.length || sandLocation.getColumn() == 0 || sandLocation.getColumn() + 1 == wall[0].length;
    }

    private boolean canMoveDown(boolean[][] wall, OffsetArrayCoordinate2D sandLocation) {
        if (sandLocation.getRow() + 1 == wall.length) {
            return false;
        }
        return !wall[sandLocation.getRow() + 1][sandLocation.getColumn()];
    }

    private boolean canMoveDownLeft(boolean[][] wall, OffsetArrayCoordinate2D sandLocation) {
        if (sandLocation.getRow() + 1 == wall.length || sandLocation.getColumn() == 0) {
            return false;
        }
        return !wall[sandLocation.getRow() + 1][sandLocation.getColumn() - 1];
    }

    private boolean canMoveDownRight(boolean[][] wall, OffsetArrayCoordinate2D sandLocation) {
        if (sandLocation.getRow() + 1 == wall.length || sandLocation.getColumn() + 1 == wall[0].length) {
            return false;
        }
        return !wall[sandLocation.getRow() + 1][sandLocation.getColumn() + 1];
    }

    private char[][] convertToCharWall(boolean[][] wall) {
        char[][] charWall = new char[wall.length][wall[0].length];
        for (int i = 0; i < wall.length; i++) {
            for (int j = 0; j < wall[0].length; j++) {
                if (wall[i][j]) {
                    charWall[i][j] = '#';
                } else {
                    charWall[i][j] = '_';
                }
            }
        }
        return charWall;
    }

    private void initializeWall(boolean[][] wall, List<List<OffsetArrayCoordinate2D>> rockPositions) {
        for (List<OffsetArrayCoordinate2D> rock : rockPositions) {
            addRockToWall(wall, rock);
        }
    }

    private void addRockToWall(boolean[][] wall, List<OffsetArrayCoordinate2D> rock) {
        OffsetArrayCoordinate2D start = rock.get(0);
        wall[start.getRow()][start.getColumn()] = true;
        for (int i = 1; i < rock.size(); i++) {
            OffsetArrayCoordinate2D nextPoint = rock.get(i);
            fillSpaceBetweenPoints(wall, start, nextPoint);
            start = nextPoint;
        }
    }

    private void fillSpaceBetweenPoints(boolean[][] wall, OffsetArrayCoordinate2D start, OffsetArrayCoordinate2D finish) {
        if (start.getRow() != finish.getRow()) {
            fillVertical(wall, start, finish);
        } else if (start.getColumn() != finish.getColumn()) {
            fillHorizontal(wall, start, finish);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void fillHorizontal(boolean[][] wall, OffsetArrayCoordinate2D start, OffsetArrayCoordinate2D finish) {
        if (start.getRow() != finish.getRow()) {
            throw new IllegalArgumentException();
        }
        for (int column = Math.min(start.getColumn(), finish.getColumn()); column <= Math.max(start.getColumn(), finish.getColumn()); column++) {
            wall[start.getRow()][column] = true;
        }
    }

    private void fillVertical(boolean[][] wall, OffsetArrayCoordinate2D start, OffsetArrayCoordinate2D finish) {

        if (start.getColumn() != finish.getColumn()) {
            throw new IllegalArgumentException();
        }
        for (int row = Math.min(start.getRow(), finish.getRow()); row <= Math.max(start.getRow(), finish.getRow()); row++) {
            wall[row][start.getColumn()] = true;
        }

    }
}