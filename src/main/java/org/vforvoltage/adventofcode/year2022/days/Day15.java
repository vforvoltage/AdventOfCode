package org.vforvoltage.adventofcode.year2022.days;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.vforvoltage.adventofcode.util.OffsetArrayCoordinate2D;
import org.vforvoltage.adventofcode.util.OffsetArrayCoordinate2D.OffsetArrayCoordinate2DBuilder;
import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day15 extends Day2022 {
    public Day15() {
        super(15);
    }

    @Override
    public Object part1() {
        String input = getTodaysInput();
        final List<Pair<OffsetArrayCoordinate2D, OffsetArrayCoordinate2D>> pairs = input.lines().map(this::mapStringToPairOfCoordinates).toList();

        final List<Pair<Integer, Integer>> coordinates = pairs.stream().map(pair -> getUnavailableCellsForRow(pair, 2000000)).filter(Objects::nonNull).toList();

        Set<Integer> noBeacons = new HashSet<>();

        for (Pair<Integer, Integer> pair : coordinates) {
            for (int i = pair.getLeft(); i <= pair.getRight(); i++) {
                int finalI = i;
                if (pairs.stream().map(Pair::getRight).filter(beacon -> beacon.getRow() == 2000000).map(beacon -> beacon.getColumn() != finalI).findAny().orElseThrow()) {
                    noBeacons.add(i);
                }
            }
        }
        return noBeacons.size();
    }

    @Override
    public Object part2() {
        String input = getTodaysInput();
        final List<Pair<OffsetArrayCoordinate2D, OffsetArrayCoordinate2D>> pairs = input.lines().map(this::mapStringToPairOfCoordinates).toList();


        for (Pair<OffsetArrayCoordinate2D, OffsetArrayCoordinate2D> sensorBeaconPair : pairs) {
            int distanceBetweenSensorAndBeacon = findDistanceBetweenPoints(sensorBeaconPair.getLeft(), sensorBeaconPair.getRight());
            List<OffsetArrayCoordinate2D> borderCells = getBorderCells(sensorBeaconPair.getLeft(), distanceBetweenSensorAndBeacon + 1);
            borderCells = borderCells.stream().filter(coord -> coord.getRow() >= 0 && coord.getRow() <= 4000000 && coord.getColumn() >= 0 && coord.getColumn() <= 4000000).toList();
            for (OffsetArrayCoordinate2D borderCell : borderCells) {
                final boolean isOutOfRangeOfAllSensors = pairs.stream().allMatch(pair -> {
                    final long checkDistance = findDistanceBetweenPoints(pair);
                    final long distanceBetweenSensorAndCheckSpot = findDistanceBetweenPoints(borderCell, pair.getLeft());
                    return distanceBetweenSensorAndCheckSpot > checkDistance;
                });
                if (isOutOfRangeOfAllSensors) {
                    return ((long) borderCell.getColumn() * 4000000L) + (long) borderCell.getRow();
                }
            }
        }
        return null;
    }

    private List<OffsetArrayCoordinate2D> getBorderCells(OffsetArrayCoordinate2D coordinate, int distance) {
        List<OffsetArrayCoordinate2D> borderCells = new ArrayList<>();

        for (int i = -distance; i <= distance; i++) {
            borderCells.add(new OffsetArrayCoordinate2DBuilder().withRow(coordinate.getRow() + (distance - Math.abs(i))).withColumn(coordinate.getColumn() + i).build());
            borderCells.add(new OffsetArrayCoordinate2DBuilder().withRow(coordinate.getRow() - (distance - Math.abs(i))).withColumn(coordinate.getColumn() + i).build());
        }

        return borderCells;
    }

    private int findDistanceBetweenPoints(Pair<OffsetArrayCoordinate2D, OffsetArrayCoordinate2D> pair) {
        return findDistanceBetweenPoints(pair.getLeft(), pair.getRight());
    }

    private int findDistanceBetweenPoints(OffsetArrayCoordinate2D left, OffsetArrayCoordinate2D right) {
        return Math.abs(left.getRow() - right.getRow()) + Math.abs(left.getColumn() - right.getColumn());
    }

    private Pair<Integer, Integer> getUnavailableCellsForRow(Pair<OffsetArrayCoordinate2D, OffsetArrayCoordinate2D> pair, int row) {
        int rowDiff = Math.abs(pair.getLeft().getRow() - pair.getRight().getRow());
        int columnDiff = Math.abs(pair.getLeft().getColumn() - pair.getRight().getColumn());
        int distanceWithNoBeacons = rowDiff + columnDiff;

        if (row > pair.getLeft().getRow()) {
            int distanceToRowTen = row - pair.getLeft().getRow();
            if (distanceToRowTen <= distanceWithNoBeacons) {
                final int size = distanceWithNoBeacons - distanceToRowTen;
                final Pair<Integer, Integer> of = Pair.of(pair.getLeft().getColumn() - size, pair.getLeft().getColumn() + size);
                return of;
            }
        } else {
            int distanceToRowTen = pair.getLeft().getRow() - row;
            if (distanceToRowTen <= distanceWithNoBeacons) {
                final int size = distanceWithNoBeacons - distanceToRowTen;
                final Pair<Integer, Integer> of = Pair.of(pair.getLeft().getColumn() - size, pair.getLeft().getColumn() + size);
                return of;
            }
        }

        return null;
    }

    private Pair<OffsetArrayCoordinate2D, OffsetArrayCoordinate2D> mapStringToPairOfCoordinates(String line) {
        final String[] split = line.split(":");
        final String x1 = StringUtils.substringBetween(split[0], "x=", ",");
        final String y1 = StringUtils.substringAfter(split[0], "y=");

        final String x2 = StringUtils.substringBetween(split[1], "x=", ",");
        final String y2 = StringUtils.substringAfter(split[1], "y=");

        final Pair<OffsetArrayCoordinate2D, OffsetArrayCoordinate2D> pair = Pair.of(new OffsetArrayCoordinate2DBuilder().withRow(Integer.parseInt(y1)).withColumn(Integer.parseInt(x1)).build(), new OffsetArrayCoordinate2DBuilder().withRow(Integer.parseInt(y2)).withColumn(Integer.parseInt(x2)).build());
        return pair;
    }
}