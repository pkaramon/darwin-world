package agh.ics.oop.model;

/**
 * The interface responsible for interacting with the map of the world.
 *
 * @author apohllo, idzik
 */
public interface WorldMap<T, P> extends MoveValidator<P> {

    /**
     * Place an element on the map.
     *
     * @param element The element to place on the map.
     * @return True if the element was placed. The element cannot be placed if the move is not valid.
     */
    boolean place(T element);

    /**
     * Moves an element (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(T element, MoveDirection direction);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the element
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(P position);

    /**
     * Return an element at a given position.
     *
     * @param position The position of the element.
     * @return animal or null if the position is not occupied.
     */
    T objectAt(P position);
}