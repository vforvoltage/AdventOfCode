package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.concurrent.atomic.AtomicInteger;

public class Day2 extends Day2022 {
    public Day2() {
        super(2);
    }

    @Override
    public Object part1() {
        String input = getTodaysInput();

        AtomicInteger myTotalScore = new AtomicInteger();
        input.lines().forEach(game -> {
            Roshambo me = getPiece(game.charAt(2));
            Roshambo them = getPiece(game.charAt(0));
            Outcome gameOutcome = me.getOutcomeAgainst(them);
            int score = calculateScore(me, gameOutcome);
            myTotalScore.addAndGet(score);
        });

        return myTotalScore.get();
    }

    @Override
    public Object part2() {
        String input = getTodaysInput();

        AtomicInteger myTotalScore = new AtomicInteger();
        input.lines().forEach(game -> {
            Roshambo them = getPiece(game.charAt(0));
            Outcome gameOutcome = getOutcome(game.charAt(2));
            Roshambo me = getPieceRequiredForResult(them, gameOutcome);
            int score = calculateScore(me, gameOutcome);
            myTotalScore.addAndGet(score);
        });

        return myTotalScore.get();
    }

    private Roshambo getPieceRequiredForResult(Roshambo them, Outcome gameOutcome) {
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

    private Roshambo getPiece(char inputCharacter) {
        return switch (inputCharacter) {
            case 'A', 'X' -> Roshambo.ROCK;
            case 'B', 'Y' -> Roshambo.PAPER;
            case 'C', 'Z' -> Roshambo.SCISSORS;
            default -> throw new IllegalArgumentException("Can't handle input: %s".formatted(inputCharacter));
        };
    }

    private int calculateScore(Roshambo me, Outcome gameOutcome) {
        return me.pointValue + gameOutcome.pointValue;
    }

    private enum Roshambo {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        public final int pointValue;

        Roshambo(int pointValue) {
            this.pointValue = pointValue;
        }

        Outcome getOutcomeAgainst(Roshambo opponent) {
            if (this.equals(opponent)) {
                return Outcome.DRAW;
            } else if (this.winsAgainst(opponent)) {
                return Outcome.WIN;
            } else return Outcome.LOSE;
        }

        boolean winsAgainst(Roshambo opponent) {
            return this.getWinsAgainst().equals(opponent);
        }

        public Roshambo getWinsAgainst() {
            return switch (this) {
                case ROCK -> SCISSORS;
                case PAPER -> ROCK;
                case SCISSORS -> PAPER;
            };
        }

        public Roshambo getLosesAgainst() {
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