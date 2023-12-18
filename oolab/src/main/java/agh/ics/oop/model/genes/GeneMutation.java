package agh.ics.oop.model.genes;

import java.util.List;

public interface GeneMutation {
    List<Integer> mutate(List<Integer> genes);
}
