package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.Arrays;

import static java.util.Collections.reverseOrder;

public class Day1 extends Day2022 {
    public Day1() {
        super(1);
    }

    @Override
    public Object part1() {
        return Arrays.stream(getTodaysInput().split("\n\n"))
                .map(String::lines)
                .mapToInt(s -> s.mapToInt(Integer::parseInt).sum())
                .max()
                .orElseThrow();
    }

    @Override
    public Object part2() {
        return Arrays.stream(getTodaysInput().split("\n\n"))
                .map(String::lines)
                .map(s -> s.mapToInt(Integer::parseInt).sum())
                .sorted(reverseOrder())
                .limit(3)
                .mapToInt(Integer::intValue)
                .sum();
    }
}