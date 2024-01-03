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
    private final List<Animal> deadAnimals = new ArrayList<>();
    private WorldMap map;
    private GrassGenerator grassGenerator;
    private List<Animal> animals;
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
        currentDay++;

        cleanUpAnimalsThatDiedDayBefore();
        moveAnimals();
        handleEating();
        growFood();
        List<Animal> animalsBornToday = handleCrossing();

        return calculateStatistics(animalsBornToday);

    }

    private void addInitialGrassIfFirstDay() {
        if (currentDay == 0) {
            grassGenerator.generateInitialGrass().forEach(map::addGrass);
        }
    }

    private void cleanUpAnimalsThatDiedDayBefore() {
        List<Animal> deadLastDay = animals.stream().filter(Animal::isDead).toList();
        deadLastDay.forEach(a -> {
            map.removeAnimal(a);
            animals.remove(a);
        });
        deadAnimals.addAll(deadLastDay);
    }

    private void moveAnimals() {
        for (Animal animal : animals) {
            map.move(animal);
        }
    }


    private void handleEating() {
        for (MapField field : map) {
            List<Animal> fieldAnimals = field.getOrderedAnimals();
            if (field.getGrass().isPresent() && !fieldAnimals.isEmpty()) {
                Grass grass = field.getGrass().get();
                Animal strongest = fieldAnimals.get(fieldAnimals.size() - 1);
                strongest.feed(grass);
                map.removeGrass(grass);
            }
        }
    }

    private List<Animal> handleCrossing() {
        List<Animal> animalsBornToday = new ArrayList<>();

        for (MapField field : map) {
            List<Animal> fieldAnimals = field.getOrderedAnimals();
            if (fieldAnimals.size() < 2) {
                continue;
            }

            Animal strongest = fieldAnimals.get(fieldAnimals.size() - 1);
            Animal secondStrongest = fieldAnimals.get(fieldAnimals.size() - 2);
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


    private SimulationStatistics calculateStatistics(List<Animal> animalsBornToday) {
        SimulationStatistics stats = new SimulationStatistics();

        stats.setAnimalsAlive(calculateAnimalsThatAreAlive());
        stats.setGrassOnMap(calculateNumberOfGrassOnMap());
        stats.setCurrentDay(currentDay);
        stats.setAnimalsBornOnLastDay(animalsBornToday.size());
        stats.setAverageLifetimeForDeadAnimals(calculateAverageLifetimeForDeadAnimals());


        int animalsDiedToday = calculateAnimalsThatDiedToday();
        stats.setAnimalsDeadOnLastDay(animalsDiedToday);
        stats.setAnimalsDeadOverall(deadAnimals.size() + animalsDiedToday);

        stats.setFreeFields(calculateNumberOfFreeFields());
        stats.setIsRunning(!animals.isEmpty());

        return stats;
    }

    private int calculateAnimalsThatAreAlive() {
        return animals
                .stream()
                .filter(a -> !a.isDead())
                .toList()
                .size();
    }

    private int calculateAnimalsThatDiedToday() {
        return animals
                .stream()
                .filter(a -> a.getDeathDay() == currentDay)
                .toList()
                .size();
    }

    private double calculateAverageLifetimeForDeadAnimals() {
        return deadAnimals
                .stream()
                .map(a -> a.getDeathDay() - a.getBirthDay())
                .reduce(Integer::sum)
                .map(sum -> {
                    System.out.println(sum);
                    return (double) sum / deadAnimals.size();
                })
                .orElse(-1.0);
    }

    private int calculateNumberOfGrassOnMap() {
        int grassOnMap = 0;
        for (MapField field : map) {
            if (field.isGrassed()) {
                grassOnMap++;
            }
        }
        return grassOnMap;
    }


    private int calculateNumberOfFreeFields() {
        int freeFields = 0;
        for (MapField field : map) {
            if (!field.isGrassed() && field.getOrderedAnimals().isEmpty()) {
                freeFields++;
            }
        }
        return freeFields;
    }
}