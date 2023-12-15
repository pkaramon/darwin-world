package agh.ics.oop;

import agh.ics.oop.model.FileMapDisplay;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();

        GrassField grassField = new GrassField(7);
        grassField.addListener(presenter);
        presenter.setWorldMap(grassField);
        grassField.addListener((worldMap, message) -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    System.out.println(LocalDateTime.now().format(formatter) + " " + message);
                }
        );
        grassField.addListener(new FileMapDisplay());


        configureStage(primaryStage, viewRoot);

        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
    }
}
