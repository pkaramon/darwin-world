package agh.ics.oop.model.genes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenotypeTest {

    private static final GenotypeInfo genotypeInfo = new GenotypeInfo(5, 0, 8, 2, 4);


    @Test
    void testRandomGenerationOfGenotype() {
        Genotype genotype = Genotype.generateRandom(genotypeInfo);

        for (int gene : genotype) {
            assertTrue(gene >= genotypeInfo.minGene() && gene <= genotypeInfo.maxGene());
        }

        assertEquals(5, genotype.getGenes().size());
    }

    @Test
    void testChangingTheGenes() {
        Genotype genotype = new Genotype(genotypeInfo, List.of(0, 3, 8, 4, 3));
        genotype.setCurrentGeneIndex(1);
        assertEquals(3, genotype.nextGene());
        assertEquals(8, genotype.nextGene());
        assertEquals(4, genotype.nextGene());
        assertEquals(3, genotype.nextGene());
        assertEquals(0, genotype.nextGene());
        assertEquals(3, genotype.nextGene());
    }

    @Test
    void testCreatingAnOffspring() {
        Genotype a = new Genotype(genotypeInfo, List.of(0, 3, 8, 4, 5));
        Genotype b = new Genotype(genotypeInfo, List.of(7, 3, 2, 1, 3));

        Genotype child = Genotype.combine(a, 3, b, 2);


        assertTrue(
                child.getGenes().equals(List.of(0, 3, 8, 1, 3))  ||
                        child.getGenes().equals(List.of(7,3,8,4,5))
        );
    }

    @Test
    void testCreatingAnOffspringUnevenDivision() {
        Genotype a = new Genotype(genotypeInfo, List.of(0, 3, 8, 4, 5));
        Genotype b = new Genotype(genotypeInfo, List.of(7, 3, 2, 1, 3));

        Genotype child = Genotype.combine(a, 9, b, 7);

        System.out.println(child.getGenes());

        assertTrue(
                child.getGenes().equals(List.of(0, 3, 8, 1, 3))  ||
                        child.getGenes().equals(List.of(7,3,8,4,5))
        );
    }


    @Test
    void applyingAMutation() {
        Genotype a = new Genotype(genotypeInfo, List.of(1,2,3,4,0));
        Genotype mutated = a.applyMutation((genes)-> {
            List<Integer> newGenes = new ArrayList<>(genes);
            newGenes.set(2, 8);
            return newGenes;
        });

        mutated.setCurrentGeneIndex(2);

        assertEquals(8, mutated.nextGene());
    }

}