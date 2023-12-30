package agh.ics.oop.model.animals;

import agh.ics.oop.model.*;
import agh.ics.oop.model.genes.Genotype;

import java.util.Optional;

public class Animal implements WorldElement {
    private AnimalData data;
    private Genotype genotype;
    private AnimalFeeder feeder;
    private AnimalMover mover;
    private AnimalCrosser crosser;
    private Pose pose;
    private int energy;

    public Animal(Pose pose, Genotype genotype, int energy) {
        this.pose = pose;
        this.genotype = genotype;
        this.energy = energy;
    }

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

    public void move(MoveValidator moveValidator) {
        mover.move(moveValidator, this.data);
    }

    public Optional<Animal> crossWith(Animal other) {
        Optional<AnimalData> childData = crosser.cross(this.data, other.data);
        return childData.map((data)-> new Animal(data, feeder, mover, crosser));
    }

    public int getEnergy() {
        return energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    @Override
    public String toString() {
        return pose.orientation().indicator();
    }

    @Override
    public Vector2d getPosition() {
        return pose.position();
    }


//    public void move(MoveValidator moveValidator) {
//        int gene = genotype.nextGene();
//
//        MapDirection desiredOrientation = pose.orientation().nextN(gene);
//        Vector2d desiredPosition = pose.position().add(desiredOrientation.toUnitVector());
//
//        pose = moveValidator.validate(new Pose(desiredPosition, desiredOrientation));
//    }

    public MapDirection getOrientation() {
        return pose.orientation();
    }

    public void useEnergy(int usage) {
        if(usage > energy) {
            throw new IllegalArgumentException("Not enough energy");
        }
        energy -= usage;
    }
}