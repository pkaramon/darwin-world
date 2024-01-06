package agh.ics.oop.model;

public enum MapDirection {
    NORTH("^", new Vector2d(0, 1)),
    NORTHEAST("↗", new Vector2d(1, 1)),
    EAST(">", new Vector2d(1, 0)),
    SOUTHEAST("↘", new Vector2d(1, -1)),
    SOUTH("v", new Vector2d(0, -1)),
    SOUTHWEST("↙", new Vector2d(-1, -1)),
    WEST("<", new Vector2d(-1, 0)),
    NORTHWEST("↖", new Vector2d(-1, 1));


    private static final MapDirection[] allValues = values();

    private final String indicator;
    private final Vector2d unitVector;

    MapDirection(String indicator, Vector2d unitVector) {
        this.indicator = indicator;
        this.unitVector = unitVector;
    }

    @Override
    public String toString() {
        return indicator;
    }

    public MapDirection next() {
        return nextN(1);
    }

    public MapDirection nextN(int n) {
        return allValues[(ordinal() + n) % allValues.length];
    }


    public MapDirection previous() {
        return previousN(1);
    }

    public MapDirection previousN(int n) {
        return allValues[(ordinal() - n + n*allValues.length) % allValues.length];
    }

    public MapDirection flip() {
        return nextN(4);
    }

    public Vector2d toUnitVector() {
        return unitVector;
    }

}
