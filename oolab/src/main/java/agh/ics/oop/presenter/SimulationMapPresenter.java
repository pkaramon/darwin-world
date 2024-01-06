package agh.ics.oop.presenter;

import agh.ics.oop.SimulationCanvas;
import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class SimulationMapPresenter {
    @FXML
    private StackPane mapContainer;

    private Simulation simulation;

    public void initializeSimulation(Simulation simulation) {
        this.simulation = simulation;
        setupSimulationCanvas();
    }

    private void setupSimulationCanvas() {
        if (simulation != null && simulation.getWorldMap() != null) {
            int mapWidth = simulation.getWorldMap().getWidth();
            int mapHeight = simulation.getWorldMap().getHeight();
            SimulationCanvas simulationCanvas = new SimulationCanvas(mapWidth, mapHeight);

            mapContainer.getChildren().add(simulationCanvas);
            startAnimationLoop(simulationCanvas);
        }
    }



    private void startAnimationLoop(SimulationCanvas simulationCanvas) {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                SimulationState state = simulation.run();

                simulationCanvas.updateAndDraw(state);
            }
        }.start();
    }
}
