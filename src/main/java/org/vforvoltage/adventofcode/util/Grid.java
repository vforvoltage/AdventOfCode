package org.vforvoltage.adventofcode.util;

import org.apache.commons.numbers.arrays.MultidimensionalCounter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Grid<T> {

    private final Map<Integer, T> objectMap = new HashMap<>();
    private final MultidimensionalCounter coordinateConverter;

    public Grid(int... size) {
        this.coordinateConverter = MultidimensionalCounter.of(size);
    }

    public void set(T value, int... coordinates) {
        objectMap.put(coordinateConverter.toUni(coordinates), value);
    }

    public Optional<T> get(int... coordinates) {
        return Optional.ofNullable(objectMap.get(coordinateConverter.toUni(coordinates)));
    }

    public int[] getSizes() {
        return coordinateConverter.getSizes();
    }

    public int[] findValue(T find) {
        for (Map.Entry<Integer, T> entry : objectMap.entrySet()) {
            if (find.equals(entry.getValue())) {
                return coordinateConverter.toMulti(entry.getKey());
            }
        }
        return null;
    }
}