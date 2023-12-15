package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.presenter.WorldElementBox;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class GridMapDrawer {
    private static final int CELL_WIDTH = 40;
    private static final int CELL_HEIGHT = 40;

    private final GridPane mapGrid;
    private final WorldMap map;

    public GridMapDrawer(GridPane mapGrid, WorldMap map) {
        this.mapGrid = mapGrid;
        this.map = map;
    }

    public void draw() {
        clearGrid();
        setCellsSizes();
        drawAxis();
        drawWorldElements();
    }


    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }


    private void setCellsSizes() {
        Boundary boundary = map.getCurrentBounds();
        int gridHeight = boundary.height() + 1;
        int gridWidth = boundary.width() + 1;
        for (int i = 0; i < gridHeight; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }
        for (int i = 0; i < gridWidth; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
    }


    private void drawAxis() {
        Boundary boundary = map.getCurrentBounds();
        addToMapGrid("y/x", 0, 0);
        for (int x = boundary.lowerLeft().getX(); x <= boundary.upperRight().getX(); x++) {
            addToMapGrid(Integer.toString(x), 1-boundary.lowerLeft().getX() + x, 0);
        }
        for (int y = boundary.upperRight().getY(); y >= boundary.lowerLeft().getY(); y--) {
            addToMapGrid(Integer.toString(y), 0, boundary.upperRight().getY() - y + 1);
        }
    }

    private void drawWorldElements() {
        Boundary boundary = map.getCurrentBounds();

        for (int x = boundary.lowerLeft().getX(); x <= boundary.upperRight().getX(); x++) {
            for (int y = boundary.lowerLeft().getY(); y <= boundary.upperRight().getY(); y++) {
                drawGridCell(x, y, boundary);
            }
        }

    }

    private void drawGridCell(int x, int y, Boundary boundary) {
        Node box = map
                .objectAt(new Vector2d(x, y))
                .map(worldElement -> (Node) new WorldElementBox(worldElement))
                .orElse(new VBox());
        addToMapGrid(box,
                1 - boundary.lowerLeft().getX() + x,
                1 + boundary.upperRight().getY() - y
        );
    }


    private void addToMapGrid(String text, int columnIndex, int rowIndex) {
        Label label = new Label(text);
        GridPane.setHalignment(label, HPos.CENTER);
        mapGrid.add(label, columnIndex, rowIndex );
    }

    private void addToMapGrid(Node node, int columnIndex, int rowIndex) {
        GridPane.setHalignment(node, HPos.CENTER);
        mapGrid.add(node, columnIndex, rowIndex );
    }
}

