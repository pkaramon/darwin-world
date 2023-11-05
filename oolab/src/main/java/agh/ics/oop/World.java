package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.Arrays;
import java.util.List;

public class World {
    public static void main(String[] options) {
        System.out.println("Start");
        List<MoveDirection> directions = OptionsParser.parse(Arrays.stream(options).toList());
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
        Simulation simulation = new Simulation(directions, positions, null);
        simulation.run();
        System.out.println("Stop");

    }

}
