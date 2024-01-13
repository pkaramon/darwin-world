package agh.ics.oop.presenter;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.animals.AnimalFactory;
import agh.ics.oop.model.generator.DeadAnimalsGrassGenerator;
import agh.ics.oop.model.generator.EquatorGrassGenerator;
import agh.ics.oop.model.generator.GrassGenerator;
import agh.ics.oop.model.generator.GrassGeneratorInfo;
import agh.ics.oop.model.maps.GlobeMap;
import agh.ics.oop.model.maps.GrassMapField;
import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.JsonUtil;
import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.simulations.SimulationConfiguration;
import agh.ics.oop.simulations.SimulationParameters;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ConfiguratorPresenter {
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
    @FXML
    private ComboBox<String> configurationsComboBox;



    @FXML
    public void initialize() {
        setupSpinner(mapHeightField, 1, 1000, 50);
        setupSpinner(maxWidthField, 1, 1000, 50);
        setupSpinner(grassEnergyProfitField, 1, 1000, 20);
        setupSpinner(minEnergyCopulationField, 1, 1000, 30);
        setupSpinner(animalStartEnergyField, 1, 1000, 100);
        setupSpinner(animalsSpawningStartField, 1, 1000, 20);
        setupSpinner(grassSpawnedDayField, 1, 1000, 20);
        setupSpinner(initialPlantsField, 1, 100000, 5);
        setupSpinner(parentEnergyGivenToChildField, 1, 1000, 10);
        setupSpinner(minMutationsField, 1, 1000, 1);
        setupSpinner(maxMutationsField, 1, 1000, 5);
        setupSpinner(genomeLengthField, 1, 1000, 20);

        plantGrowthVariantField.getItems().addAll("Equatorial Forests", "Life-giving Carcasses");
        plantGrowthVariantField.setValue("Equatorial Forests");
        mutationVariantField.getItems().addAll("Full Randomness", "Small Correction");
        mutationVariantField.setValue("Full Randomness");

        configurationsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadConfiguration(newValue);
            }
        });

        loadConfigurationNames();
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
    private void onSaveConfigurationClicked() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Zapisz konfigurację");
        dialog.setHeaderText("Wprowadź nazwę konfiguracji");
        dialog.setContentText("Nazwa:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::saveConfiguration);
    }

    private void saveConfiguration(String configurationName) {
        List<SimulationConfiguration> configurations = loadConfigurationNames();

        // Sprawdź, czy konfiguracja o tej samej nazwie już istnieje
        for (SimulationConfiguration config : configurations) {
            if (config.getName().equals(configurationName)) {
                showAlert("Błąd zapisu", "Konfiguracja o nazwie '" + configurationName + "' już istnieje.");
                return;
            }
        }

        SimulationParameters parameters = getCurrentSimulationParameters();
        SimulationConfiguration newConfig = new SimulationConfiguration(configurationName, parameters);
        configurations.add(newConfig);
        JsonUtil.serialize(configurations, "configurations.json");
    }

    private List<SimulationConfiguration> loadConfigurationNames() {
        List<SimulationConfiguration> configurations = JsonUtil.deserialize("configurations.json");
        configurationsComboBox.getItems().clear();
        for (SimulationConfiguration config : configurations) {
            configurationsComboBox.getItems().add(config.getName());
        }
        return configurations;
    }

    private SimulationParameters getCurrentSimulationParameters() {
        return new SimulationParameters(
                maxWidthField.getValue(),
                mapHeightField.getValue(),
                initialPlantsField.getValue(),
                grassEnergyProfitField.getValue(),
                grassSpawnedDayField.getValue(),
                plantGrowthVariantField.getValue(),
                animalsSpawningStartField.getValue(),
                animalStartEnergyField.getValue(),
                minEnergyCopulationField.getValue(),
                parentEnergyGivenToChildField.getValue(),
                minMutationsField.getValue(),
                maxMutationsField.getValue(),
                mutationVariantField.getValue(),
                genomeLengthField.getValue()
        );
    }
    @FXML
    private void onSimulationStartClicked() {
        SimulationParameters parameters = new SimulationParameters(
                maxWidthField.getValue(),
                mapHeightField.getValue(),
                initialPlantsField.getValue(),
                grassEnergyProfitField.getValue(),
                grassSpawnedDayField.getValue(),
                plantGrowthVariantField.getValue(),
                animalsSpawningStartField.getValue(),
                animalStartEnergyField.getValue(),
                minEnergyCopulationField.getValue(),
                parentEnergyGivenToChildField.getValue(),
                minMutationsField.getValue(),
                maxMutationsField.getValue(),
                mutationVariantField.getValue(),
                genomeLengthField.getValue()
        );

        int minEnergyCopulation = minEnergyCopulationField.getValue();
        int parentEnergyGivenToChild = parentEnergyGivenToChildField.getValue();
        int minMutations = minMutationsField.getValue();
        int maxMutations = maxMutationsField.getValue();
        int genomeLength = genomeLengthField.getValue();

        if (minEnergyCopulation <= parentEnergyGivenToChild) {
            showAlert("Validation Error", "Minimum energy to copulation must be greater than parent energy given to child.");
            return;
        }

        if (minMutations >= genomeLength || maxMutations >= genomeLength) {
            showAlert("Validation Error", "Min and max mutations must be less than genome length.");
            return;
        }

        initializeSimulationWithParameters(parameters);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateUIWithParameters(SimulationParameters parameters) {
        if (parameters == null) return;

        updateSpinnerValue(mapHeightField, parameters.mapHeight());
        updateSpinnerValue(maxWidthField, parameters.mapWidth());
        updateSpinnerValue(grassEnergyProfitField, parameters.plantEnergy());
        updateSpinnerValue(minEnergyCopulationField, parameters.energyToReproduce());
        updateSpinnerValue(animalStartEnergyField, parameters.animalStartEnergy());
        updateSpinnerValue(animalsSpawningStartField, parameters.initialNumberOfAnimals());
        updateSpinnerValue(grassSpawnedDayField, parameters.plantsPerDay());
        updateSpinnerValue(initialPlantsField, parameters.initialNumberOfPlants());
        updateSpinnerValue(parentEnergyGivenToChildField, parameters.parentEnergyGivenToChild());
        updateSpinnerValue(minMutationsField, parameters.minMutations());
        updateSpinnerValue(maxMutationsField, parameters.maxMutations());
        updateSpinnerValue(genomeLengthField, parameters.genomeLength());
    }

    private void updateSpinnerValue(Spinner<Integer> spinner, int value) {
        spinner.getValueFactory().setValue(value);
    }


    private void loadConfiguration(String configurationName) {
        List<SimulationConfiguration> configurations = loadConfigurationNames();
        for (SimulationConfiguration config : configurations) {
            if (config.getName().equals(configurationName)) {
                updateUIWithParameters(config.getParameters());
                break;
            }
        }
    }


    public void initializeSimulationWithParameters(SimulationParameters parameters) {
        Simulation simulation = new Simulation();

        WorldMap worldMap = createWorldMap(parameters);
        simulation.setWorldMap(worldMap);

        GrassGenerator grassGenerator = createGrassGenerator(parameters, worldMap, simulation::getCurrentDay);
        simulation.setGrassGenerator(grassGenerator);

        List<Animal> initialAnimals = createInitialAnimals(parameters, simulation);
        simulation.setInitialAnimals(initialAnimals);

        startSimulation(simulation);
    }

    private static WorldMap createWorldMap(SimulationParameters parameters) {
        MapField[][] mapFields = new MapField[parameters.mapWidth()][parameters.mapHeight()];
        for (int x = 0; x < parameters.mapWidth(); x++) {
            for (int y = 0; y < parameters.mapHeight(); y++) {
                mapFields[x][y] = new GrassMapField(new Vector2d(x, y));
            }
        }
        return new GlobeMap(mapFields);
    }

    private static GrassGenerator createGrassGenerator(SimulationParameters parameters, WorldMap worldMap, Supplier<Integer> getCurrentDay) {
        GrassGeneratorInfo growthInfo = new GrassGeneratorInfo(
                parameters.plantsPerDay(),
                parameters.plantEnergy(),
                parameters.initialNumberOfPlants()
        );

        return switch (parameters.plantGrowthVariant()) {
            case "Life-giving Carcasses" -> new DeadAnimalsGrassGenerator(growthInfo, worldMap, getCurrentDay);
            case "Equatorial Forests" -> new EquatorGrassGenerator(growthInfo, worldMap);
            default ->
                    throw new IllegalArgumentException("Unknown plant growth variant: " + parameters.plantGrowthVariant());
        };
    }

    private static List<Animal> createInitialAnimals(SimulationParameters parameters, Simulation simulation) {
        return AnimalFactory.createInitialAnimals(
                parameters,
                simulation::getCurrentDay
        );
    }

    private void startSimulation(Simulation simulation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulationMap.fxml"));
            StackPane root = loader.load();
            SimulationPresenter simulationPresenter = loader.getController();
            simulationPresenter.initializeSimulation(simulation);

            Stage stage = new Stage();
            stage.setTitle("Simulation Map");
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setHeaderText("Error while loading simulation map");
            alert.setContentText("Display for map could not be loaded. Please try again.");
            alert.show();
        }
    }
}

