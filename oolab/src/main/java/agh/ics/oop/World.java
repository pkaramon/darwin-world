package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

public class World {
    public static void main(String[] options) {
        System.out.println("Start");
        run(OptionsParser.parse(options));
        System.out.println("Stop");
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
