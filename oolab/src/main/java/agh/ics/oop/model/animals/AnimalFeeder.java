package agh.ics.oop.model.animals;

import agh.ics.oop.model.Grass;

import java.util.function.Consumer;

public class AnimalFeeder {
    private final AnimalData animalData;
    private final Consumer<Grass> removeGrass;

    public AnimalFeeder(AnimalData animalData, Consumer<Grass> removeGrass) {
        this.animalData = animalData;
        this.removeGrass = removeGrass;
    }

    public void feedAnimal(Grass grass) {
        animalData.giveEnergy(grass.getEnergy());
        removeGrass.accept(grass);
        animalData.incrementPlantsEaten();
    }
}
