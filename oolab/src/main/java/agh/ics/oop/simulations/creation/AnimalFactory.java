package agh.ics.oop.simulations.creation;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Pose;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animals.*;
import agh.ics.oop.model.genes.*;
import agh.ics.oop.simulations.configuration.MutationVariant;
import agh.ics.oop.simulations.configuration.SimulationParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class AnimalFactory {
    private static final Random random = new Random();
    private static final int MIN_GENE = 0, MAX_GENE = 7;

    public static List<Animal> createInitialAnimals(SimulationParameters params, Supplier<Integer> currentDaySupplier) {
        List<Animal> initialAnimals = new ArrayList<>();

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

        // the reason why we don't have true randomness in AnimalComparator is that
        // such randomness would violate TimSort contract, thus leading to spontaneous exceptions
        AnimalComparator comparator = new AnimalComparator((a, b)-> a.hashCode() - b.hashCode());

        for (int i = 0; i < params.initialNumberOfAnimals(); i++) {
            Pose pose = new Pose(getRandomPosition(params.mapWidth(), params.mapHeight()), getRandomDirection());
            Genotype genotype = Genotype.generateRandom(info);
            genotype.setCurrentGeneIndex(random.nextInt(params.genomeLength()));

            AnimalData animalData = new AnimalData(pose, genotype, params.animalStartEnergy());

            initialAnimals.add(new Animal(animalData, feeder, mover, crosser, comparator));
        }

        return initialAnimals;
    }

    private static GeneMutation getGeneMutation(MutationVariant mutationVariant, GenotypeInfo info) {
        return switch (mutationVariant) {
            case FULL_RANDOMNESS -> new CompletelyRandomGeneMutation(info);
            case STEP_MUTATION -> new StepGeneMutation(info);
        };
    }


    private static MapDirection getRandomDirection() {
        return MapDirection.NORTH.nextN(random.nextInt(MIN_GENE, MAX_GENE));
    }
    private static Vector2d getRandomPosition(int width, int height) {
        return new Vector2d(random.nextInt(0, width-1), random.nextInt(0, height));
    }
}
