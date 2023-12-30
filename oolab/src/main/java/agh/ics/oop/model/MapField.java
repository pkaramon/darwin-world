package agh.ics.oop.model;

import java.util.List;

public interface MapField {
    Vector2d getPosition();
    List<WorldElement> getElements();
    void addElement(WorldElement element);
    void removeElement(WorldElement element);
}
