package agh.ics.oop.model;

public class WordMapCreator {
    private WordMapCreator() {}

    public static WorldMap createWorldMap(int width, int height) {
        MapField[][] fields = new MapField[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height ; j++) {
                fields[i][j] = new GrassMapField(new Vector2d(i, j));
            }
        }
        return new GlobeMap(fields);
    }

}
