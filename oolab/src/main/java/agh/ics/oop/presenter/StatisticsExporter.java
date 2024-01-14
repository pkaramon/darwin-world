package agh.ics.oop.presenter;

import java.io.Closeable;

public interface StatisticsExporter extends Closeable {
    void export(SimulationStats statistics);
}
