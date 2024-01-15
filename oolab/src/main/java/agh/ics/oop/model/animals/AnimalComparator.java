package agh.ics.oop.model.animals;

import java.util.Comparator;
import java.util.function.BiFunction;

public class AnimalComparator implements Comparator<AnimalData> {
    private final BiFunction<AnimalData, AnimalData, Integer>  inCaseOfDraw;

    public AnimalComparator(BiFunction<AnimalData, AnimalData, Integer> inCaseOfDraw) {
        this.inCaseOfDraw = inCaseOfDraw;
    }

    @Override
    public int compare(AnimalData a, AnimalData b) {
        int result = Comparator.comparing(AnimalData::getEnergy)
                        .thenComparing(AnimalData::getBirthDay, Comparator.reverseOrder())
                        .thenComparing(animalData -> animalData.getChildren().size())
                        .compare(a, b);
        return result == 0 ? inCaseOfDraw.apply(a, b) : result;
    }
}
