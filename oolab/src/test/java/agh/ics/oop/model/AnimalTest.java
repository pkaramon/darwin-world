package agh.ics.oop.model;

import agh.ics.oop.model.genes.Genotype;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AnimalTest {
    @Test
    void move_PicksNextGeneRotatesAndTriesToMoveInNewDirection() {
        Animal a = new Animal(
                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                new Genotype(List.of(3,2)),
                50
        );
        MoveValidator passThroughValidator = (pose) -> pose;

        a.move(passThroughValidator);

        assertEquals(new Vector2d(3, 1), a.getPosition());
        assertEquals(MapDirection.SOUTHEAST, a.getOrientation());

        a.move(passThroughValidator);

        assertEquals(new Vector2d(2, 0), a.getPosition());
        assertEquals(MapDirection.SOUTHWEST, a.getOrientation());
    }

    @Test
    void move_ConsultsTheDesiredActionWithMoveValidator() {
        Animal a = new Animal(
                new Pose(new Vector2d(4, 5), MapDirection.NORTHWEST),
                new Genotype(List.of(2)),
                50
        );
        MoveValidator mockValidator = mock(MoveValidator.class);
        when(mockValidator.validate(any())).thenAnswer(invocation ->
                new Pose(new Vector2d(100, 100), MapDirection.NORTH)
        );

        a.move(mockValidator);

        verify(mockValidator).validate(new Pose(new Vector2d(5, 6), MapDirection.NORTHEAST));
        assertEquals(new Vector2d(100, 100), a.getPosition());
        assertEquals(MapDirection.NORTH, a.getOrientation());
    }

    @Test
    void useEnergy_UsageIsGreaterThanTotalEnergy_ThrowsIllegalArgumentException() {
        Animal a = new Animal(
                new Pose(new Vector2d(4, 5), MapDirection.NORTH),
                new Genotype(List.of()),
                50
        );

        assertThrows(IllegalArgumentException.class, () -> {
            a.useEnergy(51);
        });
    }

    @Test
    void useEnergy_UsageIsSmallerOrEqualToTotalEnergy_TotalEnergyIsReduced() {
        Animal a = new Animal(
                new Pose(new Vector2d(4, 5), MapDirection.NORTH),
                new Genotype(List.of()),
                50
        );

        a.useEnergy(20);

        assertEquals(30, a.getEnergy());
    }
}