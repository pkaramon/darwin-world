package agh.ics.oop.model.generator;

import agh.ics.oop.model.Grass;

import java.util.List;

public interface GrassGenerator {
    List<Grass> generateGrassForDay();
    List<Grass> generateInitialGrass();
}

