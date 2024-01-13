package agh.ics.oop.simulations;


import agh.ics.oop.model.genes.Genotype;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static agh.ics.oop.simulations.SimulationStatsCalculator.AnimalStatsInfo;
import static agh.ics.oop.simulations.SimulationStatsCalculator.MapFieldStatsInfo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SimulationStatsCalculatorTest {

    @Test
    void testGetNumberOfAnimalsAlive() {
        SimulationStatsCalculator calc = new SimulationStatsCalculator(
                3,
                List.of(
                        new AnimalStatsInfo(true, 3, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(false, -1, 0, 5, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(false, -1, 0, 6, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(false, -1, 0, 7, new Genotype(List.of(1, 2, 3)))
                ),
                new MapFieldStatsInfo[][]{});

        int numberOfAnimalsAlive = calc.getNumberOfAnimalsAlive();

        assertEquals(3, numberOfAnimalsAlive);
    }

    @Test
    void testGetNumberOfDeadAnimalsOverall() {
        SimulationStatsCalculator calc = new SimulationStatsCalculator(
                3,
                List.of(
                        new AnimalStatsInfo(true, 1, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(true, 2, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(true, 3, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(false, -1, 0, 7, new Genotype(List.of(1, 2, 3)))
                ),
                new MapFieldStatsInfo[][]{}
        );

        int numberOfDeadAnimalsOverall = calc.getNumberOfDeadAnimalsOverall();

        assertEquals(3, numberOfDeadAnimalsOverall);
    }

    @Test
    void testGetNumberOfAnimalsDeadOnLastDay() {
        SimulationStatsCalculator calc = new SimulationStatsCalculator(
                2,
                List.of(
                        new AnimalStatsInfo(true, 1, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(true, 2, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(true, 2, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(false, -1, 0, 7, new Genotype(List.of(1, 2, 3)))
                ),
                new MapFieldStatsInfo[][]{});

        int numberOfAnimalsDeadOnLastDay = calc.getNumberOfAnimalsDeadOnLastDay();

        assertEquals(2, numberOfAnimalsDeadOnLastDay);
    }

    @Test
    void testGetAverageEnergyWhenNoAliveAnimals() {
        SimulationStatsCalculator calc = new SimulationStatsCalculator(
                2,
                List.of(
                        new AnimalStatsInfo(true, 1, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(true, 2, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(true, 2, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(true, 2, 0, 0, new Genotype(List.of(1, 2, 3)))
                ),
                new MapFieldStatsInfo[][]{}
        );

        double averageEnergy = calc.getAverageEnergy();

        assertEquals(-1, averageEnergy);
    }

    @Test
    void testGetAverageEnergy() {
        SimulationStatsCalculator calc = new SimulationStatsCalculator(
                2,
                List.of(
                        new AnimalStatsInfo(true, 1, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(false, -1, 0, 5, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(true, 2, 0, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(false, -1, 0, 7, new Genotype(List.of(1, 2, 3)))
                ),
                new MapFieldStatsInfo[][]{});

        double averageEnergy = calc.getAverageEnergy();

        assertEquals(6, averageEnergy);
    }

    @Test
    void testGetAverageLifetimeForDeadAnimalsWhenNoDeadAnimals() {
        SimulationStatsCalculator calc = new SimulationStatsCalculator(
                2,
                List.of(
                        new AnimalStatsInfo(false, -1, 0, 2, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(false, -1, 0, 5, new Genotype(List.of(1, 2, 3)))
                ),
                new MapFieldStatsInfo[][]{});

        double averageLifetimeForDeadAnimals = calc.getAverageLifetimeForDeadAnimals();

        assertEquals(-1, averageLifetimeForDeadAnimals);
    }

    @Test
    void testGetAverageLifetimeForDeadAnimals() {
        SimulationStatsCalculator calc = new SimulationStatsCalculator(
                7,
                List.of(
                        new AnimalStatsInfo(true, 3, 1, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(true, 4, 2, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(true, 7, 3, 0, new Genotype(List.of(1, 2, 3))),
                        new AnimalStatsInfo(false, -1, 0, 7, new Genotype(List.of(1, 2, 3)))
                ),
                new MapFieldStatsInfo[][]{});

        double averageLifetimeForDeadAnimals = calc.getAverageLifetimeForDeadAnimals();

        assertEquals((double) (2 + 2 + 4) / 3, averageLifetimeForDeadAnimals);
    }

    @Test
    void testGetDominantGenotypeWhenNoAliveAnimals() {
        SimulationStatsCalculator calc = new SimulationStatsCalculator(
                7,
                List.of(
                        new AnimalStatsInfo(true, 3, 1, 0, new Genotype(List.of(1, 2, 3)))
                ),
                new MapFieldStatsInfo[][]{});

        Optional<Genotype> dominantGenotype = calc.getDominantGenotype();

        assertTrue(dominantGenotype.isEmpty());
    }

    @Test
    void testGetDominantGenotype() {
        SimulationStatsCalculator calc = new SimulationStatsCalculator(
                7,
                List.of(
                        new AnimalStatsInfo(true, 3, 1, 0, new Genotype(List.of(5, 6))),
                        new AnimalStatsInfo(true, 4, 2, 0, new Genotype(List.of(5, 6))),
                        new AnimalStatsInfo(true, 4, 2, 0, new Genotype(List.of(5, 6))),
                        new AnimalStatsInfo(false, -1, 0, 7, new Genotype(List.of(1, 2))),
                        new AnimalStatsInfo(false, -1, 0, 7, new Genotype(List.of(1, 2))),
                        new AnimalStatsInfo(false, -1, 0, 7, new Genotype(List.of(2, 3, 4)))
                ),
                new MapFieldStatsInfo[][]{});

        Optional<Genotype> dominantGenotype = calc.getDominantGenotype();

        assertTrue(dominantGenotype.isPresent());
        assertEquals(new Genotype(List.of(1, 2)), dominantGenotype.get());
    }

    @Test
    void testGetNumberOfGrassOnMap() {
        SimulationStatsCalculator calc = new SimulationStatsCalculator(
                7,
                List.of(),
                new MapFieldStatsInfo[][]{
                        {
                            new MapFieldStatsInfo(0, false),
                            new MapFieldStatsInfo(0, true),
                            new MapFieldStatsInfo(0, false)
                        },
                        {
                            new MapFieldStatsInfo(0, true),
                            new MapFieldStatsInfo(0, true),
                            new MapFieldStatsInfo(0, false)
                        },
                        {
                            new MapFieldStatsInfo(0, false),
                            new MapFieldStatsInfo(0, true),
                            new MapFieldStatsInfo(0, false)
                        }
                });

        int grassOnMap = calc.getNumberOfGrassOnMap();

        assertEquals(4, grassOnMap);
    }

    @Test
    void testGetNumberOfFreeFields() {
        SimulationStatsCalculator calc = new SimulationStatsCalculator(
                7,
                List.of(),
                new MapFieldStatsInfo[][]{
                        {
                            new MapFieldStatsInfo(2, false),
                            new MapFieldStatsInfo(0, true),
                            new MapFieldStatsInfo(3, false)
                        },
                        {
                            new MapFieldStatsInfo(0, true),
                            new MapFieldStatsInfo(0, true),
                            new MapFieldStatsInfo(1, false)
                        },
                        {
                            new MapFieldStatsInfo(0, false),
                            new MapFieldStatsInfo(0, true),
                            new MapFieldStatsInfo(0, false)
                        }
                });

        int freeFields = calc.getNumberOfFreeFields();

        assertEquals(2, freeFields);
    }



}

