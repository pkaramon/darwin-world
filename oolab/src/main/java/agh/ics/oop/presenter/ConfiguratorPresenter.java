package agh.ics.oop.presenter;

import agh.ics.oop.simulations.configuration.SimulationConfigurationMapper;
import agh.ics.oop.simulations.Simulation;
import agh.ics.oop.presenter.CSVStatisticsExporter;
import agh.ics.oop.simulations.configuration.SimulationConfiguration;
import agh.ics.oop.simulations.configuration.GrassGrowthVariant;
import agh.ics.oop.simulations.configuration.MutationVariant;
import agh.ics.oop.simulations.configuration.SimulationParameters;
import agh.ics.oop.simulations.creation.SimulationCreator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ConfiguratorPresenter {
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
    private CheckBox csvExportCheckbox;
    private List<SimulationConfiguration> configurations;

    @FXML
    public void initialize() {
        setupSpinners();
        SimulationConfiguration config = loadConfigurations();
        updateUIWithParameters(config.getParameters());
        setupComboBoxes();
    }

    private void setupComboBoxes() {
        plantGrowthVariantField.getItems().addAll(GrassGrowthVariant.getNames());
        plantGrowthVariantField.setValue(GrassGrowthVariant.EQUATORIAL_FORESTS.toString());
        mutationVariantField.getItems().addAll(MutationVariant.getNames());
        mutationVariantField.setValue(MutationVariant.FULL_RANDOMNESS.toString());

        configurationsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadConfiguration(newValue);
            }
        });

    }

    private void setupSpinners() {
        setupSpinner(mapHeightField, 1, 1000);
        setupSpinner(maxWidthField, 1, 1000);
        setupSpinner(grassEnergyProfitField, 0, 1000);
        setupSpinner(minEnergyCopulationField, 1, 1000);
        setupSpinner(animalStartEnergyField, 1, 1000);
        setupSpinner(animalsSpawningStartField, 0, 1000);
        setupSpinner(grassSpawnedDayField, 0, 1000);
        setupSpinner(initialPlantsField, 0, 100000);
        setupSpinner(parentEnergyGivenToChildField, 1, 1000);
        setupSpinner(minMutationsField, 0, 1000);
        setupSpinner(maxMutationsField, 0, 1000);
        setupSpinner(genomeLengthField, 1, 1000);
    }

    private void setupSpinner(Spinner<Integer> spinner, int min, int max) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, min));
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
                    spinner.getEditor().setText(String.valueOf(min));
                    spinner.getValueFactory().setValue(min);
                }
            }
        });
    }

    @FXML
    private void onSaveConfigurationClicked() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save configuration");
        dialog.setHeaderText("Input name of the configuration:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::saveConfiguration);
    }


    private void saveConfiguration(String configurationName) {
        for (SimulationConfiguration config : this.configurations) {
            if (config.getName().equals(configurationName)) {
                showAlert("Error when saving", "Configuration with name: '" + configurationName + "' already exists");
                return;
            }
        }

        SimulationParameters parameters = getCurrentSimulationParameters();
        SimulationConfiguration newConfig = new SimulationConfiguration(configurationName, parameters);
        this.configurations.add(newConfig);
        SimulationConfigurationMapper.serialize(this.configurations, "configurations.json");
        loadConfigurations();
        configurationsComboBox.setValue(configurationName);

        if (!configurationsComboBox.getItems().isEmpty()) {
            configurationsComboBox.setValue(configurationName);
        }
    }

    private SimulationConfiguration loadConfigurations() {
        this.configurations = SimulationConfigurationMapper.deserialize("configurations.json");
        configurationsComboBox.getItems().clear();
        for (SimulationConfiguration config : this.configurations) {
            configurationsComboBox.getItems().add(config.getName());
        }
        SimulationConfiguration defaultConfiguration = configurations.get(0);
        configurationsComboBox.setValue(defaultConfiguration.getName());
        return defaultConfiguration;
    }




    private SimulationParameters getCurrentSimulationParameters() {
        return new SimulationParameters(
                maxWidthField.getValue(),
                mapHeightField.getValue(),
                initialPlantsField.getValue(),
                grassEnergyProfitField.getValue(),
                grassSpawnedDayField.getValue(),
                GrassGrowthVariant.fromString(plantGrowthVariantField.getValue()),
                animalsSpawningStartField.getValue(),
                animalStartEnergyField.getValue(),
                minEnergyCopulationField.getValue(),
                parentEnergyGivenToChildField.getValue(),
                minMutationsField.getValue(),
                maxMutationsField.getValue(),
                MutationVariant.fromString(mutationVariantField.getValue()),
                genomeLengthField.getValue()
        );
    }
    @FXML
    private void onSimulationStartClicked() {
        SimulationParameters parameters = getCurrentSimulationParameters();

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

        SimulationCreator creator = new SimulationCreator(parameters);
        startSimulation(creator.create());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateUIWithParameters(SimulationParameters parameters) {
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
        this.configurations
                .stream()
                .filter(config-> config.getName().equals(configurationName))
                .findFirst()
                .ifPresent(config -> updateUIWithParameters(config.getParameters()));
    }

    private void startSimulation(Simulation simulation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulationMap.fxml"));
            StackPane root = loader.load();
            SimulationPresenter simulationPresenter = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Simulation Map");
            stage.setScene(new Scene(root, 1280, 900));

            simulationPresenter.setSimulation(simulation);
            simulationPresenter.setStage(stage);

            if (csvExportCheckbox.isSelected()) {
                simulationPresenter.setStatisticsExporter(new CSVStatisticsExporter("simulation.csv"));
            } else {
                simulationPresenter.setStatisticsExporter(new NullStatisticsExporter());
            }

            stage.show();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error while loading simulation map");
            alert.setContentText("Display for map could not be loaded. Please try again." + Arrays.toString(e.getStackTrace()));
            alert.show();
            throw new IllegalArgumentException(e) ;
        }
    }

//    @FXML
//    private void handleExportCSV() {
//        CSVStatisticsExporter exporter = new CSVStatisticsExporter("output.csv");
//        SimulationStats stats = getSimulationStats();
//        exporter.export(stats);
//        exporter.close();
//        System.out.println("Data exported to CSV successfully.");
//    }
//
//    private SimulationStats getSimulationStats() {
//
//    }
}

