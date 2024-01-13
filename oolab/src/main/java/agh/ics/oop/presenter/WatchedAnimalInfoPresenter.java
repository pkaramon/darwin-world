package agh.ics.oop.presenter;

import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.genes.Genotype;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

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


    public void updateWatchedAnimalInfo(Animal watchedAnimal, int currentDay) {
        if (watchedAnimal == null) {
            return;
        }

        watchedAnimalInfoTable.setItems(FXCollections.observableArrayList(
                new WatchedAnimalProperty("Status", watchedAnimal.isDead() ? "Dead" : "Alive"),
                new WatchedAnimalProperty("Genotype", prettyPrintGenotypeForWatchedAnimal(watchedAnimal.getGenotype())),
                new WatchedAnimalProperty("Energy", Integer.toString(watchedAnimal.getEnergy())),
                new WatchedAnimalProperty("Plants Eaten", Integer.toString(watchedAnimal.getPlantsEaten())),
                new WatchedAnimalProperty("Children", Integer.toString(watchedAnimal.getChildrenCount())),
                new WatchedAnimalProperty("Descendants", Integer.toString(watchedAnimal.getDescendantsCount()))
        ));

        if(watchedAnimal.isDead()) {
            watchedAnimalInfoTable.getItems().add(new WatchedAnimalProperty("Death day", Integer.toString(watchedAnimal.getDeathDay())));
        } else {
            watchedAnimalInfoTable.getItems().add(new WatchedAnimalProperty("Lifetime", Integer.toString(currentDay - watchedAnimal.getBirthDay())));
        }
    }

    private String prettyPrintGenotypeForWatchedAnimal(Genotype genotype) {
        List<Integer> genes = genotype.getGenes();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < genes.size(); i++) {
            if(i == genotype.getCurrentGeneIndex()) {
                sb.append("<").append(genes.get(i)).append(">");
            } else {
                sb.append(genes.get(i)).append(" ");
            }
        }
        return sb.toString();
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
