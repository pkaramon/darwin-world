package agh.ics.oop.model;

public interface MoveValidator {
    /**
     * @param desired the pose(position and orientation) in which the objects want to be
     * @return the pose in which an object should be
     */
    Pose validate(Pose desired);
}