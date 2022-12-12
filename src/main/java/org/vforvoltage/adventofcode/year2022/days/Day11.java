package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

public class Day11 extends Day2022 {
    public Day11() {
        super(11);
    }

    @Override
    public Object part1() {
        String input = getTodaysInput();

        final Map<Integer, Monkey> monkeys = Arrays.stream(input.split("\n\n"))
                .map(this::inputToMonkey)
                .collect(Collectors.toMap(Monkey::getId, Function.identity()));

        for (int i = 1; i <= 20; i++) {
            for (int j = 0; j < monkeys.size(); j++) {
                Monkey monkey = monkeys.get(j);
                while (monkey.getItems().size() > 0) {
                    final Long item = monkey.inspectNextDivideByThree();
                    final int nextMonkey = monkey.removeAndGetNextMonkey();
                    monkeys.get(nextMonkey).addItem(item);
                }
            }
        }
        final List<Long> collect = monkeys.values().stream().map(Monkey::getItemsInspected).sorted(Comparator.reverseOrder()).toList();
        return collect.get(0) * collect.get(1);
    }

    @Override
    public Object part2() {
        String input = getTodaysInput();

        final Map<Integer, Monkey> monkeys = Arrays.stream(input.split("\n\n"))
                .map(this::inputToMonkey)
                .collect(Collectors.toMap(Monkey::getId, Function.identity()));

        for (int i = 1; i <= 10000; i++) {
            for (int j = 0; j < monkeys.size(); j++) {
                Monkey monkey = monkeys.get(j);
                while (monkey.getItems().size() > 0) {
                    Long item = monkey.inspectNext();
                    final int nextMonkey = monkey.removeAndGetNextMonkey();
                    while (item > 9699690) {
                        item = item % 9699690;
                    }
                    monkeys.get(nextMonkey).addItem(item);
                }
            }
        }

        final List<Long> collect = monkeys.values().stream().map(Monkey::getItemsInspected).sorted(Comparator.reverseOrder()).toList();
        return collect.get(0) * collect.get(1);
    }

    private Monkey inputToMonkey(String inputBlock) {
        final String[] lines = inputBlock.split("\n");

        int id = parseId(lines[0]);
        Deque<Long> items = parseItems(lines[1]);
        Function<Long, Long> operation = parseOperation(lines[2]);
        Function<Long, Integer> test = parseTest(lines[3], lines[4], lines[5]);
        return new Monkey(id, items, operation, test);
    }

    private Function<Long, Integer> parseTest(String test, String trueValue, String falseValue) {
        final int divisor = Integer.parseInt(test.replace("Test: divisible by ", "").trim());
        final int truelong = Integer.parseInt(trueValue.replace("If true: throw to monkey ", "").trim());
        final int falselong = Integer.parseInt(falseValue.replace("If false: throw to monkey ", "").trim());
        return (x -> x % divisor == 0 ? truelong : falselong);
    }

    private Function<Long, Long> parseOperation(String line) {
        String[] function = line.replace("Operation: new = old ", "").trim().split(" ");
        boolean old = function[1].equals("old");
        switch (function[0]) {
            case "+":
                if (old) {
                    return x -> x + x;
                } else {
                    return x -> x + Long.parseLong(function[1]);
                }
            case "*":
                if (old) {
                    return x -> x * x;
                } else {
                    return x -> (x * Long.parseLong(function[1]));
                }
            default:
                throw new IllegalArgumentException();
        }
    }

    private Deque<Long> parseItems(String line) {
        return Arrays.stream(line
                        .split(":")[1]
                        .trim()
                        .split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(toCollection(ArrayDeque::new));
    }

    private int parseId(String line) {
        return Integer.parseInt(line.split(" ")[1].replace(":", ""));
    }

    private static class Monkey {
        private final int id;
        private final Deque<Long> items;
        private final Function<Long, Long> inspectionOperation;
        private final Function<Long, Integer> testAndIdentifyNextMonkey;
        private long itemsInspected = 0;

        private Monkey(int id, Deque<Long> items, Function<Long, Long> inspectionOperation, Function<Long, Integer> testAndIdentifyNextMonkey) {
            this.id = id;
            this.items = items;
            this.inspectionOperation = inspectionOperation;
            this.testAndIdentifyNextMonkey = testAndIdentifyNextMonkey;
        }

        public long getItemsInspected() {
            return itemsInspected;
        }

        public int getId() {
            return id;
        }

        public Deque<Long> getItems() {
            return items;
        }

        public Long inspectNext() {
            itemsInspected++;
            final Long apply = inspectionOperation.apply(items.removeFirst());
            items.addFirst(apply);
            return apply;
        }

        public Long inspectNextDivideByThree() {
            itemsInspected++;
            final Long apply = (inspectionOperation.apply(items.removeFirst())) / 3;
            items.addFirst(apply);
            return apply;
        }

        public int removeAndGetNextMonkey() {
            return testAndIdentifyNextMonkey.apply(items.removeFirst());
        }

        public void addItem(Long value) {
            items.addLast(value);
        }
    }
}