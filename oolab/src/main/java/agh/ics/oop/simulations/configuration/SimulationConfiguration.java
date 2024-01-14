package agh.ics.oop.simulations.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimulationConfiguration {
    private String name;
    private SimulationParameters parameters;

    @JsonCreator
    public SimulationConfiguration(@JsonProperty("name") String name,
                                   @JsonProperty("parameters") SimulationParameters parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimulationParameters getParameters() {
        return parameters;
    }

    public void setParameters(SimulationParameters parameters) {
        this.parameters = parameters;
    }
}
