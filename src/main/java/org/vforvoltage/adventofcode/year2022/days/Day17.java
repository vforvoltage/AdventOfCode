package org.vforvoltage.adventofcode.year2022.days;

import org.vforvoltage.adventofcode.year2022.Day2022;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day17 extends Day2022 {
    public Day17() {
        super(17);
    }

    @Override
    public Object part1() {
        String input = getTodaysInput();
        StringCharacterIterator iterator = new StringCharacterIterator(input);

        List<char[]> wall = new ArrayList<>();
        for (int i = 0; i < 2022; i++) {
            wall.addAll(0, getRock(i));
            settle(wall, iterator);
        }
        return wall.size();
    }

    private void settle(List<char[]> wall, StringCharacterIterator iterator) {
        boolean settled = false;
        while (!settled) {
            shift(wall, iterator);
            settled = dropOne(wall);
            trim(wall);
//            settled = !canDescend(wall);
        }
        convertToSettled(wall);
    }

    private void shift(List<char[]> wall, StringCharacterIterator iterator) {
        while (iterator.current() != '>' && iterator.current() != '<') {
            if (iterator.getIndex() == iterator.getEndIndex()) {
                iterator.setIndex(0);
            } else {
                iterator.next();
            }
        }

        switch (iterator.current()) {
            case '>' -> shiftRight(wall);
            case '<' -> shiftLeft(wall);
            default -> throw new IllegalArgumentException("Invalid character");
        }

        if (iterator.getIndex() == iterator.getEndIndex()) {
            iterator.setIndex(0);
        } else {
            iterator.next();
        }
    }

    private void shiftLeft(List<char[]> wall) {
        Map<Integer, List<Integer>> currentRockLocations = getCurrentRockLocations(wall);
        boolean canMove = canMoveLeft(wall);
        if (canMove) {
            for (Map.Entry<Integer, List<Integer>> entry : currentRockLocations.entrySet()) {
                for (Integer i : entry.getValue()) {
                    wall.get(entry.getKey())[i] = '_';
                }
            }
            for (Map.Entry<Integer, List<Integer>> entry : currentRockLocations.entrySet()) {
                for (Integer i : entry.getValue()) {
                    wall.get(entry.getKey())[i - 1] = '@';
                }
            }
        }
    }

    private void shiftRight(List<char[]> wall) {
        Map<Integer, List<Integer>> currentRockLocations = getCurrentRockLocations(wall);
        boolean canMove = canMoveRight(wall);
        if (canMove) {
            for (Map.Entry<Integer, List<Integer>> entry : currentRockLocations.entrySet()) {
                for (Integer i : entry.getValue()) {
                    wall.get(entry.getKey())[i] = '_';
                }
            }
            for (Map.Entry<Integer, List<Integer>> entry : currentRockLocations.entrySet()) {
                for (Integer i : entry.getValue()) {
                    wall.get(entry.getKey())[i + 1] = '@';
                }
            }
        }
    }

    private boolean dropOne(List<char[]> wall) {
        Map<Integer, List<Integer>> currentRockLocations = getCurrentRockLocations(wall);
        boolean canDescend = canDescend(wall);
        if (canDescend) {
            for (Map.Entry<Integer, List<Integer>> entry : currentRockLocations.entrySet()) {
                for (Integer i : entry.getValue()) {
                    wall.get(entry.getKey())[i] = '_';
                }
            }
            for (Map.Entry<Integer, List<Integer>> entry : currentRockLocations.entrySet()) {
                for (Integer i : entry.getValue()) {
                    wall.get(entry.getKey() + 1)[i] = '@';
                }
            }
        }
        return !canDescend;
    }

    private boolean canDescend(List<char[]> wall) {
        Map<Integer, List<Integer>> currentRockLocations = getCurrentRockLocations(wall);
        boolean allClear = true;
        for (Map.Entry<Integer, List<Integer>> entry : currentRockLocations.entrySet()) {
            for (Integer i : entry.getValue()) {
                if (entry.getKey() + 1 == wall.size() || wall.get(entry.getKey() + 1)[i] == '#') {
                    allClear = false;
                    break;
                }
            }
        }
        return allClear;
    }

    private boolean canMoveRight(List<char[]> wall) {
        Map<Integer, List<Integer>> currentRockLocations = getCurrentRockLocations(wall);
        boolean allClear = true;
        for (Map.Entry<Integer, List<Integer>> entry : currentRockLocations.entrySet()) {
            for (Integer i : entry.getValue()) {
                if (i + 1 == wall.get(entry.getKey()).length || wall.get(entry.getKey())[i + 1] == '#') {
                    allClear = false;
                    break;
                }
            }
        }
        return allClear;
    }

    private boolean canMoveLeft(List<char[]> wall) {
        Map<Integer, List<Integer>> currentRockLocations = getCurrentRockLocations(wall);
        boolean allClear = true;
        for (Map.Entry<Integer, List<Integer>> entry : currentRockLocations.entrySet()) {
            for (Integer i : entry.getValue()) {
                if (i == 0 || wall.get(entry.getKey())[i - 1] == '#') {
                    allClear = false;
                    break;
                }
            }
        }
        return allClear;
    }

    private Map<Integer, List<Integer>> getCurrentRockLocations(List<char[]> wall) {
        Map<Integer, List<Integer>> currentRockLocations = new HashMap<>();
        for (int i = 0; i < wall.size(); i++) {
            for (int j = 0; j < 7; j++) {
                if (wall.get(i)[j] == '@') {
                    currentRockLocations.computeIfAbsent(i, k -> new ArrayList<>()).add(j);
                }
            }
        }
        return currentRockLocations;
    }

    private void convertToSettled(List<char[]> wall) {
        Map<Integer, List<Integer>> currentRockLocations = getCurrentRockLocations(wall);
        for (Map.Entry<Integer, List<Integer>> entry : currentRockLocations.entrySet()) {
            for (Integer i : entry.getValue()) {
                wall.get(entry.getKey())[i] = '#';
            }
        }
    }

    private void trim(List<char[]> wall) {
        while (isRowEmpty(wall.get(0))) {
            wall.remove(0);
        }
    }

    private boolean isRowEmpty(char[] chars) {
        for (char aChar : chars) {
            if (aChar != '_') {
                return false;
            }
        }
        return true;
    }

    private List<char[]> getRock(int i) {
        return switch (i % 5) {
            case 0 -> getRock0();
            case 1 -> getRock1();
            case 2 -> getRock2();
            case 3 -> getRock3();
            case 4 -> getRock4();
            default -> throw new IllegalStateException("Unexpected value: " + i % 5);
        };
    }

    private List<char[]> getRock0() {
        List<char[]> rock = new ArrayList<>();
        rock.add(new char[]{'_', '_', '@', '@', '@', '@', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        return rock;
    }

    private List<char[]> getRock1() {
        List<char[]> rock = new ArrayList<>();
        rock.add(new char[]{'_', '_', '_', '@', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '@', '@', '@', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '@', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        return rock;
    }

    private List<char[]> getRock2() {
        List<char[]> rock = new ArrayList<>();
        rock.add(new char[]{'_', '_', '_', '_', '@', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '@', '_', '_'});
        rock.add(new char[]{'_', '_', '@', '@', '@', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        return rock;
    }

    private List<char[]> getRock3() {
        List<char[]> rock = new ArrayList<>();
        rock.add(new char[]{'_', '_', '@', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '@', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '@', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '@', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        return rock;
    }

    private List<char[]> getRock4() {
        List<char[]> rock = new ArrayList<>();
        rock.add(new char[]{'_', '_', '@', '@', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '@', '@', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        rock.add(new char[]{'_', '_', '_', '_', '_', '_', '_'});
        return rock;
    }

    @Override
    public Object part2() {
        return null;
    }
}