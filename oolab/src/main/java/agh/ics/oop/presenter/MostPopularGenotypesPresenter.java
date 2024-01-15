package agh.ics.oop.presenter;

import javafx.scene.Node;
import javafx.scene.control.ListView;

import java.util.List;

public class MostPopularGenotypesPresenter {
    private final ListView<String> genotypeList = new ListView<>();

    public MostPopularGenotypesPresenter() {
        this.genotypeList.setMaxHeight(100);
    }

    public void update(List<String> mostPopularGenotypes) {
        genotypeList.getItems().clear();
        genotypeList.getItems().addAll(mostPopularGenotypes);
    }

    public Node getNode() {
        return genotypeList;
    }
}
