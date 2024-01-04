package agh.ics.oop;

import agh.ics.oop.simulations.Simulation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    private SimulationCanvas simulationCanvas;
    private Simulation simulation; // Załóżmy, że masz klasę Simulation

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        initializeSimulation();
        simulationCanvas = new SimulationCanvas(800, 600, simulation.getSimulationState(), simulation.getWorldMap());

        viewRoot.setCenter(simulationCanvas);

        configureStage(primaryStage, viewRoot);
        primaryStage.show();

        startSimulationLoop();
    }


    private void initializeSimulation() {
        simulation = new Simulation();
        // Konfiguracja symulacji...
    }

    private void startSimulationLoop() {

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                simulation.run();
                simulationCanvas.updateAndDraw();
            }
        }.start();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
