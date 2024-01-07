package agh.ics.oop.model.generator;

import agh.ics.oop.model.maps.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.WorldMap;

public class EquatorGrassGenerator extends AbstractGrassGenerator {
    private static final double EQUATORIAL_REGION_PERCENTAGE = 0.2;
    private int yEquatorStart;
    private int yEquatorEnd;

    public EquatorGrassGenerator(GrassGeneratorInfo info, WorldMap worldMap) {
        super(info, worldMap);
        Boundary boundary = worldMap.getBoundary();

        calculateEquatorParameters(boundary);
    }

    private void calculateEquatorParameters(Boundary boundary) {
        int equatorHeight = (int) (boundary.height() * EQUATORIAL_REGION_PERCENTAGE);
        yEquatorStart = boundary.lowerLeft().y() + (boundary.height() - equatorHeight) / 2;
        yEquatorEnd = yEquatorStart + equatorHeight -1;
    }

    @Override
    protected boolean isPreferredPosition(Vector2d position) {
        boolean isOnEquator = position.y()>= yEquatorStart && position.y() <= yEquatorEnd;
        boolean isNotGrassed = !worldMap.mapFieldAt(position).isGrassed();
        return isOnEquator && isNotGrassed;
    }
}
