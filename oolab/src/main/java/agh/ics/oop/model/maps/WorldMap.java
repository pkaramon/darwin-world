package agh.ics.oop.model.maps;


import agh.ics.oop.model.*;
import agh.ics.oop.model.animals.Animal;

public interface WorldMap extends MoveValidator, Iterable<MapField> {
    void addAnimal(Animal animal);
    void removeAnimal(Animal animal);
    void addGrass(Grass grass);
    void removeGrass(Grass grass);
    MapField mapFieldAt(Vector2d position);
    void move(Animal animal);
    Boundary getBoundary();
    default int getHeight() {
        return getBoundary().height();
    }
    default int getWidth() {
        return getBoundary().width();
    }
}