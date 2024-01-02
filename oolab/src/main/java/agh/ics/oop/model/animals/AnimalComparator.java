package agh.ics.oop.model.animals;

import java.util.Comparator;

public class AnimalComparator implements Comparator<AnimalData> {
    @Override
    public int compare(AnimalData a, AnimalData b) {
        return Comparator.comparing(AnimalData::getEnergy)
                        .thenComparing(AnimalData::getBornDay, Comparator.reverseOrder())
                        .thenComparing(animalData -> animalData.getChildren().size())
                        .compare(a, b);
    }
}
