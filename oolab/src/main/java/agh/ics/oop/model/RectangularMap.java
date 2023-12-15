package agh.ics.oop.model;

public class RectangularMap extends AbstractWorldMap {
    private final Boundary boundary;

    public RectangularMap(int width, int height) {
        this.boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width-1, height-1));
    }

    @Override
    public Boundary getCurrentBounds() {
        return boundary;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && withinMapBounds(position);
    }

    private boolean withinMapBounds(Vector2d position) {
        return position.follows(boundary.lowerLeft()) && position.precedes(boundary.upperRight());
    }
}
