package agh.ics.oop.model.maps;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;
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

    default int amountOfAnimals() {
        return getOrderedAnimals().size();
    }
}
