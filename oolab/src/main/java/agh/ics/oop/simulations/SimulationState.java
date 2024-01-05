package agh.ics.oop.simulations;


import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.maps.WorldMap;

import java.util.Collection;

public record SimulationState(
        int currentDay,
        boolean isRunning,
        Collection<Animal> animalsOnMap,
        Collection<Animal> removedFromMapAnimals,
        WorldMap map
        ) {}
