package agh.ics.oop.presenter;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.genes.Genotype;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationState;
import agh.ics.oop.simulations.SimulationStatsCalculator;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimulationPresenter {

    private final WatchedAnimalInfoPresenter watchedAnimalPresenter = new WatchedAnimalInfoPresenter();
    private final MostPopularGenotypesPresenter mostPopularGenotypesPresenter = new MostPopularGenotypesPresenter();
    private final SimulationChartsPresenter simulationChartsPresenter = new SimulationChartsPresenter();
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
    private AnimationTimer animationTimer;
    private final BooleanProperty isRunning = new SimpleBooleanProperty(true);
    private Simulation simulation;
    private Animal watchedAnimal = null;
    private SimulationState lastState = null;
    private SimulationCanvas simulationCanvas;
    private Stage stage;
    private Genotype dominantGenotype;
    private StatisticsExporter statisticsExport;


    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest((e) -> shutDownAnimationAndSimulation());
    }

    @FXML
    public void initialize() {
        popularGenotypesBorderPane.setCenter(mostPopularGenotypesPresenter.getNode());
        simulationChartsPresenter.putCharts(leftInfoColumn, rightInfoColumn);

        preferredFieldsHighlightButton.visibleProperty().bind(isRunning.not());
        preferredFieldsHighlightButton.managedProperty().bind(isRunning.not());
        mostPopularGenotypeHighlightButton.visibleProperty().bind(isRunning.not());
        mostPopularGenotypeHighlightButton.managedProperty().bind(isRunning.not());

    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        setupSimulationCanvas();
    }

    public void setStatisticsExporter(StatisticsExporter statisticsExporter) {
        this.statisticsExport = statisticsExporter;
    }

    private void setupSimulationCanvas() {
        int mapWidth = simulation.getWorldMap().getWidth();
        int mapHeight = simulation.getWorldMap().getHeight();
        simulationCanvas = new SimulationCanvas(mapWidth, mapHeight);
        mapContainer.getChildren().add(simulationCanvas);
        startAnimationLoop(simulationCanvas);
    }

    public void shutDownAnimationAndSimulation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    @FXML
    private void toggleAnimation() {
        if (isRunning.get()) {
            animationTimer.stop();
            toggleAnimationButton.setText("Resume");

            simulationCanvas.setOnMouseClicked(e -> {
                leftInfoColumn.getChildren().remove(watchedAnimalPresenter.getNode());
                Vector2d position = simulationCanvas.correspondingWorldMapPosition((int) e.getX(), (int) e.getY());
                WorldMap map = simulation.getWorldMap();
                List<Animal> animals = map.mapFieldAt(position).getOrderedAnimals();
                if (!animals.isEmpty()) {
                    watchedAnimal = animals.get(animals.size() - 1);

                    leftInfoColumn.getChildren().add(0, watchedAnimalPresenter.getNode());
                    watchedAnimalPresenter.updateWatchedAnimalInfo(watchedAnimal, lastState.currentDay());
                } else {
                    watchedAnimal = null;
                }
            });

            preferredFieldsHighlightButton.setOnMouseClicked(e -> {
                simulationCanvas.drawPreferredFields(lastState, simulation.getGrassGenerator());
            });
            mostPopularGenotypeHighlightButton.setOnMouseClicked(e -> {
                simulationCanvas.drawDominantGenotypeAnimals(lastState, dominantGenotype);
            });

        } else {
            animationTimer.start();
            toggleAnimationButton.setText("Pause");
            simulationCanvas.setOnMouseClicked(null);

            preferredFieldsHighlightButton.setOnMouseClicked(null);

        }
        isRunning.set(!isRunning.get());
    }


    private void startAnimationLoop(SimulationCanvas simulationCanvas) {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Instant start = Instant.now();
                SimulationState state = simulation.run();
                lastState = state;

                Collection<Animal> allAnimals = Stream
                        .concat(
                                state.animalsOnMap().stream(),
                                state.removedFromMapAnimals().stream()
                        )
                        .collect(Collectors.toList());

                SimulationStatsCalculator statsCalculator = new SimulationStatsCalculator(
                        state.currentDay(),
                        allAnimals,
                        state.map()
                );

                List<Genotype> dominantGenotypes = statsCalculator.getMostPopularGenotypes(3);
                Genotype dominant = dominantGenotypes.isEmpty() ? new Genotype(List.of()) : dominantGenotypes.get(0);

                dominantGenotype = dominant;
                simulationCanvas.draw(state, dominant, watchedAnimal);
                simulationChartsPresenter.updateCharts(state, statsCalculator);
                watchedAnimalPresenter.updateWatchedAnimalInfo(watchedAnimal, state.currentDay());
                mostPopularGenotypesPresenter.update(dominantGenotypes);


                SimulationStats stats = new SimulationStats(
                        state.currentDay(),
                        statsCalculator.getNumberOfAnimalsAlive(),
                        statsCalculator.getNumberOfFreeFields(),
                        dominant.getGenes(),
                        statsCalculator.getAverageEnergy(),
                        statsCalculator.getAverageLifetimeForDeadAnimals(),
                        statsCalculator.getAverageNumberOfChildren()
                );
                statisticsExport.export(stats);

                Instant end = Instant.now();
                if (end.toEpochMilli() - start.toEpochMilli() < 1000 / 24) {
                    try {
                        Thread.sleep(1000 / 24 - (end.toEpochMilli() - start.toEpochMilli()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        animationTimer.start();
    }

}

