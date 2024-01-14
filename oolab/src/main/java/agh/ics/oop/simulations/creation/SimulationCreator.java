package agh.ics.oop.simulations.creation;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.generator.DeadAnimalsGrassGenerator;
import agh.ics.oop.model.generator.EquatorGrassGenerator;
import agh.ics.oop.model.generator.GrassGenerator;
import agh.ics.oop.model.generator.GrassGeneratorInfo;
import agh.ics.oop.model.maps.GlobeMap;
import agh.ics.oop.model.maps.GrassMapField;
import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.configuration.SimulationParameters;

import java.util.List;
import java.util.function.Supplier;

public class SimulationCreator {

    private final SimulationParameters parameters;

    public SimulationCreator(SimulationParameters parameters) {
        this.parameters = parameters;
    }

    public Simulation create() {
        Simulation simulation = new Simulation();

        WorldMap worldMap = createWorldMap();
        simulation.setWorldMap(worldMap);

        GrassGenerator grassGenerator = createGrassGenerator(worldMap, simulation::getCurrentDay);
        simulation.setGrassGenerator(grassGenerator);

        List<Animal> initialAnimals = createInitialAnimals(simulation);
        simulation.setInitialAnimals(initialAnimals);

        return simulation;
    }

    private WorldMap createWorldMap() {
        MapField[][] mapFields = new MapField[parameters.mapWidth()][parameters.mapHeight()];
        for (int x = 0; x < parameters.mapWidth(); x++) {
            for (int y = 0; y < parameters.mapHeight(); y++) {
                mapFields[x][y] = new GrassMapField(new Vector2d(x, y));
            }
        }
        return new GlobeMap(mapFields);
    }

    private GrassGenerator createGrassGenerator(WorldMap worldMap, Supplier<Integer> getCurrentDay) {
        GrassGeneratorInfo growthInfo = new GrassGeneratorInfo(
                parameters.plantsPerDay(),
                parameters.plantEnergy(),
                parameters.initialNumberOfPlants()
        );

        return switch (parameters.grassGrowthVariant()) {
            case LIFE_GIVING_CARCASSES -> new DeadAnimalsGrassGenerator(growthInfo, worldMap, getCurrentDay);
            case EQUATORIAL_FORESTS -> new EquatorGrassGenerator(growthInfo, worldMap);
        };
    }

    private List<Animal> createInitialAnimals(Simulation simulation) {
        return AnimalFactory.createInitialAnimals(
                parameters,
                simulation::getCurrentDay
        );
    }
}