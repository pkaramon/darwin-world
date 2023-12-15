package agh.ics.oop.model;

public record Boundary(Vector2d lowerLeft, Vector2d upperRight) {
    public int height() {
        return upperRight.getY() - lowerLeft().getY() + 1;
    }

    public int width() {
        return upperRight.getX() - lowerLeft().getX() + 1;
    }
}
