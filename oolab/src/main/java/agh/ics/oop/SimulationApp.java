package agh.ics.oop;

import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.simulations.Simulation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("configurator.fxml"));
        BorderPane viewRoot = loader.load();

        SimulationPresenter presenter = loader.getController();
        Simulation simulation = new Simulation();
        simulation.setPresenter(presenter);
        presenter.initializeSimulation(simulation);

        configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        Scene scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation App");
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
    }
}
