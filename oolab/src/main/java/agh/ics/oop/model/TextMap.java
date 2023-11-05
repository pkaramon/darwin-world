package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.swap;

public class TextMap implements WorldMap<String, Integer> {
    private final List<String> list = new ArrayList<>();
    private final Map<String, Integer> stringsPositions = new HashMap<>();

    @Override
    public boolean canMoveTo(Integer position) {
        return withinBounds(position);
    }

    private boolean withinBounds(Integer position) {
        return position >= 0 && position < list.size();
    }

    @Override
    public boolean place(String element) {
        if (exists(element)) return false;

        stringsPositions.put(element, list.size());
        list.add(element);
        return true;
    }

    private boolean exists(String element) {
        return this.stringsPositions.containsKey(element);
    }

    @Override
    public void move(String element, MoveDirection direction) {
        boolean isDirectionInValid = !(direction == MoveDirection.FORWARD || direction == MoveDirection.BACKWARD);
        if (isDirectionInValid || !exists(element)) {
            return;
        }

        int oldPosition = stringsPositions.get(element);
        int newPosition = switch (direction) {
            case FORWARD -> oldPosition + 1;
            case BACKWARD -> oldPosition - 1;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };

        if (canMoveTo(newPosition)) {
            String neighbour = list.get(newPosition);
            swap(list, newPosition, oldPosition);
            this.stringsPositions.put(element, newPosition);
            this.stringsPositions.put(neighbour, oldPosition);
        }
    }


    @Override
    public boolean isOccupied(Integer position) {
        return withinBounds(position);
    }

    @Override
    public String objectAt(Integer position) {
        if (withinBounds(position)) {
            return list.get(position);
        } else {
            return null;
        }
    }
}
