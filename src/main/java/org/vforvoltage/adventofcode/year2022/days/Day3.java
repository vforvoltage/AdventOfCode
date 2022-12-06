package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.groupingBy;

public class Day3 extends Day2022 {
    public Day3() {
        super(3);
    }

    private final String ALPHABET = "-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public Object part1() {
        String input = getTodaysInput();
        final IntStream intStream = input.lines().mapToInt(line -> {
            String compartment1 = line.substring(0, (line.length() / 2));
            String compartment2 = line.substring((line.length() / 2));
            Character common = findCommonCharactersBetweenTwoStrings(compartment1, compartment2).stream().findFirst().orElseThrow();
            return ALPHABET.indexOf(common);
        });
        return intStream.sum();
    }

    @Override
    public Object part2() {
        final String input = getTodaysInput();
        final int chunkSize = 3;
        final AtomicInteger counter = new AtomicInteger();

        final Collection<List<String>> result = input.lines()
                .collect(groupingBy(s -> counter.getAndIncrement() / chunkSize))
                .values();

        final IntStream intStream = result.stream().mapToInt(group -> {
            final Set<Character> commonCharactersBetweenStrings = findCommonCharactersBetweenStrings(group);
            return ALPHABET.indexOf(commonCharactersBetweenStrings.stream().findFirst().orElseThrow());
        });

        return intStream.sum();
    }

    private Set<Character> findCommonCharactersBetweenTwoStrings(String s1, String s2) {
        Set<Character> commonCharacters = new HashSet<>();
        for (char character : s1.toCharArray()) {
            if (s2.contains(valueOf(character))) {
                commonCharacters.add(character);
            }
        }
        return commonCharacters;
    }

    private Set<Character> findCommonCharactersBetweenStrings(List<String> strings) {
        Set<Character> commonCharacters = strings.get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
        for (int i = 1; i < strings.size(); i++) {

            for(Iterator<Character> iterator = commonCharacters.iterator(); iterator.hasNext();) {
                final Character next = iterator.next();
                if(!strings.get(i).contains(String.valueOf(next))){
                    iterator.remove();
                }
            }
            commonCharacters.forEach(character -> {

            });
        }

        return commonCharacters;
    }
}