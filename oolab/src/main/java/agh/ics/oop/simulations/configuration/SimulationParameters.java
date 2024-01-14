package agh.ics.oop.simulations.configuration;

public record SimulationParameters(int mapWidth, int mapHeight, int initialNumberOfPlants, int plantEnergy,
                                   int plantsPerDay, GrassGrowthVariant grassGrowthVariant, int initialNumberOfAnimals,
                                   int animalStartEnergy, int energyToReproduce, int parentEnergyGivenToChild,
                                   int minMutations, int maxMutations, MutationVariant mutationVariant, int genomeLength) {
}
