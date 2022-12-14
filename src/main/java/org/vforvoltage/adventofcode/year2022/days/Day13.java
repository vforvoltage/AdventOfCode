package org.vforvoltage.adventofcode.year2022.days;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;

public class Day13 extends Day2022 {
    public Day13() {
        super(13);
    }

    @Override
    public Object part1() {
        String input = getTodaysInput();

        final List<Pair<String, String>> pairs = stream(input.split("\n\n")).map(s -> {
                    final String[] split = s.split("\n");
                    return Pair.of(split[0], split[1]);
                })
                .toList();

        int indexSum = 0;
        for (int i = 0; i < pairs.size(); i++) {
            if (isPairInCorrectOrder(pairs.get(i))) {
                indexSum += (i + 1);
            }
        }
        return indexSum;
    }

    @Override
    public Object part2() {
        String input = getTodaysInput();

        List<Integer> indices = new ArrayList<>();
        Packet two = new Packet("[[2]]");
        Packet six = new Packet("[[6]]");

        final List<Packet> packets = concat(Stream.of(new Packet("[[2]]"), new Packet("[[6]]")), input.lines()
                .filter(StringUtils::isNotBlank)
                .map(Packet::new))
                .sorted()
                .toList();

        for (int i = 0; i < packets.size(); i++) {
            final Packet packet = packets.get(i);
            if (packet.equals(two) || packet.equals(six)) {
                indices.add(i + 1);
            }
        }

        return indices.get(0) * indices.get(1);

    }

    private boolean isPairInCorrectOrder(Pair<String, String> pair) {
        final Packet left = new Packet(pair.getLeft());
        final Packet right = new Packet(pair.getRight());
        return left.compareTo(right) <= 0;
    }

    private static final class Packet implements Comparable<Packet> {
        private Integer value;
        private final List<Packet> nestedPackets = new ArrayList<>();
        private String createdWith;

        public Packet() {

        }

        public Packet(String input) {
            createdWith = input;
            if (!input.startsWith("[")) {
                value = Integer.parseInt(input);
            } else {
                fromList(input);
            }
        }

        private void fromList(String input) {
            String oneLevelDown = input.substring(1, input.length() - 1);
            int depth = 0;
            StringBuilder sb = new StringBuilder();
            for (final char c : oneLevelDown.toCharArray()) {
                if (depth == 0 && c == ',') {
                    nestedPackets.add(new Packet(sb.toString()));
                    sb = new StringBuilder();
                } else {
                    if (c == '[') {
                        depth++;
                    } else if (c == ']') {
                        depth--;
                    }
                    sb.append(c);
                }
            }
            if (!sb.isEmpty()) {
                nestedPackets.add(new Packet(sb.toString()));
            }
        }

        private Packet wrap(Packet packet) {
            Packet p = new Packet();
            p.nestedPackets.add(packet);
            return p;
        }

        @Override
        public String toString() {
            return createdWith;
        }

        @Override
        public int compareTo(Packet that) {
            if (this.value != null && that.value != null) {
                return this.value.compareTo(that.value);
            } else if (this.value == null && that.value == null) {
                for (int i = 0; i < Math.min(this.nestedPackets.size(), that.nestedPackets.size()); i++) {
                    final int compare = this.nestedPackets.get(i).compareTo(that.nestedPackets.get(i));
                    if (compare != 0) {
                        return compare;
                    }
                }
                return Integer.compare(this.nestedPackets.size(), that.nestedPackets.size());
            } else if (this.value != null) {
                return wrap(this).compareTo(that);
            } else {
                return this.compareTo(wrap(that));
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Packet packet = (Packet) o;
            return Objects.equals(createdWith, packet.createdWith);
        }

        @Override
        public int hashCode() {
            return Objects.hash(createdWith);
        }
    }
}