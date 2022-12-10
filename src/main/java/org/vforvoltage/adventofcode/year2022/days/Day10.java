package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.ArrayList;
import java.util.List;

public class Day10 extends Day2022 {
    public Day10() {
        super(10);
    }

    @Override
    public Object part1() {
        String input = getTodaysInput();

        int currentCycle = 1;
        int x = 1;
        List<Integer> signalStrengths = new ArrayList<>();
        for (String line : input.split("\n")) {
            if (currentCycle == 20 || (currentCycle - 20) % 40 == 0) {
                signalStrengths.add(x * currentCycle);
            }
            if (!line.equals("noop")) {
                final String[] addx = line.split(" ");
                currentCycle++;
                if (currentCycle == 20 || (currentCycle - 20) % 40 == 0) {
                    signalStrengths.add(x * currentCycle);
                }
                x += Integer.parseInt(addx[1]);
            }
            currentCycle++;
        }
        return signalStrengths.stream().mapToInt(i -> i).sum();
    }

    @Override
    public Object part2() {
        return null;
    }
}