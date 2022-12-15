package org.vforvoltage.adventofcode.util;

import org.apache.commons.lang3.builder.Builder;

public class OffsetArrayCoordinate2D {

    private ArrayCoordinate2D coordinate;
    private ArrayCoordinate2D offset;

    public OffsetArrayCoordinate2D() {
    }

    public ArrayCoordinate2D getOffset() {
        return offset;
    }

    public void setOffset(ArrayCoordinate2D offset) {
        this.offset = offset;
    }

    public ArrayCoordinate2D getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(ArrayCoordinate2D coordinate) {
        this.coordinate = coordinate;
    }

    public int getRow() {
        return coordinate.getRow() + offset.getRow();
    }

    public int getColumn() {
        return coordinate.getColumn() + offset.getColumn();
    }

    @Override
    public String toString() {
        return "%s with %s offset -> [%d, %d]".formatted(coordinate.toString(), offset.toString(), this.getRow(), this.getColumn());
    }

    public static class OffsetArrayCoordinate2DBuilder implements Builder<OffsetArrayCoordinate2D> {

        private OffsetArrayCoordinate2D coordinate;

        public OffsetArrayCoordinate2DBuilder() {
            this.coordinate = new OffsetArrayCoordinate2D();
            this.coordinate.coordinate = new ArrayCoordinate2D();
        }

        public OffsetArrayCoordinate2DBuilder withRow(int row) {
            this.coordinate.getCoordinate().setRow(row);
            return this;
        }

        public OffsetArrayCoordinate2DBuilder withColumn(int column) {
            this.coordinate.getCoordinate().setColumn(column);
            return this;
        }

        public OffsetArrayCoordinate2DBuilder withOffset(ArrayCoordinate2D offset) {
            this.coordinate.setOffset(offset);
            return this;
        }

        public OffsetArrayCoordinate2DBuilder withCoordinates(ArrayCoordinate2D coordinates) {
            this.coordinate.setCoordinate(coordinates);
            return this;
        }

        @Override
        public OffsetArrayCoordinate2D build() {
            return coordinate;
        }
    }
}