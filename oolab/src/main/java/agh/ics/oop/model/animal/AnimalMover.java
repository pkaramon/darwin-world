package agh.ics.oop.model.animal;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveValidator;
import agh.ics.oop.model.Pose;
import agh.ics.oop.model.Vector2d;

import java.util.function.Supplier;

public class AnimalMover {
    private final AnimalData animalData;
    private final Supplier<Integer> getCurrentDay;

    public AnimalMover(AnimalData animalData, Supplier<Integer> getCurrentDay) {

        this.animalData = animalData;
        this.getCurrentDay = getCurrentDay;
    }

    public void move(MoveValidator moveValidator) {
        int gene = animalData.getGenotype().nextGene();

        MapDirection desiredOrientation = animalData.getOrientation().nextN(gene);
        Vector2d desiredPosition = animalData.getPosition().add(desiredOrientation.toUnitVector());

        animalData.setPose(moveValidator.validate(new Pose(desiredPosition, desiredOrientation)));
        animalData.useEnergy(1);
        animalData.setDeathDay(getCurrentDay.get());
    }
}
