package ua.ivanka.oop.lab1.io;

import ua.ivanka.oop.lab1.domain.Appliance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ApplianceCsvParser {
    private final ApplianceFactory applianceFactory;

    public ApplianceCsvParser(ApplianceFactory applianceFactory) {
        this.applianceFactory = Objects.requireNonNull(applianceFactory, "applianceFactory");
    }

    public List<Appliance> parse(Reader reader) throws IOException {
        Objects.requireNonNull(reader, "reader");

        List<Appliance> appliances = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                String[] parts = trimmed.split(",", -1);
                if (parts.length < 4) {
                    throw new IllegalArgumentException("Invalid CSV line " + lineNo + ": expected at least 4 columns");
                }

                String type = parts[0].trim();
                String id = parts[1].trim();
                String name = parts[2].trim();
                int powerWatts = parseInt(parts[3].trim(), "powerWatts", lineNo);
                String attr1 = parts.length > 4 ? parts[4].trim() : "";
                String attr2 = parts.length > 5 ? parts[5].trim() : "";

                appliances.add(applianceFactory.create(type, id, name, powerWatts, attr1, attr2));
            }
        }
        return appliances;
    }

    private static int parseInt(String raw, String fieldName, int lineNo) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid CSV line " + lineNo + ": " + fieldName + " must be present");
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid CSV line " + lineNo + ": " + fieldName + " must be integer", e);
        }
    }
}


