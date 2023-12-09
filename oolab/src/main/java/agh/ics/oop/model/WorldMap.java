package agh.ics.oop.model;

import java.util.Collection;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    /**
     * Place an animal on the map.
     *
     * @param animal The element to place on the map.
     */
    void place(Animal animal) throws PositionAlreadyOccupiedException;

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal, MoveDirection direction);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return a WorldElement at a given position.
     *
     * @param position The position of the element.
     * @return animal or null if the position is not occupied.
     */
    WorldElement objectAt(Vector2d position);

    Collection<WorldElement> getElements();

    UUID getId();

    Boundary getCurrentBounds();
}