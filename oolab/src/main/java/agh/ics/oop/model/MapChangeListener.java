package agh.ics.oop.model;

import agh.ics.oop.model.animals.Animal;

public interface MapChangeListener {
    default void animalPlaced(WorldMap worldMap, Animal animal) {}
    default void animalDied(WorldMap worldMap, Animal animal) {}
    default void animalMoved(WorldMap worldMap, Animal animal, Vector2d oldPosition) {}
}