package agh.ics.oop.model;

import agh.ics.oop.model.maps.GlobeMap;
import agh.ics.oop.model.maps.GlobeMapField;
import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;

public class WordMapCreator {
    private WordMapCreator() {}

    public static WorldMap createWorldMap(int width, int height) {
        MapField[][] fields = new MapField[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height ; j++) {
                fields[i][j] = new GlobeMapField(new Vector2d(i, j));
            }
        }
        return new GlobeMap(fields);
    }

}
