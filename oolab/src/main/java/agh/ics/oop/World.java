package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.Arrays;
import java.util.List;

public class World {
    public static void main(String[] options) {
        System.out.println("Start");
        run(OptionsParser.parse(Arrays.stream(options).toList()));
        System.out.println("Stop");
    }

    private static void run(List<MoveDirection> directions) {
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
