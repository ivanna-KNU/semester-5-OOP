package ua.ivanka.oop.lab1.io;

import ua.ivanka.oop.lab1.domain.Appliance;
import ua.ivanka.oop.lab1.domain.Fridge;
import ua.ivanka.oop.lab1.domain.Kettle;
import ua.ivanka.oop.lab1.domain.Lamp;
import ua.ivanka.oop.lab1.domain.WashingMachine;

public final class ApplianceFactory {
    public Appliance create(String type,
                            String id,
                            String name,
                            int ratedPowerWatts,
                            String attr1,
                            String attr2) {
        if (type == null) {
            throw new IllegalArgumentException("type must be non-null");
        }
        String normalizedType = type.trim().toLowerCase();
        switch (normalizedType) {
            case "kettle":
                return new Kettle(id, name, ratedPowerWatts, parseDouble(attr1, "capacityLiters"));
            case "fridge":
                return new Fridge(id, name, ratedPowerWatts, parseInt(attr1, "volumeLiters"), parseBoolean(attr2, "hasFreezer"));
            case "lamp":
                return new Lamp(id, name, ratedPowerWatts, parseInt(attr1, "brightnessLumens"));
            case "washingmachine":
            case "washing_machine":
            case "washing-machine":
                return new WashingMachine(id, name, ratedPowerWatts, parseDouble(attr1, "drumKg"), parseBoolean(attr2, "hasDryer"));
            default:
                throw new IllegalArgumentException("Unsupported appliance type: " + type);
        }
    }

    private static int parseInt(String raw, String fieldName) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must be present");
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be integer: " + raw, e);
        }
    }

    private static double parseDouble(String raw, String fieldName) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must be present");
        }
        try {
            return Double.parseDouble(raw.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be number: " + raw, e);
        }
    }

    private static boolean parseBoolean(String raw, String fieldName) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must be present");
        }
        String normalized = raw.trim().toLowerCase();
        if ("true".equals(normalized) || "false".equals(normalized)) {
            return Boolean.parseBoolean(normalized);
        }
        throw new IllegalArgumentException(fieldName + " must be true/false: " + raw);
    }
}


