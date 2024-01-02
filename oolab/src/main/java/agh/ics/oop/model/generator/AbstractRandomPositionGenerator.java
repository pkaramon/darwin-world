package agh.ics.oop.model.generator;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRandomPositionGenerator implements RandomPositionGenerator {
    protected Boundary mapBoundary;
    protected List<Boundary> preferredRegions;
    protected WorldMap worldMap;

    public AbstractRandomPositionGenerator(WorldMap worldMap, Boundary mapBoundary) {
        this.worldMap = worldMap;
        this.mapBoundary = mapBoundary;
        this.preferredRegions = null;
    }

    @Override
    public void setPreferredRegions(List<Boundary> regions) {
        this.preferredRegions = regions;
    }

    @Override
    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
    }

    @Override
    public abstract Vector2d generatePosition();
}