package agh.ics.oop.presenter;

import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class SimulationPresenter {
    @FXML
    private StackPane mapContainer;
    @FXML
    private Button toggleAnimationButton;

    private AnimationTimer animationTimer;
    private boolean isAnimationRunning = true;
    private Simulation simulation;

    public void initializeSimulation(Simulation simulation) {
        this.simulation = simulation;
        setupSimulationCanvas();
    }

    @FXML
    private void toggleAnimation() {
        if (isAnimationRunning) {
            animationTimer.stop();
            toggleAnimationButton.setText("Resume");
        } else {
            animationTimer.start();
            toggleAnimationButton.setText("Pause");
        }
        isAnimationRunning = !isAnimationRunning;
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
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                SimulationState state = simulation.run();
                simulationCanvas.updateAndDraw(state);
            }
        };
        animationTimer.start();
    }
}
