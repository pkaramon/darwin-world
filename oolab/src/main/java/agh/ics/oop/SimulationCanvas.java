package agh.ics.oop;

import agh.ics.oop.model.maps.Boundary;
import agh.ics.oop.model.maps.GrassMapField;
import agh.ics.oop.model.maps.MapField;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import agh.ics.oop.simulations.SimulationState;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.WorldMap;

public class SimulationCanvas extends Canvas {
    private SimulationState simulationState; // Stan symulacji
    private WorldMap worldMap; // Mapa świata symulacji
    private final int cellSize = 10; // Rozmiar komórki na mapie

    public SimulationCanvas(double width, double height, SimulationState simulationState, WorldMap worldMap) {
        super(width, height);
        this.simulationState = simulationState;
        this.worldMap = worldMap;
    }

    public void updateAndDraw() {
        GraphicsContext gc = getGraphicsContext2D();
        clearCanvas(gc);
        drawAnimals(gc);
        drawPlants(gc);
    }


    private void clearCanvas(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawAnimals(GraphicsContext gc) {
        for (Animal animal : simulationState.allAnimals()) {
            Vector2d position = animal.getPosition();
            gc.setFill(getAnimalColor(animal));
            gc.fillOval(position.getX() * cellSize, position.getY() * cellSize, cellSize, cellSize);
        }
    }

    private void drawPlants(GraphicsContext gc) {
        Boundary boundary = worldMap.getBoundary();
        for (int x = boundary.lowerLeft().getX(); x <= boundary.upperRight().getX(); x++) {
            for (int y = boundary.lowerLeft().getY(); y <= boundary.upperRight().getY(); y++) {
                Vector2d position = new Vector2d(x, y);
                MapField field = worldMap.mapFieldAt(position);
                if (field instanceof GrassMapField) {
                    GrassMapField grassField = (GrassMapField) field;
                    if (grassField.getGrass().isPresent()) {
                        gc.setFill(Color.GREEN);
                        gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                }
            }
        }
    }




    private Color getAnimalColor(Animal animal) {
        // Zwraca kolor zwierzęcia w zależności od jego stanu, np. energii
        return animal.getEnergy() > 50 ? Color.BLUE : Color.RED;
    }
}
