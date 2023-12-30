package agh.ics.oop.model.animals;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Pose;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.genes.Genotype;

import java.util.ArrayList;
import java.util.List;

public class AnimalData {
    private final Genotype genotype;
    private final List<AnimalData> children = new ArrayList<>();
    private final int bornDay;
    private Pose pose;
    private int energy;
    private int deathDay = -1;
    private int plantsEaten = 0;


    public AnimalData(Pose pose, Genotype genotype, int energy) {
        this(pose, genotype, energy, 0);
    }

    public AnimalData(Pose pose, Genotype genotype, int energy, int bornDay) {
        this.pose = pose;
        this.genotype = genotype;
        this.energy = energy;
        this.bornDay = bornDay;
    }

    List<AnimalData> getChildren() {
        return children;
    }

    void addChild(AnimalData child) {
        children.add(child);
    }

    int getEnergy() {
        return energy;
    }

    Genotype getGenotype() {
        return genotype;
    }

    Vector2d getPosition() {
        return pose.position();
    }

    MapDirection getOrientation() {
        return pose.orientation();
    }

    void setPose(Pose pose) {
        this.pose = pose;
    }

    void useEnergy(int usage) {
        if (usage > energy) {
            throw new IllegalArgumentException("Not enough energy");
        }
        energy -= usage;
    }

    void giveEnergy(int energy) {
        this.energy += energy;
    }


    int getBornDay() {
        return bornDay;
    }

    int getDeathDay() {
        return deathDay;
    }

    void setDeathDay(int deathDay) {
        this.deathDay = deathDay;
    }

    int getPlantsEaten() {
        return plantsEaten;
    }

    void incrementPlantsEaten() {
        plantsEaten++;
    }
}
