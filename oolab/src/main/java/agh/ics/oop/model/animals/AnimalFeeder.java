package agh.ics.oop.model.animals;

import agh.ics.oop.model.Grass;


public class AnimalFeeder {

    public AnimalFeeder() {}

    public void feedAnimal(Grass grass, AnimalData animalData) {
        animalData.giveEnergy(grass.getEnergy());
        animalData.incrementPlantsEaten();
    }
}
