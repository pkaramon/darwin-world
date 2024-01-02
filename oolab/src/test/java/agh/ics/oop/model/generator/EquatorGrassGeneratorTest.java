package agh.ics.oop.model.generator;

import agh.ics.oop.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static agh.ics.oop.model.WordMapCreator.createWorldMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EquatorGrassGeneratorTest {

    @Test
    void generatorGeneratesCorrectNumberOfGrasses() {
        for (int height = 1; height <= 10; height++) {
            for (int width = 1; width <= 10; width++) {
                WorldMap map = createWorldMap(width, height);
                GrassGeneratorInfo info = new GrassGeneratorInfo(height*width/2, 10, height*width/2);
                GrassGenerator generator = new EquatorGrassGenerator(info, map);

                List<Grass> initialGrass = generator.generateInitialGrass();
                List<Grass> dayGrass = generator.generateGrassForDay();

                assertEquals(height*width/2, initialGrass.size());
                assertEquals(height*width/2, dayGrass.size());
            }
        }
    }

    @Test
    void fillingUpTheMap() {
        int width = 10, height = 5;
        WorldMap map = createWorldMap(width, height);
        GrassGeneratorInfo info = new GrassGeneratorInfo(2, 10, height*width);
        GrassGenerator generator = new EquatorGrassGenerator(info, map);

        List<Grass> grass = generator.generateInitialGrass();
        grass.forEach(map::addGrass);


        List<Grass> dayGrass = generator.generateGrassForDay();

        // no space for new grass
        assertEquals(0, dayGrass.size());
    }

    @Test
    void majorityOfGrassLandsOnEquator() {
        int width = 10, height = 5;
        WorldMap map = createWorldMap(width, height);
        GrassGeneratorInfo info = new GrassGeneratorInfo(2, 10, 10);
        GrassGenerator generator = new EquatorGrassGenerator(info, map);

        List<Grass> grass = generator.generateInitialGrass();

        assertEquals(8, grass.stream().filter(g -> g.getPosition().getY() == 2).toList().size());
    }

}