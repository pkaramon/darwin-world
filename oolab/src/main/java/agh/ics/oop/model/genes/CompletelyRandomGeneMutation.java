package agh.ics.oop.model.genes;

import java.util.Random;

public class CompletelyRandomGeneMutation extends AbstractGeneMutation {
    public CompletelyRandomGeneMutation(GenotypeInfo info) {
        super(info);
    }
    private static final Random rand  = new Random();

    @Override
    protected int mutateGene(GenotypeInfo info, int gene) {
        int result = info.minGene() + rand.nextInt(info.maxGene() - info.minGene());
        if (result >= gene) {
            result++;
        }
        return result;
    }
}
