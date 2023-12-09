package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {
    @Test
    void isOccupied_ReturnsTrueIfPositionIsTakenOrOutOfBoundsFalseOtherwise() throws PositionAlreadyOccupiedException {
        WorldMap map = new RectangularMap(4, 4);
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
    void objectAt_ReturnsAnimalIfPresent() throws PositionAlreadyOccupiedException {
        WorldMap map = new RectangularMap(4, 4);
        Animal a = new Animal(new Vector2d(1, 1));
        Animal b = new Animal(new Vector2d(2, 3));
        map.place(a);
        map.place(b);

        assertEquals(map.objectAt(a.getPosition()),  Optional.of(a));
        assertEquals(map.objectAt(b.getPosition()), Optional.of(b));
        assertTrue(map.objectAt(new Vector2d(0, 0)).isEmpty());
        assertTrue(map.objectAt(new Vector2d(2, 2)).isEmpty());
        assertTrue(map.objectAt(new Vector2d(-3, 3)).isEmpty());
    }

    @Test
    void canMoveTo_PositionIsEmptyAndWithinBounds_ReturnsTrue() {
        WorldMap map = new RectangularMap(4, 3);

        assertTrue(map.canMoveTo(new Vector2d(1, 2)));
        assertTrue(map.canMoveTo(new Vector2d(3, 1)));
    }

    @Test
    void canMoveTo_PositionIsOccupiedOrOutOfBounds_ReturnsFalse() throws PositionAlreadyOccupiedException {
        WorldMap map = new RectangularMap(4, 3);
        map.place(new Animal(new Vector2d(1, 2)));

        assertFalse(map.canMoveTo(new Vector2d(-1, 0)));
        assertFalse(map.canMoveTo(new Vector2d(4, 3)));
        assertFalse(map.canMoveTo(new Vector2d(1, 2)));
    }

    @Test
    void place_PositionIsWithinBoundsAndIsNotOccupied_PlacesTheAnimal() throws PositionAlreadyOccupiedException {
        WorldMap map = new RectangularMap(4, 3);
        Animal a = new Animal(new Vector2d(1, 0));
        Animal b = new Animal(new Vector2d(2, 2));

        map.place(a);
        map.place(b);

        assertEquals(map.objectAt(new Vector2d(1, 0)), Optional.of(a));
        assertEquals(map.objectAt(new Vector2d(2, 2)), Optional.of(b));
    }

    @Test
    void place_PositionIsOutOfBoundsOrOccupied_ThrowsException() throws PositionAlreadyOccupiedException {
        WorldMap map = new RectangularMap(4, 4);
        Animal a = new Animal(new Vector2d(1, 0));
        map.place(a);
        Animal b = new Animal(new Vector2d(1, 0));
        Animal c = new Animal(new Vector2d(-1, 3));
        Animal d = new Animal(new Vector2d(7, 3));

        Throwable exception = assertThrows(PositionAlreadyOccupiedException.class, () ->  map.place(b));

        assertEquals("Position (1,0) is already occupied", exception.getMessage());
        assertEquals(map.objectAt(new Vector2d(1, 0)), Optional.of(a));

        assertThrows(PositionAlreadyOccupiedException.class, () ->  map.place(c));
        assertThrows(PositionAlreadyOccupiedException.class, () ->  map.place(d));

        assertTrue(map.objectAt(new Vector2d(-1, 3)).isEmpty());
        assertTrue(map.objectAt(new Vector2d(7, 3)).isEmpty());
    }

    @Test
    void move_AnimalIsNotPresentOnMap_DoesNothing() {
        WorldMap map = new RectangularMap(4, 4);
        Animal a = new Animal(new Vector2d(1, 1));

        map.move(a, MoveDirection.FORWARD);

        assertFalse(map.isOccupied(new Vector2d(1, 1)));
        assertTrue(map.objectAt(new Vector2d(1, 1)).isEmpty());
    }

    @Test
    void move_AnimalIsPresentOnMap_MovesAnimalAccordingToDirectionIfPossible() throws PositionAlreadyOccupiedException {
        WorldMap map = new RectangularMap(4, 4);
        Vector2d position = new Vector2d(1, 1);
        Vector2d newPosition = new Vector2d(1, 2);
        Animal a = new Animal(position);
        map.place(a);

        map.move(a, MoveDirection.FORWARD);

        assertEquals(map.objectAt(newPosition), Optional.of(a));
        assertTrue(map.isOccupied(newPosition));

        assertTrue(map.objectAt(position).isEmpty());
        assertFalse(map.isOccupied(position));
    }

    @Test
    void move_AnimalIsPresentOnMap_RotationsDoNotAlterAnimalsPlacement() throws PositionAlreadyOccupiedException {
        WorldMap map = new RectangularMap(4, 4);
        Vector2d position = new Vector2d(1, 1);
        Animal a = new Animal(position);
        map.place(a);

        map.move(a, MoveDirection.LEFT);

        assertEquals(map.objectAt(position), Optional.of(a));

        map.move(a, MoveDirection.RIGHT);

        assertEquals(map.objectAt(position), Optional.of(a));
    }

    @Test
    void getElements_ReturnsAllOfAnimals() throws PositionAlreadyOccupiedException {
        WorldMap map = new RectangularMap(4, 4);
        Animal a = new Animal(new Vector2d(1, 0));
        Animal b = new Animal(new Vector2d(1, 2));
        Animal c = new Animal(new Vector2d(3, 2));
        map.place(a);
        map.place(b);
        map.place(c);

        Collection<WorldElement> elements = map.getElements();

        assertEquals(3, elements.size());
        assertTrue(elements.containsAll( List.of(a, b, c) ));

    }

    @Test
    void getOrderedAnimals_ReturnsAnimalsSortedByXThenByY() throws PositionAlreadyOccupiedException {
        WorldMap map = new GrassField(5);

        Animal a = new Animal(new Vector2d(2, 3));
        Animal b = new Animal(new Vector2d(2, 1));
        Animal c = new Animal(new Vector2d(4, 0));
        Animal d = new Animal(new Vector2d(3, 1));
        Animal e = new Animal(new Vector2d(1, 10));

        for (Animal animal : List.of(a, b, c, d, e)) {
            map.place(animal);
        }

        assertIterableEquals(map.getOrderedAnimals(), List.of(e, b, a, d, c));
    }

}