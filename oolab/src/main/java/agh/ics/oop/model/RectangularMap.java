package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;

public class RectangularMap implements WorldMap {
    private final Map<Vector2d, Animal> animals = new HashMap<>();

    private final Vector2d lowerLeft = new Vector2d(0,0);
    private final Vector2d upperRight;
    private final MapVisualizer mapVisualizer = new MapVisualizer(this);

    public RectangularMap(int width, int height) {
        this.upperRight = new Vector2d(width-1, height-1);
    }

    @Override
    public String toString() {
        return mapVisualizer.draw(lowerLeft, upperRight);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return withinMapBounds(position) && !isOccupied(position);
    }

    private boolean withinMapBounds(Vector2d position) {
        return position.follows(lowerLeft) && position.precedes(upperRight);
    }

    @Override
    public boolean place(Animal animal) {
        if (canMoveTo(animal.getPosition())) {
            this.animals.put(animal.getPosition(), animal);
            return true;
        }
        return false;
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        if(objectAt(oldPosition) != animal) {
            return;
        }
        animal.move(direction, this);
        if(oldPosition.equals(animal.getPosition())) {
            return;
        }
        this.animals.remove(oldPosition);
        this.animals.put(animal.getPosition(), animal);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.animals.containsKey(position);
    }

    @Override
    public Animal objectAt(Vector2d position) {
        return this.animals.get(position);
    }
}
