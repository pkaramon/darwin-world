package agh.ics.oop.model.animals;

import agh.ics.oop.model.*;

import java.util.Optional;

public class Animal implements WorldElement, Comparable<Animal> {
    private final AnimalData data;
    private final AnimalFeeder feeder;
    private final AnimalMover mover;
    private final AnimalCrosser crosser;
    private final AnimalComparator comparator = new AnimalComparator();

    public Animal(AnimalData data,
                  AnimalFeeder feeder,
                  AnimalMover mover,
                  AnimalCrosser crosser)
    {
        this.data = data;
        this.feeder = feeder;
        this.mover = mover;
        this.crosser = crosser;
    }

    public void feed(Grass grass) {
        feeder.feedAnimal(grass, this.data);
    }

    public boolean isDead() {
        return data.getDeathDay() != -1;
    }

    public void move(MoveValidator moveValidator) {
        mover.move(moveValidator, this.data);
    }

    public Optional<Animal> crossWith(Animal other) {
        Optional<AnimalData> childData = crosser.cross(this.data, other.data);
        return childData.map((data)-> new Animal(data, feeder, mover, crosser));
    }

    @Override
    public Vector2d getPosition() {
        return data.getPosition();
    }

    @Override
    public int compareTo(Animal o) {
        return comparator.compare(this.data, o.data);
    }

    public int getEnergy() {
        return data.getEnergy();
    }

    public int getDeathDay() {
        return data.getDeathDay();
    }

    public int getBirthDay() {
        return data.getBirthDay();
    }
}