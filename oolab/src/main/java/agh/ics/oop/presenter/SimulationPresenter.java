package agh.ics.oop.presenter;

import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationState;
import agh.ics.oop.simulations.SimulationStatsCalculator;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class SimulationPresenter {
    @FXML
    private LineChart<Number, Number> animalCountChart, grassCountChart, freeFieldsCountChart, dominantGenotypeChart, averageEnergyChart, averageLifetimeChart, averageChildrenCountChart;

    private XYChart.Series<Number, Number> animalCountSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> grassCountSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> freeFieldsCountSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> dominantGenotypeSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> averageEnergySeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> averageLifeLengthSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> averageChildrenCountSeries = new XYChart.Series<>();


    @FXML
    public void initialize() {
        animalCountChart.getData().add(animalCountSeries);
        grassCountChart.getData().add(grassCountSeries);
        freeFieldsCountChart.getData().add(freeFieldsCountSeries);
        dominantGenotypeChart.getData().add(dominantGenotypeSeries);
        averageEnergyChart.getData().add(averageEnergySeries);
        averageLifetimeChart.getData().add(averageLifeLengthSeries);
        averageChildrenCountChart.getData().add(averageChildrenCountSeries);

        animalCountSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 2px; -fx-effect: null;");
        for (XYChart.Data<Number, Number> data : animalCountSeries.getData()) {
            data.getNode().setVisible(false);
        }
        grassCountSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 2px; -fx-effect: null;");
        for (XYChart.Data<Number, Number> data : grassCountSeries.getData()) {
            data.getNode().setVisible(false);
        }
        freeFieldsCountSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 2px; -fx-effect: null;");
        for (XYChart.Data<Number, Number> data : freeFieldsCountSeries.getData()) {
            data.getNode().setVisible(false);
        }
        dominantGenotypeSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 2px; -fx-effect: null;");
        for (XYChart.Data<Number, Number> data : dominantGenotypeSeries.getData()) {
            data.getNode().setVisible(false);
        }
        averageEnergySeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 2px; -fx-effect: null;");
        for (XYChart.Data<Number, Number> data : averageEnergySeries.getData()) {
            data.getNode().setVisible(false);
        }
        averageLifeLengthSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 2px; -fx-effect: null;");
        for (XYChart.Data<Number, Number> data : averageLifeLengthSeries.getData()) {
            data.getNode().setVisible(false);
        }
        averageChildrenCountSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 2px; -fx-effect: null;");
        for (XYChart.Data<Number, Number> data : averageChildrenCountSeries.getData()) {
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
        animalCountSeries.getData().add(new XYChart.Data<>(currentDay, statsCalculator.getNumberOfAnimalsAlive()));
        grassCountSeries.getData().add(new XYChart.Data<>(currentDay, statsCalculator.getNumberOfGrassOnMap()));
        freeFieldsCountSeries.getData().add(new XYChart.Data<>(currentDay, statsCalculator.getNumberOfFreeFields()));
//        dominantGenotypeSeries.getData().add(new XYChart.Data<>(currentDay, statsCalculator.getDominantGenotype()));
        averageEnergySeries.getData().add(new XYChart.Data<>(currentDay, statsCalculator.getAverageEnergy()));
        averageLifeLengthSeries.getData().add(new XYChart.Data<>(currentDay, statsCalculator.getAverageLifetimeForDeadAnimals()));
//        averageChildrenCountSeries.getData().add(new XYChart.Data<>(currentDay, statsCalculator.getAverageChildrenCount()));

        hideDataPointSymbols(animalCountSeries);
        hideDataPointSymbols(grassCountSeries);
        hideDataPointSymbols(freeFieldsCountSeries);
        hideDataPointSymbols(dominantGenotypeSeries);
        hideDataPointSymbols(averageEnergySeries);
        hideDataPointSymbols(averageLifeLengthSeries);
        hideDataPointSymbols(averageChildrenCountSeries);
    }
    private void hideDataPointSymbols(XYChart.Series<Number, Number> series) {
        for (XYChart.Data<Number, Number> data : series.getData()) {
            if (data.getNode() != null) {
                data.getNode().setVisible(false);
            }
        }
    }
}
