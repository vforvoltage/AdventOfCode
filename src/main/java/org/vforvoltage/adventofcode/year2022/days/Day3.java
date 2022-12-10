package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;

public class Day3 extends Day2022 {
    public Day3() {
        super(3);
    }

    private final String ALPHABET = "-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public Object part1() {
        String input = getTodaysInput();
        return input.lines()
                .map(line -> {
                    String s1 = line.substring(0, (line.length() / 2));
                    String s2 = line.substring((line.length() / 2));
                    return findCommonCharactersBetweenStrings(s1, s2);
                })
                .mapToInt(ALPHABET::indexOf)
                .sum();
    }

    @Override
    public Object part2() {
        final String input = getTodaysInput();
        final int chunkSize = 3;
        final AtomicInteger counter = new AtomicInteger();

        return input.lines()
                .collect(groupingBy(s -> counter.getAndIncrement() / chunkSize))
                .values()
                .stream()
                .map(this::findCommonCharactersBetweenStrings)
                .mapToInt(ALPHABET::indexOf)
                .sum();
    }

    private String findCommonCharactersBetweenStrings(List<String> strings) {
        return strings.stream().reduce(ALPHABET, this::findCommonCharactersBetweenStrings);
    }

    private String findCommonCharactersBetweenStrings(String s1, String s2) {
        return s1.chars()
                .filter(c1 -> s2.chars().anyMatch(c2 -> c1 == c2))
                .distinct()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}