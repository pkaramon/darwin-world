package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {

    @Test
    public void next_MovesDirectionClockwise() {
        assertEquals(MapDirection.NORTHEAST, MapDirection.NORTH.next());
        assertEquals(MapDirection.EAST, MapDirection.NORTHEAST.next());
        assertEquals(MapDirection.SOUTHEAST, MapDirection.EAST.next());
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTHEAST.next());
        assertEquals(MapDirection.SOUTHWEST, MapDirection.SOUTH.next());
        assertEquals(MapDirection.WEST, MapDirection.SOUTHWEST.next());
        assertEquals(MapDirection.NORTHWEST, MapDirection.WEST.next());
        assertEquals(MapDirection.NORTH, MapDirection.NORTHWEST.next());
    }


    @Test
    public void previous_MovesMapDirectionCounterClockwise() {
        assertEquals(MapDirection.NORTHWEST, MapDirection.NORTH.previous());
        assertEquals(MapDirection.NORTH, MapDirection.NORTHEAST.previous());
        assertEquals(MapDirection.NORTHEAST, MapDirection.EAST.previous());
        assertEquals(MapDirection.EAST, MapDirection.SOUTHEAST.previous());
        assertEquals(MapDirection.SOUTHEAST, MapDirection.SOUTH.previous());
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTHWEST.previous());
        assertEquals(MapDirection.SOUTHWEST, MapDirection.WEST.previous());
        assertEquals(MapDirection.WEST, MapDirection.NORTHWEST.previous());
    }

    @Test
    public void nextN_MovesDirectionClockwiseNTimes() {
        assertEquals(MapDirection.NORTH, MapDirection.NORTH.nextN(8));
        assertEquals(MapDirection.EAST, MapDirection.NORTH.nextN(2));
        assertEquals(MapDirection.NORTHEAST, MapDirection.SOUTH.nextN(5));
        assertEquals(MapDirection.NORTHEAST, MapDirection.SOUTH.nextN(10*8 + 5));
    }

    @Test
    public void previousN_MovesDirectionCounterClockwiseNTimes() {
        assertEquals(MapDirection.NORTH, MapDirection.NORTH.previousN(8));
        assertEquals(MapDirection.WEST, MapDirection.NORTH.previousN(2));
        assertEquals(MapDirection.NORTHWEST, MapDirection.SOUTH.previousN(5));
        assertEquals(MapDirection.NORTHWEST, MapDirection.SOUTH.previousN(10*8 + 5));
    }
}
