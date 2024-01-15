package agh.ics.oop.model.maps;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animals.Animal;

import java.util.*;

public class GlobeMapField implements MapField {
    private final Vector2d position;
    private final List<Animal> animals = new ArrayList<>();

    private Grass grass = null;

    public GlobeMapField(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    @Override
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    @Override
    public Optional<Grass> getGrass() {
        return Optional.ofNullable(this.grass);
    }

    @Override
    public void addGrass(Grass grass) {
        this.grass = grass;
    }

    @Override
    public void removeGrass(Grass grass) {
        if(this.grass == grass)
            this.grass = null;
    }

    @Override
    public List<Animal> getOrderedAnimals() {
        if(animals.size() <= 1) return animals;
        animals.sort(Animal::compareTo);
        return animals;
    }

    @Override
    public int amountOfAnimals() {
        return animals.size();
    }

}
