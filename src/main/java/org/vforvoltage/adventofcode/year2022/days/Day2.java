package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

public class Day2 extends Day2022 {
    public Day2() {
        super(2);
    }

    @Override
    public Object part1() {
        return getTodaysInput().lines().mapToInt(line -> {
            Shape me = getPiece(line.charAt(2));
            Shape them = getPiece(line.charAt(0));
            Outcome gameOutcome = me.getOutcomeAgainst(them);
            return calculateScore(me, gameOutcome);
        }).sum();

    }

    @Override
    public Object part2() {
        return getTodaysInput().lines().mapToInt(game -> {
            Shape them = getPiece(game.charAt(0));
            Outcome gameOutcome = getOutcome(game.charAt(2));
            Shape me = getPieceRequiredForResult(them, gameOutcome);
            return calculateScore(me, gameOutcome);
        }).sum();
    }

    private Shape getPieceRequiredForResult(Shape them, Outcome gameOutcome) {
        return switch (gameOutcome) {
            case DRAW -> them;
            case LOSE -> them.getWinsAgainst();
            case WIN -> them.getLosesAgainst();
        };
    }

    private Outcome getOutcome(char inputCharacter) {
        return switch (inputCharacter) {
            case 'X' -> Outcome.LOSE;
            case 'Y' -> Outcome.DRAW;
            case 'Z' -> Outcome.WIN;
            default -> throw new IllegalArgumentException("Can't handle input: %s".formatted(inputCharacter));
        };
    }

    private Shape getPiece(char inputCharacter) {
        return switch (inputCharacter) {
            case 'A', 'X' -> Shape.ROCK;
            case 'B', 'Y' -> Shape.PAPER;
            case 'C', 'Z' -> Shape.SCISSORS;
            default -> throw new IllegalArgumentException("Can't handle input: %s".formatted(inputCharacter));
        };
    }

    private int calculateScore(Shape me, Outcome gameOutcome) {
        return me.pointValue + gameOutcome.pointValue;
    }

    private enum Shape {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        public final int pointValue;

        Shape(int pointValue) {
            this.pointValue = pointValue;
        }

        Outcome getOutcomeAgainst(Shape opponent) {
            if (this.equals(opponent)) {
                return Outcome.DRAW;
            } else if (this.winsAgainst(opponent)) {
                return Outcome.WIN;
            } else return Outcome.LOSE;
        }

        boolean winsAgainst(Shape opponent) {
            return this.getWinsAgainst().equals(opponent);
        }

        public Shape getWinsAgainst() {
            return switch (this) {
                case ROCK -> SCISSORS;
                case PAPER -> ROCK;
                case SCISSORS -> PAPER;
            };
        }

        public Shape getLosesAgainst() {
            return switch (this) {
                case ROCK -> PAPER;
                case PAPER -> SCISSORS;
                case SCISSORS -> ROCK;
            };
        }
    }

    private enum Outcome {
        WIN(6),
        LOSE(0),
        DRAW(3);

        public final int pointValue;

        Outcome(int pointValue) {
            this.pointValue = pointValue;
        }
    }
}