package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.List;
import java.util.stream.Collectors;

public class OptionsParser {

    public static List<MoveDirection> parse(List<String> options) {
        return options.stream().map(OptionsParser::parseOption).collect(Collectors.toList());
    }

    private static MoveDirection parseOption(String option) {
        return switch (option) {
            case "f" -> MoveDirection.FORWARD;
            case "b" -> MoveDirection.BACKWARD;
            case "r" -> MoveDirection.RIGHT;
            case "l" -> MoveDirection.LEFT;
            default -> throw new IllegalArgumentException(option + " is not legal move specification.");
        };
    }
}
