package agh.ics.oop.model.animal;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveValidator;
import agh.ics.oop.model.Pose;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.genes.Genotype;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AnimalMoverTest {
    private static final MoveValidator passThroughValidator = (pose) -> pose;
    private static final Supplier<Integer> getCurrentDay = () -> 123;
    @Test
    void move_PicksNextGeneRotatesAndTriesToMoveInNewDirection() {
        AnimalData animalData = new AnimalData(
                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                new Genotype(List.of(3,2)),
                50
        );

        AnimalMover mover = new AnimalMover(animalData, getCurrentDay);

        mover.move(passThroughValidator);

        assertEquals(new Vector2d(3, 1), animalData.getPosition());
        assertEquals(MapDirection.SOUTHEAST, animalData.getOrientation());

        mover.move(passThroughValidator);

        assertEquals(new Vector2d(2, 0), animalData.getPosition());
        assertEquals(MapDirection.SOUTHWEST, animalData.getOrientation());
    }

    @Test
    void move_ConsultsTheDesiredActionWithMoveValidator() {
        AnimalData animalData = new AnimalData(
                new Pose(new Vector2d(4, 5), MapDirection.NORTHWEST),
                new Genotype(List.of(2)),
                50
        );
        MoveValidator mockValidator = mock(MoveValidator.class);
        when(mockValidator.validate(any())).thenAnswer(invocation ->
                new Pose(new Vector2d(100, 100), MapDirection.NORTH)
        );

        AnimalMover mover = new AnimalMover(animalData, getCurrentDay);

        mover.move(mockValidator);

        verify(mockValidator).validate(new Pose(new Vector2d(5, 6), MapDirection.NORTHEAST));
        assertEquals(new Vector2d(100, 100), animalData.getPosition());
        assertEquals(MapDirection.NORTH, animalData.getOrientation());
    }

    @Test
    void move_DecreaseEnergyByOne() {
        AnimalData animalData = new AnimalData(
                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                new Genotype(List.of(3,2)),
                50
        );

        AnimalMover mover = new AnimalMover(animalData, getCurrentDay);

        mover.move(passThroughValidator);

        assertEquals(49, animalData.getEnergy());
    }

    @Test
    void move_WhenEnergyIsZero_AnimalDies() {
        AnimalData animalData = new AnimalData(
                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                new Genotype(List.of(3,2)),
                1
        );

        AnimalMover mover = new AnimalMover(animalData, getCurrentDay);

        mover.move(passThroughValidator);

        assertEquals(123, animalData.getDeathDay());
    }
}