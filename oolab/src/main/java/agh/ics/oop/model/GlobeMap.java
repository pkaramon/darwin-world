package agh.ics.oop.model;

import java.util.Optional;
import java.util.stream.Stream;

public class GlobeMap<T extends MapField> implements WorldMap<T> {

    private final int height;
    private final T[][] fields;
    private final int width;

    GlobeMap(int width, int height, T[][] fields) {
        this.width = width;
        this.height = height;
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
    public void place(WorldElement element) {
        mapFieldAt(element.getPosition()).addElement(element);
    }

    @Override
    public void remove(WorldElement element) {
        mapFieldAt(element.getPosition()).removeElement(element);
    }

    @Override
    public T mapFieldAt(Vector2d position) {
        return fields[position.getX()][position.getY()];
    }

    @Override
    public Boundary getBoundary() {
        return new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
    }
}
