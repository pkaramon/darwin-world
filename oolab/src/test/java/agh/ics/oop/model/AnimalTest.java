package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalTest {

    @Test
    void newAnimalIsCreatedAtBoardCenterFacingNorth() {
        Animal a = new Animal();

        assertTrue(a.isAt(Animal.MAP_CENTER));
        assertTrue(a.facesDirection(MapDirection.NORTH));
    }

    @Test
    void toString_printsPositionAndOrientation() {
        Animal a = new Animal(new Vector2d(2, 3));
        Animal b = new Animal(new Vector2d(3, 4));
        b.move(MoveDirection.RIGHT);

        assertEquals("pozycja=(2,3), orientacja=Północ", a.toString());
        assertEquals("pozycja=(3,4), orientacja=Wschód", b.toString());
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
            a.move(moves[i]);
            assertTrue(a.facesDirection(expectedDirectionAfter[i]));
        }
    }

    @Test
    void move_ForwardOrBackward_MovesForwardOrBackwardInCurrentDirection() {
        Animal a = new Animal(new Vector2d(2, 2));

        a.move(MoveDirection.FORWARD);
        assertTrue(a.isAt(new Vector2d(2, 3)));

        a.move(MoveDirection.BACKWARD);
        assertTrue(a.isAt(new Vector2d(2, 2)));

        a.move(MoveDirection.LEFT);
        a.move(MoveDirection.FORWARD);
        assertTrue(a.isAt(new Vector2d(1, 2)));

        a.move(MoveDirection.BACKWARD);
        assertTrue(a.isAt(new Vector2d(2, 2)));
    }

    @Test
    void move_TryingToMoveOutOfBounds_CallGetsIgnored() {
        Animal a = new Animal(Animal.MAP_LOWER_LEFT);
        a.move(MoveDirection.BACKWARD);
        assertTrue(a.isAt(Animal.MAP_LOWER_LEFT));

        a.move(MoveDirection.LEFT);
        a.move(MoveDirection.FORWARD);
        assertTrue(a.isAt(Animal.MAP_LOWER_LEFT));


        Animal b = new Animal(Animal.MAP_UPPER_RIGHT);
        b.move(MoveDirection.FORWARD);
        assertTrue(b.isAt(Animal.MAP_UPPER_RIGHT));

        b.move(MoveDirection.RIGHT);
        b.move(MoveDirection.FORWARD);
        assertTrue(b.isAt(Animal.MAP_UPPER_RIGHT));
    }

}