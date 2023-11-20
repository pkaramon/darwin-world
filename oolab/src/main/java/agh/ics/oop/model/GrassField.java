package agh.ics.oop.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrassField extends AbstractWorldMap {
    private final int grassAmount;
    private final Map<Vector2d, Grass> grassElements = new HashMap<>();
    private final GrassFieldBoundsTracker boundsTracker = new GrassFieldBoundsTracker();


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
            boundsTracker.addElement(g);
        }
    }

    @Override
    Boundary getCurrentBounds() {
        return new Boundary(boundsTracker.lowerLeft(), boundsTracker.upperRight());
    }

    @Override
    public void place(Animal animal) throws PositionAlreadyOccupiedException {
        super.place(animal);
        boundsTracker.addElement(animal);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (super.objectAt(position) != null) {
            return super.objectAt(position);
        } else {
            return grassElements.get(position);
        }
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        boundsTracker.removeElement(animal);
        super.move(animal, direction);
        boundsTracker.addElement(animal);
    }

    @Override
    public Collection<WorldElement> getElements() {
        return Stream.concat(super.getElements().stream(),
                this.grassElements.values().stream()).collect(Collectors.toList());
    }
}
