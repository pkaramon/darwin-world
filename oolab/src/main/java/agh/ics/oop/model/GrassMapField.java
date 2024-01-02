package agh.ics.oop.model;

import agh.ics.oop.model.animals.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GrassMapField implements MapField {
    private final Vector2d position;
    private final List<Animal> animals = new ArrayList<>();
    private Grass grass = null;

    public GrassMapField(Vector2d position) {
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
        return animals.stream().sorted(Animal::compareTo).toList();
    }

}
