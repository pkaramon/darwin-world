package agh.ics.oop;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.MapField;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.generator.GrassGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Simulation {
    private WorldMap map;
    private GrassGenerator grassGenerator;
    private List<Animal> animals;
    private final List<Animal> deadAnimals = new ArrayList<>();
    private int currentDay = 0;


    public void setWorldMap(WorldMap map) {
        this.map = map;
    }

    public void setGrassGenerator(GrassGenerator generator) {
        this.grassGenerator = generator;
    }

    public void setInitialAnimals(List<Animal> animals) {
        this.animals = new ArrayList<>(animals);
    }


    public int getCurrentDay() {
        return currentDay;
    }


    public SimulationStatistics run() {
        addInitialGrassIfFirstDay();

        List<Animal> deadOnThisDay = handleDeadAnimals();
        moveAnimals();
        handleEating();
        growFood();
        List<Animal> animalsBornToday = handleCrossing();

        return calculateStatistics(deadOnThisDay, animalsBornToday);

    }

    private SimulationStatistics calculateStatistics(List<Animal> deadOnThisDay, List<Animal> animalsBornToday) {
        currentDay++;
        SimulationStatistics stats = new SimulationStatistics();
        stats.setAnimalsAlive(animals.size());
        stats.setAnimalsDeadOnLastDay(deadOnThisDay.size());
        stats.setAnimalsDeadOverall(deadAnimals.size());
        stats.setFreeFields(calculateNumberOfFreeFields());
        stats.setGrassOnMap(calculateNumberOfGrassOnMap());
        stats.setCurrentDay(currentDay);
        stats.setAnimalsBornOnLastDay(animalsBornToday.size());
        return stats;
    }

    private List<Animal> handleCrossing() {
        List<Animal> animalsBornToday = new ArrayList<>();

        for (MapField field: map) {
            List<Animal> fieldAnimals = field.getOrderedAnimals();
            if(fieldAnimals.size() < 2) {
                continue;
            }

            Animal strongest = fieldAnimals.get(fieldAnimals.size()-1);
            Animal secondStrongest = fieldAnimals.get(fieldAnimals.size()-2);
            Optional<Animal> child = strongest.crossWith(secondStrongest);

            child.ifPresent(c -> {
                map.addAnimal(c);
                animals.add(c);
                animalsBornToday.add(c);
            });

        }
        return animalsBornToday;
    }

    private void growFood() {
        grassGenerator.generateGrassForDay().forEach(map::addGrass);
    }

    private void handleEating() {
        for (MapField field : map) {
            if(field.isGrassed()) {
                List<Animal> fieldAnimals = field.getOrderedAnimals();
                if(fieldAnimals.isEmpty() || field.getGrass().isEmpty()) {
                    continue;
                }

                Grass grass = field.getGrass().get();
                Animal strongest = fieldAnimals.get(fieldAnimals.size()-1);
                strongest.feed(grass);

                map.removeGrass(grass);
            }
        }
    }

    private void moveAnimals() {
        for (Animal animal : animals) {
            map.move(animal);
        }
    }

    private List<Animal> handleDeadAnimals() {
        List<Animal> deadOnThisDay = animals.stream().filter(Animal::isDead).toList();
        deadOnThisDay.forEach(a -> {
            map.removeAnimal(a);
            animals.remove(a);
        });
        deadAnimals.addAll(deadOnThisDay);
        return deadOnThisDay;
    }

    private int calculateNumberOfGrassOnMap() {
        int grassOnMap = 0;
        for (MapField field : map) {
            if(field.isGrassed()) {
                grassOnMap++;
            }
        }
        return grassOnMap;
    }

    private void addInitialGrassIfFirstDay() {
        if(currentDay == 0) {
            grassGenerator.generateInitialGrass().forEach(map::addGrass);
        }
    }

    private int calculateNumberOfFreeFields() {
        int freeFields = 0;
        for (MapField field : map) {
            if(!field.isGrassed() && field.getOrderedAnimals().isEmpty()) {
                freeFields++;
            }
        }
        return freeFields;
    }
}