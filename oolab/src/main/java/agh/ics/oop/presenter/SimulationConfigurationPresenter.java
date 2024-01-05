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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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

    private SimulationParameters parameters;

    @FXML
    public void initialize() {
        setupSpinner(mapHeightField, 1, 1000, 100);
        setupSpinner(maxWidthField, 1, 1000, 100);
        setupSpinner(jungleWidthField, 1, 1000, 1);
        setupSpinner(jungleHeightField, 1, 1000, 1);
        setupSpinner(grassEnergyProfitField, 1, 1000, 10);
        setupSpinner(minEnergyCopulationField, 1, 1000, 20);
        setupSpinner(animalStartEnergyField, 1, 1000, 100);
        setupSpinner(dailyEnergyCostField, 1, 1000, 1);
        setupSpinner(animalsSpawningStartField, 1, 1000, 2);
        setupSpinner(grassSpawnedDayField, 1, 1000, 20);
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
        int mapHeight = mapHeightField.getValue();
        int maxWidth = maxWidthField.getValue();
        int grassEnergyProfit = grassEnergyProfitField.getValue();
        int minEnergyCopulation = minEnergyCopulationField.getValue();
        int animalStartEnergy = animalStartEnergyField.getValue();
        int animalsSpawningStart = animalsSpawningStartField.getValue();
        int grassSpawnedDay = grassSpawnedDayField.getValue();
        int numberOfGrassInitially = 5;
        String plantGrowthVariant = "random";
        String mutationVariant = "random";
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


        SimulationCanvas simulationCanvas = new SimulationCanvas(
                parameters.mapWidth(),
                parameters.mapHeight()
        );

        // dodaj ładowanie layout etc z .fxml
        Stage secondStage = new Stage();
        secondStage.setTitle("Second Window");
        StackPane secondRoot = new StackPane();
        secondRoot.getChildren().add(simulationCanvas);
        Scene secondScene = new Scene(secondRoot, 1000, 1000);
        secondStage.setScene(secondScene);
        secondStage.show();

            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    SimulationState state = simulation.run();
                    simulationCanvas.updateAndDraw(state); // Aktualizacja widoku symulacji
                    System.out.println(Thread.currentThread().getId());
                }
            }.start();

    }

}

