package agh.ics.oop.model;


public interface WorldMap<T extends MapField> extends MoveValidator {
    void place(WorldElement element);
    void remove(WorldElement element);
    T mapFieldAt(Vector2d position);
    void move(MoveableWorldElement element);
    Boundary getBoundary();
}