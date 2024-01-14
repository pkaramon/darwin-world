package agh.ics.oop.model.util;

import com.fasterxml.jackson.core.type.TypeReference;
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

    public static List<SimulationConfiguration> deserialize(String filename) {
        File file = new File(filename);
        if (file.exists() && file.length() > 0) {
            // Odczytaj plik, jeśli istnieje i nie jest pusty
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(file, new TypeReference<List<SimulationConfiguration>>() {});
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }


}
