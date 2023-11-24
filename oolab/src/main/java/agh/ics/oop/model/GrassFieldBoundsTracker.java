package agh.ics.oop.model;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

class GrassFieldBoundsTracker {
    private final SortedSet<WorldElement> sortedByX = new TreeSet<>(
            Comparator.comparingInt((WorldElement e) -> e.getPosition().getX()).thenComparing(System::identityHashCode));
    private final SortedSet<WorldElement> sortedByY = new TreeSet<>(
            Comparator.comparingInt((WorldElement e) -> e.getPosition().getY()).thenComparing(System::identityHashCode));

    public Vector2d lowerLeft() {
        if (isMapNotEmpty()) {
            return new Vector2d(this.sortedByX.first().getPosition().getX(),
                    this.sortedByY.first().getPosition().getY());
        } else {
            return new Vector2d(0, 0);
        }
    }

    public Vector2d upperRight() {
        if (isMapNotEmpty()) {
            return new Vector2d(sortedByX.last().getPosition().getX(),
                    sortedByY.last().getPosition().getY());
        } else {
            return new Vector2d(0, 0);
        }
    }

    private boolean isMapNotEmpty() {
        return !this.sortedByX.isEmpty() && !this.sortedByY.isEmpty();
    }


    public void addElement(WorldElement elem) {
        this.sortedByX.add(elem);
        this.sortedByY.add(elem);
    }

    public void removeElement(WorldElement elem) {
        this.sortedByX.remove(elem);
        this.sortedByY.remove(elem);
    }
}