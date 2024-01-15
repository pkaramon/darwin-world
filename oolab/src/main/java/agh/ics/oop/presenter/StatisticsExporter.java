package agh.ics.oop.presenter;

import agh.ics.oop.simulations.SimulationStats;

import java.io.Closeable;

public interface StatisticsExporter extends Closeable {
    void export(SimulationStats statistics);
}
