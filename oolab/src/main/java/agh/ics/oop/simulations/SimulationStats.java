package agh.ics.oop.simulations;

import agh.ics.oop.model.genes.Genotype;

import java.util.List;

public record SimulationStats(
        int day,
        int aliveAnimals,
        int emptyFields,
        int grassFields,
        Genotype dominantGenotype,
        List<Genotype> dominantGenotypes,
        double averageEnergy,
        double averageLifeLength,
        double averageNumberOfChildren) {}
