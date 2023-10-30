package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulationIntegrationTest {
    void assertAnimalState(Vector2d expectedPosition, MapDirection expectedOrientation, Animal animal) {
        assertTrue(animal.isAt(expectedPosition));
        assertTrue(animal.facesDirection(expectedOrientation));
    }

    private List<Animal> runSimulationWith(List<String> options, List<Vector2d> initialPositions) {
        Simulation s = new Simulation(OptionsParser.parse(options), initialPositions);
        s.run();
        return s.getAnimalsState();
    }

    @Test
    void run_BasicTest() {
        List<String> options = List.of(
                "f", "b",
                "r", "l",
                "f","f",
                "r", "r",
                "f", "f",
                "f", "f",
                "f", "f",
                "f", "f"
        );
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));

        List<Animal> finalState = runSimulationWith(options, positions);

        assertAnimalState(new Vector2d(3, 0), MapDirection.SOUTH, finalState.get(0));
        assertAnimalState(new Vector2d(2, 4), MapDirection.NORTH, finalState.get(1));
    }

    @Test
    void run_NoMoves() {
        List<Vector2d> initialPositions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
        List<String> options = List.of();

        List<Animal> finalState = runSimulationWith(options, initialPositions);

        assertAnimalState(new Vector2d(2, 2), MapDirection.NORTH, finalState.get(0));
        assertAnimalState(new Vector2d(3, 4), MapDirection.NORTH, finalState.get(1));
    }

    @Test
    void run_MovingAroundAndChangingDirections() {
        List<Vector2d> initialPositions = List.of(new Vector2d(0, 0), new Vector2d(2, 2));
        List<String> options = List.of(
                "f", "b",
                "r", "l",
                "f", "b"
        );

        List<Animal> finalState = runSimulationWith(options, initialPositions);

        assertAnimalState(new Vector2d(1, 1), MapDirection.EAST, finalState.get(0));
        assertAnimalState(new Vector2d(3, 1), MapDirection.WEST, finalState.get(1));
    }

    @Test
    void run_MovementsWithinBoard_SomeOptionsAreInvalid() {
        List<Vector2d> initialPositions = List.of(new Vector2d(3, 3), new Vector2d(0, 1));
        List<String> options = List.of(
                "b", "r",
                "hello", "world",
                "l", "r",
                "f", "f",
                "r", "asdfasfd"
        );

        List<Animal> finalState = runSimulationWith(options, initialPositions);

        assertAnimalState(new Vector2d(2, 2), MapDirection.NORTH, finalState.get(0));
        assertAnimalState(new Vector2d(0, 0), MapDirection.SOUTH, finalState.get(1));
    }


    @Test
    void run_TryingToEscapeFromBoardFromMoreVertices() {
        List<Vector2d> initialPositions = List.of(
                Animal.MAP_UPPER_RIGHT,
                Animal.MAP_LOWER_LEFT,
                new Vector2d(0, Animal.MAP_UPPER_RIGHT.getY()),
                new Vector2d(Animal.MAP_UPPER_RIGHT.getX(), 0)
        );
        List<String> options = List.of(
                "f", "b", "l", "r",
                "f", "b", "l", "r",
                "f", "b", "l", "r",
                "f", "b", "l", "r",
                "f", "b", "l", "r"
        );

        List<Animal> finalState = runSimulationWith(options, initialPositions);

        assertAnimalState(initialPositions.get(0), MapDirection.NORTH, finalState.get(0));
        assertAnimalState(initialPositions.get(1), MapDirection.NORTH, finalState.get(1));
        assertAnimalState(initialPositions.get(2), MapDirection.WEST, finalState.get(2));
        assertAnimalState(initialPositions.get(3), MapDirection.EAST, finalState.get(3));
    }

}