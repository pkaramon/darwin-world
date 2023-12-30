package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static agh.ics.oop.model.MapDirection.*;
import static org.junit.jupiter.api.Assertions.*;

class GlobeMapTest {
    @Test
    void validateMove_ObjectWantsToMoveInsideOfMapBoundaries_MoveIsAccepted() {
        GlobeMap globeMap = new GlobeMap(5, 5);

        Pose newPose = globeMap.validateMove(new Pose(new Vector2d(2, 2), NORTH));

        assertEquals(new Pose(new Vector2d(2, 2), NORTH), newPose);
    }

    @Test
    void validateMove_ObjectsWantsToMoveOutsideOfLeftOrRightMapBoundaries_GetsMovedToOtherBoundary() {
        GlobeMap globeMap = new GlobeMap(5, 5);

        Pose newPose = globeMap.validateMove(new Pose(new Vector2d(5, 2), EAST));
        assertEquals(new Pose(new Vector2d(0, 2), EAST), newPose);

        newPose = globeMap.validateMove(new Pose(new Vector2d(-1, 2), WEST));
        assertEquals(new Pose(new Vector2d(4, 2), WEST), newPose);


        newPose = globeMap.validateMove(new Pose(new Vector2d(5, 0), SOUTHEAST));
        assertEquals(new Pose(new Vector2d(0,  0), SOUTHEAST), newPose);
    }


    @Test
    void validateMove_ObjectsWantsToMoveOutsideOfTopOrBottomBoundary_GetsTurnedAround() {
        GlobeMap globeMap = new GlobeMap(4, 3);

        Pose newPose = globeMap.validateMove(new Pose(new Vector2d(2, 3), NORTH));
        assertEquals(new Pose(new Vector2d(2, 2), SOUTH), newPose);

        newPose = globeMap.validateMove(new Pose(new Vector2d(2, -1 ), SOUTH));
        assertEquals(new Pose(new Vector2d(2, 0), NORTH), newPose);


        newPose = globeMap.validateMove(new Pose(new Vector2d(2, 3), NORTHEAST));
        assertEquals(new Pose(new Vector2d(2, 2), SOUTHWEST), newPose);
    }

    @Test
    void validateMove_Corners() {
        GlobeMap globeMap = new GlobeMap(5, 5);

        Pose newPose = globeMap.validateMove(new Pose(new Vector2d(5, 5), NORTHEAST));
        assertEquals(new Pose(new Vector2d(4, 4), SOUTHWEST), newPose);

        newPose = globeMap.validateMove(new Pose(new Vector2d(-1, -1), SOUTHWEST));
        assertEquals(new Pose(new Vector2d(0, 0), NORTHEAST), newPose);

        newPose = globeMap.validateMove(new Pose(new Vector2d(5, -1), SOUTHEAST));
        assertEquals(new Pose(new Vector2d(4, 0), NORTHWEST), newPose);

        newPose = globeMap.validateMove(new Pose(new Vector2d(-1, 5), NORTHWEST));
        assertEquals(new Pose(new Vector2d(0, 4), SOUTHEAST), newPose);
    }
}