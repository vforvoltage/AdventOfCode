package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

public class Day6 extends Day2022 {
    public Day6() {
        super(6);
    }

    @Override
    public Object part1() {
        String input = getTodaysInput();
        return findIndexOfFirstStringWithNUniqueCharacters(input, 4);
    }

    @Override
    public Object part2() {
        String input = getTodaysInput();
        return findIndexOfFirstStringWithNUniqueCharacters(input, 14);
    }

    private int findIndexOfFirstStringWithNUniqueCharacters(String string, int n) {
        final char[] chars = string.toCharArray();

        Deque<Character> lastFourOrdered = new ArrayDeque<>();
        for(int i = 0; i < n; i++) {
            lastFourOrdered.addLast(chars[i]);
        }

        int totalCharactersProcessed = n;
        for(int i = n; i < chars.length; i++) {
            final HashSet<Character> uniqueCharacters = new HashSet<>(lastFourOrdered);
            if(uniqueCharacters.size() == n) {
                return totalCharactersProcessed;
            } else {
                totalCharactersProcessed++;
                lastFourOrdered.removeFirst();
                lastFourOrdered.addLast(chars[i]);
            }
        }
        return totalCharactersProcessed;
    }
}