package agh.ics.oop.presenter;

import agh.ics.oop.model.genes.Genotype;
import javafx.scene.Node;
import javafx.scene.control.ListView;

import java.util.List;

public class MostPopularGenotypesPresenter {
    private final ListView<String> genotypeList = new ListView<>();

    public MostPopularGenotypesPresenter() {
        this.genotypeList.setMaxHeight(100);
    }

    public void update(List<Genotype> mostPopularGenotypes) {
        genotypeList.getItems().clear();
        genotypeList.getItems().addAll(
                mostPopularGenotypes.stream()
                        .map(Genotype::getGenes)
                        .map(List::toString)
                        .toList()
        );
    }

    public Node getNode() {
        return genotypeList;
    }
}
