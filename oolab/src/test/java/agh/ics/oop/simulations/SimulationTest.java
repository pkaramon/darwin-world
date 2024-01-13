package agh.ics.oop.simulations;

import agh.ics.oop.model.*;
import agh.ics.oop.model.animals.*;
import agh.ics.oop.model.generator.GrassGenerator;
import agh.ics.oop.model.genes.Genotype;
import agh.ics.oop.model.maps.WorldMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static agh.ics.oop.model.WordMapCreator.createWorldMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimulationTest {
    private static List<Animal> createInitialAnimals(Simulation simulation,
                                                     AnimalCrosser crosser,
                                                     List<Pose> poses,
                                                     List<Genotype> genotypes,
                                                     List<Integer> energies
    ) {
        AnimalMover mover = new AnimalMover(simulation::getCurrentDay);
        AnimalFeeder feeder = new AnimalFeeder();
        return new ArrayList<>(IntStream
                .range(0, poses.size())
                .mapToObj(i ->
                        new Animal(
                                new AnimalData(
                                        poses.get(i),
                                        genotypes.get(i),
                                        energies.get(i)
                                ),
                                feeder, mover, crosser,
                                new AnimalComparator(()-> 1)
                        )
                ).toList()
        );
    }

    private static List<Animal> createInitialAnimals(
            Simulation simulation,
            List<Pose> poses,
            List<Genotype> genotypes,
            List<Integer> energies
    ) {
        return createInitialAnimals(
                simulation,
                mock(AnimalCrosser.class),
                poses,
                genotypes,
                energies);

    }


    private static GrassGenerator getEmptyGrassGenerator() {
        GrassGenerator grassGenerator = mock(GrassGenerator.class);
        when(grassGenerator.generateInitialGrass()).thenReturn(List.of());
        when(grassGenerator.generateGrassForDay()).thenReturn(List.of());
        return grassGenerator;
    }

    @Test
    void animalsMoveAround() {
        WorldMap map = createWorldMap(5, 5);
        Simulation simulation = new Simulation();
        simulation.setWorldMap(map);
        simulation.setGrassGenerator(getEmptyGrassGenerator());
        List<Animal> initialAnimals = createInitialAnimals(simulation,
                List.of(
                        new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                        new Pose(new Vector2d(3, 4), MapDirection.SOUTH)
                ),
                List.of(
                        new Genotype(List.of(0, 2, 2)),
                        new Genotype(List.of(0, 1, 2))
                ),
                List.of(10, 10)
        );
        simulation.setInitialAnimals(initialAnimals);

        simulation.run();

        assertEquals(new Vector2d(2, 3), initialAnimals.get(0).getPosition());
        assertEquals(0, map.mapFieldAt(new Vector2d(2,2)) .amountOfAnimals());
        assertEquals(1, map.mapFieldAt(new Vector2d(2,3)) .amountOfAnimals());

        assertEquals(new Vector2d(3, 3), initialAnimals.get(1).getPosition());
        assertEquals(0, map.mapFieldAt(new Vector2d(3,4)) .amountOfAnimals());
        assertEquals(1, map.mapFieldAt(new Vector2d(3,3)) .amountOfAnimals());

        simulation.run();

        assertEquals(new Vector2d(3, 3), initialAnimals.get(0).getPosition());
        assertEquals(new Vector2d(2, 2), initialAnimals.get(1).getPosition() );
    }

    @Test
    void animalDied() {
        Simulation simulation = new Simulation();
        WorldMap map = createWorldMap(5, 5);
        simulation.setWorldMap(map);
        simulation.setGrassGenerator(getEmptyGrassGenerator());
        List<Animal> initialAnimals = createInitialAnimals(simulation,
                List.of(
                        new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                        new Pose(new Vector2d(3, 4), MapDirection.SOUTH)
                ),
                List.of(
                        new Genotype(List.of(0, 2, 2)),
                        new Genotype(List.of(0, 1, 2))
                ),
                List.of(1, 10)
        );
        simulation.setInitialAnimals(initialAnimals);

        simulation.run();

        assertTrue(initialAnimals.get(0).isDead());
        // there is still a corpse on the map
        assertEquals(1, map.mapFieldAt(new Vector2d(2,3)).amountOfAnimals());

        simulation.run();

        assertEquals(0, map.mapFieldAt(new Vector2d(2,3)).amountOfAnimals());
    }

    @Test
    void deathsOnDifferentDays() {
        Simulation simulation = new Simulation();
        WorldMap map = createWorldMap(5, 5);
        simulation.setWorldMap(map);
        simulation.setGrassGenerator(getEmptyGrassGenerator());
        List<Animal> initialAnimals = createInitialAnimals(simulation,
                List.of(
                        new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                        new Pose(new Vector2d(3, 4), MapDirection.SOUTH)
                ),
                List.of(
                        new Genotype(List.of(0, 0, 2)),
                        new Genotype(List.of(0, 0, 2))
                ),
                List.of(2, 3)
        );
        simulation.setInitialAnimals(initialAnimals);

        simulation.run();
        simulation.run();

        assertTrue(initialAnimals.get(0).isDead());
        assertFalse(initialAnimals.get(1).isDead());

        assertEquals(1, map.mapFieldAt(new Vector2d(2, 4)).amountOfAnimals());

        simulation.run();

        assertTrue(initialAnimals.get(1).isDead());
        assertEquals(0, map.mapFieldAt(new Vector2d(2, 4)).amountOfAnimals());
    }

    @Test
    void foodGrowthAndEating() {
        Simulation simulation = new Simulation();
        WorldMap map = createWorldMap(5, 5);
        simulation.setWorldMap(map);
        GrassGenerator grassGenerator = mock(GrassGenerator.class);
        when(grassGenerator.generateInitialGrass()).thenReturn(
                List.of(new Grass(new Vector2d(2, 2), 10))
        );
        when(grassGenerator.generateGrassForDay()).thenReturn(
                List.of(new Grass(new Vector2d(3, 3), 20)),
                List.of()
        );
        simulation.setGrassGenerator(grassGenerator);
        List<Animal> initialAnimals = createInitialAnimals(
                simulation,
                List.of(
                        new Pose(new Vector2d(1, 1), MapDirection.NORTHEAST),
                        new Pose(new Vector2d(4, 4), MapDirection.WEST)
                ),
                List.of(
                        new Genotype(List.of(0, 4)),
                        new Genotype(List.of(0, 6))
                ),
                List.of(20, 30)
        );
        simulation.setInitialAnimals(initialAnimals);

        simulation.run();

        assertEquals(29 , initialAnimals.get(0).getEnergy());
        assertFalse(map.mapFieldAt(new Vector2d(2,2)).isGrassed());
        assertTrue(map.mapFieldAt(new Vector2d(3, 3)).isGrassed());

        simulation.run();

        assertEquals(48, initialAnimals.get(1).getEnergy());
        assertFalse(map.mapFieldAt(new Vector2d(3,3)).isGrassed());
    }

    @Test
    void foodConflict() {
        WorldMap map = createWorldMap(3, 3);
        GrassGenerator grassGenerator = mock(GrassGenerator.class);
        when(grassGenerator.generateInitialGrass())
                .thenReturn(List.of(new Grass(new Vector2d(1, 1), 10)));
        when(grassGenerator.generateGrassForDay()).thenReturn(List.of());
        Simulation simulation = new Simulation();
        simulation.setWorldMap(map);
        simulation.setGrassGenerator(grassGenerator);
        List<Animal> initialAnimals = createInitialAnimals(
                simulation,
                List.of(
                        new Pose(new Vector2d(0, 0), MapDirection.NORTHEAST),
                        new Pose(new Vector2d(2, 2), MapDirection.SOUTHWEST)
                ),
                List.of(
                        new Genotype(List.of(0)),
                        new Genotype(List.of(0))
                ),
                List.of(20, 10)
        );
        simulation.setInitialAnimals(initialAnimals);

        simulation.run();

        assertEquals(29, initialAnimals.get(0).getEnergy());
        assertEquals(9, initialAnimals.get(1).getEnergy());
        assertFalse(map.mapFieldAt(new Vector2d(1,1)).isGrassed());
    }

    @Test
    void crossingWithConflict() {
        WorldMap map = createWorldMap(3, 3);
        Simulation simulation = new Simulation();
        simulation.setWorldMap(map);
        simulation.setGrassGenerator(getEmptyGrassGenerator());
        AnimalCrosser crosser = new AnimalCrosser(new AnimalCrossingInfo(
                10,
                5,
                genes -> genes,
                () -> true,
                () -> MapDirection.SOUTH,
                simulation::getCurrentDay
        ));
        List<Animal> initialAnimals = createInitialAnimals(
                simulation,
                crosser,
                List.of(
                        new Pose(new Vector2d(0, 0), MapDirection.NORTHEAST),
                        new Pose(new Vector2d(0, 1), MapDirection.EAST),
                        new Pose(new Vector2d(2, 2), MapDirection.SOUTHWEST)
                ),
                List.of(
                        new Genotype(List.of(0)),
                        new Genotype(List.of(0)),
                        new Genotype(List.of(0))
                ),
                List.of(30, 20, 10)
        );
        simulation.setInitialAnimals(initialAnimals);

        simulation.run();

        assertEquals(4, map.mapFieldAt(new Vector2d(1,1)).amountOfAnimals());

        map.mapFieldAt(new Vector2d(1,1))
                .getOrderedAnimals()
                .stream()
                .filter(a -> a.getEnergy() == 10 &&
                                a.getGenotype().equals(new Genotype(List.of(0)))
                        && a.getBirthDay() == 1
                        && a.getOrientation() == MapDirection.SOUTH
                )
                .findFirst()
                .orElseThrow();

    }

    @Test
    void simulationStopsWhenAllAnimalsAreDead() {
        Simulation simulation = new Simulation();
        simulation.setWorldMap(createWorldMap(5, 5));
        simulation.setGrassGenerator(getEmptyGrassGenerator());
        List<Animal> initialAnimals = createInitialAnimals(
                simulation,
                List.of(new Pose(new Vector2d(0, 0), MapDirection.NORTH),
                        new Pose(new Vector2d(0, 2), MapDirection.NORTH)),
                List.of(new Genotype(List.of(0)),
                        new Genotype(List.of(0))),
                List.of(1, 2)
        );
        simulation.setInitialAnimals(initialAnimals);

        SimulationState state = simulation.run();

        assertTrue(state.isRunning());
        assertEquals(1, state.currentDay());

        state = simulation.run();

        // there are still dead animals present on map
        assertTrue(state.isRunning());
        assertEquals(2, state.currentDay());

        state = simulation.run();

        assertFalse(state.isRunning());
        assertEquals(3, state.currentDay());
    }

    @Test
    void publishingEvents() {
        WorldMap map = createWorldMap(3, 3);
        Simulation simulation = new Simulation();
        simulation.setWorldMap(map);

        SimulationListener listener = mock(SimulationListener.class);

        simulation.addListener(listener);

        simulation.setGrassGenerator(getEmptyGrassGenerator());
        AnimalCrosser crosser = new AnimalCrosser(new AnimalCrossingInfo(
                10,
                5,
                genes -> genes,
                () -> true,
                () -> MapDirection.SOUTH,
                simulation::getCurrentDay
        ));
        List<Animal> initialAnimals = createInitialAnimals(
                simulation,
                crosser,
                List.of(
                        new Pose(new Vector2d(0, 0), MapDirection.NORTHEAST),
                        new Pose(new Vector2d(0, 1), MapDirection.EAST),
                        new Pose(new Vector2d(2, 2), MapDirection.SOUTHWEST)
                ),
                List.of(
                        new Genotype(List.of(0)),
                        new Genotype(List.of(0)),
                        new Genotype(List.of(0))
                ),
                List.of(30, 20, 1)
        );
        simulation.setInitialAnimals(initialAnimals);

        simulation.run();

        verify(listener).onAnimalPlaced(map, initialAnimals.get(0));
        verify(listener).onAnimalPlaced(map, initialAnimals.get(1));
        verify(listener).onAnimalPlaced(map, initialAnimals.get(2));
        verify(listener).onAnimalPlaced(
                argThat(m -> map == m),
                argThat(a -> a.getEnergy() == 10 && a.getBirthDay() == 1)
        );

        verify(listener).onAnimalMoved(map, initialAnimals.get(0), new Vector2d(0, 0));
        verify(listener).onAnimalMoved(map, initialAnimals.get(1), new Vector2d(0, 1));
        verify(listener).onAnimalMoved(map, initialAnimals.get(2), new Vector2d(2, 2));

        simulation.run();

        verify(listener).onAnimalDied(map, initialAnimals.get(2));

    }

}