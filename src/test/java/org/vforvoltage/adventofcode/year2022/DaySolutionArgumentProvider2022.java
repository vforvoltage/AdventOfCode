package org.vforvoltage.adventofcode.year2022;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.vforvoltage.adventofcode.year2022.days.Day1;
import org.vforvoltage.adventofcode.year2022.days.Day2;
import org.vforvoltage.adventofcode.year2022.days.Day3;
import org.vforvoltage.adventofcode.year2022.days.Day4;
import org.vforvoltage.adventofcode.year2022.days.Day5;
import org.vforvoltage.adventofcode.year2022.days.Day6;
import org.vforvoltage.adventofcode.year2022.days.Day7;
import org.vforvoltage.adventofcode.year2022.days.Day8;
import org.vforvoltage.adventofcode.year2022.days.Day9;

import java.util.stream.Stream;

public class DaySolutionArgumentProvider2022 implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(new Day1(), 72602, 207410),
                Arguments.of(new Day2(), 9241, 14610),
                Arguments.of(new Day3(), 7766, 2415),
                Arguments.of(new Day4(), 556L, 876L),
                Arguments.of(new Day5(), "PTWLTDSJV", "WZMFVGGZP"),
                Arguments.of(new Day6(), 1647, 2447),
                Arguments.of(new Day7(), 1723892L, 8474158L),
                Arguments.of(new Day8(), 1672, 327180),
                Arguments.of(new Day9(), 6271, 2458));
    }
}