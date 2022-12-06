package org.vforvoltage.adventofcode.year2015.days;

import org.vforvoltage.adventofcode.year2015.Day2015;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class Day1 extends Day2015 {
    public Day1() {
        super(1);
    }

    @Override
    public Object part1() {
        return getTodaysInput().chars().map(c -> c == '(' ? 1 : -1).sum();
    }

    @Override
    public Object part2() {
        String input = getTodaysInput();
        CharacterIterator iterator = new StringCharacterIterator(input);
        int floor = 0;
        int count = 0;
        while(floor > -1) {
            floor += iterator.current() == '(' ? 1 : -1;
            count++;
            iterator.next();
        }
        return count;
    }
}