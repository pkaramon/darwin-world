package agh.ics.oop.model;

import agh.ics.oop.model.genes.GeneMutation;
import agh.ics.oop.model.genes.Genotype;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class AnimalCrosserTest {
    private static final GeneMutation fakeGeneMutation = ((genes) -> {
        List<Integer> newGenes = new ArrayList<>(genes);
        newGenes.set(2, 0);
        return newGenes;
    });
    private static final AnimalMatingInfo info = new AnimalMatingInfo(
            30,
            10,
            fakeGeneMutation
    );

//    @Test
//    void animalReproducing_NotEnoughEnergyNoChild() {
//        Animal father = new Animal(info,
//                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
//                mock(Genotype.class),
//                25
//        );
//        Animal mother = new Animal(
//                info,
//                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
//                mock(Genotype.class),
//                40
//        );
//
//        Optional<Animal> child = father.mateWith(mother);
//        assertTrue(child.isEmpty());
//    }
//
//
//    @Test
//    void animalReproducing_EnoughEnergyChildIsCreated() {
//        Genotype fatherGenotype = mock(Genotype.class);
//        Animal father = new Animal(
//                info,
//                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
//                fatherGenotype,
//                50
//        );
//        Genotype motherGenotype = mock(Genotype.class);
//        Animal mother = new Animal(
//                info,
//                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
//                motherGenotype,
//                40
//        );
//        Genotype childGenotype = mock(Genotype.class);
//        when(fatherGenotype.combine(motherGenotype, 50, 40)).thenReturn(childGenotype);
//        when(childGenotype.applyMutation(fakeGeneMutation)).thenReturn(childGenotype);
//
//        Optional<Animal> child = father.mateWith(mother);
//
//        assertTrue(child.isPresent());
//        Animal offspring = child.get();
//        assertEquals(20, offspring.getEnergy());
//        assertEquals(40, father.getEnergy());
//        assertEquals(30, mother.getEnergy());
//        assertEquals(childGenotype, offspring.getGenotype());
//        verify(fatherGenotype).combine(motherGenotype, 50, 40);
//        verify(childGenotype).applyMutation(fakeGeneMutation);
//    }


//    @Test
//    void testCreatingAnOffspring() {
//        GenotypeImp a = new GenotypeImp( List.of(0, 3, 8, 4, 5));
//        GenotypeImp b = new GenotypeImp( List.of(7, 3, 2, 1, 3));
//
//        GenotypeImp child = GenotypeImp.combine(a, 3, b, 2);
//
//
//        assertTrue(
//                child.getGenes().equals(List.of(0, 3, 8, 1, 3))  ||
//                        child.getGenes().equals(List.of(7,3,8,4,5))
//        );
//    }
//
//    @Test
//    void testCreatingAnOffspringUnevenDivision() {
//        GenotypeImp a = new GenotypeImp( List.of(0, 3, 8, 4, 5));
//        GenotypeImp b = new GenotypeImp( List.of(7, 3, 2, 1, 3));
//
//        GenotypeImp child = GenotypeImp.combine(a, 9, b, 7);
//
//        System.out.println(child.getGenes());
//
//        assertTrue(
//                child.getGenes().equals(List.of(0, 3, 8, 1, 3))  ||
//                        child.getGenes().equals(List.of(7,3,8,4,5))
//        );
//    }

}