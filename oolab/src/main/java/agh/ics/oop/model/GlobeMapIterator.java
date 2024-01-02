package agh.ics.oop.model;

import java.util.Iterator;

public class GlobeMapIterator implements Iterator<MapField> {
    private final MapField[][] fields;
    private final int width;
    private final int height;

    public GlobeMapIterator(MapField[][] fields) {
        this.fields = fields;
        this.width = fields.length;
        this.height = fields[0].length;
    }

    private int x = 0;
    private int y = 0;

    @Override
    public boolean hasNext() {
        return x < width && y < height;
    }

    @Override
    public MapField next() {
        MapField field = fields[x][y];
        x++;
        if (x == width) {
            x = 0;
            y++;
        }
        return field;
    }
}
