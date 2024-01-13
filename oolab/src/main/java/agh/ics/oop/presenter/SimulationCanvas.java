package agh.ics.oop.presenter;

import agh.ics.oop.model.generator.GrassGenerator;
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
    private Animal watchedAnimal;

    private static int getCellSize(int mapWidth, int mapHeight) {
        return Math.min(MAX_DIMENSION/mapWidth, MAX_DIMENSION/mapHeight);
    }

    public SimulationCanvas(int mapWidth, int mapHeight) {
        super(mapWidth * getCellSize(mapWidth, mapHeight),mapHeight * getCellSize(mapWidth, mapHeight));
        cellSize = getCellSize(mapWidth, mapHeight);
        this.mapHeight = mapHeight;
    }

    public void drawWhenRunning(SimulationState state) {
        GraphicsContext gc = getGraphicsContext2D();
        clearCanvas(gc);
        drawPlants(state, gc);
        drawAnimals(state, gc);
    }

    public void setWatchedAnimal(Animal watchedAnimal) {
        this.watchedAnimal = watchedAnimal;
    }

    public void drawWhenPaused(SimulationState state,
                               GrassGenerator grassGenerator){
        GraphicsContext gc = getGraphicsContext2D();
        clearCanvas(gc);

        for (MapField field: state.map()) {
            Vector2d position = field.getPosition();
            if(grassGenerator.isPreferredPosition(position))
                drawRectangle(gc, position, Color.LIGHTGREEN);
            else if (field.isGrassed()) {
                drawRectangle(gc, position, Color.GREEN);
            }
        }

        for (Animal animal : state.animalsOnMap()) {
            Vector2d position = animal.getPosition();
            if(animal == watchedAnimal)
                drawRectangle(gc, position, Color.RED);
            else
                drawRectangle(gc, position, getAnimalColor(animal));
        }
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
        Vector2d mapped = convertToCanvasCoordinateSystem(position);
        gc.fillRect(mapped.x() * cellSize, mapped.y() * cellSize, cellSize, cellSize);
    }

    private Vector2d convertToCanvasCoordinateSystem(Vector2d position) {
        return new Vector2d(position.x(), mapHeight - position.y() - 1);
    }

    public Vector2d correspondingWorldMapPosition(int canvasX, int canvasY) {
        return new Vector2d(canvasX/cellSize, -canvasY/cellSize + mapHeight-1);

    }


    private Color getAnimalColor(Animal animal) {
        double colorValue = Math.min(1, (double) animal.getEnergy() /100);
        return Color.color(colorValue,0,colorValue);
    }
}
