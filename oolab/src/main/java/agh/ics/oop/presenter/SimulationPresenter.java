package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.GridMapDrawer;
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

public class SimulationPresenter implements MapChangeListener {
    @FXML
    public TextField textField;
    @FXML
    public Label moveInfoLabel;
    @FXML
    public Button startButton;
    @FXML
    public GridPane mapGrid;


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

        String[] options = textField.getText().split(" ");

        List<Vector2d> initialPositions = List.of(new Vector2d(-3, 5), new Vector2d(3, 4));
        Simulation simulation = new Simulation(tryToParseOptions(options), initialPositions, map);

        SimulationEngine simulationEngine = new SimulationEngine(List.of(simulation));
        simulationEngine.runAsync();
    }

    private static List<MoveDirection> tryToParseOptions(String[] options) {
        try {
            return OptionsParser.parse(Arrays.stream(options).toList());
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid moves");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw e;
        }
    }

}

