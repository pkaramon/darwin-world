package agh.ics.oop.model;

import agh.ics.oop.model.genes.Genotype;

import java.util.Optional;

public class AnimalCrosser {
    private final AnimalMatingInfo matingInfo;

    public AnimalCrosser(AnimalMatingInfo matingInfo) {
        this.matingInfo = matingInfo;
    }

    public Optional<Animal> cross(Animal father, Animal mother) {
        if (parentEnergyDeficiency(father, mother)) return Optional.empty();
        Animal child = createChild(father, mother);
        updateParentsEnergies(father, mother);
        return Optional.of(child);
    }


    private boolean parentEnergyDeficiency(Animal father, Animal mother) {
        return father.getEnergy() < matingInfo.minEnergyToReproduce() ||
                mother.getEnergy() < matingInfo.minEnergyToReproduce();
    }

    private Animal createChild(Animal father, Animal mother) {
        Genotype fatherGenotype = father.getGenotype();
        Genotype motherGenotype = mother.getGenotype();

        Genotype combined = combine(fatherGenotype, father.getEnergy(), motherGenotype, mother.getEnergy());
        Genotype mutated = combined.applyMutation(matingInfo.mutation());

        return new Animal(
                new Pose(father.getPosition(), matingInfo.getChildOrientation().get()),
                mutated,
                matingInfo.parentEnergyGivenToChild() * 2
        );
    }

    private Genotype combine(Genotype a, int aStrength, Genotype b, int bStrength) {
        int splitPoint = getSplitPoint(a.length(), aStrength, bStrength);
        Genotype stronger, weaker;
        if(aStrength > bStrength) {
            stronger = a;
            weaker = b;
        } else {
            stronger = b;
            weaker = a;
        }

        return getCombinedGenotype(splitPoint, stronger, weaker);
    }


    private static int getSplitPoint(int genotypeLength, int aStrength, int bStrength) {
        return (int) Math.ceil(
                (double) (aStrength * genotypeLength) / (double) (aStrength + bStrength)
        );
    }

    private Genotype getCombinedGenotype(int splitPoint, Genotype stronger, Genotype weaker) {
        if(matingInfo.rightSideOfGenotypeForStrongerParent().get()) {
            var strongerSplit = stronger.splitAt(stronger.length() - splitPoint);
            var weakerSplit = weaker.splitAt(stronger.length() - splitPoint);
            return weakerSplit.left().concat(strongerSplit.right());
        } else {
            var strongerSplit = stronger.splitAt(splitPoint);
            var weakerSplit = weaker.splitAt(splitPoint);
            return strongerSplit.left().concat(weakerSplit.right());
        }
    }


    private void updateParentsEnergies(Animal father, Animal mother) {
        father.useEnergy(matingInfo.parentEnergyGivenToChild());
        mother.useEnergy(matingInfo.parentEnergyGivenToChild());
    }
}
