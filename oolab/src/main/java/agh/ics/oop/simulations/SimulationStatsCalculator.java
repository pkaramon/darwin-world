package agh.ics.oop.simulations;

import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.genes.Genotype;

import java.util.*;


public class SimulationStatsCalculator {
    private final WorldMap map;
    private final int currentDay;
    private final List<Animal> aliveAnimals;
    private final List<Animal> deadAnimals;

    public SimulationStatsCalculator(Collection<Animal> allAnimals,
                                     WorldMap map,
                                     int currentDay) {
        this.map = map;
        this.currentDay = currentDay;

        this.aliveAnimals = allAnimals.stream().filter(a -> !a.isDead()).toList();
        this.deadAnimals = allAnimals.stream().filter(a -> !a.isDead()).toList();
    }

    public int getNumberOfAnimalsAlive() {
        return aliveAnimals.size();
    }

    public int getNumberOfAnimalsDeadOverall() {
        return deadAnimals.size();
    }

    public int getAnimalsDeadOnLastDay() {
        return deadAnimals.stream().filter(a -> a.getDeathDay() == currentDay).toList().size();
    }

    public int getAnimalsBornOnLastDay() {
        return aliveAnimals.stream().filter(a -> a.getBirthDay() == currentDay).toList().size();
    }

    public double getAverageLifetimeForDeadAnimals() {
        return deadAnimals
                .stream()
                .mapToInt(a -> a.getDeathDay() - a.getBirthDay())
                .average()
                .orElse(-1);
    }


    public Optional<Genotype> getDominantGenotype() {
        Map<Genotype, Integer> counter = new HashMap<Genotype, Integer>();

        aliveAnimals
            .stream()
            .map(Animal::getGenotype)
            .forEach(g -> {
                counter.put(g, counter.getOrDefault(g, 0) + 1);
            });

        return counter
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }


    public int getAmountOfGrassOnMap() {
        int grassOnMap = 0;
        for (MapField field: map) {
            if (field.isGrassed()) {
                grassOnMap++;
            }
        }
        return grassOnMap;
    }


    public int getFreeFields() {
        int freeFields = 0;
        for (MapField field: map) {
            if (!field.isGrassed() && field.amountOfAnimals() == 0) {
                freeFields++;
            }
        }
        return freeFields;
    }
}
