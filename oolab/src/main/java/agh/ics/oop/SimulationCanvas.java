package agh.ics.oop;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import agh.ics.oop.simulations.SimulationState;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.MapField;

public class SimulationCanvas extends Canvas {
    private static final int CELL_SIZE = 10;
    private final int mapHeight;

    public SimulationCanvas(int mapWidth, int mapHeight) {
        super(mapWidth*CELL_SIZE, mapHeight*CELL_SIZE);
        this.mapHeight = mapHeight;
    }

    public void updateAndDraw(SimulationState state) {
        GraphicsContext gc = getGraphicsContext2D();
        clearCanvas(gc);
        drawAnimals(state, gc);
        drawPlants(state, gc);
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
        gc.fillRect(mapped.getX() * CELL_SIZE, mapped.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private Vector2d convertToGraphicsContextCoordinateSystem(Vector2d position) {
        return new Vector2d(position.getX(), mapHeight - position.getY() - 1);
    }


    private Color getAnimalColor(Animal animal) {
        return animal.getEnergy() > 50 ? Color.BLUE : Color.RED;
    }
}
