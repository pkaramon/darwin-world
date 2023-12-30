package agh.ics.oop.model;

public interface MoveableWorldElement extends WorldElement{
    void move(MoveValidator moveValidator);
}
