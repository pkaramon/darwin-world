package agh.ics.oop.model.animals;

import agh.ics.oop.model.Grass;

import java.util.function.Consumer;

public class AnimalFeeder {
    private final Consumer<Grass> removeGrass;

    public AnimalFeeder(Consumer<Grass> removeGrass) {
        this.removeGrass = removeGrass;
    }

    public void feedAnimal(Grass grass, AnimalData animalData) {
        animalData.giveEnergy(grass.getEnergy());
        removeGrass.accept(grass);
        animalData.incrementPlantsEaten();
    }
}
