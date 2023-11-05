package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextMapTest {

    @Test
    void canMoveTo_ReturnsTrueIfPositionIsWithinBoundsFalseOtherwise() {
        WorldMap<String, Integer> map = new TextMap();
        map.place("ala");
        map.place("ma");
        map.place("kota");

        assertFalse(map.canMoveTo(-1));
        assertTrue(map.canMoveTo(0));
        assertTrue(map.canMoveTo(1));
        assertTrue(map.canMoveTo(2));
        assertFalse(map.canMoveTo(3));

    }

    @Test
    void isOccupied_ReturnsTrueIfPositionIsTakenFalseOtherwise() {
        WorldMap<String, Integer> map = new TextMap();
        map.place("hello");
        map.place("there");

        assertTrue(map.isOccupied(0));
        assertTrue(map.isOccupied(1));
        assertFalse(map.isOccupied(-1));
        assertFalse(map.isOccupied(2));
    }

    @Test
    void objectAt_ReturnsStringAtPositionOrNullIfPositionIsEmpty() {
        WorldMap<String, Integer> map = new TextMap();
        map.place("hello");
        map.place("there");

        assertEquals(map.objectAt(0), "hello");
        assertEquals(map.objectAt(1), "there");
        assertNull(map.objectAt(2));
    }


    private void assertObjectsListIsEqual(WorldMap<String, Integer> map, List<String> strings) {
        for (int i = 0; i < strings.size(); i++) {
            assertEquals(map.objectAt(i), strings.get(i));
        }
    }

    @Test
    void place_AlwaysAddsStringToEndIfNotAlreadyPresent() {
        WorldMap<String, Integer> map = new TextMap();
        map.place("hello");
        map.place("world");
        map.place("!");

        assertObjectsListIsEqual(map, List.of("hello", "world", "!"));

        map.place("world");

        assertNull(map.objectAt(3));
    }

    @Test
    void move_ForwardOrBackward_MovesStringForwardOrBackwardAndSwitchesItWithOneNextToIt() {
        WorldMap<String, Integer> map = new TextMap();
        map.place("ala");
        map.place("ma");
        map.place("kota");
        map.place(".");

        map.move("ma", MoveDirection.FORWARD);

        assertObjectsListIsEqual(map, List.of("ala", "kota", "ma", "."));

        map.move("ala", MoveDirection.FORWARD);

        assertObjectsListIsEqual(map, List.of("kota", "ala", "ma", "."));

        map.move(".", MoveDirection.BACKWARD);

        assertObjectsListIsEqual(map, List.of("kota", "ala", ".", "ma"));

        map.move("ala",  MoveDirection.BACKWARD);

        assertObjectsListIsEqual(map, List.of("ala", "kota", ".", "ma"));
    }

    @Test
    void move_DoesNotAllowStringToGoOutOfBounds() {
        WorldMap<String, Integer> map = new TextMap();
        map.place("ala");
        map.place("ma");
        map.place("kota");

        map.move("ala", MoveDirection.BACKWARD);
        assertObjectsListIsEqual(map, List.of("ala", "ma", "kota"));


        map.move("kota", MoveDirection.FORWARD);
        assertObjectsListIsEqual(map, List.of("ala", "ma", "kota"));
    }
}