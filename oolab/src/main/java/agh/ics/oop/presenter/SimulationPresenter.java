package agh.ics.oop.presenter;

import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationState;
import agh.ics.oop.simulations.SimulationStatsCalculator;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SimulationPresenter {

    @FXML
    private VBox leftColumn;
    @FXML
    private VBox rightChartColumn;


    StatsChart animalCountChart = new StatsChart("Animal Count", "#ff0000");
    StatsChart grassCountChart = new StatsChart("Grass Count", "#00ff00");
    StatsChart averageEnergyChart = new StatsChart("Average Energy", "#0000ff");
    StatsChart averageLifeLengthChart = new StatsChart("Average Life Length", "#ff00ff");
    StatsChart averageChildrenCountChart = new StatsChart("Average Children Count", "#00ffff");



    @FXML
    private StackPane mapContainer;
    @FXML
    private Button toggleAnimationButton;
    private AnimationTimer animationTimer;
    private boolean isAnimationRunning = true;
    private Simulation simulation;

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
                System.out.println(state.currentDay());


                SimulationStatsCalculator statsCalculator = new SimulationStatsCalculator(
                        state.currentDay(),
                        state.animalsOnMap(),
                        state.map()
                );

                updateCharts(state, statsCalculator);
            }
        };
        animationTimer.start();
    }




    public void updateCharts(SimulationState state, SimulationStatsCalculator statsCalculator) {
        int currentDay = state.currentDay();
        animalCountChart.update(statsCalculator.getNumberOfAnimalsAlive(), currentDay);
        grassCountChart.update(statsCalculator.getNumberOfGrassOnMap(), currentDay);
        averageEnergyChart.update(statsCalculator.getAverageEnergy(), currentDay);
        averageLifeLengthChart.update(statsCalculator.getAverageLifetimeForDeadAnimals(), currentDay);

    }

}
