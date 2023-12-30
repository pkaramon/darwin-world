package agh.ics.oop.model.animals;

import agh.ics.oop.model.Pose;
import agh.ics.oop.model.genes.Genotype;

import java.util.Optional;

public class AnimalCrosser {
    private final AnimalMatingInfo matingInfo;

    public AnimalCrosser(AnimalMatingInfo matingInfo) {
        this.matingInfo = matingInfo;
    }

    public Optional<AnimalData> cross(AnimalData father, AnimalData mother) {
        if (parentEnergyDeficiency(father, mother)) return Optional.empty();
        AnimalData child = createChild(father, mother);
        updateParentsEnergies(father, mother);
        attributeChildToParents(child, father, mother);
        return Optional.of(child);
    }

    private void attributeChildToParents(AnimalData child, AnimalData father, AnimalData mother) {
        father.addChild(child);
        mother.addChild(child);
    }


    private boolean parentEnergyDeficiency(AnimalData father, AnimalData mother) {
        return father.getEnergy() < matingInfo.minEnergyToReproduce() ||
                mother.getEnergy() < matingInfo.minEnergyToReproduce();
    }

    private AnimalData createChild(AnimalData father, AnimalData mother) {
        Genotype fatherGenotype = father.getGenotype();
        Genotype motherGenotype = mother.getGenotype();

        Genotype combined = combine(fatherGenotype, father.getEnergy(), motherGenotype, mother.getEnergy());
        Genotype mutated = combined.applyMutation(matingInfo.mutation());

        return new AnimalData(
                new Pose(father.getPosition(), matingInfo.getChildOrientation().get()),
                mutated,
                matingInfo.parentEnergyGivenToChild() * 2,
                matingInfo.getCurrentDay().get()
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


    private void updateParentsEnergies(AnimalData father, AnimalData mother) {
        father.useEnergy(matingInfo.parentEnergyGivenToChild());
        mother.useEnergy(matingInfo.parentEnergyGivenToChild());
    }
}
