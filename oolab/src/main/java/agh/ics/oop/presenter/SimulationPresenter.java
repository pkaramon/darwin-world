package agh.ics.oop.presenter;

import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class SimulationPresenter {
    @FXML
    private LineChart<Number, Number> animalCountChart, grassCountChart, averageEnergyChart, averageLifeLengthChart, averageChildrenCountChart;

    private XYChart.Series<Number, Number> animalCountSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> grassCountSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> averageEnergySeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> averageLifeLengthSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> averageChildrenCountSeries = new XYChart.Series<>();


    @FXML
    public void initialize() {
        animalCountChart.getData().add(animalCountSeries);
        grassCountChart.getData().add(grassCountSeries);
        averageEnergyChart.getData().add(averageEnergySeries);
        averageLifeLengthChart.getData().add(averageLifeLengthSeries);
        averageChildrenCountChart.getData().add(averageChildrenCountSeries);

        animalCountSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 2px; -fx-effect: null;");
        for (XYChart.Data<Number, Number> data : animalCountSeries.getData()) {
            data.getNode().setVisible(false);
        }
    }
    @FXML
    private StackPane mapContainer;
    @FXML
    private Button toggleAnimationButton;

    private AnimationTimer animationTimer;
    private boolean isAnimationRunning = true;
    private Simulation simulation;

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
                updateCharts(state);
            }
        };
        animationTimer.start();
    }


    public void updateCharts(SimulationState state) {
        int currentDay = state.currentDay();
        int animalCount = state.animalsOnMap().size();

        XYChart.Data<Number, Number> newData = new XYChart.Data<>(currentDay, animalCount);
        animalCountChart.getData().add(animalCountSeries);
        grassCountChart.getData().add(grassCountSeries);
        averageEnergyChart.getData().add(averageEnergySeries);
        averageLifeLengthChart.getData().add(averageLifeLengthSeries);
        averageChildrenCountChart.getData().add(averageChildrenCountSeries);

        newData.getNode().setVisible(false);
    }

}
