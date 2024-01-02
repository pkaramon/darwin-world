package agh.ics.oop.model;

import agh.ics.oop.model.animals.Animal;
import org.junit.jupiter.api.Test;

import java.util.List;

import static agh.ics.oop.model.MapDirection.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobeMapTest {
    private WorldMap createGlobeMap(int width, int height) {
        MapField[][] fields = new MapField[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                fields[i][j] = new GrassMapField(new Vector2d(i, j));
            }
        }

        return new GlobeMap(fields);
    }


    @Test
    void validateMove_ObjectWantsToMoveInsideOfMapBoundaries_MoveIsAccepted() {
        var globeMap = createGlobeMap(5, 5);

        Pose newPose = globeMap.validateMove(new Pose(new Vector2d(2, 2), NORTH));

        assertEquals(new Pose(new Vector2d(2, 2), NORTH), newPose);
    }

    @Test
    void validateMove_ObjectsWantsToMoveOutsideOfLeftOrRightMapBoundaries_GetsMovedToOtherBoundary() {
        var globeMap = createGlobeMap(5, 5);

        Pose newPose = globeMap.validateMove(new Pose(new Vector2d(5, 2), EAST));
        assertEquals(new Pose(new Vector2d(0, 2), EAST), newPose);

        newPose = globeMap.validateMove(new Pose(new Vector2d(-1, 2), WEST));
        assertEquals(new Pose(new Vector2d(4, 2), WEST), newPose);


        newPose = globeMap.validateMove(new Pose(new Vector2d(5, 0), SOUTHEAST));
        assertEquals(new Pose(new Vector2d(0, 0), SOUTHEAST), newPose);
    }


    @Test
    void validateMove_ObjectsWantsToMoveOutsideOfTopOrBottomBoundary_GetsTurnedAround() {
        var globeMap = createGlobeMap(5, 3);

        Pose newPose = globeMap.validateMove(new Pose(new Vector2d(2, 3), NORTH));
        assertEquals(new Pose(new Vector2d(2, 2), SOUTH), newPose);

        newPose = globeMap.validateMove(new Pose(new Vector2d(2, -1), SOUTH));
        assertEquals(new Pose(new Vector2d(2, 0), NORTH), newPose);


        newPose = globeMap.validateMove(new Pose(new Vector2d(2, 3), NORTHEAST));
        assertEquals(new Pose(new Vector2d(2, 2), SOUTHWEST), newPose);
    }

    @Test
    void validateMove_Corners() {
        var globeMap = createGlobeMap(5, 5);

        Pose newPose = globeMap.validateMove(new Pose(new Vector2d(5, 5), NORTHEAST));
        assertEquals(new Pose(new Vector2d(4, 4), SOUTHWEST), newPose);

        newPose = globeMap.validateMove(new Pose(new Vector2d(-1, -1), SOUTHWEST));
        assertEquals(new Pose(new Vector2d(0, 0), NORTHEAST), newPose);

        newPose = globeMap.validateMove(new Pose(new Vector2d(5, -1), SOUTHEAST));
        assertEquals(new Pose(new Vector2d(4, 0), NORTHWEST), newPose);

        newPose = globeMap.validateMove(new Pose(new Vector2d(-1, 5), NORTHWEST));
        assertEquals(new Pose(new Vector2d(0, 4), SOUTHEAST), newPose);
    }

    @Test
    void addingAnimals() {
        WorldMap map = createGlobeMap(5, 5);
        Animal a = mock(Animal.class);
        when(a.getPosition()).thenReturn(new Vector2d(1, 1));

        map.addAnimal(a);

        assertTrue(map.mapFieldAt(new Vector2d(1, 1)).getOrderedAnimals().contains(a));

        Animal b = mock(Animal.class);
        when(b.getPosition()).thenReturn(new Vector2d(1, 1));

        map.addAnimal(b);

        assertEquals(2, map.mapFieldAt(new Vector2d(1, 1)).getOrderedAnimals().size());
        assertTrue(map.mapFieldAt(new Vector2d(1, 1)).getOrderedAnimals().contains(b));
    }

    @Test
    void addingGrass() {
        WorldMap map = createGlobeMap(5, 5);
        Grass g = mock(Grass.class);
        when(g.getPosition()).thenReturn(new Vector2d(3, 2));

        assertTrue(map.mapFieldAt(new Vector2d(3, 2)).getGrass().isEmpty());

        map.addGrass(g);

        assertTrue(map.mapFieldAt(new Vector2d(3, 2)).getGrass().isPresent());
        assertEquals(g, map.mapFieldAt(new Vector2d(3, 2)).getGrass().get());
    }

    @Test
    void removingAnimals() {
        WorldMap map = createGlobeMap(5, 5);
        Animal a = mock(Animal.class);
        when(a.getPosition()).thenReturn(new Vector2d(3, 2));

        map.addAnimal(a);

        assertEquals(1, map.mapFieldAt(new Vector2d(3, 2)).getOrderedAnimals().size());

        map.removeAnimal(a);

        assertEquals(0, map.mapFieldAt(new Vector2d(3, 2)).getOrderedAnimals().size());
    }

    @Test
    void removingGrass() {
        WorldMap map = createGlobeMap(3, 3);
        Grass g = mock(Grass.class);
        when(g.getPosition()).thenReturn(new Vector2d(1, 1));

        map.addGrass(g);

        assertFalse(map.mapFieldAt(g.getPosition()).getGrass().isEmpty());

        map.removeGrass(g);

        assertTrue(map.mapFieldAt(g.getPosition()).getGrass().isEmpty());
    }

    @Test
    void move_MovesAnimalToCorrectMapField() {
        var map = createGlobeMap(3, 4);
        Animal animal = mock(Animal.class);
        when(animal.getPosition()).thenReturn(new Vector2d(1, 1),
                new Vector2d(1, 1),
                new Vector2d(1, 2));


        map.addAnimal(animal);
        map.move(animal);

        assertTrue(map.mapFieldAt(new Vector2d(1, 1)).getOrderedAnimals().isEmpty());
        assertTrue(map.mapFieldAt(new Vector2d(1, 2)).getOrderedAnimals().contains(animal));
    }

    @Test
    void getBoundary_ReturnsRectangleINXYPlane() {
        var map = createGlobeMap(3, 4);
        var boundary = map.getBoundary();

        assertEquals(new Vector2d(0, 0), boundary.lowerLeft());
        assertEquals(new Vector2d(2, 3), boundary.upperRight());
    }

    @Test
    void iteratingGoesRowByRow() {
        var map = createGlobeMap(2, 2);

        assertIterableEquals(
                map,
                List.of(map.mapFieldAt(new Vector2d(0, 0)),
                        map.mapFieldAt(new Vector2d(1, 0)),
                        map.mapFieldAt(new Vector2d(0, 1)),
                        map.mapFieldAt(new Vector2d(1, 1)))
        );

    }
}