package agh.ics.oop.model;

public class Animal implements WorldElement{
    public static final Vector2d DEFAULT_POSITION = new Vector2d(2, 2);

    private Vector2d position;
    private MapDirection orientation;

    public Animal(Vector2d position) {
        this.position = position;
        this.orientation = MapDirection.NORTH;
    }

    public Animal() {
        this(DEFAULT_POSITION);
    }

    @Override
    public String toString() {
        return orientation.indicator();
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public boolean facesDirection(MapDirection dir) {
        return this.orientation.equals(dir);
    }

    public void move(MoveDirection direction, MoveValidator moveValidator) {
        switch (direction) {
            case RIGHT -> orientation = orientation.next();
            case LEFT -> orientation = orientation.previous();
            case FORWARD -> setPosition(position.add(orientation.toUnitVector()), moveValidator);
            case BACKWARD -> setPosition(position.subtract(orientation.toUnitVector()), moveValidator);
        }
    }

    private void setPosition(Vector2d newPosition, MoveValidator validator) {
        if (validator.canMoveTo(newPosition)) {
            position = newPosition;
        }
    }
}