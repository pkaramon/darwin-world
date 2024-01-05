package agh.ics.oop.simulations;

import agh.ics.oop.model.*;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.generator.GrassGenerator;
import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;

import java.util.*;

public class Simulation {
    private final List<Animal> removedFromMapAnimals = new ArrayList<>();
    private Set<Animal> animals;
    private WorldMap map;
    private GrassGenerator grassGenerator;
    private int currentDay = 0;

    private final Set<SimulationListener> listeners = new LinkedHashSet<>();

    public void addListener(SimulationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SimulationListener listener) {
        listeners.remove(listener);
    }

    public void setWorldMap(WorldMap map) {
        this.map = map;
    }

    public void setGrassGenerator(GrassGenerator generator) {
        this.grassGenerator = generator;
    }

    public void setInitialAnimals(List<Animal> animals) {
        this.animals = new HashSet<>(animals);
    }

    public int getCurrentDay() {
        return currentDay;
    }


    public WorldMap getWorldMap() {
        return this.map;
    }


    public SimulationState run() {
        initializeIfFirstLaunch();

        currentDay++;

        cleanUpAnimalsThatDiedDayBefore();
        moveAnimals();
        handleEating();
        handleCrossing();
        growFood();


        return new SimulationState(
                currentDay,
                !animals.isEmpty(),
                animals,
                removedFromMapAnimals,
                map
        );
    }

    private void initializeIfFirstLaunch() {
        if (currentDay == 0) {
            grassGenerator.generateInitialGrass().forEach(map::addGrass);
            animals.forEach(a -> {
                map.addAnimal(a);
                listeners.forEach(l -> l.onAnimalPlaced(map, a));
            });
        }
    }

    private void cleanUpAnimalsThatDiedDayBefore() {
        List<Animal> deadLastDay = animals.stream().filter(Animal::isDead).toList();
        deadLastDay.forEach(a -> {
            map.removeAnimal(a);
            animals.remove(a);
            listeners.forEach(l -> l.onAnimalDied(map, a));
        });
        removedFromMapAnimals.addAll(deadLastDay);
    }

    private void moveAnimals() {
        for (Animal animal : animals) {
            Vector2d oldPosition = animal.getPosition();
            map.move(animal);
            listeners.forEach(l -> l.onAnimalMoved(map, animal, oldPosition));
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

    private void handleCrossing() {
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
                listeners.forEach(l -> l.onAnimalPlaced(map, c));
            });

        }
    }

    private void growFood() {
        grassGenerator.generateGrassForDay().forEach(map::addGrass);
    }
}