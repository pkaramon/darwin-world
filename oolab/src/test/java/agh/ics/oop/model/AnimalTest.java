package agh.ics.oop.model;

import agh.ics.oop.model.genes.Genotype;
import agh.ics.oop.model.genes.GenotypeInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalTest {
    private static final AnimalReproducingInfo info = new AnimalReproducingInfo(
            30,
            10,
            ((genes) -> {
                List<Integer> newGenes = new ArrayList<>(genes);
                newGenes.set(2, 8);
                return newGenes;

            })
    );
    private static final GenotypeInfo genotypeInfo = new GenotypeInfo(5,
            0,
            8,
            1,
            1);

    @Test
    void animalReproducing_NotEnoughEnergyNoChild() {
        Animal father = new Animal(
                info,
                new Vector2d(2, 2),
                Genotype.generateRandom(genotypeInfo),
                25
        );
        Animal mother = new Animal(
                info,
                new Vector2d(2, 2),
                Genotype.generateRandom(genotypeInfo),
                40
        );

        Optional<Animal> child = father.reproduce(mother);
        assertTrue(child.isEmpty());

    }


    @Test
    void animalReproducing_EnoughEnergyChildIsCreated() {
        Animal father = new Animal(
                info,
                new Vector2d(2, 2),
                new Genotype(genotypeInfo, List.of(3, 2, 1, 1, 2)),
                50
        );
        Animal mother = new Animal(
                info,
                new Vector2d(2, 2),
                new Genotype(genotypeInfo, List.of(8, 7, 6, 5, 4)),
                40
        );

        Optional<Animal> child = father.reproduce(mother);
        assertTrue(child.isPresent());

        Animal offspring = child.get();

        offspring.getGenotype().forEach(System.out::println);

        assertEquals(20, offspring.getEnergy());
        assertEquals(40, father.getEnergy());
        assertEquals(30, mother.getEnergy());
        assertTrue(
                Objects.equals(offspring.getGenotype(), new Genotype(genotypeInfo, List.of(8, 7, 8, 1, 2))) ||
                        Objects.equals(offspring.getGenotype(), new Genotype(genotypeInfo, List.of(3, 2, 8, 5, 4)))
        );
    }


}