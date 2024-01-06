package agh.ics.oop.model.generator;

import agh.ics.oop.model.maps.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.WorldMap;

import java.util.*;

public class EquatorGrassGenerator extends AbstractGrassGenerator {
    private static final double EQUATORIAL_REGION_PERCENTAGE = 0.2;

    public EquatorGrassGenerator(GrassGeneratorInfo info, WorldMap worldMap) {
        super(info, worldMap);
    }

    @Override
    protected Set<Vector2d> getPreferredPositions() {
        Boundary mapBoundary = worldMap.getBoundary();
        int equatorHeight = (int) (mapBoundary.height() * EQUATORIAL_REGION_PERCENTAGE);
        int yStart = mapBoundary.lowerLeft().y() + (mapBoundary.height() - equatorHeight) / 2;
        int yEnd = yStart + equatorHeight -1;

        return generateEquatorPositions(yStart, yEnd);
    }


    private Set<Vector2d> generateEquatorPositions(int equatorYStart, int equatorYEnd) {
        Boundary mapBoundary = worldMap.getBoundary();
        Set<Vector2d> equatorPositions = new HashSet<>();

        for (int x = mapBoundary.lowerLeft().x(); x <= mapBoundary.upperRight().x(); x++) {
            for (int y = equatorYStart; y <= equatorYEnd; y++) {
                Vector2d position = new Vector2d(x, y);
                if (isNonGrassed(position)) {
                    equatorPositions.add(position);
                }
            }
        }
        return equatorPositions;
    }
}
