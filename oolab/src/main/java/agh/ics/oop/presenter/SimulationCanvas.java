package agh.ics.oop.presenter;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import agh.ics.oop.model.Vector2d;

import java.util.List;

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

    public void draw(MapVisualizer mapVisualizer){
        GraphicsContext gc = clearCanvas();
        mapVisualizer.positionStream().forEach(position ->
                drawFigures(mapVisualizer.whatToDrawAtNormally(position), position, gc)
        );
    }

    public void drawPreferredFields(MapVisualizer visualizer) {
        GraphicsContext gc = clearCanvas();
        visualizer.positionStream().forEach(position ->
            drawFigures(visualizer.whatToDrawWhenShowingPreferredFields(position), position, gc)
        );
    }

    public void drawDominantGenotypeAnimals(MapVisualizer visualizer) {
        GraphicsContext gc = clearCanvas();
        visualizer.positionStream().forEach(position ->
                drawFigures(visualizer.whatToDrawWhenShowingDominantGenotypeAnimals(position), position, gc)
        );
    }

    private GraphicsContext clearCanvas() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        return gc;
    }

    private void drawFigures(List<MapVisualizer.Figure> visualizer, Vector2d position, GraphicsContext gc) {
        visualizer.forEach(figure -> {
            Vector2d mapped = convertToCanvasCoordinateSystem(position);
            switch (figure.shape()) {
                case RECTANGLE -> drawRectangle(gc, mapped, figure.color());
                case CIRCLE -> drawCircleAt(gc, mapped, figure.color());
                case STAR -> drawStartAt(gc, mapped, figure.color());
            }
        });
    }


    private void drawRectangle(GraphicsContext gc, Vector2d mapped, Color color) {
        gc.setFill(color);
        gc.fillRect(mapped.x() * cellSize, mapped.y() * cellSize, cellSize, cellSize);
    }

    private void drawCircleAt(GraphicsContext gc, Vector2d mapped, Color color) {
        gc.setFill(color);
        gc.fillOval(mapped.x() * cellSize, mapped.y() * cellSize, cellSize, cellSize);
    }

    private void drawStartAt(GraphicsContext gc, Vector2d mapped, Color color) {
        double xCenter = mapped.x() * cellSize + (double) cellSize /2;
        double yCenter = mapped.y() * cellSize + (double) cellSize /2;
        drawStar(gc,color,  xCenter, yCenter , (double) cellSize * 0.42);
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
}
