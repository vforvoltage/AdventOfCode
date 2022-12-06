package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day5 extends Day2022 {
    public Day5() {
        super(5);
    }

    @Override
    public Object part1() {
        Map<Integer, Stack<Character>> stacks = initializeStacks();
        List<String> commands = getCommands();

        for (String command : commands) {
            Command parsed = parseCommand(command);
            for(int i = 0; i < parsed.numberOfCrates; i++) {
                stacks.get(parsed.toColumn).push(stacks.get(parsed.fromColumn).pop());
            }
        }

        StringBuilder sb = new StringBuilder();
        for(Stack<Character> stack : stacks.values()) {
            sb.append(stack.peek());
        }

        return sb.toString();
    }

    @Override
    public Object part2() {
        Map<Integer, Stack<Character>> stacks = initializeStacks();
        List<String> commands = getCommands();

        for (String command : commands) {
            Command parsed = parseCommand(command);
            Stack<Character> cratesInOrder = new Stack<>();
            for(int i = 0; i < parsed.numberOfCrates; i++) {
                cratesInOrder.push(stacks.get(parsed.fromColumn).pop());
            }
            while(cratesInOrder.size() > 0) {
                stacks.get(parsed.toColumn).push(cratesInOrder.pop());
            }
        }

        StringBuilder sb = new StringBuilder();
        for(Stack<Character> stack : stacks.values()) {
            sb.append(stack.peek());
        }

        return sb.toString();
    }

    private Map<Integer, Stack<Character>> initializeStacks() {
        String startingStage = getTodaysInput().split("\n\n")[0];
        final String[] rows = startingStage.split("\n");
        final IntStream intStream = Stream.of(rows[rows.length - 1].trim().split("\\p{Zs}+")).mapToInt(Integer::parseInt);

        Map<Integer, Stack<Character>> stackMap = new HashMap<>();
        intStream.forEach(i -> stackMap.computeIfAbsent(i, j -> new Stack<>()));

        for (int i = rows.length - 2; i >= 0; i--) {
            String row = rows[i];
            for (int j = 1; j <= stackMap.size(); j++) {
                Character crateLabel = row.charAt(1 + (4 * (j - 1)));
                if (!crateLabel.toString().isBlank()) {
                    stackMap.get(j).push(crateLabel);
                }

            }
        }
        return stackMap;
    }

    private List<String> getCommands() {
        return Arrays.asList(getTodaysInput().split("\n\n")[1].split("\n"));
    }

    private Command parseCommand(String command) {
        int numberOfCrates = Integer.parseInt(command.substring(5, command.length() - 12));
        int fromColumn = Character.getNumericValue(command.charAt(command.length() - 6));
        int toColumn = Character.getNumericValue(command.charAt(command.length() - 1));
        return new Command(numberOfCrates, fromColumn, toColumn);
    }

    private record Command(int numberOfCrates, int fromColumn, int toColumn) { }
}