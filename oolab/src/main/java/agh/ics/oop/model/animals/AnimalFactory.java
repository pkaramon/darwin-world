package agh.ics.oop.model.animals;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Pose;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.genes.Genotype;
import agh.ics.oop.model.genes.GenotypeInfo;
import agh.ics.oop.model.genes.StepGeneMutation;
import agh.ics.oop.model.util.RandomNumbersGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class AnimalFactory {
    public static List<Animal> createInitialAnimals(int mapWidth, int mapHeight, int animalCount, int startEnergy, Supplier<Integer> currentDaySupplier) {
        List<Animal> initialAnimals = new ArrayList<>();
        List<Integer> randomX = RandomNumbersGenerator.generate(animalCount, 0, mapWidth);
        List<Integer> randomY = RandomNumbersGenerator.generate(animalCount, 0, mapHeight);
        MapDirection defaultDirection = MapDirection.NORTH;


        Random rand = new Random();
        GenotypeInfo info = new GenotypeInfo(5, 0, 8, 2, 4);
        AnimalFeeder feeder = new AnimalFeeder();
        AnimalMover mover = new AnimalMover(currentDaySupplier);
        AnimalCrossingInfo crossingInfo = new AnimalCrossingInfo(
                20,
                10,
                new StepGeneMutation(info),
                rand::nextBoolean,
                () -> MapDirection.NORTH.nextN(rand.nextInt(0,7)),
                currentDaySupplier
        );
        AnimalCrosser crosser = new AnimalCrosser(crossingInfo);


        for (int i = 0; i < animalCount; i++) {
            Vector2d position = new Vector2d(randomX.get(i), randomY.get(i));
            Pose pose = new Pose(position, defaultDirection);
            Genotype genotype = Genotype.generateRandom(info);

            AnimalData animalData = new AnimalData(pose, genotype, startEnergy);
            initialAnimals.add(new Animal(animalData, feeder, mover, crosser));
        }

        return initialAnimals;
    }

}
