package agh.ics.oop.simulations;

public class SimulationParameters {
    private final int mapWidth;
    private final int mapHeight;
    private final int initialNumberOfPlants;
    private final int plantEnergy;
    private final int plantsPerDay;
    private final String plantGrowthVariant;
    private final int initialNumberOfAnimals;
    private final int animalStartEnergy;
    private final int energyToReproduce;
    private final int parentEnergyGivenToChild;
    private final int minMutations;
    private final int maxMutations;
    private final String mutationVariant;
    private final int genomeLength;

    public SimulationParameters(int mapWidth, int mapHeight, int initialNumberOfPlants, int plantEnergy, int plantsPerDay, String plantGrowthVariant, int initialNumberOfAnimals, int animalStartEnergy, int energyToReproduce, int parentEnergyGivenToChild, int minMutations, int maxMutations, String mutationVariant, int genomeLength) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.initialNumberOfPlants = initialNumberOfPlants;
        this.plantEnergy = plantEnergy;
        this.plantsPerDay = plantsPerDay;
        this.plantGrowthVariant = plantGrowthVariant;
        this.initialNumberOfAnimals = initialNumberOfAnimals;
        this.animalStartEnergy = animalStartEnergy;
        this.energyToReproduce = energyToReproduce;
        this.parentEnergyGivenToChild = parentEnergyGivenToChild;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.mutationVariant = mutationVariant;
        this.genomeLength = genomeLength;
    }

    // Gettery dla wszystkich pól
    public int getMapWidth() { return mapWidth; }
    public int getMapHeight() { return mapHeight; }
    public int getInitialNumberOfPlants() { return initialNumberOfPlants; }
    public int getPlantEnergy() { return plantEnergy; }
    public int getPlantsPerDay() { return plantsPerDay; }
    public String getPlantGrowthVariant() { return plantGrowthVariant; }
    public int getInitialNumberOfAnimals() { return initialNumberOfAnimals; }
    public int getAnimalStartEnergy() { return animalStartEnergy; }
    public int getEnergyToReproduce() { return energyToReproduce; }
    public int getParentEnergyGivenToChild() { return parentEnergyGivenToChild; }
    public int getMinMutations() { return minMutations; }
    public int getMaxMutations() { return maxMutations; }
    public String getMutationVariant() { return mutationVariant; }
    public int getGenomeLength() { return genomeLength; }
}
