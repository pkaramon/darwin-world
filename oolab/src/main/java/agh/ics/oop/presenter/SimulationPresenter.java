package agh.ics.oop.presenter;

import agh.ics.oop.*;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;


public class SimulationPresenter implements MapChangeListener {
    @FXML
    public Label moveInfoLabel;
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


    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            moveInfoLabel.setText(message);
            GridMapDrawer gridMapDrawer = new GridMapDrawer(mapGrid, worldMap);
            gridMapDrawer.draw();
        });
    }

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
        startSimulation();
    }

    private void startSimulation() {
    }

}

