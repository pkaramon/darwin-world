package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalTest {
    private static class RectMoveValidator implements MoveValidator {
        public final int HEIGHT = 5;
        public final int WIDTH = 5;

        @Override
        public boolean canMoveTo(Vector2d position) {
            int x = position.getX();
            int y = position.getY();
            return x >=0 && x < WIDTH && y >= 0 && y < HEIGHT;
        }
    }

    private final MoveValidator moveValidator = new RectMoveValidator();

    @Test
    void newAnimalIsCreatedAtDefaultPositionFacingNorth() {
        Animal a = new Animal();

        assertTrue(a.isAt(Animal.DEFAULT_POSITION));
        assertTrue(a.facesDirection(MapDirection.NORTH));
    }

    @Test
    void toString_printsOrientationIndicator() {
        Animal a = new Animal(new Vector2d(2, 3));
        assertEquals("^", a.toString());

        a.move(MoveDirection.RIGHT, moveValidator);
        assertEquals(">", a.toString());

        a.move(MoveDirection.RIGHT, moveValidator);
        assertEquals("v", a.toString());

        a.move(MoveDirection.RIGHT, moveValidator);
        assertEquals("<", a.toString());
    }

    @Test
    void move_LeftOrRight_ChangesAnimalsOrientation() {
        Animal a = new Animal();
        MoveDirection[] moves = {
                MoveDirection.LEFT, MoveDirection.LEFT, MoveDirection.LEFT,
                MoveDirection.LEFT, MoveDirection.RIGHT, MoveDirection.RIGHT,
                MoveDirection.RIGHT,
        };
        MapDirection[] expectedDirectionAfter = {
                MapDirection.WEST, MapDirection.SOUTH, MapDirection.EAST,
                MapDirection.NORTH, MapDirection.EAST, MapDirection.SOUTH,
                MapDirection.WEST,
        };

        for (int i = 0; i < moves.length; i++){
            a.move(moves[i], moveValidator);
            assertTrue(a.facesDirection(expectedDirectionAfter[i]));
        }
    }

    @Test
    void move_ForwardOrBackward_MovesForwardOrBackwardInCurrentDirection() {
        Animal a = new Animal(new Vector2d(2, 2));

        a.move(MoveDirection.FORWARD, moveValidator);
        assertTrue(a.isAt(new Vector2d(2, 3)));

        a.move(MoveDirection.BACKWARD, moveValidator);
        assertTrue(a.isAt(new Vector2d(2, 2)));

        a.move(MoveDirection.LEFT, moveValidator);
        a.move(MoveDirection.FORWARD, moveValidator);
        assertTrue(a.isAt(new Vector2d(1, 2)));

        a.move(MoveDirection.BACKWARD, moveValidator);
        assertTrue(a.isAt(new Vector2d(2, 2)));
    }

    @Test
    void move_TryingToMoveOutOfBounds_CallGetsIgnored() {
        Animal a = new Animal(new Vector2d(0,0));
        a.move(MoveDirection.BACKWARD, moveValidator);
        assertTrue(a.isAt(new Vector2d(0,0)));

        a.move(MoveDirection.LEFT, moveValidator);
        a.move(MoveDirection.FORWARD, moveValidator);
        assertTrue(a.isAt(new Vector2d(0,0)));


        Animal b = new Animal(new Vector2d(4,4));
        b.move(MoveDirection.FORWARD, moveValidator);
        assertTrue(b.isAt(new Vector2d(4,4)));

        b.move(MoveDirection.RIGHT, moveValidator);
        b.move(MoveDirection.FORWARD, moveValidator);
        assertTrue(b.isAt(new Vector2d(4,4)));
    }

}