package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.MapDirection;


public class World {
    public static void main(String[] options) {
        System.out.println("Start");
        run(OptionsParser.parse(options));
        System.out.println("Stop");

        Vector2d position1 = new Vector2d(1,2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2,1);
        System.out.println(position2);
        System.out.println(position1.add(position2));


        MapDirection[] directions = {MapDirection.NORTH, MapDirection.EAST, MapDirection.SOUTH, MapDirection.WEST};
        for (var dir: directions) {
            System.out.printf("next %s = %s\n", dir, dir.next());
        }
        System.out.println();

        for (var dir: directions) {
            System.out.printf("previous %s = %s\n", dir, dir.previous());
        }
        System.out.println();

        for (var dir: directions) {
            System.out.printf("unit vector for %s = %s\n", dir, dir.toUnitVector());
        }
    }

    private static void run(MoveDirection[] directions) {
        for (MoveDirection dir : directions) {
            String message = switch (dir) {
                case FORWARD -> "Zwierzak idzie do przodu";
                case BACKWARD -> "Zwierzak idzie do tyłu";
                case RIGHT -> "Zwierzak skręca w prawo";
                case LEFT -> "Zwierzak skręca w lewo";
            };
            System.out.println(message);
        }
    }
}
