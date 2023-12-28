package agh.ics.oop.model;

import agh.ics.oop.model.genes.GeneMutation;
import agh.ics.oop.model.genes.Genotype;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AnimalTest {
    private static final GeneMutation fakeGeneMutation = ((genes) -> {
        List<Integer> newGenes = new ArrayList<>(genes);
        newGenes.set(2, 8);
        return newGenes;
    });
    private static final AnimalReproducingInfo info = new AnimalReproducingInfo(
            30,
            10,
            fakeGeneMutation
    );

    @Test
    void animalReproducing_NotEnoughEnergyNoChild() {
        Animal father = new Animal(info,
                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                mock(Genotype.class),
                25
        );
        Animal mother = new Animal(
                info,
                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                mock(Genotype.class),
                40
        );

        Optional<Animal> child = father.mateWith(mother);
        assertTrue(child.isEmpty());
    }


    @Test
    void animalReproducing_EnoughEnergyChildIsCreated() {
        Genotype fatherGenotype = mock(Genotype.class);
        Animal father = new Animal(
                info,
                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                fatherGenotype,
                50
        );
        Genotype motherGenotype = mock(Genotype.class);
        Animal mother = new Animal(
                info,
                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                motherGenotype,
                40
        );
        Genotype childGenotype = mock(Genotype.class);
        when(fatherGenotype.combine(motherGenotype, 50, 40)).thenReturn(childGenotype);
        when(childGenotype.applyMutation(fakeGeneMutation)).thenReturn(childGenotype);

        Optional<Animal> child = father.mateWith(mother);

        assertTrue(child.isPresent());
        Animal offspring = child.get();
        assertEquals(20, offspring.getEnergy());
        assertEquals(40, father.getEnergy());
        assertEquals(30, mother.getEnergy());
        assertEquals(childGenotype, offspring.getGenotype());
        verify(fatherGenotype).combine(motherGenotype, 50, 40);
        verify(childGenotype).applyMutation(fakeGeneMutation);
    }


    @Test
    void move_PicksNextGeneRotatesAndTriesToMoveInNewDirection() {
        Genotype mockGenotype = mock(Genotype.class);
        when(mockGenotype.nextGene()).thenReturn(3,2);
        Animal a = new Animal(
                info,
                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                mockGenotype,
                50
        );
        MoveValidator passThroughValidator = (pose) -> pose;

        a.move(passThroughValidator);

        assertEquals(new Vector2d(3, 1), a.getPosition());
        assertEquals(MapDirection.SOUTHEAST, a.getOrientation());

        a.move(passThroughValidator);

        assertEquals(new Vector2d(2, 0), a.getPosition());
        assertEquals(MapDirection.SOUTHWEST, a.getOrientation());
    }

    @Test
    void move_ConsultsTheDesiredActionWithMoveValidator() {
        Genotype mockGenotype = mock(Genotype.class);
        when(mockGenotype.nextGene()).thenReturn(2);
        Animal a = new Animal(
                info,
                new Pose(new Vector2d(4, 5), MapDirection.NORTHWEST),
                mockGenotype,
                50
        );
        MoveValidator mockValidator = mock(MoveValidator.class);
        when(mockValidator.validate(any())).thenAnswer(invocation ->
                new Pose(new Vector2d(100, 100), MapDirection.NORTH)
        );

        a.move(mockValidator);

        verify(mockValidator).validate(new Pose(new Vector2d(5, 6), MapDirection.NORTHEAST));
        assertEquals(new Vector2d(100, 100), a.getPosition());
        assertEquals(MapDirection.NORTH, a.getOrientation());
    }
}