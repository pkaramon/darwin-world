package agh.ics.oop.model;

import agh.ics.oop.model.animals.Animal;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

public class GlobeMap implements WorldMap {

    private final int height;
    private final MapField[][] fields;
    private final int width;

    public GlobeMap(MapField[][] fields) {
        this.width = fields.length;
        this.height = fields[0].length;
        this.fields = fields;
    }

    @Override
    public Pose validateMove(Pose desired) {
        int desiredX = desired.position().getX();
        int desiredY = desired.position().getY();
        MapDirection desiredOrientation = desired.orientation();

        return Stream.of(
                        checkForCorners(desiredX, desiredY, desiredOrientation),
                        checkForUpperLowerBoundaries(desiredY, desiredX, desiredOrientation),
                        checkForLeftRightBoundaries(desiredX, desiredY, desiredOrientation))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(desired);
    }

    private Optional<Pose> checkForCorners(int desiredX, int desiredY, MapDirection desiredOrientation) {
        if (isCorner(desiredX, desiredY)) {
            return Optional.of(new Pose(
                    new Vector2d(desiredX, desiredY).subtract(desiredOrientation.toUnitVector()),
                    desiredOrientation.flip())
            );
        }
        return Optional.empty();
    }

    private boolean isCorner(int desiredX, int desiredY) {
        return desiredX == width && desiredY == height ||
                desiredX == -1 && desiredY == -1 ||
                desiredX == width && desiredY == -1 ||
                desiredX == -1 && desiredY == height;
    }


    private Optional<Pose> checkForUpperLowerBoundaries(int desiredY, int desiredX, MapDirection desiredOrientation) {
        if (desiredY < 0) {
            return Optional.of(new Pose(new Vector2d(desiredX, 0), desiredOrientation.flip()));
        } else if (desiredY >= height) {
            return Optional.of(new Pose(new Vector2d(desiredX, height - 1), desiredOrientation.flip()));
        }
        return Optional.empty();
    }

    private Optional<Pose> checkForLeftRightBoundaries(int desiredX, int desiredY, MapDirection desiredOrientation) {
        if (desiredX < 0) {
            return Optional.of(new Pose(new Vector2d(width - 1, desiredY), desiredOrientation));
        } else if (desiredX >= width) {
            return Optional.of(new Pose(new Vector2d(0, desiredY), desiredOrientation));
        }
        return Optional.empty();
    }


    @Override
    public void addAnimal(Animal animal) {
        mapFieldAt(animal.getPosition()).addAnimal(animal);
    }

    @Override
    public void removeAnimal(Animal animal) {
        mapFieldAt(animal.getPosition()).removeAnimal(animal);

    }

    @Override
    public void addGrass(Grass grass) {
        mapFieldAt(grass.getPosition()).addGrass(grass);
    }

    @Override
    public void removeGrass(Grass grass) {
        mapFieldAt(grass.getPosition()).removeGrass(grass);
    }

    @Override
    public MapField mapFieldAt(Vector2d position) {
        return fields[position.getX()][position.getY()];
    }

    @Override
    public void move(Animal animal) {
        mapFieldAt(animal.getPosition()).removeAnimal(animal);
        animal.move(this);
        mapFieldAt(animal.getPosition()).addAnimal(animal);
    }

    @Override
    public Boundary getBoundary() {
        return new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
    }

    @Override
    public Iterator<MapField> iterator() {
        return new GlobeMapIterator(fields);
    }
}
