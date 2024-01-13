package agh.ics.oop.simulations;

import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.genes.Genotype;
import agh.ics.oop.model.maps.Boundary;
import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;

import java.util.*;


public class SimulationStatsCalculator {


    record AnimalStatsInfo(
            boolean isDead,
            int deathDay,
            int birthDay,
            int energy,
            Genotype genotype,
            int numberOfChildren) {}

    record MapFieldStatsInfo(int numberOfAnimals, boolean isGrassed) {}

    private final int currentDay;
    private final MapFieldStatsInfo[][] fieldsStats;

    private final List<AnimalStatsInfo> aliveAnimalStats;
    private final List<AnimalStatsInfo> deadAnimalStats;

    SimulationStatsCalculator(int currentDay,
                                     Collection<AnimalStatsInfo> allAnimalsStats,
                                     MapFieldStatsInfo[][] fieldsStats) {
        this.aliveAnimalStats = allAnimalsStats.stream().filter(a -> !a.isDead()).toList();
        this.deadAnimalStats = allAnimalsStats.stream().filter(AnimalStatsInfo::isDead).toList();
        this.currentDay = currentDay;
        this.fieldsStats = fieldsStats;
    }

    public SimulationStatsCalculator(int currentDay, Collection<Animal> allAnimals, WorldMap map) {
        this(
                currentDay,
                getAllAnimalsStats(allAnimals),
                getMapFieldStatsInfos(map)
        );
    }


    public double getAverageNumberOfChildren() {
        return aliveAnimalStats
                .stream()
                .map(AnimalStatsInfo::numberOfChildren)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    private static List<AnimalStatsInfo> getAllAnimalsStats(Collection<Animal> allAnimals) {
        return allAnimals.stream().map(a -> new AnimalStatsInfo(
                a.isDead(),
                a.getDeathDay(),
                a.getBirthDay(),
                a.getEnergy(),
                a.getGenotype(),
                a.getChildrenCount())).toList();
    }

    private static MapFieldStatsInfo[][] getMapFieldStatsInfos(WorldMap map) {
        Boundary boundary = map.getBoundary();
        MapFieldStatsInfo[][] fieldsStats = new MapFieldStatsInfo[boundary.width()][boundary.height()];

        for (MapField field: map) {
            int x = field.getPosition().x();
            int y = field.getPosition().y();
            fieldsStats[x][y] = new MapFieldStatsInfo(field.amountOfAnimals(), field.isGrassed());
        }
        return fieldsStats;
    }

    public double getAverageEnergy() {
        return aliveAnimalStats
                .stream()
                .filter(a -> !a.isDead())
                .mapToInt(AnimalStatsInfo::energy)
                .average()
                .orElse(0);
    }

    public int getNumberOfAnimalsAlive() {
        return aliveAnimalStats.size();
    }

    public int getNumberOfDeadAnimalsOverall() {
        return deadAnimalStats.size();
    }

    public int getNumberOfAnimalsDeadOnLastDay() {
        return deadAnimalStats
                .stream()
                .filter(a -> a.deathDay() == currentDay)
                .toList()
                .size();
    }

    public double getAverageLifetimeForDeadAnimals() {
        return deadAnimalStats
                .stream()
                .mapToInt(a -> a.deathDay() - a.birthDay())
                .average()
                .orElse(0);
    }

    public Optional<Genotype> getDominantGenotype() {
        return getMostPopularGenotypes(1).stream().findFirst();
    }

    public List<Genotype> getMostPopularGenotypes(int amount) {
        Map<Genotype, Integer> counter = new HashMap<>();

        aliveAnimalStats
                .stream()
                .map(AnimalStatsInfo::genotype)
                .forEach(g ->
                        counter.put(g, counter.getOrDefault(g, 0) + 1)
                );
        return counter.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(amount)
                .map(Map.Entry::getKey)
                .toList();
    }
    public int getNumberOfGrassOnMap() {
        return Arrays.stream(fieldsStats)
                .flatMap(Arrays::stream)
                .mapToInt(f -> f.isGrassed() ? 1 : 0)
                .sum();
    }



    public int getNumberOfFreeFields() {
        return Arrays.stream(fieldsStats)
                .flatMap(Arrays::stream)
                .mapToInt(f -> !f.isGrassed() && f.numberOfAnimals() == 0 ? 1 : 0)
                .sum();
    }
}
