package agh.ics.oop.presenter;

import javafx.scene.layout.VBox;

public class SimulationChartsPresenter {

    private final StatsChart animalCountChart = new StatsChart("Animal Count", "#ff0000");
    private final StatsChart grassCountChart = new StatsChart("Grass Count", "#00ff00");
    private final StatsChart emptyFieldsChart = new StatsChart("Empty fields", "#0074d9");
    private final StatsChart averageEnergyChart = new StatsChart("Average Energy", "#0000ff");
    private final StatsChart averageLifeLengthChart = new StatsChart("Average Life Length", "#ff00ff");
    private final StatsChart averageChildrenCountChart = new StatsChart("Average Children Count", "#00ffff");

    public void updateCharts(int currentDay, SimulationStats stats) {
        animalCountChart.update(stats.aliveAnimals(), currentDay);
        grassCountChart.update(stats.grassFields(), currentDay);
        emptyFieldsChart.update(stats.emptyFields(), currentDay);
        averageEnergyChart.update(stats.averageEnergy(), currentDay);
        averageLifeLengthChart.update(stats.averageLifeLength(), currentDay);
        averageChildrenCountChart.update(stats.averageNumberOfChildren(), currentDay);
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
