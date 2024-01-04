package agh.ics.oop.model.maps;

import agh.ics.oop.model.Pose;

public interface MoveValidator {
    /**
     * @param desired the pose(position and orientation) in which the object wants to be
     * @return the pose in which an object should be
     */
    Pose validateMove(Pose desired);
}