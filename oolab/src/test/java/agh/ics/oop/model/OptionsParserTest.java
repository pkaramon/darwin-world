package agh.ics.oop.model;

import agh.ics.oop.OptionsParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class OptionsParserTest {
    @Test
    public void parse_ValidOptions_ReturnsCorrespondingMoveDirections() {
        String[] options = {"f", "b", "r", "l"};
        MoveDirection[] expectedDirections = {MoveDirection.FORWARD, MoveDirection.BACKWARD, MoveDirection.RIGHT, MoveDirection.LEFT,};

        MoveDirection[] parsedDirections = OptionsParser.parse(options);

        assertArrayEquals(expectedDirections, parsedDirections);
    }

    @Test
    public void parse_InvalidOptions_InvalidOptionsAreDiscarded() {
        String[] options = {"f", "hello", "r", "abc"};
        MoveDirection[] expectedDirections = {MoveDirection.FORWARD, MoveDirection.RIGHT,};

        MoveDirection[] parsedDirections = OptionsParser.parse(options);

        assertArrayEquals(expectedDirections, parsedDirections);
    }
}