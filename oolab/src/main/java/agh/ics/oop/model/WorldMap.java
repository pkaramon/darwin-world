package agh.ics.oop.model;

import agh.ics.oop.model.animal.Animal;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorldMap extends MoveValidator {
    void place(WorldElement element);
    void remove(WorldElement element);

    void place(Grass grass) throws PositionAlreadyOccupiedException;
    Optional<WorldElement> objectAt(Vector2d position);

    Collection<WorldElement> getElements();

    UUID getId();

    Boundary getCurrentBounds();

    List<Animal> getOrderedAnimals();
}