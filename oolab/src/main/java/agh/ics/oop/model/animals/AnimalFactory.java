package agh.ics.oop.model.animals;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Pose;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.genes.*;
import agh.ics.oop.model.util.RandomNumbersGenerator;
import agh.ics.oop.simulations.SimulationParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class AnimalFactory {
    private static final Random random = new Random();
    private static final int MIN_GENE = 0, MAX_GENE = 7;

    public static List<Animal> createInitialAnimals(SimulationParameters params, Supplier<Integer> currentDaySupplier) {
        List<Animal> initialAnimals = new ArrayList<>();
        List<Integer> randomX = RandomNumbersGenerator.generate(params.initialNumberOfAnimals(), 0, params.mapWidth());
        List<Integer> randomY = RandomNumbersGenerator.generate(params.initialNumberOfAnimals(), 0, params.mapHeight());

        GenotypeInfo info = new GenotypeInfo(
                params.genomeLength(),
                MIN_GENE,
                MAX_GENE,
                params.minMutations(),
                params.maxMutations());
        AnimalFeeder feeder = new AnimalFeeder();
        AnimalMover mover = new AnimalMover(currentDaySupplier);

        AnimalCrossingInfo crossingInfo = new AnimalCrossingInfo(
                params.energyToReproduce(),
                params.parentEnergyGivenToChild(),
                getGeneMutation(params.mutationVariant(), info),
                random::nextBoolean,
                AnimalFactory::getRandomDirection,
                currentDaySupplier
        );
        AnimalCrosser crosser = new AnimalCrosser(crossingInfo);


        for (int i = 0; i < params.initialNumberOfAnimals(); i++) {
            Vector2d position = new Vector2d(randomX.get(i), randomY.get(i));
            Pose pose = new Pose(position, getRandomDirection());
            Genotype genotype = Genotype.generateRandom(info);

            AnimalData animalData = new AnimalData(pose, genotype, params.animalStartEnergy());
            initialAnimals.add(new Animal(animalData, feeder, mover, crosser));
        }

        return initialAnimals;
    }

    private static GeneMutation getGeneMutation(String mutationVariant, GenotypeInfo info) {
        return switch(mutationVariant) {
            case "Full Randomness" -> new CompletelyRandomGeneMutation(info);
            case "Small Correction" -> new StepGeneMutation(info);
            default -> throw new IllegalArgumentException("Unknown mutation variant: " + mutationVariant);
        };
    }


    private static MapDirection getRandomDirection() {
        return MapDirection.NORTH.nextN(random.nextInt(MIN_GENE,MAX_GENE));
    }
}
