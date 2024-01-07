package agh.ics.oop.presenter;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import agh.ics.oop.simulations.SimulationState;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.MapField;

public class SimulationCanvas extends Canvas {
    private final int mapHeight;

    private final int cellSize;
    private final static int MAX_DIMENSION = 700;

    private static int getCellSize(int mapWidth, int mapHeight) {
        return Math.min(MAX_DIMENSION/mapWidth, MAX_DIMENSION/mapHeight);
    }

    public SimulationCanvas(int mapWidth, int mapHeight) {
        super(mapWidth * getCellSize(mapWidth, mapHeight),mapHeight * getCellSize(mapWidth, mapHeight));
        cellSize = getCellSize(mapWidth, mapHeight);
        this.mapHeight = mapHeight;
    }

    public void updateAndDraw(SimulationState state) {
        GraphicsContext gc = getGraphicsContext2D();
        clearCanvas(gc);
        drawPlants(state, gc);
        drawAnimals(state, gc);
    }

    private void clearCanvas(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawAnimals(SimulationState state, GraphicsContext gc) {
        for (Animal animal : state.animalsOnMap()) {
            Vector2d position = animal.getPosition();
            drawRectangle(gc, position, getAnimalColor(animal));
        }
    }

    private void drawPlants(SimulationState state, GraphicsContext gc) {
        for (MapField field: state.map()) {
            Vector2d position = field.getPosition();
            if (field.isGrassed()) {
                drawRectangle(gc, position, Color.GREEN);
            }
        }
    }

    private void drawRectangle(GraphicsContext gc, Vector2d position, Color color) {
        gc.setFill(color);
        Vector2d mapped = convertToGraphicsContextCoordinateSystem(position);
        gc.fillRect(mapped.x() * cellSize, mapped.y() * cellSize, cellSize, cellSize);
    }

    private Vector2d convertToGraphicsContextCoordinateSystem(Vector2d position) {
        return new Vector2d(position.x(), mapHeight - position.y() - 1);
    }


    private Color getAnimalColor(Animal animal) {
        return animal.getEnergy() > 50 ? Color.BLUE : Color.RED;
    }
}
