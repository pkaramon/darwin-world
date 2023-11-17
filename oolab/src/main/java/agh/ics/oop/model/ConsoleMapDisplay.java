package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener{
    private int totalUpdates = 0;

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        System.out.println(message);
        System.out.println(worldMap);

        totalUpdates++;
        System.out.printf("Total updates: %d%n%n", totalUpdates);
    }
}
