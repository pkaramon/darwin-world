package agh.ics.oop.presenter;

import agh.ics.oop.simulations.SimulationState;
import agh.ics.oop.simulations.SimulationStatsCalculator;
import javafx.scene.layout.VBox;

public class SimulationChartsPresenter {

    private final StatsChart animalCountChart = new StatsChart("Animal Count", "#ff0000");
    private final StatsChart grassCountChart = new StatsChart("Grass Count", "#00ff00");
    private final StatsChart emptyFieldsChart = new StatsChart("Empty fields", "#0074d9");
    private final StatsChart averageEnergyChart = new StatsChart("Average Energy", "#0000ff");
    private final StatsChart averageLifeLengthChart = new StatsChart("Average Life Length", "#ff00ff");
    private final StatsChart averageChildrenCountChart = new StatsChart("Average Children Count", "#00ffff");

    public void updateCharts(SimulationState state, SimulationStatsCalculator statsCalculator) {
        int currentDay = state.currentDay();
        animalCountChart.update(statsCalculator.getNumberOfAnimalsAlive(), currentDay);
        grassCountChart.update(statsCalculator.getNumberOfGrassOnMap(), currentDay);
        emptyFieldsChart.update(statsCalculator.getNumberOfFreeFields(), currentDay);
        averageEnergyChart.update(statsCalculator.getAverageEnergy(), currentDay);
        averageLifeLengthChart.update(statsCalculator.getAverageLifetimeForDeadAnimals(), currentDay);
        averageChildrenCountChart.update(statsCalculator.getAverageNumberOfChildren(), currentDay);
    }

    public void putCharts(VBox leftInfoColumn, VBox rightInfoColumn) {
        setupLeftColumn(leftInfoColumn);
        setupRightColumn(rightInfoColumn);
    }

    private void setupLeftColumn(VBox leftInfoColumn) {
        leftInfoColumn.getChildren().addAll(
                animalCountChart.getNode(),
                grassCountChart.getNode(),
                emptyFieldsChart.getNode(),
                averageEnergyChart.getNode()
        );
    }

    private void setupRightColumn(VBox rightInfoColumn) {
        rightInfoColumn.getChildren().addAll(
                averageLifeLengthChart.getNode(),
                averageEnergyChart.getNode(),
                averageChildrenCountChart.getNode()
        );
    }
}
