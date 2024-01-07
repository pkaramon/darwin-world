package agh.ics.oop.model.generator;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.RandomNumbersGenerator;

import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractGrassGenerator implements GrassGenerator {
    private final GrassGeneratorInfo info;
    protected WorldMap worldMap;
    private static final double CHANCE_FOR_GROWTH_ON_PREFERRED_POSITION = 0.8;

    public AbstractGrassGenerator(GrassGeneratorInfo info, WorldMap worldMap) {
        this.info = info;
        this.worldMap = worldMap;
    }

    @Override
    public List<Grass> generateGrassForDay() {
        return tryToGenerateNGrasses(info.grassPerDay());
    }


    @Override
    public List<Grass> generateInitialGrass() {
        return tryToGenerateNGrasses(info.numberOfGrassInitially());
    }


    private List<Grass> tryToGenerateNGrasses(int amountOfGrasses) {
        GrassPositions grassPositions = getGrassPositions();
        List<Vector2d> preferred = grassPositions.preferred();
        List<Vector2d> normal = grassPositions.normal();


        GrassQuantities quantities = getGrassQuantities(amountOfGrasses, preferred, normal);

        return Stream.concat(
                RandomNumbersGenerator.generateRandomSubset(
                        preferred,
                        quantities.onPreferredRegion()
                ).stream(),
                RandomNumbersGenerator.generateRandomSubset(
                        normal,
                        quantities.onNormalRegion()
                ).stream()
        )
                .map(position -> new Grass(position, info.grassEnergy()))
                .toList();
    }


    private static GrassQuantities getGrassQuantities(int amountOfGrasses,
                                                      Collection<Vector2d> preferredPositions,
                                                      Collection<Vector2d> normalPositions) {
        if(preferredPositions.size() < normalPositions.size()) {
            int onPreferredRegion = Math.min(
                    (int) Math.ceil(amountOfGrasses * CHANCE_FOR_GROWTH_ON_PREFERRED_POSITION),
                    preferredPositions.size()
            );
            int onNormalRegion = Math.min(
                    amountOfGrasses - onPreferredRegion,
                    normalPositions.size()
            );
            return new GrassQuantities(onPreferredRegion, onNormalRegion);

        } else {
            int onNormalRegion = Math.min(
                    (int) Math.ceil(amountOfGrasses * (1-CHANCE_FOR_GROWTH_ON_PREFERRED_POSITION)),
                    normalPositions.size()
            );
            int onPreferredRegion = Math.min(
                    amountOfGrasses - onNormalRegion,
                    preferredPositions.size()
            );
            return new GrassQuantities(onPreferredRegion, onNormalRegion);
        }
    }



    abstract protected boolean isPreferredPosition(Vector2d position);

    private GrassPositions getGrassPositions() {
        List<Vector2d> normal = new ArrayList<>();
        List<Vector2d> preferred = new ArrayList<>();

        for (MapField field: worldMap) {
            Vector2d position = field.getPosition();
            if(isPreferredPosition(position)) {
                preferred.add(position);
            } else if (!field.isGrassed()) {
                normal.add(position);
            }
        }
        return new GrassPositions(preferred, normal);
    }

    private record GrassPositions(List<Vector2d> preferred, List<Vector2d> normal) {}
    private record GrassQuantities(int onPreferredRegion, int onNormalRegion) {}
}