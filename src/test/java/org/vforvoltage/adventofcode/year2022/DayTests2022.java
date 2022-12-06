package org.vforvoltage.adventofcode.year2022;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.vforvoltage.adventofcode.common.Day;

public class DayTests2022 {

    @ParameterizedTest
    @ArgumentsSource(DaySolutionArgumentProvider2022.class)
    public void testDays2022(Day day, Object part1Expected, Object part2Expected) {
        Assertions.assertEquals(part1Expected, day.part1());
        Assertions.assertEquals(part2Expected, day.part2());
    }
}