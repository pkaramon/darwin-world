package agh.ics.oop.model;

import agh.ics.oop.model.genes.Genotype;

import java.util.Optional;
import java.util.Random;

public class AnimalCrosser {
//    private final AnimalMatingInfo matingInfo;
//
//    public AnimalCrosser(AnimalMatingInfo matingInfo) {
//        this.matingInfo = matingInfo;
//    }
//
//
//
//    public Optional<Animal> cross(Animal father, Animal mother) {
//        if (parentEnergyDeficiency(father, mother)) return Optional.empty();
//        Animal child = createChild(father, mother);
//        updateParentsEnergies(father, mother);
//        return Optional.of(child);
//    }
//
//
//    private boolean parentEnergyDeficiency(Animal father, Animal mother) {
//        return father.getEnergy() < matingInfo.minEnergyToReproduce() ||
//                mother.getEnergy() < matingInfo.minEnergyToReproduce();
//    }
//
//    private Animal createChild(Animal father, Animal mother) {
//        Genotype fatherGenotype = father.getGenotype();
//        Genotype motherGenotype = mother.getGenotype();
//
//
//
//        Genotype combined = combine(fatherGenotype, father.getEnergy(), motherGenotype, mother.getEnergy());
//        Genotype mutated = combined.applyMutation(matingInfo.mutation());
//        return new Animal(
//                matingInfo,
//                new Pose(new Vector2d(0,0), MapDirection.EAST),
//                mutated,
//                matingInfo.parentEnergyGivenToChild() * 2
//        );
//    }
//
//    private Genotype combine(Genotype a, int aStrength, Genotype b, int bStrength) {
//        int splitPoint = (int) Math.ceil(
//                (double) ((aStrength) * a.length()) / (double) (aStrength + bStrength)
//        );
//
//        Random random = new Random();
//
//        if (random.nextBoolean()) {
//            var aSplit = a.splitAt(splitPoint);
//            var bSplit = b.splitAt(splitPoint);
//            return aSplit.left().concat(bSplit.right());
//        } else {
//            var aSplit = a.splitAt(a.length() - splitPoint);
//            var bSplit = b.splitAt(a.length() - splitPoint);
//            return bSplit.left().concat(aSplit.right());
//        }
//    }
//
//    private void updateParentsEnergies(Animal father, Animal mother) {
//        father.useEnergy(matingInfo.parentEnergyGivenToChild());
//        mother.useEnergy(matingInfo.parentEnergyGivenToChild());
//    }
}
