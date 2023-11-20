package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation {
    private final List<MoveDirection> moves;
    private final List<Animal> animals;
    private final WorldMap map;

    public Simulation(List<MoveDirection> moves, List<Vector2d> initialPositions, WorldMap map) {
        this.moves = moves;
        this.animals = new ArrayList<>();
        this.map = map;
        initializeMapAndAnimals(initialPositions);
    }

    private void initializeMapAndAnimals(List<Vector2d> initialPositions) {
        for (Vector2d position: initialPositions) {
            Animal animal = new Animal(position);
            try {
                map.place(animal);
                animals.add(animal);
            } catch(PositionAlreadyOccupiedException exception) {
                // animal cannot be placed, so we don't include it in simulation
            }
        }
   }

    public void run() {
        int animalIndex = 0;
        for (MoveDirection mv: moves) {
            Animal animal = animals.get(animalIndex);
            map.move(animal, mv);
            animalIndex = (animalIndex + 1) % animals.size();
        }
    }

    List<Animal> getAnimalsState() {
        return Collections.unmodifiableList(animals);
    }
}
