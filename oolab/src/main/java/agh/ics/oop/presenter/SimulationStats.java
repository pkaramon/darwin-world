package agh.ics.oop.presenter;

import java.util.List;

public record SimulationStats(
        int day,
        int aliveAnimals,
        int emptyFields,
        int grassFields,
        List<Integer> dominantGenes,
        double averageEnergy,
        double averageLifeLength,
        double averageNumberOfChildren)
{

}
