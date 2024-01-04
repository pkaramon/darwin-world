package agh.ics.oop.simulations;


import agh.ics.oop.model.animals.Animal;

import java.util.Collection;

public record SimulationState(
        int currentDay,
        boolean isRunning,
        Collection<Animal> allAnimals
) {

}
