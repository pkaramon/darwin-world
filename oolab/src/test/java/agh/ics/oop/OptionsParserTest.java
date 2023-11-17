package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    public void parse_InvalidOptions_ExceptionIsThrown() {
        List<String> options = List.of("f", "hello", "r", "abc");

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> OptionsParser.parse(options));

        assertEquals("hello is not legal move specification.", exception.getMessage());
    }
}