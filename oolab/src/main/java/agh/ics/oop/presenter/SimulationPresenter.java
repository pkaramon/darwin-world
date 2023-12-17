package agh.ics.oop.presenter;

import agh.ics.oop.*;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private TextField mapHeightField;
    @FXML
    private TextField maxWidthField;
    @FXML
    private TextField jungleWidthField;
    @FXML
    private TextField jungleHeightField;
    @FXML
    private TextField grassEnergyProfitField;
    @FXML
    private TextField minEnergyCopulationField;
    @FXML
    private TextField animalStartEnergyField;
    @FXML
    private TextField dailyEnergyCostField;
    @FXML
    private TextField animalsSpawningStartField;
    @FXML
    private TextField grassSpawnedDayField;
    @FXML
    private TextField realRefreshTimeField;

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
    private void onSimulationStartClicked() {
        String missingFields = getMissingFields();
        String invalidFields = getInvalidFields();

        if (!missingFields.isEmpty()) {
            showAlert("Missing Fields", "Please fill in the following fields:\n" + missingFields);
        } else if (!invalidFields.isEmpty()) {
            showAlert("Invalid Input", "Please enter valid numbers in the following fields:\n" + invalidFields);
        } else {
            startSimulation();
        }
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

    private boolean validateInputs() {
        try {
            int mapHeight = Integer.parseInt(mapHeightField.getText());
            int maxWidth = Integer.parseInt(maxWidthField.getText());
            int jungleWidth = Integer.parseInt(jungleWidthField.getText());
            int jungleHeight = Integer.parseInt(jungleHeightField.getText());
            int grassEnergyProfit = Integer.parseInt(grassEnergyProfitField.getText());
            int minEnergyCopulation = Integer.parseInt(minEnergyCopulationField.getText());
            int animalStartEnergy = Integer.parseInt(animalStartEnergyField.getText());
            int dailyEnergyCost = Integer.parseInt(dailyEnergyCostField.getText());
            int animalsSpawningStart = Integer.parseInt(animalsSpawningStartField.getText());
            int grassSpawnedDay = Integer.parseInt(grassSpawnedDayField.getText());
            int realRefreshTime = Integer.parseInt(realRefreshTimeField.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getMissingFields() {
        StringBuilder missingFields = new StringBuilder();

        if (mapHeightField.getText().isEmpty()) {
            missingFields.append("Map Height\n");
        }
        if (maxWidthField.getText().isEmpty()) {
            missingFields.append("Max Width\n");
        }
        if (jungleWidthField.getText().isEmpty()) {
            missingFields.append("Jungle Width\n");
        }
        if (jungleHeightField.getText().isEmpty()) {
            missingFields.append("Jungle Height\n");
        }
        if (grassEnergyProfitField.getText().isEmpty()) {
            missingFields.append("Grass Energy Profit\n");
        }
        if (minEnergyCopulationField.getText().isEmpty()) {
            missingFields.append("Minimum Energy to Copulation\n");
        }
        if (animalStartEnergyField.getText().isEmpty()) {
            missingFields.append("Animal Start Energy\n");
        }
        if (dailyEnergyCostField.getText().isEmpty()) {
            missingFields.append("Daily Energy Cost\n");
        }
        if (animalsSpawningStartField.getText().isEmpty()) {
            missingFields.append("Animals Spawning at Start\n");
        }
        if (grassSpawnedDayField.getText().isEmpty()) {
            missingFields.append("Grass Spawned Each Day\n");
        }
        if (realRefreshTimeField.getText().isEmpty()) {
            missingFields.append("Real Refresh Time (ms)\n");
        }

        return missingFields.toString();
    }

    private String getInvalidFields() {
        StringBuilder invalidFields = new StringBuilder();

        if (!isNumeric(mapHeightField.getText())) {
            invalidFields.append("Map Height\n");
        }
        if (!isNumeric(maxWidthField.getText())) {
            invalidFields.append("Max Width\n");
        }
        if (!isNumeric(jungleWidthField.getText())) {
            invalidFields.append("Jungle Width\n");
        }
        if (!isNumeric(jungleHeightField.getText())) {
            invalidFields.append("Jungle Height\n");
        }
        if (!isNumeric(grassEnergyProfitField.getText())) {
            invalidFields.append("Grass Energy Profit\n");
        }
        if (!isNumeric(minEnergyCopulationField.getText())) {
            invalidFields.append("Minimum Energy to Copulation\n");
        }
        if (!isNumeric(animalStartEnergyField.getText())) {
            invalidFields.append("Animal Start Energy\n");
        }
        if (!isNumeric(dailyEnergyCostField.getText())) {
            invalidFields.append("Daily Energy Cost\n");
        }
        if (!isNumeric(animalsSpawningStartField.getText())) {
            invalidFields.append("Animals Spawning at Start\n");
        }
        if (!isNumeric(grassSpawnedDayField.getText())) {
            invalidFields.append("Grass Spawned Each Day\n");
        }
        if (!isNumeric(realRefreshTimeField.getText())) {
            invalidFields.append("Real Refresh Time (ms)\n");
        }

        return invalidFields.toString();
    }
    private boolean isNumeric(String text) {
        if (text == null || text.isEmpty()) {
            return true; // Puste pola są obsługiwane przez getMissingFields
        }
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

