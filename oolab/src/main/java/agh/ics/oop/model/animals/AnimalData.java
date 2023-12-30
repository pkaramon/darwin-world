package agh.ics.oop.model.animals;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Pose;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.genes.Genotype;

import java.util.ArrayList;
import java.util.List;

public class AnimalData {
    private final Genotype genotype;
    private Pose pose;
    private int energy;
    private final List<AnimalData> children = new ArrayList<>();
    private int deathDay = -1;
    private int plantsEaten = 0;
    private final int bornDay;


    public AnimalData(Pose pose, Genotype genotype,int energy) {
        this(pose, genotype, energy, 0);
    }

    public AnimalData(Pose pose, Genotype genotype,int energy, int bornDay) {
        this.pose = pose;
        this.genotype = genotype;
        this.energy = energy;
        this.bornDay = bornDay;
    }

    public List<AnimalData> getChildren() {
        return children;
    }

    public void addChild(AnimalData child) {
        children.add(child);
    }

    public int getEnergy() {
        return energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public Vector2d getPosition() {
        return pose.position();
    }

    public MapDirection getOrientation() {
        return pose.orientation();
    }

    public void setPose(Pose pose) {
        this.pose = pose;
    }

    public void useEnergy(int usage) {
        if(usage > energy) {
            throw new IllegalArgumentException("Not enough energy");
        }
        energy -= usage;
    }

    public void giveEnergy(int energy) {
        this.energy += energy;
    }


    public int getBornDay() {
        return bornDay;
    }

    public int getDeathDay() {
        return deathDay;
    }

    public void setDeathDay(int deathDay) {
        this.deathDay = deathDay;
    }

    public int getPlantsEaten() {
        return plantsEaten;
    }

    public void incrementPlantsEaten() {
        plantsEaten++;
    }
}
