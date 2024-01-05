package agh.ics.oop.presenter;

import agh.ics.oop.SimulationCanvas;
import agh.ics.oop.model.animals.*;
import agh.ics.oop.model.generator.DeadAnimalsGrassGenerator;
import agh.ics.oop.model.generator.GrassGenerator;
import agh.ics.oop.model.generator.GrassGeneratorInfo;
import agh.ics.oop.model.maps.Boundary;
import agh.ics.oop.model.maps.GlobeMap;
import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationParameters;
import agh.ics.oop.simulations.SimulationState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;

import static agh.ics.oop.model.animals.AnimalFactory.createInitialAnimals;


public class SimulationPresenter {
    @FXML
    public Button startButton;
    @FXML
    public GridPane mapGrid;
    @FXML
    private Spinner<Integer> mapHeightField;
    @FXML
    private Spinner<Integer> maxWidthField;
    @FXML
    private Spinner<Integer> jungleWidthField;
    @FXML
    private Spinner<Integer> jungleHeightField;
    @FXML
    private Spinner<Integer> grassEnergyProfitField;
    @FXML
    private Spinner<Integer> minEnergyCopulationField;
    @FXML
    private Spinner<Integer> animalStartEnergyField;
    @FXML
    private Spinner<Integer> dailyEnergyCostField;
    @FXML
    private Spinner<Integer> animalsSpawningStartField;
    @FXML
    private Spinner<Integer> grassSpawnedDayField;
    @FXML
    private Spinner<Integer> realRefreshTimeField;

    private Simulation simulation;

    @FXML
    public void initialize() {
        setupSpinner(mapHeightField, 1, 1000, 1);
        setupSpinner(maxWidthField, 1, 1000, 1);
        setupSpinner(jungleWidthField, 1, 1000, 1);
        setupSpinner(jungleHeightField, 1, 1000, 1);
        setupSpinner(grassEnergyProfitField, 1, 1000, 1);
        setupSpinner(minEnergyCopulationField, 1, 1000, 1);
        setupSpinner(animalStartEnergyField, 1, 1000, 1);
        setupSpinner(dailyEnergyCostField, 1, 1000, 1);
        setupSpinner(animalsSpawningStartField, 1, 1000, 1);
        setupSpinner(grassSpawnedDayField, 1, 1000, 1);
        setupSpinner(realRefreshTimeField, 1, 1000, 1);
    }

    private void setupSpinner(Spinner<Integer> spinner, int min, int max, int initialValue) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue));
        spinner.setEditable(true);

        spinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") && !newValue.isEmpty()) {
                spinner.getEditor().setText(oldValue);
            }
        });

        spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    Integer value = Integer.parseInt(spinner.getEditor().getText());
                    spinner.getValueFactory().setValue(value);
                } catch (NumberFormatException e) {
                    spinner.getEditor().setText(String.valueOf(initialValue));
                    spinner.getValueFactory().setValue(initialValue);
                }
            }
        });
    }


    @FXML
    private void onSimulationStartClicked() {
        int mapHeight = mapHeightField.getValue();
        int maxWidth = maxWidthField.getValue();
        int grassEnergyProfit = grassEnergyProfitField.getValue();
        int minEnergyCopulation = minEnergyCopulationField.getValue();
        int animalStartEnergy = animalStartEnergyField.getValue();
        int animalsSpawningStart = animalsSpawningStartField.getValue();
        int grassSpawnedDay = grassSpawnedDayField.getValue();
        int numberOfGrassInitially = 5;
        String plantGrowthVariant = "random";
        String mutationVariant = "random";
        int parentEnergyGivenToChild = 50;
        int minMutations = 1;);
        int maxMutations = 20;
        int genomeLength = 32;

        SimulationParameters parameters = new SimulationParameters(
                maxWidth, mapHeight, numberOfGrassInitially, grassEnergyProfit, grassSpawnedDay,
                plantGrowthVariant, animalsSpawningStart, animalStartEnergy, minEnergyCopulation,
                parentEnergyGivenToChild, minMutations, maxMutations, mutationVariant, genomeLength
        );

        WorldMap worldMap = new GlobeMap(new MapField[maxWidth][mapHeight]);
        GrassGeneratorInfo grassGeneratorInfo = new GrassGeneratorInfo(grassSpawnedDay, grassEnergyProfit, numberOfGrassInitially);
        GrassGenerator grassGenerator = new DeadAnimalsGrassGenerator(grassGeneratorInfo, worldMap, simulation::getCurrentDay);

        List<Animal> initialAnimals = createInitialAnimals(parameters);

        simulation = new Simulation();
        simulation.setWorldMap(worldMap);
        simulation.setGrassGenerator(grassGenerator);
        simulation.setInitialAnimals(initialAnimals);

        startSimulation();
    }

    private void startSimulation() {
        Boundary boundary = simulation.getWorldMap().getBoundary();
        SimulationCanvas simulationCanvas = new SimulationCanvas(boundary.width(), boundary.height());

        mapGrid.getChildren().add(simulationCanvas);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                SimulationState state = simulation.run();
                simulationCanvas.updateAndDraw(state);
            }
        }.start();
    }
}

