package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private List<Thread> currentThreads = new ArrayList<>();
    private ExecutorService threadPool;

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
    }

    public void runSync() {
        for (Simulation simulation : simulations) {
            simulation.run();
        }
    }

    public void runAsync() {
        currentThreads = simulations.stream().map(Thread::new).collect(Collectors.toList());
        for (Thread t : currentThreads) {
            t.start();
        }
    }

    public void awaitSimulationEnd() throws InterruptedException {
        for (Thread thread : currentThreads) {
            thread.join();
        }
        currentThreads.clear();

        if (threadPool == null) {
            return;
        }
        try {
            threadPool.shutdown();
            if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
    }

    public void runAsyncInThreadPool() {
        threadPool = Executors.newFixedThreadPool(4);
        for (Simulation simulation : simulations) {
            threadPool.submit(simulation);
        }
    }
}
