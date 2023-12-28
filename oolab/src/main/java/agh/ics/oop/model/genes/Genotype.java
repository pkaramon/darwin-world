package agh.ics.oop.model.genes;

import java.util.List;

public interface Genotype {
    int nextGene();
    List<Integer> getGenes();
    Genotype combine(Genotype other, int thisStrength, int otherStrength);
    Genotype applyMutation(GeneMutation mutation);
}
