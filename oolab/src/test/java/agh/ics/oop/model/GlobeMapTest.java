package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static agh.ics.oop.model.MapDirection.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobeMapTest {
    private GlobeMap<MapField> createGlobeMap(int width, int height) {
        MapField[][] fields = new MapField[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0 ; j < height; j++) {
                fields[i][j] = mock(MapField.class);
            }
        }

        return new GlobeMap<>(width, height, fields);
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
        assertEquals(new Pose(new Vector2d(0,  0), SOUTHEAST), newPose);
    }


    @Test
    void validateMove_ObjectsWantsToMoveOutsideOfTopOrBottomBoundary_GetsTurnedAround() {
        var globeMap = createGlobeMap(5, 5);

        Pose newPose = globeMap.validateMove(new Pose(new Vector2d(2, 3), NORTH));
        assertEquals(new Pose(new Vector2d(2, 2), SOUTH), newPose);

        newPose = globeMap.validateMove(new Pose(new Vector2d(2, -1 ), SOUTH));
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


    private static final WorldElement dummyElement = new WorldElement() {
        @Override
        public Vector2d getPosition() {
            return new Vector2d(1, 3);
        }
    };

    @Test
    void place_AddsWorldElementToCorrectMapField() {
        var map = createGlobeMap(3, 4);

        map.place(dummyElement);

        verify(map.mapFieldAt(new Vector2d(1, 3))).addElement(dummyElement);
    }

    @Test
    void remove_RemovesWorldElementFromCorrectMapField() {
        var map = createGlobeMap(3, 4);

        map.place(dummyElement);
        map.remove(dummyElement);

        verify(map.mapFieldAt(new Vector2d(1, 3))).removeElement(dummyElement);
    }

    @Test
    void boundary_ReturnsBoundaryBasedOnWidthAndHeight() {
        var map = createGlobeMap(3, 4);

        assertEquals(new Boundary(new Vector2d(0, 0), new Vector2d(2, 3)), map.getBoundary());
    }
}