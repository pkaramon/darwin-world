package agh.ics.oop;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.animals.AnimalFactory;
import agh.ics.oop.model.generator.DeadAnimalsGrassGenerator;
import agh.ics.oop.model.generator.GrassGenerator;
import agh.ics.oop.model.generator.GrassGeneratorInfo;
import agh.ics.oop.model.maps.*;
import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class SimulationApp extends Application {
    private SimulationCanvas simulationCanvas;
    private Simulation simulation;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        initializeSimulation();
        Boundary mapBoundary = simulation.getWorldMap().getBoundary();
        simulationCanvas = new SimulationCanvas(mapBoundary.width(), mapBoundary.height());

        viewRoot.setCenter(simulationCanvas);

        configureStage(primaryStage, viewRoot);
        primaryStage.show();

        startSimulationLoop();
    }

    private void initializeSimulation() {
        int mapWidth = 50;
        int mapHeight = 50;

        simulation = new Simulation();

        WorldMap worldMap = createWorldMap(mapWidth, mapHeight);
        simulation.setWorldMap(worldMap);

        GrassGenerator grassGenerator = createGrassGenerator(worldMap);
        simulation.setGrassGenerator(grassGenerator);

        List<Animal> initialAnimals = AnimalFactory.createInitialAnimals(
                mapWidth, mapHeight, 50, 20, simulation::getCurrentDay
        );
        simulation.setInitialAnimals(initialAnimals);
    }

    private WorldMap createWorldMap(int mapWidth, int mapHeight) {
        MapField[][] fields = new MapField[mapWidth][mapHeight];
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++)
                fields[x][y] = new GrassMapField(new Vector2d(x, y));
        }

        return new GlobeMap(fields);
    }

    private GrassGenerator createGrassGenerator(WorldMap worldMap) {
        GrassGeneratorInfo grassGeneratorInfo = new GrassGeneratorInfo(2, 10, 50);
        DeadAnimalsGrassGenerator grassGenerator = new DeadAnimalsGrassGenerator(grassGeneratorInfo, worldMap, () -> simulation.getCurrentDay());
        simulation.addListener(grassGenerator);
        return new DeadAnimalsGrassGenerator(grassGeneratorInfo, worldMap, simulation::getCurrentDay);

    }

    private void startSimulationLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                SimulationState state = simulation.run();
                if (!state.isRunning()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("KONIEC");
                    alert.setContentText("Symulacja zakończona");
                    alert.show();
                    this.stop();

                }

                simulationCanvas.updateAndDraw(state);
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
}
