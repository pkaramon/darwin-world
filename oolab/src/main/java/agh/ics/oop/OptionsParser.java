package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {

    public static List<MoveDirection> parse(List<String> options) {
        var directions = new ArrayList<MoveDirection>();

        for(String opt: options) {
            MoveDirection dir = parseOption(opt);
            if(dir != null) directions.add(dir);
        }

        return directions;
    }

    private static MoveDirection parseOption(String option) {
        return switch (option) {
            case "f" -> MoveDirection.FORWARD;
            case "b" -> MoveDirection.BACKWARD;
            case "r" -> MoveDirection.RIGHT;
            case "l" -> MoveDirection.LEFT;
            default -> null;
        };
    }
}
