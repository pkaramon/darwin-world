package agh.ics.oop.model.animals;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.genes.GeneMutation;

import java.util.function.Supplier;

public record AnimalMatingInfo(
        int minEnergyToReproduce,
        int parentEnergyGivenToChild,
        GeneMutation mutation,
        Supplier<Boolean> rightSideOfGenotypeForStrongerParent,
        Supplier<MapDirection> getChildOrientation,
        Supplier<Integer> getCurrentDay
) {

}
