package agh.ics.oop.model;

public enum MapDirection {
    NORTH("Północ", new Vector2d(0, 1)),
    EAST("Wschód", new Vector2d(1, 0)),
    SOUTH("Południe", new Vector2d(0, -1)),
    WEST("Zachód", new Vector2d(-1, 0));

    private static final MapDirection[] allValues = values();

    private final String str;
    private final Vector2d unitVector;

    MapDirection(String str, Vector2d unitVector) {
        this.str = str;
        this.unitVector = unitVector;
    }

    @Override
    public String toString() {
        return str;
    }

    public MapDirection next() {
        return allValues[(ordinal() + 1) % allValues.length];
    }

    public MapDirection previous() {
        return allValues[(ordinal() - 1 + allValues.length) % allValues.length];
    }

    public Vector2d toUnitVector() {
        return unitVector;
    }
}
