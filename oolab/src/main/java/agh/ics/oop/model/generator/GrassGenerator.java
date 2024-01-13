package agh.ics.oop.model.generator;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;

import java.util.List;

public interface GrassGenerator {
    List<Grass> generateGrassForDay();
    List<Grass> generateInitialGrass();
    boolean isPreferredPosition(Vector2d position);
}

