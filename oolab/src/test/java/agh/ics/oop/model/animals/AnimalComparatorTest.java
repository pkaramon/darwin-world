package agh.ics.oop.model.animals;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Pose;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.genes.Genotype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AnimalComparatorTest {
    private AnimalData createAnimalData(int energy, int bornDay) {
        return new AnimalData(
                new Pose(new Vector2d(2, 2), MapDirection.NORTH),
                new Genotype(List.of(1, 2, 3, 4)), energy,
                bornDay
        );
    }


    private AnimalComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new AnimalComparator(()-> 1);
    }

    @Test
    void firstComparesByEnergy() {
        AnimalData a = createAnimalData(10, 0);
        AnimalData b = createAnimalData(20, 0);
        assertTrue(comparator.compare(a, b) < 0);
        assertTrue(comparator.compare(b, a) > 0);
    }

    @Test
    void secondComparesByAge() {
        AnimalData a = createAnimalData(10, 0);
        AnimalData b = createAnimalData(10, 1);
        assertTrue(comparator.compare(a, b) > 0);
        assertTrue(comparator.compare(b, a) < 0);
    }

    @Test
    void thirdComparesByAmountOfChildren() {
        AnimalData a = createAnimalData(10, 0);
        AnimalData b = createAnimalData(10, 0);

        b.addChild(createAnimalData(10, 0));

        assertTrue(comparator.compare(a, b) < 0);
        assertTrue(comparator.compare(b, a) > 0);
    }

    @Test
    void inCaseOfADrawItSelectsUsingPassedFunction() {
        Supplier<Integer> inCaseOfDraw = mock(Supplier.class);
        when(inCaseOfDraw.get()).thenReturn(1);
        AnimalComparator comparator = new AnimalComparator(inCaseOfDraw);
        AnimalData a = createAnimalData(10, 0);
        AnimalData b = createAnimalData(10, 0);

        assertTrue(comparator.compare(a, b) > 0);
        verify(inCaseOfDraw).get();
    }

}