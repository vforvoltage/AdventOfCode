package org.vforvoltage.adventofcode.util;

import org.apache.commons.lang3.builder.Builder;

public class ArrayCoordinate2D {

    private int row;
    private int column;

    public ArrayCoordinate2D() {
    }

    public ArrayCoordinate2D(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "[%d, %d]".formatted(row, column);
    }

    public static class ArrayCoordinate2DBuilder implements Builder<ArrayCoordinate2D> {
        private final ArrayCoordinate2D coordinate;

        public ArrayCoordinate2DBuilder() {
            this.coordinate = new ArrayCoordinate2D();
        }

        public ArrayCoordinate2DBuilder withRow(int row) {
            this.coordinate.setRow(row);
            return this;
        }

        public ArrayCoordinate2DBuilder withColumn(int column) {
            this.coordinate.setColumn(column);
            return this;
        }

        @Override
        public ArrayCoordinate2D build() {
            return coordinate;
        }
    }
}