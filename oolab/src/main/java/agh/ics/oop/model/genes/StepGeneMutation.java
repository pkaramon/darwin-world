package agh.ics.oop.model.genes;

import java.util.Random;

public class StepGeneMutation extends AbstractGeneMutation {
    public StepGeneMutation(GenotypeInfo info) {
        super(info);
    }

    protected int mutateGene(GenotypeInfo genotypeInfo, int gene) {
        Random rand = new Random();
        if (rand.nextBoolean()) {
            return (gene + 1) % genotypeInfo.length();
        } else {
            return (gene - 1 + genotypeInfo.length()) % genotypeInfo.length();
        }
    }

}
