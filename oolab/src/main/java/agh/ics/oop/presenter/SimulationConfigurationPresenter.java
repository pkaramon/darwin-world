package agh.ics.oop.presenter;

import agh.ics.oop.SimulationCanvas;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animals.*;
import agh.ics.oop.model.generator.DeadAnimalsGrassGenerator;
import agh.ics.oop.model.generator.GrassGenerator;
import agh.ics.oop.model.generator.GrassGeneratorInfo;
import agh.ics.oop.model.maps.*;
import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationParameters;
import agh.ics.oop.simulations.SimulationState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class SimulationConfigurationPresenter {
    @FXML
    public Button startButton;
    @FXML
    public GridPane mapGrid;
    @FXML
    private Spinner<Integer> mapHeightField;
    @FXML
    private Spinner<Integer> maxWidthField;
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
    private Spinner<Integer> initialPlantsField;
    @FXML
    private Spinner<Integer> parentEnergyGivenToChildField;
    @FXML
    private Spinner<Integer> minMutationsField;
    @FXML
    private Spinner<Integer> maxMutationsField;
    @FXML
    private Spinner<Integer> genomeLengthField;

    @FXML
    private ComboBox<String> plantGrowthVariantField;
    @FXML
    private ComboBox<String> mutationVariantField;

    private SimulationParameters parameters;

    @FXML
    public void initialize() {
        setupSpinner(mapHeightField, 1, 1000, 50);
        setupSpinner(maxWidthField, 1, 1000, 50);
        setupSpinner(grassEnergyProfitField, 1, 1000, 20);
        setupSpinner(minEnergyCopulationField, 1, 1000, 5);
        setupSpinner(animalStartEnergyField, 1, 1000, 100);
        setupSpinner(dailyEnergyCostField, 1, 1000, 1);
        setupSpinner(animalsSpawningStartField, 1, 1000, 20);
        setupSpinner(grassSpawnedDayField, 1, 1000, 20);
        setupSpinner(initialPlantsField, 1, 1000, 5);
        setupSpinner(parentEnergyGivenToChildField, 1, 1000, 50);
        setupSpinner(minMutationsField, 1, 1000, 1);
        setupSpinner(maxMutationsField, 1, 1000, 20);
        setupSpinner(genomeLengthField, 1, 1000, 10);
        plantGrowthVariantField.getItems().addAll("Equatorial Forests", "Other Option");
        plantGrowthVariantField.setValue("Equatorial Forests");
        mutationVariantField.getItems().addAll("Full Randomness", "Other Option");
        mutationVariantField.setValue("Full Randomness");
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
        String plantGrowthVariant = "Equatorial Forests";
        String mutationVariant = "Full Randomness";
        int parentEnergyGivenToChild = 50;
        int minMutations = 1;
        int maxMutations = 20;
        int genomeLength = 32;

        SimulationParameters parameters = new SimulationParameters(
                maxWidth, mapHeight, numberOfGrassInitially, grassEnergyProfit, grassSpawnedDay,
                plantGrowthVariant, animalsSpawningStart, animalStartEnergy, minEnergyCopulation,
                parentEnergyGivenToChild, minMutations, maxMutations, mutationVariant, genomeLength
        );

        initializeSimulationWithParameters(parameters);
    }


    public void initializeSimulationWithParameters(SimulationParameters parameters) {
        this.parameters = parameters;
        Simulation simulation = new Simulation();

        MapField[][] mapFields = new MapField[parameters.mapWidth()][parameters.mapHeight()];
        for (int x = 0; x < parameters.mapWidth(); x++) {
            for (int y = 0; y < parameters.mapHeight(); y++) {
                mapFields[x][y] = new GrassMapField(new Vector2d(x, y));
            }
        }
        WorldMap worldMap = new GlobeMap(mapFields);
        simulation.setWorldMap(worldMap);

        GrassGeneratorInfo grassGeneratorInfo = new GrassGeneratorInfo(parameters.plantsPerDay(), parameters.plantEnergy(), parameters.initialNumberOfPlants());
        GrassGenerator grassGenerator = new DeadAnimalsGrassGenerator(grassGeneratorInfo, worldMap, simulation::getCurrentDay);

        List<Animal> initialAnimals = AnimalFactory.createInitialAnimals(
                parameters.mapWidth(),
                parameters.mapHeight(),
                parameters.initialNumberOfAnimals(),
                parameters.animalStartEnergy(),
                simulation::getCurrentDay
        );


        simulation.setGrassGenerator(grassGenerator);
        simulation.setInitialAnimals(initialAnimals);

        startSimulation(simulation);
    }

    private void startSimulation(Simulation simulation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulationMap.fxml"));
            StackPane root = loader.load();
            SimulationMapPresenter simulationPresenter = loader.getController();
            simulationPresenter.initializeSimulation(simulation);

            Stage stage = new Stage();
            stage.setTitle("Simulation Map");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

