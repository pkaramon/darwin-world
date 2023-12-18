package agh.ics.oop.model;

import agh.ics.oop.model.genes.Genotype;

import java.util.Optional;

public class Animal implements WorldElement {
    private final Genotype genotype;
    private final AnimalReproducingInfo animalReproducingInfo;
    private Vector2d position;
    private MapDirection orientation;
    private int energy;

    public Animal(AnimalReproducingInfo animalReproducingInfo,
                  Vector2d position,
                  Genotype genotype,
                  int energy) {
        this.position = position;
        this.orientation = MapDirection.NORTH;
        this.animalReproducingInfo = animalReproducingInfo;
        this.genotype = genotype;
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    Optional<Animal> reproduce(Animal partner) {
        if (this.energy < animalReproducingInfo.minEnergyToReproduce() ||
            partner.energy < animalReproducingInfo.minEnergyToReproduce()
        ) {
            return Optional.empty();
        }

        Genotype combined = Genotype.combine(
                this.genotype, this.energy, partner.genotype,partner.energy
        );
        Genotype mutated = combined.applyMutation(animalReproducingInfo.mutation());

        Animal child = new Animal(
                animalReproducingInfo,
                this.getPosition(),
                mutated,
                animalReproducingInfo.parentEnergyGivenToChild() * 2
        );
        this.energy -= animalReproducingInfo.parentEnergyGivenToChild();
        partner.energy -= animalReproducingInfo.parentEnergyGivenToChild();
        return Optional.of(child);
    }

    @Override
    public String toString() {
        return orientation.indicator();
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String getImagePath() {
        return orientation.getImagePath();
    }

    @Override
    public String getDisplayText() {
        return "Z %s".formatted(position);
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public boolean facesDirection(MapDirection dir) {
        return this.orientation.equals(dir);
    }

    // TODO : CHANGE
    public void move(MoveDirection direction, MoveValidator moveValidator) {
        switch (direction) {
            case RIGHT -> orientation = orientation.next();
            case LEFT -> orientation = orientation.previous();
            case FORWARD -> setPosition(position.add(orientation.toUnitVector()), moveValidator);
            case BACKWARD -> setPosition(position.subtract(orientation.toUnitVector()), moveValidator);
        }
    }

    private void setPosition(Vector2d newPosition, MoveValidator validator) {
        if (validator.canMoveTo(newPosition)) {
            position = newPosition;
        }
    }
}