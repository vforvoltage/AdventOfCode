package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import static java.lang.Integer.parseInt;

public class Day4 extends Day2022 {
    public Day4() {
        super(4);
    }

    @Override
    public Object part1() {
        return getTodaysInput()
                .lines()
                .map(this::convertToRangePair)
                .filter(this::isCompleteOverlap)
                .count();
    }

    @Override
    public Object part2() {
        return getTodaysInput()
                .lines()
                .map(this::convertToRangePair)
                .filter(this::isAnyOverlap)
                .count();
    }

    private RangePair convertToRangePair(String s) {
        final String[] split = s.split(",");
        Range r1 = convertToRange(split[0]);
        Range r2 = convertToRange(split[1]);
        return new RangePair(r1, r2);
    }

    private Range convertToRange(String s) {
        final String[] split = s.split("-");
        return new Range(parseInt(split[0]), parseInt(split[1]));
    }

    private boolean isAnyOverlap(RangePair rangePair) {
        Range range1 = rangePair.firstRange;
        Range range2 = rangePair.secondRange;
        return ((range1.startInclusive <= range2.startInclusive && range1.endInclusive >= range2.startInclusive)
                || (range1.startInclusive <= range2.endInclusive && range1.endInclusive >= range2.endInclusive))
                || ((range2.startInclusive <= range1.startInclusive && range2.endInclusive >= range1.startInclusive)
                || (range2.startInclusive <= range1.endInclusive && range2.endInclusive >= range1.endInclusive));
    }

    private boolean isCompleteOverlap(RangePair rangePair) {
        Range range1 = rangePair.firstRange;
        Range range2 = rangePair.secondRange;
        return (range1.startInclusive <= range2.startInclusive && range1.endInclusive >= range2.endInclusive) ||
                (range2.startInclusive <= range1.startInclusive && range2.endInclusive >= range1.endInclusive);
    }

    private record Range(int startInclusive, int endInclusive) {
    }

    private record RangePair(Range firstRange, Range secondRange) {
    }
}