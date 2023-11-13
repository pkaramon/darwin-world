package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {
    @Test
    void move_allowsAnimalToMoveOnGrassOrEmptyFieldButNotOnOtherAnimal() {
        WorldMap map = new GrassField(1);
        Grass grass = (Grass) map.getElements().iterator().next();
        Animal a = new Animal(grass.getPosition().add(new Vector2d(0, 1)));
        Animal b = new Animal(grass.getPosition().add(new Vector2d(0, -1)));
        map.place(a);
        map.place(b);


        map.move(a, MoveDirection.BACKWARD);
        assertEquals(grass.getPosition(), a.getPosition());

        Vector2d bOldPosition = b.getPosition();
        map.move(b, MoveDirection.FORWARD);

        assertEquals(bOldPosition, b.getPosition());

        map.move(b, MoveDirection.RIGHT);
        map.move(b, MoveDirection.FORWARD);

        assertEquals(b.getPosition(), bOldPosition.add(new Vector2d(1, 0)));
    }

    @Test
    void test_toString() {
        int grassCount = 40;
        WorldMap map = new GrassField(grassCount);
        Animal a = new Animal(new Vector2d(-3, 4));
        Animal b = new Animal(new Vector2d(1000, 1000));
        Animal c = new Animal(new Vector2d(-5, 8));
        map.place(a);
        map.place(b);
        map.place(c);

        String grid = map.toString();

        assertEquals(grassCount, grid.chars().filter(ch -> ch == '*').count());
        assertEquals(3, grid.chars().filter(ch -> ch == '^').count());
    }

    @Test
    void test_GrassCount() {
        int grassCount = 20;
        int maxBound = (int)Math.round(Math.sqrt(grassCount * 10));
        WorldMap map = new GrassField(grassCount);

        int encountered = 0;
        for (int i = 0; i <= maxBound; ++i) {
            for (int j = 0; j <= maxBound; ++j) {
                Vector2d pos = new Vector2d(i,j);
                boolean isOccupiedByGrass = map.isOccupied(pos)&& map.objectAt(pos) instanceof Grass;
                if (isOccupiedByGrass) {
                    encountered++;
                }
            }
        }

        assertEquals(grassCount, encountered);
    }

    @Test
    void place_ReturnsTrueIfAnimalWasSuccessfullyPlaceFalseOtherwise() {
        WorldMap map = new GrassField(1);
        Grass firstGrass = (Grass) map.getElements().iterator().next();

        assertTrue(map.place(new Animal(firstGrass.getPosition())));
        assertTrue(map.place(new Animal(new Vector2d( -2, 3))));
        assertTrue(map.place(new Animal( new Vector2d(-3, 5))));
        assertFalse(map.place(new Animal( new Vector2d(-3, 5))));
    }


    @Test
    void isOccupied_ThereIsGrassOrAnimalReturnsTrueFalseOtherwise() {
        WorldMap map = new GrassField(1);
        Grass firstGrass = (Grass) map.getElements().iterator().next();

        assertTrue(map.isOccupied(firstGrass.getPosition()));

        Vector2d animalPosition = firstGrass.getPosition().add(new Vector2d(3, 4));

        map.place(new Animal(animalPosition));
        assertTrue(map.isOccupied(animalPosition));
    }

    @Test
    void canMoveTo_ReturnsTrueUnlessThereIsAnimal() {
        WorldMap map = new GrassField(1);
        Grass firstGrass = (Grass) map.getElements().iterator().next();

        assertEquals(firstGrass, map.objectAt(firstGrass.getPosition()));

        assertTrue(map.canMoveTo(firstGrass.getPosition()));

        assertTrue(map.canMoveTo(new Vector2d(6, -3)));
        assertTrue(map.canMoveTo(new Vector2d(-1244, 35)));

        Vector2d animalPosition = firstGrass.getPosition().add(new Vector2d(1,2));
        map.place(new Animal(animalPosition));
        assertFalse(map.canMoveTo(animalPosition));
    }



    @Test
    void objectAt_ReturnsAnimalOrGrassOrNull() {
        WorldMap map = new GrassField(1);
        Animal animal = new Animal(new Vector2d(-3, 4));
        Grass firstGrass = (Grass) map.getElements().iterator().next();

        map.place(animal);
        assertEquals(animal, map.objectAt(new Vector2d(-3, 4)));
        assertEquals(firstGrass, map.objectAt(firstGrass.getPosition()));

        assertNull(map.objectAt(new Vector2d(-4, 3)));
        assertNull(map.objectAt(new Vector2d(4, -3)));
    }

    @Test
    void getElements_ReturnsAllElementsOnMap() {
        WorldMap map = new GrassField(3);
        Animal a = new Animal(new Vector2d(3,4));
        map.place(a);

        Collection<WorldElement> elements = map.getElements();
        assertEquals(4, elements.size());
        assertTrue(elements.contains(a));
    }


}