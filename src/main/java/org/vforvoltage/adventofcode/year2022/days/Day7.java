package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7 extends Day2022 {
    public Day7() {
        super(7);
    }

    @Override
    public Object part1() {
        String input = getTodaysInput();
        final List<String> lines = input.lines().toList();

        Directory currentDirectory = new Directory(null, null, new HashSet<>(), new HashSet<>());

        for (String line : lines) {
            if (isCommand(line)) {
                currentDirectory = handleCommand(currentDirectory, parseLineToCommandInput(line));
            } else {
                final String[] lineTokens = line.split(" ");
                if ("dir".equals(lineTokens[0])) {
                    currentDirectory.containedDirectories.add(new Directory(lineTokens[1], currentDirectory, new HashSet<>(), new HashSet<>()));
                } else {
                    addFile(currentDirectory, Integer.parseInt(lineTokens[0]), lineTokens[1]);
                }
            }
        }

        while (currentDirectory.parentDirectory != null) {
            currentDirectory = currentDirectory.parentDirectory;
        }
        final Set<Directory> collect = currentDirectory.getAllChildDirectories().filter(directory -> directory.getSize() < 100000L).collect(Collectors.toSet());
        return collect.stream().mapToLong(Directory::getSize).sum();
    }

    @Override
    public Object part2() {
        String input = getTodaysInput();
        final List<String> lines = input.lines().toList();

        Directory currentDirectory = new Directory(null, null, new HashSet<>(), new HashSet<>());

        for (String line : lines) {
            if (isCommand(line)) {
                currentDirectory = handleCommand(currentDirectory, parseLineToCommandInput(line));
            } else {
                final String[] lineTokens = line.split(" ");
                if ("dir".equals(lineTokens[0])) {
                    currentDirectory.containedDirectories.add(new Directory(lineTokens[1], currentDirectory, new HashSet<>(), new HashSet<>()));
                } else {
                    addFile(currentDirectory, Integer.parseInt(lineTokens[0]), lineTokens[1]);
                }
            }
        }

        while (currentDirectory.parentDirectory != null) {
            currentDirectory = currentDirectory.parentDirectory;
        }
        long l = currentDirectory.getSize();
        long spaceLeft = 70000000 - l;
        long requiredSpace = 30000000;
        long needToDelete = requiredSpace - spaceLeft;
        return currentDirectory.getAllChildDirectories().mapToLong(Directory::getSize).filter(size -> size >= needToDelete).sorted().findFirst().orElseThrow();
    }


    private CommandInput parseLineToCommandInput(String line) {
        final String[] commandTokens = line.split(" ");

        if (!"$".equals(commandTokens[0])) {
            throw new IllegalArgumentException();
        }

        final Command command = Command.valueOf(commandTokens[1].toUpperCase());
        if (Command.LS.equals(command)) {
            return new CommandInput(command, Optional.empty());
        } else if (Command.CD.equals(command)) {
            return new CommandInput(command, Optional.of(commandTokens[2]));
        }
        throw new IllegalArgumentException();
    }


    private boolean isCommand(String line) {
        return '$' == line.charAt(0);
    }

    private Directory handleCommand(Directory currentDirectory, CommandInput input) {
        if (Command.LS.equals(input.command)) {
            return currentDirectory;
        } else if (Command.CD.equals(input.command)) {
            return changeDirectory(currentDirectory, input.directoryName.orElseThrow());
        }
        throw new IllegalArgumentException();
    }

    private Directory changeDirectory(Directory currentDirectory, String directoryName) {
        if ("..".equals(directoryName)) {
            return currentDirectory.parentDirectory;
        } else {
            if (currentDirectory.name == null) {
                return new Directory(directoryName, null, new HashSet<>(), new HashSet<>());
            }
            return currentDirectory.containedDirectories.stream()
                    .filter(dir -> dir.name.equals(directoryName))
                    .findAny()
                    .orElse(new Directory(directoryName, currentDirectory, new HashSet<>(), new HashSet<>()));
        }
    }

    private void addFile(Directory currentDirectory, int fileSize, String fileName) {
        if (currentDirectory.files.stream().anyMatch(file -> file.name.equals(fileName))) {
            throw new IllegalArgumentException();
        }

        currentDirectory.files.add(new ElfFile(fileName, fileSize));
    }

    private enum Command {
        CD, LS
    }

    private record Directory(String name, Directory parentDirectory, Set<Directory> containedDirectories,
                             Set<ElfFile> files) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Directory directory = (Directory) o;
            return Objects.equals(name, directory.name) && Objects.equals(parentDirectory.name, directory.parentDirectory.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, parentDirectory.name);
        }

        @Override
        public String toString() {
            return "Directory{" +
                    "name='" + name + '\'' +
                    '}';
        }

        public long getSize() {
            return files.stream().mapToLong(file -> file.size).sum() + containedDirectories.stream().mapToLong(Directory::getSize).sum();
        }

        public Stream<Directory> getAllChildDirectories() {
            return Stream.concat(containedDirectories.stream(), containedDirectories.stream().flatMap(Directory::getAllChildDirectories));
        }
    }

    private record ElfFile(String name, int size) {
    }

    private record CommandInput(Command command, Optional<String> directoryName) {
    }
}