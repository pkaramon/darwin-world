package agh.ics.oop.model;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationStatistics;
import agh.ics.oop.model.animals.*;
import agh.ics.oop.model.generator.GrassGenerator;
import agh.ics.oop.model.genes.Genotype;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static agh.ics.oop.model.WordMapCreator.createWorldMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimulationTest {
    private static List<Animal> createInitialAnimals(Simulation simulation,
                                                     AnimalFeeder feeder,
                                                     AnimalCrosser crosser,
                                                     List<Pose> poses,
                                                     List<Genotype> genotypes,
                                                     List<Integer> energies
    ) {
        AnimalMover mover = new AnimalMover(simulation::getCurrentDay);
        return new ArrayList<>(IntStream
                .range(0, poses.size())
                .mapToObj(i ->
                        new Animal(
                                new AnimalData(
                                        poses.get(i),
                                        genotypes.get(i),
                                        energies.get(i)
                                ), feeder, mover, crosser
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
                new AnimalFeeder(),
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

        SimulationStatistics stats = simulation.run();

        assertEquals(2, stats.getAnimalsAlive());
        assertEquals(
                new Vector2d(2, 3),
                initialAnimals.get(0).getPosition()
        );
        assertEquals(
                new Vector2d(3, 3),
                initialAnimals.get(1).getPosition()
        );

        stats = simulation.run();

        assertEquals(2, stats.getAnimalsAlive());
        assertEquals(
                new Vector2d(3, 3),
                initialAnimals.get(0).getPosition()
        );
        assertEquals(
                new Vector2d(2, 2),
                initialAnimals.get(1).getPosition()
        );

        assertEquals(5*5 - 2, stats.getFreeFields());
        assertEquals(2, stats.getCurrentDay());
    }

    @Test
    void animalDied() {
        Simulation simulation = new Simulation();
        simulation.setWorldMap(createWorldMap(5, 5));
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
        SimulationStatistics stats = simulation.run();

        assertEquals(1, stats.getAnimalsAlive());
        assertEquals(1, stats.getAnimalsDeadOverall());
        assertEquals(1, stats.getAnimalsDeadOnLastDay());
        assertEquals(5 * 5 -1, stats.getFreeFields());

        assertTrue(initialAnimals.get(0).isDead());
        assertFalse(initialAnimals.get(1).isDead());
    }

    @Test
    void deathsOnDifferentDays() {
        Simulation simulation = new Simulation();
        simulation.setWorldMap(createWorldMap(5, 5));
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
                List.of(2, 3)
        );
        simulation.setInitialAnimals(initialAnimals);

        simulation.run();
        simulation.run();
        SimulationStatistics stats = simulation.run();

        assertEquals(1, stats.getAnimalsAlive());
        assertEquals(1, stats.getAnimalsDeadOnLastDay());
        assertEquals(1, stats.getAnimalsDeadOverall());

        stats = simulation.run();

        assertEquals(0, stats.getAnimalsAlive());
        assertEquals(2, stats.getAnimalsDeadOverall());
        assertEquals(1, stats.getAnimalsDeadOnLastDay());

        assertTrue(initialAnimals.get(0).isDead());
        assertTrue(initialAnimals.get(1).isDead());
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
        //noinspection unchecked
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

        SimulationStatistics stats = simulation.run();

        assertEquals(5*5 - 3, stats.getFreeFields());
        assertEquals(1, stats.getGrassOnMap());
        assertEquals(29 , initialAnimals.get(0).getEnergy());
        assertFalse(map.mapFieldAt(new Vector2d(2,2)).isGrassed());

        stats = simulation.run();

        assertEquals(5*5 -2 , stats.getFreeFields());
        assertEquals(0, stats.getGrassOnMap());
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

        SimulationStatistics stats = simulation.run();

        assertEquals(0, stats.getGrassOnMap());
        assertEquals(29, initialAnimals.get(0).getEnergy());
        assertEquals(9, initialAnimals.get(1).getEnergy());
    }

    @Test
    void crossingWithConflict() {
        WorldMap map = createWorldMap(3, 3);
        Simulation simulation = new Simulation();
        simulation.setWorldMap(map);
        simulation.setGrassGenerator(getEmptyGrassGenerator());
        List<Animal> initialAnimals = createInitialAnimals(
                simulation,
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
        Animal animalSpy = spy(initialAnimals.get(0));
        Animal child = new Animal(
                new AnimalData(
                        new Pose(new Vector2d(1,1), MapDirection.NORTH),
                        new Genotype(List.of(0)),
                        1
                ), mock(AnimalFeeder.class), mock(AnimalMover.class), mock(AnimalCrosser.class)
        );
        doReturn(Optional.of(child)).when(animalSpy).crossWith(initialAnimals.get(1));
        initialAnimals.set(0, animalSpy);
        simulation.setInitialAnimals(initialAnimals);

        SimulationStatistics stats = simulation.run();

        assertEquals(1, stats.getAnimalsBornOnLastDay());
        assertEquals(4, stats.getAnimalsAlive());
    }


}