package agh.ics.oop.model.generator;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.Random;

public class EquatorRandomPositionGenerator extends AbstractRandomPositionGenerator {
    private static final double EQUATORIAL_REGION_PERCENTAGE = 0.2;

    public EquatorRandomPositionGenerator(WorldMap worldMap, Boundary mapBoundary) {
        super(worldMap, mapBoundary);
    }

    @Override
    public Vector2d generatePosition() {
        Random random = new Random();
        int equatorialHeight = (int) (mapBoundary.height() * EQUATORIAL_REGION_PERCENTAGE);
        int yStart = mapBoundary.lowerLeft().getY() + (mapBoundary.height() - equatorialHeight) / 2;
        int yEnd = yStart + equatorialHeight;

        int x = random.nextInt(mapBoundary.width()) + mapBoundary.lowerLeft().getX();
        int y = random.nextInt(yEnd - yStart + 1) + yStart;
        return new Vector2d(x, y);
    }
}
