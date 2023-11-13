package agh.ics.oop;

import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.List;

public class World {
    public static void main(String[] options) {
        System.out.println("Start");

        List<MoveDirection> directions = OptionsParser.parse(List.of("f b r l f f r r f f f f f f f f".split(" ")));
        GrassField gf = new GrassField(10);
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
        Simulation simulation = new Simulation(directions, positions,  gf);
        simulation.run();

        System.out.println(gf);
        System.out.println("Stop");

    }

}
