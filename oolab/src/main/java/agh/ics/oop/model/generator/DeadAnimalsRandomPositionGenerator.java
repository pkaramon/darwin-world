package agh.ics.oop.model.generator;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeadAnimalsRandomPositionGenerator extends AbstractRandomPositionGenerator {
    private List<Vector2d> deadAnimalsPositions;

    public DeadAnimalsRandomPositionGenerator(WorldMap worldMap, Boundary mapBoundary) {
        super(worldMap, mapBoundary);
        this.deadAnimalsPositions = new ArrayList<>();
    }

    @Override
    public Vector2d generatePosition() {
        if (!deadAnimalsPositions.isEmpty()) {
            int index = new Random().nextInt(deadAnimalsPositions.size());
            return deadAnimalsPositions.get(index);
        } else {
            return generateRandomPositionWithinBoundary();
        }
    }

    private Vector2d generateRandomPositionWithinBoundary() {
        Random random = new Random();
        int x = random.nextInt(mapBoundary.width()) + mapBoundary.lowerLeft().getX();
        int y = random.nextInt(mapBoundary.height()) + mapBoundary.lowerLeft().getY();
        return new Vector2d(x, y);
    }

    public void updateDeadAnimalsPositions(List<Vector2d> positions) {
        this.deadAnimalsPositions = positions;
    }
}
