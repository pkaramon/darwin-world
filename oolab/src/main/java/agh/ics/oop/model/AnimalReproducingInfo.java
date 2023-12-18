package agh.ics.oop.model;

import agh.ics.oop.model.genes.GeneMutation;

public record AnimalReproducingInfo(
        int minEnergyToReproduce,
        int parentEnergyGivenToChild,
        GeneMutation mutation
) {

}
