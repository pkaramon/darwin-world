package agh.ics.oop.model;

import agh.ics.oop.model.genes.Genotype;

import java.util.Optional;

public class Animal implements WorldElement {
    private final Genotype genotype;
    private final AnimalReproducingInfo animalReproducingInfo;
    private Pose pose;
    private int energy;

    public Animal(AnimalReproducingInfo animalReproducingInfo,
                  Pose pose,
                  Genotype genotype,
                  int energy) {
        this.pose = pose;
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

    Optional<Animal> mateWith(Animal partner) {
        if (this.energy < animalReproducingInfo.minEnergyToReproduce() ||
            partner.energy < animalReproducingInfo.minEnergyToReproduce()
        ) {
            return Optional.empty();
        }

        Genotype combined = this.genotype.combine(partner.getGenotype(), this.energy, partner.energy);

        Genotype mutated = combined.applyMutation(animalReproducingInfo.mutation());
        Animal child = new Animal(
                animalReproducingInfo,
                pose,
                mutated,
                animalReproducingInfo.parentEnergyGivenToChild() * 2
        );
        this.energy -= animalReproducingInfo.parentEnergyGivenToChild();
        partner.energy -= animalReproducingInfo.parentEnergyGivenToChild();
        return Optional.of(child);
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
}