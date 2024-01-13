package agh.ics.oop.simulations;

public record SimulationParameters(int mapWidth, int mapHeight, int initialNumberOfPlants, int plantEnergy,
                                   int plantsPerDay, String plantGrowthVariant, int initialNumberOfAnimals,
                                   int animalStartEnergy, int energyToReproduce, int parentEnergyGivenToChild,
                                   int minMutations, int maxMutations, String mutationVariant, int genomeLength) {
}
