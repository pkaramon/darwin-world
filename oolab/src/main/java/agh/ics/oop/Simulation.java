package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Simulation {
    private final List<MoveDirection> moves;
    private final List<Animal> animals;

    public Simulation(List<MoveDirection> moves, List<Vector2d> initialPositions) {
        this.moves = moves;
        this.animals = initialPositions.stream().map(Animal::new).collect(Collectors.toList());
    }

    public void run() {
        int animalIndex = 0;
        for (MoveDirection mv: moves) {
            Animal animal = animals.get(animalIndex);
            animal.move(mv);

            String message = "Zwierzę %d: %s".formatted(animalIndex, animal);
            System.out.println(message);

            animalIndex = (animalIndex + 1) % animals.size();
        }
    }

    List<Animal> getAnimalsState() {
        return Collections.unmodifiableList(animals);
    }
}
