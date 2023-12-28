package agh.ics.oop.model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorldMap extends MoveValidator {

    void place(Animal animal) throws PositionAlreadyOccupiedException;

    void move(Animal animal);

    boolean isOccupied(Vector2d position);

    Optional<WorldElement> objectAt(Vector2d position);

    Collection<WorldElement> getElements();

    UUID getId();

    Boundary getCurrentBounds();

    List<Animal> getOrderedAnimals();
}