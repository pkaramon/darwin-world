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
    private LineChart<Number, Number> animalCountChart;
    private XYChart.Series<Number, Number> animalCountSeries = new XYChart.Series<>();

    @FXML
    public void initialize() {
        Simulation simulation = new Simulation();
        simulation.setPresenter(this);
        animalCountChart.getData().add(animalCountSeries);
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
            }
        };
        animationTimer.start();
    }

    public void updateCharts(SimulationState state) {
        System.out.println("Aktualizacja wykresu dla dnia: " + state.currentDay());
        int currentDay = state.currentDay();
        int animalCount = state.animalsOnMap().size();

        if (animalCountSeries.getData().size() > 100) {
            animalCountSeries.getData().remove(0);
        }
        animalCountSeries.getData().add(new XYChart.Data<>(currentDay, animalCount));
    }

}
