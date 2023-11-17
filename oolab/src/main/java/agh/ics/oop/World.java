package agh.ics.oop;

import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.Arrays;
import java.util.List;

public class World {
    public static void main(String[] options) {
        System.out.println("Start");

        List<MoveDirection> directions = tryToParseOptions(options);
        GrassField gf = new GrassField(10);
        ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
        gf.addListener(consoleMapDisplay);

        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
        Simulation simulation = new Simulation(directions, positions,  gf);
        simulation.run();

        System.out.println("Stop");

    }

    private static List<MoveDirection> tryToParseOptions(String[] options) {
        try {
            return OptionsParser.parse(Arrays.stream(options).toList());
        } catch (IllegalArgumentException e) {
            System.out.printf("Could not parse options: %s%n", e.getMessage());
            System.exit(1);
            return null;
        }
    }
}
