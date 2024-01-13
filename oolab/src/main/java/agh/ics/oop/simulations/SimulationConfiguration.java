package agh.ics.oop.simulations;

public class SimulationConfiguration {
    private final String name;
    private final SimulationParameters parameters;

    public SimulationConfiguration(String name, SimulationParameters parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public SimulationParameters getParameters() {
        return parameters;
    }
}
