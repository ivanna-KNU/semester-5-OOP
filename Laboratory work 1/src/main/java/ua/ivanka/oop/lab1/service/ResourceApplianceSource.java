package ua.ivanka.oop.lab1.service;

import ua.ivanka.oop.lab1.domain.Appliance;
import ua.ivanka.oop.lab1.io.ApplianceCsvParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public final class ResourceApplianceSource implements ApplianceSource {
    private final ClassLoader classLoader;
    private final String csvResource;
    private final ApplianceCsvParser parser;

    public ResourceApplianceSource(ClassLoader classLoader, String csvResource, ApplianceCsvParser parser) {
        this.classLoader = Objects.requireNonNull(classLoader, "classLoader");
        this.csvResource = Objects.requireNonNull(csvResource, "csvResource");
        this.parser = Objects.requireNonNull(parser, "parser");
    }

    @Override
    public List<Appliance> loadAppliances() throws IOException {
        try (InputStream is = classLoader.getResourceAsStream(csvResource)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + csvResource);
            }
            try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                return parser.parse(reader);
            }
        }
    }
}


