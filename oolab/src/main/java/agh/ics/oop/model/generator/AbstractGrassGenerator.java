package agh.ics.oop.model.generator;

import agh.ics.oop.model.maps.Boundary;
import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.RandomNumbersGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        Set<Vector2d> preferredPositions = getPreferredPositions();
        Set<Vector2d> normalPositions = getNormalNonGrassedPositions(preferredPositions);

        GrassQuantities quantities = getGrassQuantities(amountOfGrasses, preferredPositions, normalPositions);

        return Stream.concat(
                RandomNumbersGenerator.generateRandomSubset(
                        new ArrayList<>(preferredPositions), quantities.onPreferredRegion()
                ).stream(),
                RandomNumbersGenerator.generateRandomSubset(
                        new ArrayList<>(normalPositions), quantities.onNormalRegion()
                ).stream()
        )
                .map(position -> new Grass(position, info.grassEnergy()))
                .toList();
    }


    private static GrassQuantities getGrassQuantities(int amountOfGrasses,
                                                      Set<Vector2d> preferredPositions,
                                                      Set<Vector2d> normalPositions) {
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

    private record GrassQuantities(int onPreferredRegion, int onNormalRegion) {
    }


    protected abstract Set<Vector2d> getPreferredPositions();

    private Set<Vector2d> getNormalNonGrassedPositions(Set<Vector2d> preferred) {
        Boundary mapBoundary = worldMap.getBoundary();

        Set<Vector2d> nonGrassedPositions = new HashSet<>();
        for (int x = mapBoundary.lowerLeft().x(); x <= mapBoundary.upperRight().x(); x++) {
            for (int y = 0; y <= mapBoundary.upperRight().y(); y++) {
                Vector2d position = new Vector2d(x, y);
                if(!preferred.contains(position) && isNonGrassed(position)) {
                    nonGrassedPositions.add(position);
                }

            }
        }
        return nonGrassedPositions;
    }



    protected boolean isNonGrassed(Vector2d position) {
        MapField mapField = worldMap.mapFieldAt(position);
        return mapField != null && mapField.getGrass().isEmpty();
    }


}