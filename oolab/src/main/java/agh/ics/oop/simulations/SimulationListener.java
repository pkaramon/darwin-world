package agh.ics.oop.simulations;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.animals.Animal;

public interface SimulationListener {
    default void onAnimalPlaced(WorldMap worldMap, Animal animal) {}
    default void onAnimalDied(WorldMap worldMap, Animal animal) {}
    default void onAnimalMoved(WorldMap worldMap, Animal animal, Vector2d oldPosition) {}
}