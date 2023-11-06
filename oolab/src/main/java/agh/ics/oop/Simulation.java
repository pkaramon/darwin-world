package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation {
    private final List<MoveDirection> moves;
    private final List<Animal> animals;
    private final WorldMap<Animal, Vector2d> map;

    public Simulation(List<MoveDirection> moves, List<Vector2d> initialPositions, WorldMap<Animal, Vector2d> map) {
        this.moves = moves;
        this.animals = new ArrayList<>();
        this.map = map;
        initializeMapAndAnimals(initialPositions);
    }

    private void initializeMapAndAnimals(List<Vector2d> initialPositions) {
        for (Vector2d position: initialPositions) {
            Animal animal = new Animal(position);
            if(map.place(animal)) {
                animals.add(animal);
            }
        }
    }

    public void run() {
        int animalIndex = 0;
        for (MoveDirection mv: moves) {
            Animal animal = animals.get(animalIndex);
            map.move(animal, mv);
            System.out.println(map);
            animalIndex = (animalIndex + 1) % animals.size();
        }
    }

    List<Animal> getAnimalsState() {
        return Collections.unmodifiableList(animals);
    }
}
