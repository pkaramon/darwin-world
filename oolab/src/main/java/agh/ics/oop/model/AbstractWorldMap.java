package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    private final MapVisualizer mapVisualizer = new MapVisualizer(this);
    private final Set<MapChangeListener> listeners = new LinkedHashSet<>();
    private final UUID id = UUID.randomUUID();

    @Override
    public UUID getId() {
        return id;
    }

    public void addListener(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MapChangeListener listener) {
        listeners.remove(listener);
    }

    private void mapChanged(String message) {
        for (MapChangeListener listener : listeners) {
            listener.mapChanged(this, message);
        }
    }

    @Override
    public String toString() {
        Boundary boundary = getCurrentBounds();

        return mapVisualizer.draw(boundary.lowerLeft(), boundary.upperRight());
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !this.animals.containsKey(position);
    }

    @Override
    public void place(Animal animal) throws PositionAlreadyOccupiedException {
        if (!canMoveTo(animal.getPosition())) {
            throw new PositionAlreadyOccupiedException(animal.getPosition());
        }

        this.animals.put(animal.getPosition(), animal);
        mapChanged("Animal was placed at %s".formatted(animal.getPosition()));
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        String oldInfo = "%s, %s".formatted(animal.getPosition(), animal.toString());

        Vector2d oldPosition = animal.getPosition();
        animal.move(direction, this);

        String message = "Animal changed from: %s to %s, %s".formatted(
                oldInfo, animal.getPosition(), animal.toString()
        );
        mapChanged(message);

        if (oldPosition.equals(animal.getPosition())) {
            return;
        }
        this.animals.remove(oldPosition);
        this.animals.put(animal.getPosition(), animal);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return this.animals.get(position);
    }

    @Override
    public Collection<WorldElement> getElements() {
        return new ArrayList<>(this.animals.values());
    }

    @Override
    public List<Animal> getOrderedAnimals() {
        List<Animal> listOfAnimals = new ArrayList<>(animals.values());
        Collections.sort(listOfAnimals,
                Comparator.comparingInt((Animal a) -> a.getPosition().getX())
                        .thenComparingInt((Animal a) -> a.getPosition().getY())
        );
        return listOfAnimals;
    }
}
