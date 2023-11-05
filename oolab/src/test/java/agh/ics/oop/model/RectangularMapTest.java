package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {
    @Test
    void isOccupied_ReturnsTrueIfPositionIsTakenOrOutOfBoundsFalseOtherwise() {
        WorldMap<Animal, Vector2d> map = new RectangularMap(4, 4);
        Animal a = new Animal(new Vector2d(1, 1));
        Animal b = new Animal(new Vector2d(2, 3));
        map.place(a);
        map.place(b);

        assertTrue(map.isOccupied(a.getPosition()));
        assertTrue(map.isOccupied(b.getPosition()));
        assertFalse(map.isOccupied(new Vector2d(0, 0)));

        assertFalse(map.isOccupied(new Vector2d(2, 2)));

        assertFalse(map.isOccupied(new Vector2d(4, 4)));
        assertFalse(map.isOccupied(new Vector2d(-1, 2)));
    }

    @Test
    void objectAt_ReturnsAnimalIfPresentNullOtherwise() {
        WorldMap<Animal, Vector2d> map = new RectangularMap(4, 4);
        Animal a = new Animal(new Vector2d(1, 1));
        Animal b = new Animal(new Vector2d(2, 3));
        map.place(a);
        map.place(b);

        assertSame(map.objectAt(a.getPosition()), a);
        assertSame(map.objectAt(b.getPosition()), b);
        assertNull(map.objectAt(new Vector2d(0, 0)));
        assertNull(map.objectAt(new Vector2d(2, 2)));
        assertNull(map.objectAt(new Vector2d(-3, 3)));
    }

    @Test
    void canMoveTo_PositionIsEmptyAndWithinBounds_ReturnsTrue() {
        WorldMap<Animal, Vector2d> map = new RectangularMap(4, 3);

        assertTrue(map.canMoveTo(new Vector2d(1, 2)));
        assertTrue(map.canMoveTo(new Vector2d(3, 1)));
    }

    @Test
    void canMoveTo_PositionIsOccupiedOrOutOfBounds_ReturnsFalse() {
        WorldMap<Animal, Vector2d> map = new RectangularMap(4, 3);
        map.place(new Animal(new Vector2d(1, 2)));

        assertFalse(map.canMoveTo(new Vector2d(-1, 0)));
        assertFalse(map.canMoveTo(new Vector2d(4, 3)));
        assertFalse(map.canMoveTo(new Vector2d(1, 2)));
    }

    @Test
    void place_PositionIsWithinBoundsAndIsNotOccupied_ReturnsTrueAndPlacesTheAnimal() {
        WorldMap<Animal, Vector2d> map = new RectangularMap(4, 3);
        Animal a = new Animal(new Vector2d(1, 0));
        Animal b = new Animal(new Vector2d(2, 2));

        assertTrue(map.place(a));
        assertTrue(map.place(b));

        assertSame(map.objectAt(new Vector2d(1, 0)), a);
        assertSame(map.objectAt(new Vector2d(2, 2)), b);
    }

    @Test
    void place_PositionIsOutOfBoundsOrOccupied_ReturnsFalseDoesNothing() {
        WorldMap<Animal, Vector2d> map = new RectangularMap(4, 4);
        Animal a = new Animal(new Vector2d(1, 0));
        map.place(a);
        Animal b = new Animal(new Vector2d(1, 0));
        Animal c = new Animal(new Vector2d(-1, 3));
        Animal d = new Animal(new Vector2d(7, 3));

        assertFalse(map.place(b));
        assertSame(map.objectAt(new Vector2d(1, 0)), a);

        assertFalse(map.place(c));
        assertFalse(map.place(d));

        assertNull(map.objectAt(new Vector2d(-1, 3)));
        assertNull(map.objectAt(new Vector2d(7, 3)));
    }

    @Test
    void move_AnimalIsNotPresentOnMap_DoesNothing() {
        WorldMap<Animal, Vector2d> map = new RectangularMap(4, 4);
        Animal a = new Animal(new Vector2d(1, 1));

        map.move(a, MoveDirection.FORWARD);

        assertFalse(map.isOccupied(new Vector2d(1, 1)));
        assertNull(map.objectAt(new Vector2d(1, 1)));
    }

    @Test
    void move_AnimalIsPresentOnMap_MovesAnimalAccordingToDirectionIfPossible() {
        WorldMap<Animal, Vector2d> map = new RectangularMap(4, 4);
        Vector2d position = new Vector2d(1, 1);
        Vector2d newPosition = new Vector2d(1, 2);
        Animal a = new Animal(position);
        map.place(a);

        map.move(a, MoveDirection.FORWARD);

        assertSame(map.objectAt(newPosition), a);
        assertTrue(map.isOccupied(newPosition));

        assertNull(map.objectAt(position));
        assertFalse(map.isOccupied(position));
    }

    @Test
    void move_AnimalIsPresentOnMap_RotationsDoNotAlterAnimalsPlacement() {
        WorldMap<Animal, Vector2d> map = new RectangularMap(4, 4);
        Vector2d position = new Vector2d(1, 1);
        Animal a = new Animal(position);
        map.place(a);

        map.move(a, MoveDirection.LEFT);

        assertSame(map.objectAt(position), a);

        map.move(a, MoveDirection.RIGHT);

        assertSame(map.objectAt(position), a);
    }

}