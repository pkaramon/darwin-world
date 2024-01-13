package agh.ics.oop.presenter;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatsChart {
    private static final int CHART_DAYS_SPAN = 1000;

    private final XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
    private final NumberAxis xAxis;
    private VBox chartBox;

    public StatsChart(String label, String color) {
        xAxis = configureXAxis();
        NumberAxis yAxis = configureYAxis();
        LineChart<Number, Number> chart = configureChart(color, yAxis);
        configureChartBox(label, chart);
    }

    private void configureChartBox(String label, LineChart<Number, Number> chart) {
        chartBox = new VBox();
        chartBox.getChildren().add(new Label(label));
        chartBox.getChildren().add(chart);
        chartBox.setAlignment(Pos.CENTER);
    }

    private LineChart<Number, Number> configureChart(String color, NumberAxis yAxis) {
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setMaxWidth(300);
        chart.setMinWidth(300);
        chart.setMaxHeight(200);
        chart.setMinHeight(200);
        chart.setCreateSymbols(false);
        chart.getData().add(dataSeries);
        chart.setStyle("CHART_COLOR_1:" + color + ";");

        chart.setLegendVisible(false);
        return chart;
    }

    private static NumberAxis configureYAxis() {
        NumberAxis yAxis = new NumberAxis();
        yAxis.setVisible(false);
        yAxis.setAutoRanging(true);
        yAxis.setAnimated((false));
        return yAxis;
    }

    private NumberAxis configureXAxis() {
        final NumberAxis xAxis;
        xAxis = new NumberAxis();
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        xAxis.setAnimated(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);
        xAxis.setVisible(false);
        return xAxis;
    }

    public void update(double value, int currentDay) {
        dataSeries.getData().add(new XYChart.Data<>(currentDay, value));

        if (dataSeries.getData().size()> CHART_DAYS_SPAN) {
            dataSeries.getData().remove(0);
        }
    }

    public Node getNode() {
        return chartBox;
    }
}
