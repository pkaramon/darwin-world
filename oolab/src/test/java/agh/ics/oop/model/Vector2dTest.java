package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class Vector2dTest {
    @Test
    public void equals_OtherIsNotVector_ReturnsFalse() {
        Vector2d v = new Vector2d(2, 3);

        assertFalse(v.equals("not a string"));
    }

    @Test
    public void equals_OtherIsNull_ReturnsFalse() {
        Vector2d v = new Vector2d(2, 3);

        assertFalse(v.equals(null));
    }

    @Test
    public void equals_VectorsHaveTheSameComponents_ReturnsTrue() {
        Vector2d a = new Vector2d(2, 3);
        Vector2d b = new Vector2d(2, 3);

        assertTrue(a.equals(b));
    }

    @Test
    public void equals_VectorsHaveDifferentComponents_ReturnsFalse() {
        Vector2d a = new Vector2d(2, 3);
        Vector2d b = new Vector2d(8, 9);

        assertFalse(a.equals(b));
    }


    @Test
    public void toString_returnsVectorStringRepresentation() {
        Vector2d v = new Vector2d(3, -4);

        assertEquals("(3,-4)", v.toString());
    }

    @Test
    public void precedes_OtherHasBothComponentsGreaterOrEqual_ReturnsTrue() {
        Vector2d a = new Vector2d(2, 3);
        Vector2d b = new Vector2d(2, 4);
        Vector2d c = new Vector2d(3, 4);

        assertTrue(a.precedes(a));
        assertTrue(a.precedes(b));
        assertTrue(a.precedes(c));
    }

    @Test
    public void precedes_OtherHasOneOrTwoComponentsSmaller_ReturnsFalse() {
        Vector2d a = new Vector2d(2, 3);
        Vector2d b = new Vector2d(1, 3);
        Vector2d c = new Vector2d(1, 2);

        assertFalse(a.precedes(b));
        assertFalse(a.precedes(c));
    }

    @Test
    public void follows_OtherHasBothComponentsSmallerOrEqual_ReturnsTrue() {
        Vector2d a = new Vector2d(2, 3);
        Vector2d b = new Vector2d(1, 3);
        Vector2d c = new Vector2d(1, 2);

        assertTrue(a.follows(a));
        assertTrue(a.follows(b));
        assertTrue(a.follows(c));
    }

    @Test
    public void follows_OtherHasOneOrTwoComponentsBigger_ReturnsFalse() {
        Vector2d a = new Vector2d(2, 3);
        Vector2d b = new Vector2d(2, 4);
        Vector2d c = new Vector2d(3, 5);

        assertFalse(a.follows(b));
        assertFalse(a.follows(c));
    }

    @Test
    public void upperRight_ReturnsVectorWithMaximumComponents() {
        Vector2d a = new Vector2d(2, 6);
        Vector2d b = new Vector2d(3, 5);
        Vector2d expected = new Vector2d(3, 6);

        Vector2d upperRight = a.upperRight(b);

        assertEquals(expected, upperRight);
    }

    @Test
    public void lowerLeft_ReturnsVectorWithMinimumComponents() {
        Vector2d a = new Vector2d(2, 6);
        Vector2d b = new Vector2d(3, 5);
        Vector2d expected = new Vector2d(2,5);

        Vector2d lowerLeft = a.lowerLeft(b);

        assertEquals(expected, lowerLeft);
    }

    @Test
    public void add_DoesElementwiseAddition() {
        Vector2d a = new Vector2d(2, 6);
        Vector2d b = new Vector2d(3, 5);
        Vector2d expectedSum = new Vector2d(5, 11);

        Vector2d sum = a.add(b);

        assertEquals(expectedSum, sum);
    }

    @Test
    public void subtract_DoesElementwiseSubtraction() {
        Vector2d a = new Vector2d(2, 6);
        Vector2d b = new Vector2d(3, 5);
        Vector2d expectedDiff = new Vector2d(-1, 1);

        Vector2d diff = a.subtract(b);

        assertEquals(expectedDiff, diff);
    }

    @Test
    public void opposite_FlipsSignsOfComponents() {
        Vector2d a = new Vector2d(1, -2);
        Vector2d expectedOpposite = new Vector2d(-1, 2);

        Vector2d b = a.opposite();

        assertEquals(expectedOpposite, b);
    }
}
