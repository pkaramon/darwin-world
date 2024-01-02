package agh.ics.oop.model.generator;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.animals.Animal;
import org.junit.jupiter.api.Test;

import java.util.List;

import static agh.ics.oop.model.WordMapCreator.createWorldMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeadAnimalsGrassGeneratorTest {

    @Test
    void generateGeneratesCorrectNumberOfGrasses() {
        for (int height = 1; height <= 10; height++) {
            for (int width = 1; width <= 10; width++) {
                WorldMap map = createWorldMap(width, height);
                GrassGeneratorInfo info = new GrassGeneratorInfo(height*width/2, 10, height*width/2);
                GrassGenerator generator = new DeadAnimalsGrassGenerator(info, map, ()-> 0);

                List<Grass> initialGrass = generator.generateInitialGrass();
                List<Grass> dayGrass = generator.generateGrassForDay();

                assertEquals(height*width/2, initialGrass.size());
                assertEquals(height*width/2, dayGrass.size());
            }
        }
    }

    @Test
    void fillingUpTheMap() {
        int width = 10, height=10;
        WorldMap map = createWorldMap(width, height);
        GrassGeneratorInfo info = new GrassGeneratorInfo(1, 10, height*width-1);
        GrassGenerator generator = new DeadAnimalsGrassGenerator(info, map, ()-> 0);

        List<Grass> generatedGrass = generator.generateInitialGrass();
        assertEquals(height*width-1, generatedGrass.size());

        generatedGrass.forEach(map::addGrass);

        List<Grass> firstDayGrass = generator.generateGrassForDay();
        assertEquals(1, firstDayGrass.size());

        firstDayGrass.forEach(map::addGrass);

        List<Grass> secondDayGrass = generator.generateGrassForDay();
        assertTrue(secondDayGrass.isEmpty());
    }

    @Test
    void majorityShouldLandOnDeadAnimalPositions() {
        int width = 3, height=3;
        WorldMap map = createWorldMap(width, height);
        GrassGeneratorInfo info = new GrassGeneratorInfo(5, 10, 0);
        DeadAnimalsGrassGenerator generator = new DeadAnimalsGrassGenerator(info, map, ()-> 0);
        Animal a = mock(Animal.class);
        when(a.getPosition()).thenReturn(new Vector2d(0, 0));

        Animal b = mock(Animal.class);
        when(b.getPosition()).thenReturn(new Vector2d(1, 1));

        Animal c = mock(Animal.class);
        when(c.getPosition()).thenReturn(new Vector2d(2, 2));

        Animal d = mock(Animal.class);
        when(d.getPosition()).thenReturn(new Vector2d(1, 2));


        generator.animalDied(map, a);
        generator.animalDied(map, b);
        generator.animalDied(map, c);
        generator.animalDied(map, d);


        List<Grass> grasses = generator.generateGrassForDay();
        List<Vector2d> positions = grasses.stream().map(Grass::getPosition).toList();

        assertEquals(5, grasses.size());
        assertTrue(positions.contains(new Vector2d(0, 0)));
        assertTrue(positions.contains(new Vector2d(1, 1)));
        assertTrue(positions.contains(new Vector2d(2, 2)));
        assertTrue(positions.contains(new Vector2d(1, 2)));
    }

}