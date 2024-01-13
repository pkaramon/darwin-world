package agh.ics.oop.model.animals;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Pose;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.genes.Genotype;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnimalDataTest {
    private AnimalData createAnimalData() {
        return new AnimalData(
                new Pose(new Vector2d(2, 3), MapDirection.NORTH),
                new Genotype(List.of(2, 3, 5)),
                1
        );
    }


    @Test
    void getDescendantsCount_NoChildren_ReturnsZero() {
        AnimalData parent = createAnimalData();

        assertEquals(0, parent.getDescendantsCount());
    }

    @Test
    void getDescendantsCount_OnlyChildrenWithNoOffspringOnTheirOwn_ReturnsNumberOfChildren() {
        AnimalData parent = createAnimalData();
        AnimalData child1 = createAnimalData();
        AnimalData child2 = createAnimalData();

        parent.addChild(child1);
        parent.addChild(child2);

        assertEquals(2, parent.getDescendantsCount());
    }

    @Test
    void getDescendantsCount_ChildrenWithOffspringsOfTheirOwn() {
        AnimalData parent = createAnimalData();
        AnimalData child1 = createAnimalData();
        AnimalData child2 = createAnimalData();
        AnimalData grandchild1 = createAnimalData();
        AnimalData grandchild2 = createAnimalData();
        AnimalData grandchild3 = createAnimalData();

        parent.addChild(child1);
        parent.addChild(child2);
        child1.addChild(grandchild1);
        child1.addChild(grandchild2);
        child2.addChild(grandchild3);

        assertEquals(5, parent.getDescendantsCount());
    }


    @Test
    void getDescendentsCount_Incest(){
        AnimalData parent = createAnimalData();
        AnimalData child1 = createAnimalData();
        AnimalData child2 = createAnimalData();
        AnimalData grandchild1 = createAnimalData();
        AnimalData grandchild2 = createAnimalData();
        AnimalData grandchild3 = createAnimalData();

        parent.addChild(child1);
        parent.addChild(child2);
        child1.addChild(grandchild1);
        child1.addChild(grandchild2);
        child2.addChild(grandchild3);
        grandchild1.addChild(parent);
        grandchild2.addChild(child1);

        assertEquals(5, parent.getDescendantsCount());

    }

}