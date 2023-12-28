package agh.ics.oop.model;

public enum MapDirection {
    NORTH("Północ", "^", new Vector2d(0, 1), "/images/up.png"),
    NORTHEAST("Północny Wschód", "↗", new Vector2d(1, 1), "/images/northeast.png"),
    EAST("Wschód", ">", new Vector2d(1, 0), "/images/right.png"),
    SOUTHEAST("Południowy Wschód", "↘", new Vector2d(1, -1), "/images/southeast.png"),
    SOUTH("Południe", "v", new Vector2d(0, -1), "/images/down.png"),
    SOUTHWEST("Południowy Zachód", "↙", new Vector2d(-1, -1), "/images/southwest.png"),
    WEST("Zachód", "<", new Vector2d(-1, 0), "/images/left.png"),
    NORTHWEST("Północny Zachód", "↖", new Vector2d(-1, 1), "/images/northwest.png");


    private static final MapDirection[] allValues = values();

    private final String string;
    private final String indicator;
    private final Vector2d unitVector;
    private final String imagePath;

    MapDirection(String string, String indicator, Vector2d unitVector, String imagePath) {
        this.string = string;
        this.indicator = indicator;
        this.unitVector = unitVector;
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return string;
    }

    public String indicator() {
        return indicator;
    }

    public String getImagePath() {
        return imagePath;
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


    public Vector2d toUnitVector() {
        return unitVector;
    }

}
