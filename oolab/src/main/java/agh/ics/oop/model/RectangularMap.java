package agh.ics.oop.model;

public class RectangularMap extends AbstractWorldMap {
    private final Vector2d lowerLeft = new Vector2d(0,0);
    private final Vector2d upperRight;

    public RectangularMap(int width, int height) {
        this.upperRight = new Vector2d(width-1, height-1);
    }

    @Override
    public String toString() {
        return mapVisualizer.draw(lowerLeft, upperRight);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && withinMapBounds(position);
    }

    private boolean withinMapBounds(Vector2d position) {
        return position.follows(lowerLeft) && position.precedes(upperRight);
    }
}
