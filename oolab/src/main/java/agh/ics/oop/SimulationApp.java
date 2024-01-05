package agh.ics.oop;

import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.animals.AnimalFactory;
import agh.ics.oop.model.generator.DeadAnimalsGrassGenerator;
import agh.ics.oop.model.generator.GrassGenerator;
import agh.ics.oop.model.generator.GrassGeneratorInfo;
import agh.ics.oop.model.maps.GlobeMap;
import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.simulations.Simulation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class SimulationApp extends Application {
    private SimulationCanvas simulationCanvas;
    private Simulation simulation;

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
        // Przykładowe wymiary mapy
        int mapWidth = 100;
        int mapHeight = 100;

        // Tworzenie mapy świata
        WorldMap worldMap = new GlobeMap(new MapField[mapWidth][mapHeight]);

        // Tworzenie generatora trawy
        GrassGeneratorInfo grassGeneratorInfo = new GrassGeneratorInfo(5, 10, 50);
        GrassGenerator grassGenerator = new DeadAnimalsGrassGenerator(grassGeneratorInfo, worldMap, () -> simulation.getCurrentDay());

        // Tworzenie początkowych zwierząt
        List<Animal> initialAnimals = AnimalFactory.createInitialAnimals(
                mapWidth, mapHeight, 50, 10, simulation::getCurrentDay
        );



        // Inicjalizacja symulacji
        simulation = new Simulation();
        simulation.setWorldMap(worldMap);
        simulation.setGrassGenerator(grassGenerator);
        simulation.setInitialAnimals(initialAnimals);
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
