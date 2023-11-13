package agh.ics.oop.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MapBoundsTest {
    private MapBounds mapBounds;
    @BeforeEach
    public void setUp() {
        mapBounds = new MapBounds();
    }

    @Test
    public void lowerLeftCornerOfEmptyMapShouldBeZeroZero() {
        Vector2d result = mapBounds.lowerLeft();
        assertEquals(new Vector2d(0, 0), result);
    }

    @Test
    public void upperRightCornerOfEmptyMapShouldBeZeroZero() {
        Vector2d result = mapBounds.upperRight();
        assertEquals(new Vector2d(0, 0), result);
    }

    @Test
    public void addingOneElement() {
        WorldElement element = new Animal(new Vector2d(3, 5));
        mapBounds.addElement(element);

        Vector2d lowerLeft = mapBounds.lowerLeft();
        Vector2d upperRight = mapBounds.upperRight();

        assertEquals(new Vector2d(3, 5), lowerLeft);
        assertEquals(new Vector2d(3, 5), upperRight);
    }

    @Test
    public void addingTwoElements() {
        WorldElement element1 = new Grass(new Vector2d(3, 5));
        WorldElement element2 = new Animal(new Vector2d(1, 2));
        mapBounds.addElement(element1);
        mapBounds.addElement(element2);

        Vector2d lowerLeft = mapBounds.lowerLeft();
        Vector2d upperRight = mapBounds.upperRight();

        assertEquals(new Vector2d(1, 2), lowerLeft);
        assertEquals(new Vector2d(3, 5), upperRight);
    }

    @Test
    public void addingThreeElements() {
        WorldElement element1 = new Animal(new Vector2d(3, 5));
        WorldElement element2 = new Grass(new Vector2d(1, 2));
        WorldElement element3 = new Grass(new Vector2d(6, 8));
        mapBounds.addElement(element1);
        mapBounds.addElement(element2);
        mapBounds.addElement(element3);

        Vector2d lowerLeft = mapBounds.lowerLeft();
        Vector2d upperRight = mapBounds.upperRight();

        assertEquals(new Vector2d(1, 2), lowerLeft);
        assertEquals(new Vector2d(6, 8), upperRight);
    }

    @Test
    public void removingElement() {
        WorldElement element = new Animal(new Vector2d(2, 4));
        mapBounds.addElement(element);

        mapBounds.removeElement(element);

        Vector2d lowerLeft = mapBounds.lowerLeft();
        Vector2d upperRight = mapBounds.upperRight();

        assertEquals(new Vector2d(0, 0), lowerLeft);
        assertEquals(new Vector2d(0, 0), upperRight);
    }
}