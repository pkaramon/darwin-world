package agh.ics.oop.model;

import java.io.FileWriter;
import java.io.IOException;

public class FileMapDisplay implements MapChangeListener{
    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        String logFileName = "map_%s.log".formatted(worldMap.getId());

        try (FileWriter fileWriter = new FileWriter(logFileName, true )) {
            fileWriter.write(message + "\n");
            fileWriter.write(worldMap.toString());
        } catch (IOException e) {
            System.out.println("Could not write log to file: " + e.getMessage());
        }
    }
}
