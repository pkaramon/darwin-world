package agh.ics.oop.presenter;

import agh.ics.oop.SimulationCanvas;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Pose;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animals.*;
import agh.ics.oop.model.generator.DeadAnimalsGrassGenerator;
import agh.ics.oop.model.generator.GrassGenerator;
import agh.ics.oop.model.generator.GrassGeneratorInfo;
import agh.ics.oop.model.genes.Genotype;
import agh.ics.oop.model.maps.GlobeMap;
import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.RandomNumbersGenerator;
import agh.ics.oop.simulations.Simulation;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;


public class SimulationPresenter {
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

    private List<Animal> createInitialAnimals(int mapWidth, int mapHeight, int animalCount, int startEnergy) {
        List<Animal> initialAnimals = new ArrayList<>();
        List<Integer> randomX = RandomNumbersGenerator.generate(animalCount, 0, mapWidth);
        List<Integer> randomY = RandomNumbersGenerator.generate(animalCount, 0, mapHeight);

        Genotype defaultGenotype = new Genotype(); // Przykładowy genotyp
        MapDirection defaultDirection = MapDirection.NORTH; // Przykładowa orientacja

        for (int i = 0; i < animalCount; i++) {
            Vector2d position = new Vector2d(randomX.get(i), randomY.get(i));
            Pose pose = new Pose(position, defaultDirection);
            AnimalData animalData = new AnimalData(pose, defaultGenotype, startEnergy);

            // Używamy domyślnych lub konfigurowalnych parametrów
            AnimalFeeder feeder = new AnimalFeeder(/* domyślne lub konfigurowalne parametry */);
            AnimalMover mover = new AnimalMover(/* domyślne lub konfigurowalne parametry */);
            AnimalCrosser crosser = new AnimalCrosser(/* domyślne lub konfigurowalne parametry */);

            initialAnimals.add(new Animal(animalData, feeder, mover, crosser));
        }

        return initialAnimals;
    }

    private Simulation simulation;
    @FXML
    private void onSimulationStartClicked() {
        int mapHeight = mapHeightField.getValue();
        int maxWidth = maxWidthField.getValue();
        int jungleWidth = jungleWidthField.getValue();
        int jungleHeight = jungleHeightField.getValue();
        int grassEnergyProfit = grassEnergyProfitField.getValue();
        int minEnergyCopulation = minEnergyCopulationField.getValue();
        int animalStartEnergy = animalStartEnergyField.getValue();
        int dailyEnergyCost = dailyEnergyCostField.getValue();
        int animalsSpawningStart = animalsSpawningStartField.getValue();
        int grassSpawnedDay = grassSpawnedDayField.getValue();
        int realRefreshTime = realRefreshTimeField.getValue();

        // Tworzenie mapy świata
        WorldMap worldMap = new GlobeMap(new MapField[maxWidth][mapHeight]);

        // Tworzenie generatora trawy
        GrassGeneratorInfo grassGeneratorInfo = new GrassGeneratorInfo(grassEnergyProfit);
        GrassGenerator grassGenerator = new DeadAnimalsGrassGenerator(grassGeneratorInfo, worldMap, simulation::getCurrentDay);

        int animalCount = animalsSpawningStartField.getValue();
        int startEnergy = animalStartEnergyField.getValue();

        List<Animal> initialAnimals = createInitialAnimals(maxWidth, mapHeight, animalCount, startEnergy);

        simulation = new Simulation();
        simulation.setWorldMap(worldMap);
        simulation.setGrassGenerator(grassGenerator);
        simulation.setInitialAnimals(initialAnimals);

        startSimulation();
    }

    private List<Animal> createInitialAnimals(/* parametry */) {
        List<Animal> initialAnimals = new ArrayList<>();
        return initialAnimals;
    }

    private void startSimulation() {
        // Tworzenie instancji SimulationCanvas
        SimulationCanvas simulationCanvas = new SimulationCanvas(
                maxWidthField.getValue(),
                mapHeightField.getValue(),
                simulation.getSimulationState(),
                simulation.getWorldMap()
        );

        // Dodanie SimulationCanvas do interfejsu użytkownika
        mapGrid.getChildren().add(simulationCanvas);

        // Uruchomienie pętli animacji
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                simulation.run(); // Aktualizacja stanu symulacji
                simulationCanvas.updateAndDraw(); // Rysowanie symulacji
            }
        }.start();
    }
}

