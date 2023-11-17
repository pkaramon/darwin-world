package agh.ics.oop.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class GrassFieldBoundsTrackerTest {
    private GrassFieldBoundsTracker grassFieldsBoundsTracker;
    @BeforeEach
    public void setUp() {
        grassFieldsBoundsTracker = new GrassFieldBoundsTracker();
    }

    @Test
    public void lowerLeftCornerOfEmptyMapShouldBeZeroZero() {
        Vector2d result = grassFieldsBoundsTracker.lowerLeft();
        assertEquals(new Vector2d(0, 0), result);
    }

    @Test
    public void upperRightCornerOfEmptyMapShouldBeZeroZero() {
        Vector2d result = grassFieldsBoundsTracker.upperRight();
        assertEquals(new Vector2d(0, 0), result);
    }

    @Test
    public void addingOneElement() {
        WorldElement element = new Animal(new Vector2d(3, 5));
        grassFieldsBoundsTracker.addElement(element);

        Vector2d lowerLeft = grassFieldsBoundsTracker.lowerLeft();
        Vector2d upperRight = grassFieldsBoundsTracker.upperRight();

        assertEquals(new Vector2d(3, 5), lowerLeft);
        assertEquals(new Vector2d(3, 5), upperRight);
    }

    @Test
    public void addingTwoElements() {
        WorldElement element1 = new Grass(new Vector2d(3, 5));
        WorldElement element2 = new Animal(new Vector2d(1, 2));
        grassFieldsBoundsTracker.addElement(element1);
        grassFieldsBoundsTracker.addElement(element2);

        Vector2d lowerLeft = grassFieldsBoundsTracker.lowerLeft();
        Vector2d upperRight = grassFieldsBoundsTracker.upperRight();

        assertEquals(new Vector2d(1, 2), lowerLeft);
        assertEquals(new Vector2d(3, 5), upperRight);
    }

    @Test
    public void addingThreeElements() {
        WorldElement element1 = new Animal(new Vector2d(3, 5));
        WorldElement element2 = new Grass(new Vector2d(1, 2));
        WorldElement element3 = new Grass(new Vector2d(6, 8));
        grassFieldsBoundsTracker.addElement(element1);
        grassFieldsBoundsTracker.addElement(element2);
        grassFieldsBoundsTracker.addElement(element3);

        Vector2d lowerLeft = grassFieldsBoundsTracker.lowerLeft();
        Vector2d upperRight = grassFieldsBoundsTracker.upperRight();

        assertEquals(new Vector2d(1, 2), lowerLeft);
        assertEquals(new Vector2d(6, 8), upperRight);
    }

    @Test
    public void removingElement() {
        WorldElement element = new Animal(new Vector2d(2, 4));
        grassFieldsBoundsTracker.addElement(element);

        grassFieldsBoundsTracker.removeElement(element);

        Vector2d lowerLeft = grassFieldsBoundsTracker.lowerLeft();
        Vector2d upperRight = grassFieldsBoundsTracker.upperRight();

        assertEquals(new Vector2d(0, 0), lowerLeft);
        assertEquals(new Vector2d(0, 0), upperRight);
    }
}