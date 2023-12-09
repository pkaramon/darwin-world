package agh.ics.oop.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(upperBound, upperBound, grassAmount);
        for (Vector2d grassPosition : randomPositionGenerator) {
            Grass g = new Grass(grassPosition);
            grassElements.put(grassPosition, g);
        }
    }

    @Override
    public Boundary getCurrentBounds() {
        if(animals.size() + grassElements.size() == 0) {
            return new Boundary(new Vector2d(0, 0), new Vector2d(0,0));
        }
        Vector2d lowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Vector2d upperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for(WorldElement elem: getElements()) {
            lowerLeft = elem.getPosition().lowerLeft(lowerLeft);
            upperRight = elem.getPosition().upperRight(upperRight);
        }

        return new Boundary(lowerLeft, upperRight);
    }


    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {
        if (super.objectAt(position).isPresent()) {
            return super.objectAt(position);
        } else {
            return Optional.ofNullable(grassElements.get(position));
        }
    }

    @Override
    public Collection<WorldElement> getElements() {
        return Stream.concat(
                super.getElements().stream(),
                this.grassElements.values().stream()).collect(Collectors.toList()
        );
    }
}
