package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.Arrays;
import java.util.List;

public class World {
    public static void main(String[] options) {
        System.out.println("Start");

        SimulationEngine simulationEngine = new SimulationEngine(getGrassFieldSimulations());
        simulationEngine.runAsync();

        try {
            simulationEngine.awaitSimulationEnd();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("End");
    }


    private static List<Simulation> getGrassFieldSimulations() {
        GrassField map1 = new GrassField(10);
        GrassField map2 = new GrassField(20);
        ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
        map1.addListener(consoleMapDisplay);
        map2.addListener(consoleMapDisplay);

        Simulation sim1 = new Simulation(
                tryToParseOptions("f b r l f f r r f f f f f f f".split(" ")),
                List.of(new Vector2d(2, 2), new Vector2d(3, 4)),
                map1
        );

        Simulation sim2 = new Simulation(
                OptionsParser.parse(
                        Arrays.stream("f b r l f f r r f f f f".split(" ")).toList()
                ),
                List.of(new Vector2d(0, 0), new Vector2d(1, 3)),
                map2
        );

        return List.of(sim1, sim2);
    }


    private static List<Simulation> getRectangularMapSimulations() {
        RectangularMap map1 = new RectangularMap(5, 5);
        RectangularMap map2 = new RectangularMap(4, 4);
        ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
        map1.addListener(consoleMapDisplay);
        map2.addListener(consoleMapDisplay);

        Simulation sim1 = new Simulation(
                tryToParseOptions("f b r l f f r r f f f f f f f".split(" ")),
                List.of(new Vector2d(2, 2), new Vector2d(3, 4)),
                map1
        );

        Simulation sim2 = new Simulation(
                tryToParseOptions("f b r l f f r r f f f f f f f".split(" ")),
                List.of(new Vector2d(0, 0), new Vector2d(1, 3)),
                map2
        );

        return List.of(sim1, sim2);
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
