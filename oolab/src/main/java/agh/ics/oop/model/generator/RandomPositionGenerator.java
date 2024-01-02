package agh.ics.oop.model.generator;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.List;

public interface RandomPositionGenerator {
    Vector2d generatePosition();
    void setPreferredRegions(List<Boundary> regions);
    void setWorldMap(WorldMap map);
}

