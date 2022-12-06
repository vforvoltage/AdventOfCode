package org.vforvoltage.adventofcode.year2015;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.vforvoltage.adventofcode.year2015.days.Day1;

import java.util.stream.Stream;

public class DaySolutionArgumentProvider2015 implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(new Day1(), 138, 1771));
    }
}