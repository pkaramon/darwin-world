package agh.ics.oop.model.generator;

import agh.ics.oop.simulations.SimulationListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.animals.Animal;

import java.util.*;
import java.util.function.Supplier;

public class DeadAnimalsGrassGenerator extends AbstractGrassGenerator implements SimulationListener {
    private static final int ANIMAL_BODY_DECOMPOSITION_DAYS = 10;
    private final Supplier<Integer> getCurrentDay;

    private final Map<Vector2d, Integer> positionToMostRecentDeathDay = new HashMap<>();

    public DeadAnimalsGrassGenerator(GrassGeneratorInfo info,
                                     WorldMap worldMap,
                                     Supplier<Integer> getCurrentDay)
    {
        super(info, worldMap);
        this.getCurrentDay = getCurrentDay;
    }

    @Override
    public boolean isPreferredPosition(Vector2d position) {
        int deathDay =  positionToMostRecentDeathDay.getOrDefault(position, -ANIMAL_BODY_DECOMPOSITION_DAYS -1);
        return getCurrentDay.get() - deathDay <= ANIMAL_BODY_DECOMPOSITION_DAYS;
    }


    @Override
    public void onAnimalDied(WorldMap worldMap, Animal animal) {
        positionToMostRecentDeathDay.put(animal.getPosition(), getCurrentDay.get());
    }

}
