package agh.ics.oop.presenter;

import agh.ics.oop.SimulationCanvas;
import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class SimulationMapPresenter {
    @FXML
    private StackPane simulationContainer;

    public void initializeSimulation(Simulation simulation) {
        SimulationCanvas simulationCanvas = new SimulationCanvas(
                simulation.getWorldMap().getWidth(),
                simulation.getWorldMap().getHeight()
        );

        simulationContainer.getChildren().add(simulationCanvas);

        // Uruchomienie pętli animacji
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                SimulationState state = simulation.run();
                simulationCanvas.updateAndDraw(state); // Aktualizacja widoku symulacji
            }
        }.start();
    }
}
