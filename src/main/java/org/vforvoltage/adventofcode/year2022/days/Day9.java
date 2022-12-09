package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day9 extends Day2022 {
    public Day9() {
        super(9);
    }

    @Override
    public Object part1() {
        String input = getTodaysInput();
        List<MovementCommand> commands = parseInputToCommands(input);
        Set<GridPosition> tailPositions = new HashSet<>();
        GridPosition currentHeadPosition = new GridPosition(0, 0);
        GridPosition currentTailPosition = new GridPosition(0, 0);
        tailPositions.add(currentTailPosition);

        for (MovementCommand command : commands) {
            int spacesMoved = 0;
            while (spacesMoved < command.numberOfSpaces()) {
                currentHeadPosition = moveDirection(currentHeadPosition, command.direction());
                currentTailPosition = catchUp(currentHeadPosition, currentTailPosition);
                tailPositions.add(currentTailPosition);
                spacesMoved++;
            }
        }

        return tailPositions.size();
    }

    @Override
    public Object part2() {
        String input = getTodaysInput();
        List<MovementCommand> commands = parseInputToCommands(input);
        Set<GridPosition> tailPositions = new HashSet<>();

        Map<Integer, GridPosition> knotPositions = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            knotPositions.put(i, new GridPosition(0, 0));
        }

        for (MovementCommand command : commands) {
            int spacesMoved = 0;
            while (spacesMoved < command.numberOfSpaces()) {
                knotPositions.computeIfPresent(0, (k, v) -> moveDirection(v, command.direction()));
                for (int i = 1; i <= 9; i++) {
                    knotPositions.computeIfPresent(i, (k, v) -> catchUp(knotPositions.get(k - 1), v));
                }
                tailPositions.add(knotPositions.get(9));
                spacesMoved++;
            }
        }

        return tailPositions.size();
    }

    private GridPosition catchUp(GridPosition leader, GridPosition follower) {
        int xDiff = leader.x() - follower.x();
        int yDiff = leader.y() - follower.y();

        if (Math.abs(xDiff) <= 1 && Math.abs(yDiff) <= 1) {
            return new GridPosition(follower.x(), follower.y());
        } else if (xDiff == 0) {
            return new GridPosition(follower.x(), follower.y() + yDiff - Integer.signum(yDiff));
        } else if (yDiff == 0) {
            return new GridPosition(follower.x() + xDiff - Integer.signum(xDiff), follower.y());
        } else {
            return new GridPosition(follower.x() + (xDiff / Math.abs(xDiff)), follower.y() + (yDiff / Math.abs(yDiff)));
        }
    }

    private List<MovementCommand> parseInputToCommands(String input) {
        return input.lines().map(this::parseCommand).toList();
    }

    private MovementCommand parseCommand(String input) {
        final String[] split = input.split(" ");
        return new MovementCommand(directionFromCharacter(split[0]), Integer.parseInt(split[1]));
    }

    private Direction directionFromCharacter(String direction) {
        return switch (direction) {
            case "U" -> Direction.UP;
            case "D" -> Direction.DOWN;
            case "L" -> Direction.LEFT;
            case "R" -> Direction.RIGHT;
            default -> throw new IllegalArgumentException();
        };
    }

    private GridPosition moveDirection(GridPosition currentPosition, Direction direction) {
        return new GridPosition(currentPosition.x() + direction.x, currentPosition.y() + direction.y);
    }

    private record GridPosition(int x, int y) {
    }

    private record MovementCommand(Direction direction, int numberOfSpaces) {
    }

    private enum Direction {
        UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0);

        public final int x;
        public final int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}