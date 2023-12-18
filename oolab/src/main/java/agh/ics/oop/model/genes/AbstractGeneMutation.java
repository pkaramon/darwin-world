package agh.ics.oop.model.genes;

import agh.ics.oop.model.util.RandomNumbersGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractGeneMutation implements GeneMutation {
    protected final GenotypeInfo info;

    public AbstractGeneMutation(GenotypeInfo info) {
        this.info = info;
    }

    @Override
    public List<Integer> mutate(List<Integer> genes) {
        Random rand = new Random();
        List<Integer> mutatedGenes = new ArrayList<>(genes);
        RandomNumbersGenerator.generate(
                        rand.nextInt(info.minNumberOfMutations(), info.maxNumberOfMutations() + 1),
                        0,
                        genes.size()
                )
                .forEach(index -> mutatedGenes.set(index, mutateGene(info, genes.get(index))));
        return mutatedGenes;
    }

    protected abstract int mutateGene(GenotypeInfo info, int gene);
}
