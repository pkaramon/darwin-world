package agh.ics.oop.model.genes;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompletelyRandomGeneMutationTest {

    @Test
    void mutateGene() {
        GenotypeInfo info = new GenotypeInfo(5, 0, 7, 1,1);
        CompletelyRandomGeneMutation mutation = new CompletelyRandomGeneMutation(info);
        for (int i = 0; i < 10; i++) {
            int gene = 5 ;
            int mutatedGene = mutation.mutateGene(info, gene);
            assertTrue(mutatedGene >= info.minGene());
            assertTrue(mutatedGene <= info.maxGene());
            assertTrue(mutatedGene != gene);
        }
    }

    @Test
    void mutate() {
        for (int i = 0; i < 100; i++) {

            GenotypeInfo info = new GenotypeInfo(5, 0, 7, 4,6);
            CompletelyRandomGeneMutation mutation = new CompletelyRandomGeneMutation(info);
            List<Integer> genes = List.of(1,2,3,4,6,6,7);
            List<Integer> mutated = mutation.mutate(genes);
            assertEquals(genes.size(), mutated.size());

            int changed = 0;
            for (int j = 0; j < genes.size(); j++) {
                assertTrue(mutated.get(j) >= info.minGene());
                assertTrue(mutated.get(j) <= info.maxGene());
                if(!mutated.get(j).equals(genes.get(j))) {
                    changed++;
                }
            }

            assertTrue(changed >= 4 && changed <= 6);
        }
    }

}