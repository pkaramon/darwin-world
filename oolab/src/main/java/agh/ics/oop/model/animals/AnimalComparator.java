package agh.ics.oop.model.animals;

import java.util.Comparator;
import java.util.function.Supplier;

public class AnimalComparator implements Comparator<AnimalData> {
    private final Supplier<Integer> inCaseOfDraw;

    public AnimalComparator(Supplier<Integer> inCaseOfDraw) {
        this.inCaseOfDraw = inCaseOfDraw;
    }


    @Override
    public int compare(AnimalData a, AnimalData b) {
        int result = Comparator.comparing(AnimalData::getEnergy)
                        .thenComparing(AnimalData::getBirthDay, Comparator.reverseOrder())
                        .thenComparing(animalData -> animalData.getChildren().size())
                        .compare(a, b);
        return result == 0 ? inCaseOfDraw.get() : result;
    }
}
