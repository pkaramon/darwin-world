package agh.ics.oop.simulations.configuration;

import java.util.ArrayList;
import java.util.List;

public enum GrassGrowthVariant {
    EQUATORIAL_FORESTS("Equatorial Forests"),
    LIFE_GIVING_CARCASSES("Life Giving Carcasses");

    private final String name;

    GrassGrowthVariant(String name) {
        this.name = name;
    }

    public static List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (GrassGrowthVariant variant : GrassGrowthVariant.values()) {
            names.add(variant.name);
        }
        return names;
    }

    @Override
    public String toString() {
        return name;
    }

    public static GrassGrowthVariant fromString(String name) {
        for (GrassGrowthVariant variant : GrassGrowthVariant.values()) {
            if (variant.name.equals(name)) {
                return variant;
            }
        }
        return EQUATORIAL_FORESTS;
    }
}
