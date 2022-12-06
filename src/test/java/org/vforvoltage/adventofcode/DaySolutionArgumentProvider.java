package org.vforvoltage.adventofcode;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.vforvoltage.adventofcode.year2022.days.Day1;
import org.vforvoltage.adventofcode.year2022.days.Day2;
import org.vforvoltage.adventofcode.year2022.days.Day3;
import org.vforvoltage.adventofcode.year2022.days.Day4;
import org.vforvoltage.adventofcode.year2022.days.Day5;
import org.vforvoltage.adventofcode.year2022.days.Day6;

import java.util.stream.Stream;

public class DaySolutionArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(new Day1(), 72602, 207410),
                Arguments.of(new Day2(), 9241, 14610),
                Arguments.of(new Day3(), 7766, 2415),
                Arguments.of(new Day4(), 556L, 876L),
                Arguments.of(new Day5(), "PTWLTDSJV", "WZMFVGGZP"),
                Arguments.of(new Day6(), 1647, 2447));
    }
}