package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class OptionsParserTest {
    @Test
    public void parse_ValidOptions_ReturnsCorrespondingMoveDirections() {
        List<String> options = List.of("f", "b", "r", "l");
        List<MoveDirection> expectedDirections = List.of(MoveDirection.FORWARD,
                MoveDirection.BACKWARD,
                MoveDirection.RIGHT,
                MoveDirection.LEFT);

        List<MoveDirection> parsedDirections = OptionsParser.parse(options);

       assertIterableEquals(expectedDirections, parsedDirections);
    }

    @Test
    public void parse_InvalidOptions_InvalidOptionsAreDiscarded() {
        List<String> options = List.of("f", "hello", "r", "abc");
        List<MoveDirection> expectedDirections = List.of(MoveDirection.FORWARD, MoveDirection.RIGHT);

        List<MoveDirection> parsedDirections = OptionsParser.parse(options);

        assertIterableEquals(expectedDirections, parsedDirections);
    }
}