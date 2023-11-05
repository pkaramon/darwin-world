package agh.ics.oop.model;

public enum MapDirection {
    NORTH("Północ", "^",  new Vector2d(0, 1)),
    EAST("Wschód", ">", new Vector2d(1, 0)),
    SOUTH("Południe", "v", new Vector2d(0, -1)),
    WEST("Zachód", "<", new Vector2d(-1, 0));

    private static final MapDirection[] allValues = values();

    private final String string;
    private final String indicator;
    private final Vector2d unitVector;

    MapDirection(String longString, String indicator, Vector2d unitVector) {
        this.string = longString;
        this.indicator = indicator;
        this.unitVector = unitVector;
    }

    @Override
    public String toString() {
        return string;
    }

    public String indicator() {
        return indicator;
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
