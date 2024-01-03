package agh.ics.oop.model;

import agh.ics.oop.model.animals.Animal;

import java.util.List;
import java.util.Optional;

public interface MapField {
    Vector2d getPosition();
    void addAnimal(Animal animal);
    void removeAnimal(Animal animal);
    Optional<Grass> getGrass();
    void addGrass(Grass grass);
    void removeGrass(Grass grass);
    List<Animal> getOrderedAnimals();

    default boolean isGrassed() {
        return getGrass().isPresent();
    }

}
