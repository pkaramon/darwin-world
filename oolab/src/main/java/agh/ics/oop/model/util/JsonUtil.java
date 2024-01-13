package agh.ics.oop.model.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import agh.ics.oop.simulations.SimulationConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void serialize(List<SimulationConfiguration> configurations, String fileName) {
        try {
            objectMapper.writeValue(new File(fileName), configurations);
            System.out.println("Serialization successful.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<SimulationConfiguration> deserialize(String fileName) {
        try {
            System.out.println("Deserializing configurations from " + fileName);
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            CollectionType collectionType = typeFactory.constructCollectionType(ArrayList.class, SimulationConfiguration.class);

            List<SimulationConfiguration> configurations = objectMapper.readValue(new File(fileName), collectionType);
            System.out.println("Deserialization successful. Loaded " + configurations.size() + " configurations.");
            return configurations;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Deserialization failed.");
            return new ArrayList<>();
        }
    }

}
