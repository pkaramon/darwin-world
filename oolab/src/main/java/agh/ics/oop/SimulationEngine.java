package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private List<Thread> currentThreads = new ArrayList<>();

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
    }

    public void runSync() {
        for (Simulation sim : simulations) {
            sim.run();
        }
    }

    public void runAsync() {
        currentThreads = simulations.stream().map(Thread::new).toList();
        for (Thread t : currentThreads) {
            t.start();
        }
    }

    public void awaitSimulationEnd() throws InterruptedException {
        for(Thread thread: currentThreads) {
            thread.join();
        }
        currentThreads = new ArrayList<>();
    }
}
