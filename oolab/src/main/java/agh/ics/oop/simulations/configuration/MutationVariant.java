package agh.ics.oop.simulations.configuration;

import java.util.ArrayList;
import java.util.List;

public enum MutationVariant {
    FULL_RANDOMNESS("Full Randomness"),
    STEP_MUTATION("Step Mutation");

    private final String name;

    MutationVariant(String name) {
        this.name = name;
    }

    public static List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (MutationVariant variant : MutationVariant.values()) {
            names.add(variant.name);
        }
        return names;
    }

    @Override
    public String toString() {
        return name;
    }

    public static MutationVariant fromString(String name) {
        for (MutationVariant variant : MutationVariant.values()) {
            if (variant.name.equals(name)) {
                return variant;
            }
        }
        return FULL_RANDOMNESS;
    }
}
