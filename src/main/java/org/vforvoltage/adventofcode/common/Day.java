package org.vforvoltage.adventofcode.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class Day {

    protected final int year;
    protected final int day;

    public Day(int year, int day) {
        this.year = year;
        this.day = day;
    }

    public static String getResourceAsString(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }

    private String getDayPath() {
        return "src/main/resources/%d/day%d.txt".formatted(year, day);
    }

    protected String getTodaysInput() {
        try {
            return getResourceAsString(getDayPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public abstract Object part1();

    public abstract Object part2();

    public void printParts() {
        System.out.println("Part 1: " + part1());
        System.out.println("Part 2: " + part2());
    }
}