package agh.ics.oop.model;

import agh.ics.oop.model.genes.GeneMutation;
import agh.ics.oop.model.genes.Genotype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalCrosserTest {
    private static final Pose defaultPose = new Pose(new Vector2d(2, 2), MapDirection.NORTHWEST);

    Animal father, mother;
    @BeforeEach
    void setUp() {
        father = new Animal(
                defaultPose,
                new Genotype(List.of(4, 5, 6, 7)),
                60
        );
        mother = new Animal(
                defaultPose,
                new Genotype(List.of(0, 1, 2, 3)),
                20
        );
    }

    @Test
    void cross_NotEnoughEnergy_NoChild() {
        AnimalMatingInfo info = new AnimalMatingInfo(
                50,
                10,
                (genes)-> genes, () -> true, () -> MapDirection.NORTH
        );
        AnimalCrosser crosser = new AnimalCrosser(info);

        Optional<Animal> child = crosser.cross(father, mother);
        assertTrue(child.isEmpty());
    }

    @Test
    void cross_EnoughEnergy_EnergyFromParentsIsGivenToChild() {
        AnimalMatingInfo info = new AnimalMatingInfo(
                20,
                10,
                (genes)-> genes, () -> true, () -> MapDirection.NORTH
        );
        AnimalCrosser crosser = new AnimalCrosser(info);

        Optional<Animal> child = crosser.cross(father, mother);
        assertTrue(child.isPresent());

        Animal offspring = child.get();
        assertEquals(20, offspring.getEnergy());
        assertEquals(50, father.getEnergy());
        assertEquals(10, mother.getEnergy());
    }

    @Test
    void cross_EnoughEnergyWeChooseLeftSideOfGenotypeForStrongerParent() {
        AnimalMatingInfo info = new AnimalMatingInfo(
                20,
                10,
                (genes)-> genes, () -> false, () -> MapDirection.NORTH
        );
        AnimalCrosser crosser = new AnimalCrosser(info);

        Optional<Animal> child = crosser.cross(father, mother);
        assertTrue(child.isPresent());

        Animal offspring = child.get();
        assertEquals(new Genotype(List.of(4,5,6,3)), offspring.getGenotype());
    }


    @Test
    void cross_EnoughEnergyWeChooseRightSideOfGenotypeForStrongerParent() {
        AnimalMatingInfo info = new AnimalMatingInfo(
                20,
                10,
                (genes)-> genes, () -> true, () -> MapDirection.NORTH
        );
        AnimalCrosser crosser = new AnimalCrosser(info);

        Optional<Animal> child = crosser.cross(father, mother);
        assertTrue(child.isPresent());

        Animal offspring = child.get();
        assertEquals(new Genotype(List.of(0,5,6,7)), offspring.getGenotype());
    }

    @Test
    void cross_EnoughEnergyWeChooseRightSideOfGenotypeForStrongerParentAndWeHaveUnevenDivision() {
        AnimalMatingInfo info = new AnimalMatingInfo(
                20,
                10,
                (genes)-> genes, () -> true, () -> MapDirection.NORTH
        );
        AnimalCrosser crosser = new AnimalCrosser(info);

        father = new Animal(
                defaultPose,
                new Genotype(List.of(4, 5, 6, 7, 8, 9)),
                60
        );
        mother = new Animal(
                defaultPose,
                new Genotype(List.of(10, 11, 12, 13, 14, 15)),
                20
        );

        Optional<Animal> child = crosser.cross(father, mother);
        assertTrue(child.isPresent());

        Animal offspring = child.get();
        assertEquals(new Genotype(List.of(10, 5,6,7,8,9)), offspring.getGenotype());
    }

    @Test
    void cross_EnoughEnergy_ChildHasTheSamePositionAsParent() {
        AnimalMatingInfo info = new AnimalMatingInfo(
                20,
                10,
                (genes)-> genes, () -> true, () -> MapDirection.NORTH
        );
        AnimalCrosser crosser = new AnimalCrosser(info);

        Optional<Animal> child = crosser.cross(father, mother);
        assertTrue(child.isPresent());

        Animal offspring = child.get();
        assertEquals(new Vector2d(2, 2), offspring.getPosition());
    }

    @Test
    void cross_EnoughEnergyStrongerParentRightSide_MutationIsApplied() {
        GeneMutation changeThirdTo123 = ((genes) -> {
            List<Integer> newGenes = new ArrayList<>(genes);
            newGenes.set(2, 123);
            return newGenes;
        });

        AnimalMatingInfo info = new AnimalMatingInfo(
                20,
                10,
                changeThirdTo123, () -> true, () -> MapDirection.NORTH
        );
        AnimalCrosser crosser = new AnimalCrosser(info);

        Optional<Animal> child = crosser.cross(father, mother);
        assertTrue(child.isPresent());

        Animal offspring = child.get();
        assertEquals(new Genotype(List.of(0,5,123,7)), offspring.getGenotype());
    }

    @Test
    void child_orientation_is_consulted() {
        AnimalMatingInfo info = new AnimalMatingInfo(
                20,
                10,
                (genes)-> genes, () -> true, () -> MapDirection.SOUTH
        );
        AnimalCrosser crosser = new AnimalCrosser(info);

        Optional<Animal> child = crosser.cross(father, mother);
        assertTrue(child.isPresent());

        Animal offspring = child.get();
        assertEquals(MapDirection.SOUTH, offspring.getOrientation());
    }


}