package agh.ics.oop.presenter;

import agh.ics.oop.model.generator.GrassGenerator;
import agh.ics.oop.model.genes.Genotype;
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

    public void draw(SimulationState state,
                     Genotype dominantGenotype,
                     Animal watchedAnimal){
        GraphicsContext gc = getGraphicsContext2D();
        clearCanvas(gc);
        drawPlants(gc, state);
        drawAnimals(gc, state,dominantGenotype);

        if(watchedAnimal != null) {
            drawWatchedAnimal(watchedAnimal, gc);
        }
    }

    public void drawPreferredFields(SimulationState state, GrassGenerator grassGenerator) {
        GraphicsContext gc = getGraphicsContext2D();
        clearCanvas(gc);

        for (MapField field: state.map()) {
            Vector2d position = field.getPosition();
            if(grassGenerator.isPreferredPosition(position)) {
                drawRectangle(gc, position, Color.LIGHTGREEN);
            }
        }
    }

    public void drawDominantGenotypeAnimals(SimulationState lastState, Genotype dominantGenotype) {
        GraphicsContext gc = getGraphicsContext2D();
        clearCanvas(gc);

        for (Animal animal : lastState.animalsOnMap()) {
            if(animal.getGenotype().equals(dominantGenotype)) {
                drawCircleAt(gc, animal.getPosition(),  getAnimalColor(animal));
                drawStartAt(gc, animal.getPosition(), Color.GOLD);
            }
        }
    }

    private void drawWatchedAnimal(Animal watchedAnimal, GraphicsContext gc) {
        drawCircleAt(gc, watchedAnimal.getPosition(),  getAnimalColor(watchedAnimal));
        drawStartAt(gc, watchedAnimal.getPosition(), Color.RED);
    }


    private void clearCanvas(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawAnimals(GraphicsContext gc, SimulationState state, Genotype dominantGenotype) {
        for (Animal animal : state.animalsOnMap()) {
            drawCircleAt(gc, animal.getPosition(),  getAnimalColor(animal));
            if(animal.getGenotype().equals(dominantGenotype)) {
                drawStartAt(gc, animal.getPosition(), Color.GOLD);
            }
        }
    }



    private void drawPlants(GraphicsContext gc, SimulationState state) {
        for (MapField field: state.map()) {
            Vector2d position = field.getPosition();
            if (field.isGrassed()) {
                drawRectangle(gc, position, Color.GREEN);
            }
        }
    }

    private void drawRectangle(GraphicsContext gc, Vector2d position, Color color) {
        Vector2d mapped = convertToCanvasCoordinateSystem(position);
        gc.setFill(color);
        gc.fillRect(mapped.x() * cellSize, mapped.y() * cellSize, cellSize, cellSize);
    }

    private void drawStartAt(GraphicsContext gc, Vector2d position, Color color) {
        Vector2d mapped = convertToCanvasCoordinateSystem(position);
        double xCenter = mapped.x() * cellSize + (double) cellSize /2;
        double yCenter = mapped.y() * cellSize + (double) cellSize /2;
        drawStar(gc,color,  xCenter, yCenter , (double) cellSize * 0.42);
    }

    private void drawCircleAt(GraphicsContext gc, Vector2d position, Color color) {
        Vector2d mapped = convertToCanvasCoordinateSystem(position);
        gc.setFill(color);
        gc.fillOval(mapped.x() * cellSize, mapped.y() * cellSize, cellSize, cellSize);
    }

    private void drawStar(GraphicsContext gc, Color color, double x, double y, double r) {
        gc.setFill(color);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        double angleIncrement = Math.PI / 5;

        double[] xPoints = new double[10];
        double[] yPoints = new double[10];

        for (int i = 0; i < 10; i++) {
            double radius = (i % 2 == 0) ? r : r / 2;
            double angle = i * angleIncrement;
            xPoints[i] = x + radius * Math.cos(angle);
            yPoints[i] = y + radius * Math.sin(angle);
        }

        gc.fillPolygon(xPoints, yPoints, 10);
        gc.strokePolygon(xPoints, yPoints, 10);
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
