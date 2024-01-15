package agh.ics.oop.presenter;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class WatchedAnimalInfoPresenter {
    private final TableView<WatchedAnimalProperty> watchedAnimalInfoTable = new TableView<>();


    public WatchedAnimalInfoPresenter() {
        setup();
    }

    private void setup(){
        setupPropertiesColumn();
        setupValuesColumn();
        setupTable();
    }

    private void setupValuesColumn() {
        TableColumn<WatchedAnimalProperty, String> values = new TableColumn<>("Value");
        values.setCellValueFactory(new PropertyValueFactory<>("value"));
        values.setPrefWidth(200);
        values.setSortable(false);
        values.setResizable(true);
        watchedAnimalInfoTable.getColumns().add(values);
    }

    private void setupPropertiesColumn() {
        TableColumn<WatchedAnimalProperty, String> properties = new TableColumn<>("Property");
        properties.setCellValueFactory(new PropertyValueFactory<>("name"));
        properties.setSortable(false);
        properties.setResizable(false);
        properties.setPrefWidth(100);
        watchedAnimalInfoTable.getColumns().add(properties);
    }

    private void setupTable() {
        watchedAnimalInfoTable.setPrefHeight(200);
        watchedAnimalInfoTable.setTableMenuButtonVisible(false);
    }



    public Node getNode() {
        return watchedAnimalInfoTable;
    }


    public void updateWatchedAnimalInfo(WatchedAnimalInfo animalInfo) {
        if (animalInfo == null) {
            return;
        }

        watchedAnimalInfoTable.setItems(FXCollections.observableArrayList(
                new WatchedAnimalProperty("Status", animalInfo.isDead() ? "Dead" : "Alive"),
                new WatchedAnimalProperty("Genotype", animalInfo.genotype()),
                new WatchedAnimalProperty("Energy", Integer.toString(animalInfo.energy())),
                new WatchedAnimalProperty("Plants Eaten", Integer.toString(animalInfo.plantsEaten())),
                new WatchedAnimalProperty("Children", Integer.toString(animalInfo.childrenCount())),
                new WatchedAnimalProperty("Descendants", Integer.toString(animalInfo.descendantsCount()))
        ));

        if(animalInfo.isDead()) {
            watchedAnimalInfoTable.getItems().add(new WatchedAnimalProperty("Death day", Integer.toString(animalInfo.deathDay())));
        } else {
            watchedAnimalInfoTable.getItems().add(new WatchedAnimalProperty("Lifetime", Integer.toString(animalInfo.lifeLength())));
        }
    }


    // used by javaFX, needs to be in this particular format
    @SuppressWarnings("unused")
    public static class WatchedAnimalProperty {
        private final String name;
        private final String value;

        public WatchedAnimalProperty(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
