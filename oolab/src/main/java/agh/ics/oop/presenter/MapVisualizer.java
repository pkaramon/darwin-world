package agh.ics.oop.presenter;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.maps.MapField;
import agh.ics.oop.model.maps.WorldMap;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MapVisualizer {
    private WorldMap worldMap;
    private Function<Vector2d, Boolean> isPreferred;
    private Function<Animal, Boolean> shouldHighlight;
    private Animal watchedAnimal;

    public enum Shape {
        RECTANGLE,
        CIRCLE,
        STAR
    }
    public record Figure(Shape shape, Color color) {}

    public void update(WorldMap worldMap, Function<Vector2d, Boolean> isPreferred,
                         Function<Animal, Boolean> shouldHighlightAnimal,
                         Animal watchedAnimal) {
        this.worldMap = worldMap;
        this.isPreferred = isPreferred;
        this.shouldHighlight = shouldHighlightAnimal;
        this.watchedAnimal = watchedAnimal;

    }



    // second figure is drawn on top of the first one and so on
    public List<Figure> whatToDrawAtNormally(Vector2d position) {
        List<Figure> figures = new ArrayList<>();
        MapField field = worldMap.mapFieldAt(position);
        if(field == null) {
            return figures;
        }
        if(field.isGrassed()) {
            figures.add(new Figure(Shape.RECTANGLE, Color.GREEN));
        }

        List<Animal> animals = field.getOrderedAnimals();
        if(animals.isEmpty()) {
            return figures;
        }

        Animal dominantAnimal = animals.get(animals.size()-1);
        figures.add(new Figure(Shape.CIRCLE, getAnimalColor(dominantAnimal)));

        if (shouldHighlight.apply(dominantAnimal)) {
            figures.add(new Figure(Shape.STAR, Color.YELLOW));
        }
        if (Objects.equals(dominantAnimal, watchedAnimal)) {
            figures.add(new Figure(Shape.STAR, Color.RED));
        }
        return figures;
    }


    public Stream<Vector2d> positionStream() {
        return StreamSupport.stream(worldMap.spliterator(), false).map(MapField::getPosition);
    }

    public List<Figure> whatToDrawWhenShowingPreferredFields(Vector2d position) {
        List<Figure> figures = new ArrayList<>();
        if(isPreferred.apply(position)) {
            figures.add(new Figure(Shape.RECTANGLE, Color.LIGHTGREEN));
        }
        return figures;
    }

    public List<Figure> whatToDrawWhenShowingDominantGenotypeAnimals(Vector2d position) {
        List<Figure> figures = new ArrayList<>();
        MapField field = worldMap.mapFieldAt(position);
        if(field == null) {
            return figures;
        }
        List<Animal> animals = field.getOrderedAnimals();
        if(animals.isEmpty()) {
            return figures;
        }

        Animal dominantAnimal = animals.get(animals.size()-1);
        if(shouldHighlight.apply(dominantAnimal)) {
            figures.add(new Figure(Shape.CIRCLE, getAnimalColor(dominantAnimal)));
            figures.add(new Figure(Shape.STAR, Color.GOLD));
        }
        return figures;
    }


    private Color getAnimalColor(Animal animal) {
        double colorValue = Math.min(1, (double) animal.getEnergy() / 100);
        return Color.color(colorValue,0,colorValue);
    }

}
