package agh.ics.oop.model.generator;

import agh.ics.oop.simulations.SimulationListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.animals.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DeadAnimalsGrassGenerator extends AbstractGrassGenerator implements SimulationListener {
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
                .collect(Collectors.toList());

        return deadAnimalEntries.stream().map(DeadAnimalEntry::position).collect(Collectors.toSet());
    }


    @Override
    public void onAnimalDied(WorldMap worldMap, Animal animal) {
        deadAnimalEntries.add(new DeadAnimalEntry(animal.getPosition(), getCurrentDay.get()));
    }

}
