package agh.ics.oop.model.maps;

import agh.ics.oop.model.Vector2d;

public record Boundary(Vector2d lowerLeft, Vector2d upperRight) {
    public int height() {
        return upperRight.y() - lowerLeft().y() + 1;
    }

    public int width() {
        return upperRight.x() - lowerLeft().x() + 1;
    }
}
