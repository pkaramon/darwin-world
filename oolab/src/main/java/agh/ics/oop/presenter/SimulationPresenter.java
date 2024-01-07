package agh.ics.oop.presenter;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationState;
import agh.ics.oop.simulations.SimulationStatsCalculator;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class SimulationPresenter {

    StatsChart animalCountChart = new StatsChart("Animal Count", "#ff0000");
    StatsChart grassCountChart = new StatsChart("Grass Count", "#00ff00");
    StatsChart averageEnergyChart = new StatsChart("Average Energy", "#0000ff");
    StatsChart averageLifeLengthChart = new StatsChart("Average Life Length", "#ff00ff");
    StatsChart averageChildrenCountChart = new StatsChart("Average Children Count", "#00ffff");
    @FXML
    private Label watchedAnimalLabel;
    @FXML
    private VBox leftColumn;
    @FXML
    private VBox rightChartColumn;
    @FXML
    private StackPane mapContainer;
    @FXML
    private Button toggleAnimationButton;
    private AnimationTimer animationTimer;
    private boolean isAnimationRunning = true;
    private Simulation simulation;
    private SimulationCanvas simulationCanvas;
    private Animal watchedAnimal = null;

    @FXML
    public void initialize() {
        leftColumn.getChildren().addAll(
                animalCountChart.getNode(),
                grassCountChart.getNode(),
                averageEnergyChart.getNode()
        );
        rightChartColumn.getChildren().addAll(
                averageLifeLengthChart.getNode(),
                averageChildrenCountChart.getNode()
        );
    }

    public void initializeSimulation(Simulation simulation) {
        if (simulation == null) {
            throw new IllegalStateException("Symulacja nie została zainicjalizowana.");
        }
        this.simulation = simulation;
        setupSimulationCanvas();
    }

    @FXML
    private void toggleAnimation() {
        if (isAnimationRunning) {
            animationTimer.stop();
            toggleAnimationButton.setText("Resume");

            simulationCanvas.setOnMouseClicked(e -> {
                Vector2d position = simulationCanvas.correspondingWorldMapPosition((int) e.getX(), (int) e.getY());
                WorldMap map = simulation.getWorldMap();
                List<Animal> animals = map.mapFieldAt(position).getOrderedAnimals();
                System.out.println(animals);
                if (!animals.isEmpty()) {
                    watchedAnimal = animals.get(animals.size() - 1);
                }
            });
        } else {
            animationTimer.start();
            toggleAnimationButton.setText("Pause");
            simulationCanvas.setOnMouseClicked(null);
        }
        isAnimationRunning = !isAnimationRunning;
    }

    private void setupSimulationCanvas() {
        int mapWidth = simulation.getWorldMap().getWidth();
        int mapHeight = simulation.getWorldMap().getHeight();
        simulationCanvas = new SimulationCanvas(mapWidth, mapHeight);


        mapContainer.getChildren().add(simulationCanvas);
        startAnimationLoop(simulationCanvas);
    }

    private void startAnimationLoop(SimulationCanvas simulationCanvas) {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                SimulationState state = simulation.run();
                simulationCanvas.updateAndDraw(state);
                System.out.println(state.currentDay());

                SimulationStatsCalculator statsCalculator = new SimulationStatsCalculator(
                        state.currentDay(),
                        state.animalsOnMap(),
                        state.map()
                );

                updateCharts(state, statsCalculator);
                updateWatchedAnimalLabel();
            }
        };
        animationTimer.start();
    }

    private void updateWatchedAnimalLabel() {
        if (watchedAnimal == null) {
            watchedAnimalLabel.setText("");
        } else {
            watchedAnimalLabel.setText("Watched animal: " + watchedAnimal + "\n" +
                    "Genotype: " + watchedAnimal.getGenotype().toString() + "\n" +
                    "Energy: " + watchedAnimal.getEnergy() + "\n" +
                    "Birth day: " + watchedAnimal.getBirthDay() + "\n" +
                    "Death day: " + watchedAnimal.getDeathDay() + "\n");
        }
    }


    public void updateCharts(SimulationState state, SimulationStatsCalculator statsCalculator) {
        int currentDay = state.currentDay();
        animalCountChart.update(statsCalculator.getNumberOfAnimalsAlive(), currentDay);
        grassCountChart.update(statsCalculator.getNumberOfGrassOnMap(), currentDay);
        averageEnergyChart.update(statsCalculator.getAverageEnergy(), currentDay);
        averageLifeLengthChart.update(statsCalculator.getAverageLifetimeForDeadAnimals(), currentDay);
    }
}
