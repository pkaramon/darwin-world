package agh.ics.oop.model;

import agh.ics.oop.model.genes.Genotype;

public class Animal implements WorldElement {
    private final Genotype genotype;
    private Pose pose;
    private int energy;

    public Animal(Pose pose,
                  Genotype genotype,
                  int energy) {
        this.pose = pose;
        this.genotype = genotype;
        this.energy = energy;
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


    public void move(MoveValidator moveValidator) {
        int gene = genotype.nextGene();

        MapDirection desiredOrientation = pose.orientation().nextN(gene);
        Vector2d desiredPosition = pose.position().add(desiredOrientation.toUnitVector());

        pose = moveValidator.validate(new Pose(desiredPosition, desiredOrientation));
    }

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