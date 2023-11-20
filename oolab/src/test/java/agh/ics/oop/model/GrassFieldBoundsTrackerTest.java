package agh.ics.oop.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class GrassFieldBoundsTrackerTest {
    private GrassFieldBoundsTracker boundsTracker;
    @BeforeEach
    public void setUp() {
        boundsTracker = new GrassFieldBoundsTracker();
    }

    @Test
    public void lowerLeftCornerOfEmptyMapShouldBeZeroZero() {
        Vector2d result = boundsTracker.lowerLeft();
        assertEquals(new Vector2d(0, 0), result);
    }

    @Test
    public void upperRightCornerOfEmptyMapShouldBeZeroZero() {
        Vector2d result = boundsTracker.upperRight();
        assertEquals(new Vector2d(0, 0), result);
    }

    @Test
    public void addingOneElement() {
        WorldElement element = new Animal(new Vector2d(3, 5));
        boundsTracker.addElement(element);

        Vector2d lowerLeft = boundsTracker.lowerLeft();
        Vector2d upperRight = boundsTracker.upperRight();

        assertEquals(new Vector2d(3, 5), lowerLeft);
        assertEquals(new Vector2d(3, 5), upperRight);
    }

    @Test
    public void addingTwoElements() {
        WorldElement element1 = new Grass(new Vector2d(3, 5));
        WorldElement element2 = new Animal(new Vector2d(1, 2));
        boundsTracker.addElement(element1);
        boundsTracker.addElement(element2);

        Vector2d lowerLeft = boundsTracker.lowerLeft();
        Vector2d upperRight = boundsTracker.upperRight();

        assertEquals(new Vector2d(1, 2), lowerLeft);
        assertEquals(new Vector2d(3, 5), upperRight);
    }

    @Test
    public void addingThreeElements() {
        WorldElement element1 = new Animal(new Vector2d(3, 5));
        WorldElement element2 = new Grass(new Vector2d(1, 2));
        WorldElement element3 = new Grass(new Vector2d(6, 8));
        boundsTracker.addElement(element1);
        boundsTracker.addElement(element2);
        boundsTracker.addElement(element3);

        Vector2d lowerLeft = boundsTracker.lowerLeft();
        Vector2d upperRight = boundsTracker.upperRight();

        assertEquals(new Vector2d(1, 2), lowerLeft);
        assertEquals(new Vector2d(6, 8), upperRight);
    }

    @Test
    public void removingElement() {
        WorldElement element = new Animal(new Vector2d(2, 4));
        boundsTracker.addElement(element);

        boundsTracker.removeElement(element);

        Vector2d lowerLeft = boundsTracker.lowerLeft();
        Vector2d upperRight = boundsTracker.upperRight();

        assertEquals(new Vector2d(0, 0), lowerLeft);
        assertEquals(new Vector2d(0, 0), upperRight);
    }
}