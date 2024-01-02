package agh.ics.oop.model.generator;

import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.animals.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DeadAnimalsGrassGenerator extends AbstractGrassGenerator implements MapChangeListener {
    private static final int ANIMAL_BODY_DECOMPOSITION_DAYS = 5;
    private final Supplier<Integer> getCurrentDay;

    private record DeadAnimalEntry(Vector2d position, int dayOfDeath)  {}

    private List<DeadAnimalEntry> deadAnimalEntries = new ArrayList<>();

    public DeadAnimalsGrassGenerator(GrassGeneratorInfo info,
                                     WorldMap worldMap,
                                     Supplier<Integer> getCurrentDay)
    {
        super(info, worldMap);
        this.getCurrentDay = getCurrentDay;
    }



    @Override
    protected Set<Vector2d> getPreferredPositions() {
        int currentDay = getCurrentDay.get();
        deadAnimalEntries = deadAnimalEntries
                .stream()
                .filter(entry -> currentDay - entry.dayOfDeath() <= ANIMAL_BODY_DECOMPOSITION_DAYS)
                .toList();

        return deadAnimalEntries.stream().map(DeadAnimalEntry::position).collect(Collectors.toSet());
    }


    @Override
    public void animalDied(WorldMap worldMap, Animal animal) {
        MapChangeListener.super.animalDied(worldMap, animal);
        deadAnimalEntries.add(new DeadAnimalEntry(animal.getPosition(), getCurrentDay.get()));
    }

}
