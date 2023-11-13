package agh.ics.oop.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrassField extends AbstractWorldMap {
    private final int grassAmount;
    private final Map<Vector2d, Grass> grassElements = new HashMap<>();


    public GrassField(int grassAmount) {
        this.grassAmount = grassAmount;
        initializeGrassElements();
    }

    private void initializeGrassElements() {
        int upperBound = (int) Math.sqrt(10 * grassAmount);
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(
                upperBound, upperBound, grassAmount);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grassElements.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public String toString() {
        Vector2d lowerLeft = new Vector2d(0, 0);
        Vector2d upperRight = new Vector2d(0, 0);

        for (WorldElement elem : getElements()) {
            lowerLeft = elem.getPosition().lowerLeft(lowerLeft);
            upperRight = elem.getPosition().upperRight(upperRight);
        }

        return mapVisualizer.draw(lowerLeft, upperRight);
    }


    @Override
    public WorldElement objectAt(Vector2d position) {
        if (super.objectAt(position) != null) {
            return super.objectAt(position);
        } else {
            return this.grassElements.get(position);
        }
    }

    @Override
    public Collection<WorldElement> getElements() {
        return Stream.concat(super.getElements().stream(), this.grassElements.values().stream())
                .collect(Collectors.toList());

    }
}
