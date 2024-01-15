package agh.ics.oop.presenter;

import agh.ics.oop.model.genes.Genotype;
import agh.ics.oop.simulations.SimulationStats;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVStatisticsExporter implements StatisticsExporter {
    private final File file;
    public CSVStatisticsExporter(String path) {
        file = new File(path);
        writeHeaders();
    }

    private void writeHeaders() {
        try(var writer = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write("day,aliveAnimals,emptyFields,dominantGenes,averageEnergy,averageLifeLength,averageNumberOfChildren\n");
        } catch (IOException e) {
            System.out.println("Error while writing to file");
        }
    }


    @Override
    public void export(SimulationStats statistics) {
        try(var writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(statistics.day() + "," +
                    statistics.aliveAnimals() + "," +
                    statistics.grassFields() + "," +
                    statistics.emptyFields() + "," +
                    printGenes(statistics.dominantGenotype()) + "," +
                    statistics.averageEnergy() + "," +
                    statistics.averageLifeLength() + "," +
                    statistics.averageNumberOfChildren() + "\n");
        } catch (IOException e) {
            System.out.println("Error while writing to file");
        }
    }

    private String printGenes(Genotype genotype) {
        return "[" + String.join(" ", genotype.getGenes().stream().map(Object::toString).toList()) + "]";
    }

    @Override
    public void close() {}
}
