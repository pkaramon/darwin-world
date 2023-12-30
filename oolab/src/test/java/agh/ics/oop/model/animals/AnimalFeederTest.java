package agh.ics.oop.model.animals;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Pose;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.genes.Genotype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AnimalFeederTest {
    AnimalData animalData;
    Grass grass;

    @BeforeEach
    void setUp() {
        animalData = new AnimalData(
                new Pose(new Vector2d(4, 5), MapDirection.NORTHWEST),
                new Genotype(List.of(2)),
                50
        );
        grass = new Grass(new Vector2d(4, 5), 10);
    }

    @Test
    void grassGivesAnimalTheSpecifiedEnergy() {
        AnimalFeeder feeder = new AnimalFeeder(animalData, (g)->{});
        feeder.feedAnimal(grass);

        assertEquals(60, animalData.getEnergy());
    }

    @Test
    @SuppressWarnings("unchecked")
    void grassIsRemoved() {
        Consumer<Grass> removeGrass = (Consumer<Grass>)mock(Consumer.class);
        AnimalFeeder feeder = new AnimalFeeder(animalData,removeGrass);
        feeder.feedAnimal(grass);

        verify(removeGrass).accept(grass);
    }

    @Test
    void plantsEatenIsIncremented() {
        AnimalFeeder feeder = new AnimalFeeder(animalData, (g)->{});
        feeder.feedAnimal(grass);

        assertEquals(1, animalData.getPlantsEaten());
    }




}