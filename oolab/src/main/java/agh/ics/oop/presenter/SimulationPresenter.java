package agh.ics.oop.presenter;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.genes.Genotype;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationState;
import agh.ics.oop.simulations.SimulationStats;
import agh.ics.oop.simulations.SimulationStatsCalculator;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimulationPresenter {

    private final WatchedAnimalInfoPresenter watchedAnimalPresenter = new WatchedAnimalInfoPresenter();
    private final MostPopularGenotypesPresenter mostPopularGenotypesPresenter = new MostPopularGenotypesPresenter();
    private final SimulationChartsPresenter simulationChartsPresenter = new SimulationChartsPresenter();

    @FXML
    private Label currentDayLabel;
    @FXML
    private Button mostPopularGenotypeHighlightButton;
    @FXML
    private Button preferredFieldsHighlightButton;
    @FXML
    private VBox leftInfoColumn;
    @FXML
    private VBox rightInfoColumn;
    @FXML
    private StackPane mapContainer;
    @FXML
    private Button toggleAnimationButton;
    @FXML
    private BorderPane popularGenotypesBorderPane;
    @FXML
    private Slider frameRateSlider;

    private AnimationTimer animationTimer;
    private final BooleanProperty isRunning = new SimpleBooleanProperty(true);
    private Simulation simulation;
    private Animal watchedAnimal = null;
    private SimulationCanvas simulationCanvas;
    private StatisticsExporter statisticsExporter;
    private Stage stage;
    private MapVisualizer mapVisualizer;
    private final ExecutorService simulationExecutor = Executors.newSingleThreadExecutor();


    public void setStage(Stage stage) {
        stage.setOnCloseRequest((e) -> terminate());
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        frameRateSlider.setMin(1);
        frameRateSlider.setMax(240);
        frameRateSlider.setValue(24);
        popularGenotypesBorderPane.setCenter(mostPopularGenotypesPresenter.getNode());
        simulationChartsPresenter.putCharts(leftInfoColumn, rightInfoColumn);

        preferredFieldsHighlightButton.visibleProperty().bind(isRunning.not());
        preferredFieldsHighlightButton.managedProperty().bind(isRunning.not());
        mostPopularGenotypeHighlightButton.visibleProperty().bind(isRunning.not());
        mostPopularGenotypeHighlightButton.managedProperty().bind(isRunning.not());

    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.mapVisualizer = new MapVisualizer(
                simulation.getWorldMap(),
                simulation.getGrassGenerator()::isPreferredPosition
        );
        setupSimulationCanvas();
    }

    public void setStatisticsExporter(StatisticsExporter statisticsExporter) {
        this.statisticsExporter = statisticsExporter;
    }

    private void setupSimulationCanvas() {
        int mapWidth = simulation.getWorldMap().getWidth();
        int mapHeight = simulation.getWorldMap().getHeight();
        simulationCanvas = new SimulationCanvas(mapWidth, mapHeight);
        mapContainer.getChildren().add(simulationCanvas);
        startAnimationLoop();
    }

    public void terminate() {
        stopAnimationTimer();
        tryToCloseStatisticsExporter();
        tryToTerminateSimulationExecutor();
    }

    private void stopAnimationTimer() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    private void tryToCloseStatisticsExporter() {
        if (statisticsExporter != null) {
            try {
                statisticsExporter.close();
            } catch (IOException e) {
                System.out.println("Error while closing statistics exporter");
            }
        }
    }

    private void tryToTerminateSimulationExecutor() {
        try {
            if(!simulationExecutor.awaitTermination(250, TimeUnit.MILLISECONDS)) {
                simulationExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("Error while terminating simulation executor");
        }
    }


    private void onSimulationEnded() {
        terminate();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Simulation ended");
        alert.setContentText("Simulation ended after " + simulation.getCurrentDay() + " days");
        alert.show();
        alert.setOnCloseRequest(e -> stage.close());
    }

    @FXML
    private void toggleAnimation() {
        if (isRunning.get()) {
            animationTimer.stop();
            simulationCanvas.draw(mapVisualizer);

            toggleAnimationButton.setText("Resume");
            simulationCanvas.setOnMouseClicked(this::canvasOnClickWhenPaused);
            preferredFieldsHighlightButton.setOnMouseClicked(e -> simulationCanvas.drawPreferredFields(mapVisualizer));
            mostPopularGenotypeHighlightButton.setOnMouseClicked(e -> simulationCanvas.drawDominantGenotypeAnimals(mapVisualizer));
        } else {
            animationTimer.start();
            toggleAnimationButton.setText("Pause");
            simulationCanvas.setOnMouseClicked(null);
            preferredFieldsHighlightButton.setOnMouseClicked(null);
        }
        isRunning.set(!isRunning.get());
    }

    private void canvasOnClickWhenPaused(MouseEvent e) {
        leftInfoColumn.getChildren().remove(watchedAnimalPresenter.getNode());
        Vector2d position = simulationCanvas.correspondingWorldMapPosition((int) e.getX(), (int) e.getY());
        WorldMap map = simulation.getWorldMap();
        List<Animal> animals = map.mapFieldAt(position).getOrderedAnimals();
        if (!animals.isEmpty()) {
            watchedAnimal = animals.get(animals.size() - 1);
            leftInfoColumn.getChildren().add(0, watchedAnimalPresenter.getNode());
            watchedAnimalPresenter.updateWatchedAnimalInfo(
                    WatchedAnimalInfo.fromAnimal(watchedAnimal, simulation.getCurrentDay())
            );
        } else {
            watchedAnimal = null;
        }
    }


    private void startAnimationLoop() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                tryToKeepFrameRate(SimulationPresenter.this::animationIteration);
            }
        };
        animationTimer.start();
    }

    public void animationIteration() {
        SimulationState state = getNewSimulationState();
        SimulationStats simulationStats = getNewSimulationStats(state);

        var watchedAnimalInfo  = WatchedAnimalInfo.fromAnimal(watchedAnimal, state.currentDay());
        mapVisualizer.update(
                animal -> animal.getGenotype().equals(simulationStats.dominantGenotype()),
                watchedAnimal
        );

        Platform.runLater(() -> {
            simulationCanvas.draw(mapVisualizer);
            simulationChartsPresenter.updateCharts(state.currentDay(), simulationStats);
            watchedAnimalPresenter.updateWatchedAnimalInfo(watchedAnimalInfo);
            mostPopularGenotypesPresenter.update(formatGenotypes(simulationStats.dominantGenotypes()));
            currentDayLabel.setText("Current day: " + state.currentDay());

            statisticsExporter.export(simulationStats);

            if(!state.isRunning()) {
                onSimulationEnded();
            }
        });
    }

    private List<String> formatGenotypes(List<Genotype> dominantGenotypes) {
        return dominantGenotypes.stream()
                .map(Genotype::getGenes)
                .map(genes -> "[" + String.join(" ", genes.stream().map(Object::toString).toList()) + "]")
                .toList();
    }

    private SimulationState getNewSimulationState() {
        try {
            Future<SimulationState> simulationFuture = simulationExecutor.submit(this.simulation::run);
            return simulationFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error while running simulation");
            throw new RuntimeException("Could not run simulation: " + e.getMessage());
        }
    }

    private SimulationStats getNewSimulationStats(SimulationState state) {
        try {
            Future<SimulationStats> allOfStatsFuture = simulationExecutor.submit(()-> this.calculateSimulationStats(state));
            return allOfStatsFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error while calculating statistics");
            throw new RuntimeException("Could not calculate statistics: " + e.getMessage());
        }
    }

    private SimulationStats calculateSimulationStats(SimulationState state) {
        Collection<Animal> allAnimals = Stream
                .concat(state.animalsOnMap().stream(), state.removedFromMapAnimals().stream())
                .collect(Collectors.toList());

        SimulationStatsCalculator statsCalculator = new SimulationStatsCalculator(
                state.currentDay(),
                allAnimals,
                state.map()
        );

        List<Genotype> dominantGenotypes = statsCalculator.getMostPopularGenotypes(3);
        Genotype dominant = dominantGenotypes.isEmpty() ? new Genotype(List.of()) : dominantGenotypes.get(0);

        return new SimulationStats(
                state.currentDay(),
                statsCalculator.getNumberOfAnimalsAlive(),
                statsCalculator.getNumberOfFreeFields(),
                statsCalculator.getNumberOfGrassOnMap(),
                dominant,
                dominantGenotypes,
                statsCalculator.getAverageEnergy(),
                statsCalculator.getAverageLifetimeForDeadAnimals(),
                statsCalculator.getAverageNumberOfChildren()
        );
    }


    private void tryToKeepFrameRate(Runnable runnable) {
        int frameDurationMS = (int)(1000 / frameRateSlider.getValue());
        Instant now = Instant.now();
        runnable.run();
        Instant after = Instant.now();
        int elapsed = (int)(after.toEpochMilli() - now.toEpochMilli());
        if (elapsed < frameDurationMS) {
            try {
                Thread.sleep(frameDurationMS - elapsed);
            } catch (InterruptedException ignored) {}
        }
    }

}

