package org.vforvoltage.adventofcode.year2022.days;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.vforvoltage.adventofcode.year2022.Day2022;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day16 extends Day2022 {
    public Day16() {
        super(16);
    }

    @Override
    public Object part1() {
        String input = getTodaysInput();

        ValveGraph valveGraph = generateValveGraph(input);
        List<Valve> valvesToOpen = getValvesToOpen(valveGraph);
        valvesToOpen.add(valveGraph.getValve("AA"));
        Map<String, Map<String, Deque<String>>> paths = buildPathMap(valvesToOpen, valveGraph);

        valvesToOpen.remove(valveGraph.getValve("AA"));
        List<Deque<Valve>> pathOptions = new ArrayList<>();
        List<Valve> lessValves = valvesToOpen.stream().limit(99).collect(Collectors.toList());
        getPathOptions(lessValves, new ArrayDeque<>(), pathOptions, 30, paths);

        int maxPressureReleased = 0;
        int pathsChecked = 0;
        for (Deque<Valve> path : pathOptions) {
            if (pathsChecked == 294) {
                int x = 0;
            }
            path.addFirst(valveGraph.getValve("AA"));
            final Pair<Integer, StringBuilder> pressureReleasedByPath = getPressureReleasedByPath(path, paths, 30, valveGraph);
            int pressureReleasedFromPath = pressureReleasedByPath.getLeft();
            if (pressureReleasedFromPath > maxPressureReleased) {
                maxPressureReleased = pressureReleasedFromPath;
                System.out.println(pressureReleasedByPath.getRight());
            }
            pathsChecked++;
        }

        return maxPressureReleased;
    }

    @Override
    public Object part2() {
        String input = getTodaysInput();

        ValveGraph valveGraph = generateValveGraph(input);
        List<Valve> valvesToOpen = getValvesToOpen(valveGraph);
        valvesToOpen.add(valveGraph.getValve("AA"));
        Map<String, Map<String, Deque<String>>> paths = buildPathMap(valvesToOpen, valveGraph);

        valvesToOpen.remove(valveGraph.getValve("AA"));
        List<Deque<Valve>> pathOptions = new ArrayList<>();
        List<Valve> lessValves = valvesToOpen.stream().limit(99).collect(Collectors.toList());
        getPathOptions(lessValves, new ArrayDeque<>(), pathOptions, 26, paths);

        List<Pair<Deque<Valve>, Integer>> pathValues = new ArrayList<>();
        int pathsChecked = 0;
        for (int i = 0; i < pathOptions.size(); i++) {
            Deque<Valve> path = new ArrayDeque<>(pathOptions.get(i));
            path.addFirst(valveGraph.getValve("AA"));
            final Pair<Integer, StringBuilder> pressureReleasedByPath = getPressureReleasedByPath(path, paths, 26, valveGraph);
            pathValues.add(Pair.of(pathOptions.get(i), pressureReleasedByPath.getLeft()));
            pathsChecked++;
        }

        pathValues = pathValues.stream()
                .sorted(Comparator.comparing(Pair::getRight))
                .collect(Collectors.toList());

        int max = 0;
        for (int i = pathValues.size() - 1; i >= 1; i--) {
            Pair<Deque<Valve>, Integer> pathValue1 = pathValues.get(i);
            if (pathValue1.getRight() + pathValues.get(i - 1).getRight() < max) {
                break;
            }
            for (int j = i - 1; j >= 0; j--) {
                Pair<Deque<Valve>, Integer> pathValue2 = pathValues.get(j);
                if (pathValue1.getRight() + pathValue2.getRight() < max) {
                    break;
                }
                if (!CollectionUtils.containsAny(pathValue1.getLeft(), pathValue2.getLeft())) {
                    int total = pathValue1.getRight() + pathValue2.getRight();
                    if (total > max) {
                        max = total;
                    }
                    break;
                }
            }
        }
        return max;
    }

    private Pair<Integer, StringBuilder> getPressureReleasedByPath(Deque<Valve> chosenPath, Map<String, Map<String, Deque<String>>> pathsBetweenValves, int totalMinutes, ValveGraph valveGraph) {
        StringBuilder sb = new StringBuilder();
        int totalPressureReleased = 0;
        int openPressureValvesTotal = 0;
        Valve currentValve = chosenPath.removeFirst();
        Valve targetValve = chosenPath.removeFirst();
        Deque<String> valvesToVisit = new ArrayDeque<>(pathsBetweenValves.get(currentValve.getName()).get(targetValve.getName()));
        valvesToVisit.removeFirst();
        List<Valve> openValves = new ArrayList<>();
        for (int i = 1; i <= totalMinutes; i++) {
            sb.append("== Minute ").append(i).append(" ==").append("\n");
            if (openValves.size() == 0) {
                sb.append("No valves are open.").append("\n");
            } else if (openValves.size() == 1) {
                sb.append("Valve ").append(openValves.get(0).getName()).append(" is open, releasing ").append(openPressureValvesTotal).append(" pressure.").append("\n");
            } else {
                sb.append("Valves ").append(openValves.stream().map(Valve::getName).sorted().collect(Collectors.joining(","))).append(" are open, releasing ").append(openPressureValvesTotal).append(" pressure.").append("\n");
            }
            //release pressure from open valves
            totalPressureReleased += openPressureValvesTotal;
            //check if current valve should be opened

            if (currentValve.equals(targetValve)) {
                //if current valve releases pressure, open it
                sb.append("You open valve ").append(currentValve.getName()).append(".\n");
                openPressureValvesTotal += currentValve.getRate();
                currentValve.open = true;
                openValves.add(currentValve);
                //then get our next target
                targetValve = Optional.ofNullable(chosenPath.pollFirst()).orElse(valveGraph.getValve("AA"));
                valvesToVisit = new ArrayDeque<>(pathsBetweenValves.get(currentValve.getName()).get(targetValve.getName()));
                valvesToVisit.removeFirst();

            } else {
                currentValve = valveGraph.getValve(valvesToVisit.removeFirst());
                sb.append("You move to valve ").append(currentValve.getName()).append(".\n");
            }

            sb.append("\n");
        }
        return Pair.of(totalPressureReleased, sb);
    }

    private void getPathOptions(List<Valve> valvesToOpen, Deque<Valve> currentPath, List<Deque<Valve>> pathOptions, int maxLength, Map<String, Map<String, Deque<String>>> shortestPaths) {
        if (getTraversalLengthOfPath(currentPath, shortestPaths) >= maxLength || valvesToOpen.isEmpty()) {
            pathOptions.add(currentPath);
        } else {
            for (int i = 0; i < valvesToOpen.size(); i++) {
                Deque<Valve> newPath = new ArrayDeque<>(currentPath);
                newPath.addLast(valvesToOpen.get(i));
                List<Valve> reducedValvesToOpen = new ArrayList<>(valvesToOpen);
                reducedValvesToOpen.remove(i);
                getPathOptions(reducedValvesToOpen, newPath, pathOptions, maxLength, shortestPaths);
            }
        }
    }

    private int getTraversalLengthOfPath(Deque<Valve> currentPath, Map<String, Map<String, Deque<String>>> shortestPaths) {
        final Iterator<Valve> iterator = currentPath.iterator();
        int totalTraversalLength = 0;
        if (iterator.hasNext()) {
            Valve first = iterator.next();
            while (iterator.hasNext()) {
                Valve current = iterator.next();
                final Map<String, Deque<String>> stringDequeMap = shortestPaths.get(first.getName());
                final Deque<String> strings = stringDequeMap.get(current.getName());
                final int size = strings.size();
                totalTraversalLength += size;
                first = current;
            }
        }
        return totalTraversalLength;
    }

    private Map<String, Map<String, Deque<String>>> buildPathMap(List<Valve> valvesToOpen, ValveGraph valveGraph) {
        Map<String, Map<String, Deque<String>>> paths = new HashMap<>();
        for (Valve valve : valvesToOpen) {
            paths.put(valve.getName(), generatePaths(valve.getName(), valvesToOpen, valveGraph));
        }
        return paths;
    }

    private List<Valve> getValvesToOpen(ValveGraph valveGraph) {
        return valveGraph.getValves().stream()
                .filter(valve -> valve.getRate() > 0)
                .sorted(Comparator.comparingInt(Valve::getRate).reversed())
                .collect(Collectors.toList());
    }

    private ValveGraph generateValveGraph(String input) {
        ValveGraph valveGraph = new ValveGraph();

        input.lines().forEach(s -> {
            Valve valve = new Valve(StringUtils.substringBetween(s, "Valve ", " has"), Integer.parseInt(StringUtils.substringBetween(s, "rate=", ";")));
            valveGraph.addVertex(valve);

            if (s.contains(",")) {
                Arrays.stream(StringUtils.substringAfter(s, "tunnels lead to valves ").split(",")).forEach(connection -> {
                    valveGraph.addEdge(valve.name, connection.trim());
                });
            } else {
                valveGraph.addEdge(valve.name, s.substring(s.length() - 2));
            }
        });

        return valveGraph;
    }

    private Map<String, Deque<String>> generatePaths(String start, List<Valve> valvesToOpen, ValveGraph valveGraph) {
        Map<String, Deque<String>> pathMap = new HashMap<>();
        for (Valve valveToOpen : valvesToOpen) {
            Deque<String> path = new ArrayDeque<>();
            path.addLast(start);
            pathMap.put(valveToOpen.name, findShortestPath(path, valveToOpen.name, valveGraph));
        }
        return pathMap;
    }

    private Deque<String> findShortestPath(Deque<String> pathSoFar, String destinationValve, ValveGraph valveGraph) {
        final String currentValve = pathSoFar.getLast();
        final List<String> adjacentValves = valveGraph.getAdjVertices(currentValve).stream()
                .filter(s -> !pathSoFar.contains(s))
                .toList();

        if (pathSoFar.getLast().equals(destinationValve)) {
            return pathSoFar;
        } else if (adjacentValves.contains(destinationValve)) {
            pathSoFar.addLast(destinationValve);
            return pathSoFar;
        }

        if (adjacentValves.size() > 0) {
            Deque<String> shortestPathCandidate = null;
            for (String valve : adjacentValves) {
                Deque<String> testPath = new ArrayDeque<>(pathSoFar);
                testPath.add(valve);
                testPath = findShortestPath(testPath, destinationValve, valveGraph);
                if ((shortestPathCandidate == null || testPath.size() < shortestPathCandidate.size()) && testPath.getLast().equals(destinationValve)) {
                    shortestPathCandidate = testPath;
                }
            }

            if (shortestPathCandidate != null) {
                return shortestPathCandidate;
            }
        }

        return pathSoFar;
    }

    private static class Valve {
        private final String name;
        private final int rate;
        private boolean open = false;

        public Valve(String name, int rate) {
            this.name = name;
            this.rate = rate;
        }

        public String getName() {
            return name;
        }

        public int getRate() {
            return rate;
        }

        public boolean isOpen() {
            return open;
        }

        public void close() {
            open = false;
        }

        @Override
        public String toString() {
            return "Valve{name='%s', rate=%d, open=%s}".formatted(name, rate, open);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Valve valve = (Valve) o;
            return name.equals(valve.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    private static class ValveGraph {
        Map<String, List<String>> vertices = new HashMap<>();
        Map<String, Valve> valveMap = new HashMap<>();

        void addVertex(Valve valve) {
            vertices.putIfAbsent(valve.name, new ArrayList<>());
            valveMap.put(valve.name, valve);
        }

        void addEdge(String v1, String v2) {
            vertices.get(v1).add(v2);
        }

        List<String> getAdjVertices(String name) {
            return vertices.get(name);
        }

        Valve getValve(String name) {
            return valveMap.get(name);
        }

        Collection<Valve> getValves() {
            return valveMap.values();
        }
    }
}