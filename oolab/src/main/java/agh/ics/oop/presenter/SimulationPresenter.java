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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    public TextField textField;
    @FXML
    public Label moveInfoLabel;
    @FXML
    public Button startButton;
    @FXML
    public GridPane mapGrid;

    private WorldMap worldMap;

    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            GridMapDrawer gridMapDrawer = new GridMapDrawer(mapGrid, worldMap);
            gridMapDrawer.draw();
            moveInfoLabel.setText(message);
        });

    }

    public void onSimulationStartClicked(ActionEvent actionEvent) {
        try {
            startSimulation();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void startSimulation() {
        GrassField map = new GrassField(10);
        map.addListener(this);
        map.addListener((worldMap, message) -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    System.out.println(LocalDateTime.now().format(formatter) + " " + message);
                }
        );

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

