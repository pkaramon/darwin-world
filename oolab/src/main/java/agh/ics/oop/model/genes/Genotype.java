package agh.ics.oop.model.genes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

public class Genotype {

    public record GenotypeSplit(Genotype left, Genotype right) {}

    private final List<Integer> genes;
    private int currentGeneIndex = 0;

    public Genotype(List<Integer> genes) {
        this.genes = genes;
    }

    public static Genotype generateRandom(GenotypeInfo info) {
        Random random = new Random();
        List<Integer> genes = random
                .ints(info.length(), info.minGene(), info.maxGene() + 1)
                .boxed()
                .toList();
        return new Genotype(genes);
    }


    public Genotype applyMutation(GeneMutation mutation) {
        List<Integer> mutatedGenes = mutation.mutate(new ArrayList<>(genes));
        return new Genotype(mutatedGenes);
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public Genotype concat(Genotype other) {
        return new Genotype(
                Stream.concat(genes.stream(), other.getGenes().stream()).toList()
        );
    }

    public int getCurrentGeneIndex() {
        return currentGeneIndex;
    }

    public void setCurrentGeneIndex(int currentGeneIndex) {
        if(currentGeneIndex < 0 || currentGeneIndex >= length()) {
            throw new IllegalArgumentException(
                    "gene index: %d is out of bounds for a genotype of length %d".formatted(currentGeneIndex, length())
            );
        }
        this.currentGeneIndex = currentGeneIndex;
    }

    public int nextGene() {
        int gene = genes.get(currentGeneIndex);
        currentGeneIndex = (currentGeneIndex + 1) % length();
        return gene;
    }

    public int length() {
        return genes.size();
    }

    public GenotypeSplit splitAt(int i) {
        List<Integer> left = new ArrayList<>(genes.subList(0, i));
        List<Integer> right = new ArrayList<>(genes.subList(i, length()));
        return new Genotype.GenotypeSplit(new Genotype(left), new Genotype(right));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genotype other = (Genotype) o;
        return Objects.equals(genes, other.getGenes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(genes);
    }

    @Override
    public String toString() {
        return "Genotype{genes=%s}".formatted(genes);
    }
}
