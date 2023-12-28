package agh.ics.oop.model;

import agh.ics.oop.model.genes.Genotype;

import java.util.Optional;

public class Animal implements WorldElement {
    private final Genotype genotype;
    private final AnimalMatingInfo animalMatingInfo;
    private Pose pose;
    private int energy;

    public Animal(AnimalMatingInfo animalMatingInfo,
                  Pose pose,
                  Genotype genotype,
                  int energy) {
        this.pose = pose;
        this.animalMatingInfo = animalMatingInfo;
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
        if (parentEnergyDeficiency(partner)) return Optional.empty();
        Animal child = createChild(partner);
        updateParentsEnergies(partner);
        return Optional.of(child);
    }


    private boolean parentEnergyDeficiency(Animal partner) {
        return this.energy < animalMatingInfo.minEnergyToReproduce() ||
                partner.energy < animalMatingInfo.minEnergyToReproduce();
    }

    private Animal createChild(Animal partner) {
        Genotype combined = this.genotype.combine(partner.getGenotype(), this.energy, partner.energy);
        Genotype mutated = combined.applyMutation(animalMatingInfo.mutation());
        return new Animal(
                animalMatingInfo,
                pose,
                mutated,
                animalMatingInfo.parentEnergyGivenToChild() * 2
        );
    }

    private void updateParentsEnergies(Animal partner) {
        this.energy -= animalMatingInfo.parentEnergyGivenToChild();
        partner.energy -= animalMatingInfo.parentEnergyGivenToChild();
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