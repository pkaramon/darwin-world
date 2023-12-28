package agh.ics.oop.model.genes;

import java.util.*;
import java.util.stream.Stream;

public class GenotypeImp implements Iterable<Integer> {
    private final List<Integer> genes;
    private final GenotypeInfo genotypeInfo;
    private int currentGeneIndex = 0;

    public GenotypeImp(GenotypeInfo info, List<Integer> genes) {
        this.genes = genes;
        this.genotypeInfo = info;
    }

    public static GenotypeImp generateRandom(GenotypeInfo info) {
        Random random = new Random();
        List<Integer> genes = random
                .ints(info.length(), info.minGene(), info.maxGene() + 1)
                .boxed()
                .toList();
        return new GenotypeImp(info, genes);
    }

    public static GenotypeImp combine(GenotypeImp a, int aStrength, GenotypeImp b, int bStrength) {
        int splitPoint = (int) Math.ceil(
                (double) ((aStrength) * a.genotypeInfo.length()) / (double) (aStrength + bStrength)
        );

        Random random = new Random();

        if (random.nextBoolean()) {
            GenotypeSplit aSplit = a.splitAt(splitPoint);
            GenotypeSplit bSplit = b.splitAt(splitPoint);
            return aSplit.left().concat(bSplit.right());
        } else {
            GenotypeSplit aSplit = a.splitAt(a.genotypeInfo.length() - splitPoint);
            GenotypeSplit bSplit = b.splitAt(a.genotypeInfo.length() - splitPoint);
            return bSplit.left().concat(aSplit.right());
        }
    }

    public GenotypeImp applyMutation(GeneMutation mutation) {
        List<Integer> mutatedGenes = mutation.mutate(genes);
        return new GenotypeImp(genotypeInfo, mutatedGenes);
    }


    // tests only
    List<Integer> getGenes() {
        return genes;
    }


    public void setCurrentGeneIndex(int currentGeneIndex) {
        this.currentGeneIndex = currentGeneIndex;
    }

    @Override
    public Iterator<Integer> iterator() {
        return genes.iterator();
    }

    public int nextGene() {
        int gene = genes.get(currentGeneIndex);
        currentGeneIndex = (currentGeneIndex + 1) % genotypeInfo.length();
        return gene;
    }


    private GenotypeSplit splitAt(int i) {
        List<Integer> left = new ArrayList<>(genes.subList(0, i));
        List<Integer> right = new ArrayList<>(genes.subList(i, genotypeInfo.length()));
        return new GenotypeSplit(new GenotypeImp(genotypeInfo, left), new GenotypeImp(genotypeInfo, right));
    }

    private GenotypeImp concat(GenotypeImp b) {
        return new GenotypeImp(genotypeInfo, Stream.concat(genes.stream(), b.genes.stream()).toList());
    }

    private record GenotypeSplit(GenotypeImp left, GenotypeImp right) {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenotypeImp integers = (GenotypeImp) o;
        return Objects.equals(genes, integers.genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genes);
    }
}
