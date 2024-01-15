package agh.ics.oop.simulations.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SimulationConfigurationMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void serialize(List<SimulationConfiguration> configurations, String fileName) {
        List<SimulationConfiguration> uniqueConfigurations = removeDuplicatesKeepLastOccurrence(configurations);
        try {
            objectMapper.writeValue(new File(fileName), uniqueConfigurations);
        } catch (IOException e) {
            System.out.println("Could not serialize configurations " + e.getMessage());
        }
    }

    private static List<SimulationConfiguration> removeDuplicatesKeepLastOccurrence(List<SimulationConfiguration> configurations) {
        LinkedHashMap<String, SimulationConfiguration> map = new LinkedHashMap<>();
        for (SimulationConfiguration config : configurations) {
            map.put(config.getName(), config);
        }
        return new ArrayList<>(map.values());
    }

    public static List<SimulationConfiguration> deserialize(String filename) {
        File file = new File(filename);
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(file, new TypeReference<>(){});
        } catch (IOException e) {
            System.out.println("Error when reading configurations file " + e.getMessage());
            return createDefaultConfigurationList();
        }
    }

    private static List<SimulationConfiguration> createDefaultConfigurationList() {
        List<SimulationConfiguration> configurations = new ArrayList<>();
        configurations.add(defaultConfiguration);
        return configurations;
    }

    private static final SimulationConfiguration defaultConfiguration = new SimulationConfiguration(
            "Default",
            new SimulationParameters(
                    50,
                    50,
                    10,
                    3,
                    10,
                    GrassGrowthVariant.EQUATORIAL_FORESTS,
                    50,
                    30,
                    20,
                    10,
                    2,
                    5,
                    MutationVariant.FULL_RANDOMNESS,
                    10
            )
    );


}
