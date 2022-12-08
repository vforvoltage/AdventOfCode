package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.util.Collections.reverseOrder;

public class Day1 extends Day2022 {
    public Day1() {
        super(1);
    }

    @Override
    public Object part1() {
        return getElfTotals().max().orElseThrow();
    }

    @Override
    public Object part2() {
        return getElfTotals().boxed().sorted(reverseOrder()).limit(3).mapToInt(Integer::intValue).sum();
    }

    private IntStream getElfTotals() {
        return Arrays.stream(getTodaysInput().split("\n\n"))
                .map(s -> s.lines().mapToInt(Integer::parseInt))
                .mapToInt(IntStream::sum);
    }
}