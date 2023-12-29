package agh.ics.oop.model.genes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenotypeTest {

    @Test
    void testRandomGenerationOfGenotype() {
        GenotypeInfo genotypeInfo = new GenotypeInfo(5, 0, 8, 2, 4);
        Genotype genotype = Genotype.generateRandom(genotypeInfo);
        for (int gene : genotype.getGenes()) {
            assertTrue(gene >= genotypeInfo.minGene() && gene <= genotypeInfo.maxGene());
        }
        assertEquals(5, genotype.getGenes().size());
    }

    @Test
    void nextGene_CyclesThroughGenes() {
        Genotype genotype = new Genotype( List.of(0, 3, 8, 4, 3));
        genotype.setCurrentGeneIndex(1);
        assertEquals(3, genotype.nextGene());
        assertEquals(8, genotype.nextGene());
        assertEquals(4, genotype.nextGene());
        assertEquals(3, genotype.nextGene());
        assertEquals(0, genotype.nextGene());
        assertEquals(3, genotype.nextGene());
    }


    @Test
    void applyMutation_MutatesGenesAndReturnsANewGenotype() {
        Genotype a = new Genotype(List.of(1,2,3,4,0));
        Genotype mutated = a.applyMutation((genes)-> {
            List<Integer> newGenes = new ArrayList<>(genes);
            newGenes.set(2, 8);
            return newGenes;
        });

        mutated.setCurrentGeneIndex(2);

        assertEquals(8, mutated.nextGene());
    }

    @Test
    void length_ReturnsTheLengthOfGenotype() {
        Genotype genotype = new Genotype(List.of(1, 2, 3));
        assertEquals(3, genotype.length());
    }

    @Test
    void setCurrentGeneIndex_IndexOutOfBounds_ThrowsException() {
        Genotype genotype = new Genotype(List.of(2, 3, 4));
        assertThrows(IllegalArgumentException.class, ()-> genotype.setCurrentGeneIndex(-1));
        assertThrows(IllegalArgumentException.class, ()-> genotype.setCurrentGeneIndex(3));
    }

    @Test
    void setCurrentGeneIndex_IndexInBounds_ChangesCurrentGene() {
        Genotype genotype = new Genotype(List.of(2, 3,4));
        genotype.setCurrentGeneIndex(1);
        assertEquals(3, genotype.nextGene());
        genotype.setCurrentGeneIndex(1);
        assertEquals(3, genotype.nextGene());
    }

    @Test
    void getCurrentGeneIndex() {
        Genotype genotype = new Genotype(List.of(3, 4, 5));

        genotype.setCurrentGeneIndex(2);
        assertEquals(2, genotype.getCurrentGeneIndex());

        genotype.setCurrentGeneIndex(0);
        assertEquals(0, genotype.getCurrentGeneIndex());
        assertEquals(0, genotype.getCurrentGeneIndex());
    }

    @Test
    void split_SplitsGenotypeIntoTwoParts() {
        Genotype genotype = new Genotype(List.of(1,2,3,4,5,6));
        var split = genotype.splitAt(3);

        assertEquals(new Genotype(List.of(1,2,3)) , split.left());
        assertEquals(new Genotype(List.of(4,5,6)) , split.right());
    }

    @Test
    void concat_CombinesTwoGenotypeIntoOne() {
        Genotype a = new Genotype(List.of(1,2));
        Genotype b = new Genotype(List.of(4,5,6));

        assertEquals(new Genotype(List.of(1,2, 4,5,6)), a.concat(b));
    }
}