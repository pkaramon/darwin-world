package agh.ics.oop.model;

public interface WorldElement {
    Vector2d getPosition();
    String getImagePath();
    default String getDisplayText() {
        return this.toString();
    }
}
