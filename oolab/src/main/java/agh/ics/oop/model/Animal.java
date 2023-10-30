package agh.ics.oop.model;

public class Animal {
    public static final Vector2d MAP_LOWER_LEFT = new Vector2d(0,0);
    public static final Vector2d MAP_UPPER_RIGHT = new Vector2d(4,4);
    public static final Vector2d MAP_CENTER = new Vector2d(2,2);

    private Vector2d position;
    private MapDirection orientation;

    public Animal(Vector2d position) {
        this.position = position;
        this.orientation = MapDirection.NORTH;
    }

    public Animal() {
        this(MAP_CENTER);
    }

    @Override
    public String toString() {
        return "pozycja=%s, orientacja=%s".formatted(position, orientation);
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public boolean facesDirection(MapDirection dir) {
        return this.orientation.equals(dir);
    }

    public void move(MoveDirection direction) {
        switch(direction) {
            case RIGHT ->  orientation = orientation.next();
            case LEFT -> orientation = orientation.previous();
            case FORWARD ->  setPosition(position.add(orientation.toUnitVector()));
            case BACKWARD ->   setPosition( position.subtract(orientation.toUnitVector()));
        }
    }

    private void setPosition(Vector2d newPosition) {
        if(newPosition.follows(MAP_LOWER_LEFT) && newPosition.precedes(MAP_UPPER_RIGHT))        {
            position = newPosition;
        }
    }
}