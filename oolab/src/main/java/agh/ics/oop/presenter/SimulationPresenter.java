package agh.ics.oop.presenter;

import agh.ics.oop.*;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    public TextField textField;
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

    private WorldMap worldMap;

    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
    }
    private final ConcurrentHashMap<WorldElement, WorldElementBox> boxes = new ConcurrentHashMap<>();

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
        String[] options = textField.getText().split(" ");

        List<Vector2d> initialPositions = List.of(new Vector2d(-3, 5), new Vector2d(3, 4));
        Simulation simulation = new Simulation(tryToParseOptions(options), initialPositions, worldMap);

        SimulationEngine simulationEngine = new SimulationEngine(List.of(simulation));
        simulationEngine.runAsync();
        Platform.runLater(() -> startButton.setDisable(true));
    }

    private static List<MoveDirection> tryToParseOptions(String[] options) {
        try {
            return OptionsParser.parse(Arrays.stream(options).toList());
        } catch (IllegalArgumentException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid moves");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
            throw e;
        }
    }
}

