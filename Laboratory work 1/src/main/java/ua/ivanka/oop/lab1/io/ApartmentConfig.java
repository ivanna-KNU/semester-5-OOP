package ua.ivanka.oop.lab1.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

public final class ApartmentConfig {
    private final Set<String> pluggedInIds;
    private final int minWatts;
    private final int maxWatts;

    public ApartmentConfig(Set<String> pluggedInIds, int minWatts, int maxWatts) {
        this.pluggedInIds = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(pluggedInIds, "pluggedInIds")));
        if (minWatts < 0 || maxWatts < 0 || minWatts > maxWatts) {
            throw new IllegalArgumentException("Invalid power range");
        }
        this.minWatts = minWatts;
        this.maxWatts = maxWatts;
    }

    public Set<String> getPluggedInIds() {
        return pluggedInIds;
    }

    public int getMinWatts() {
        return minWatts;
    }

    public int getMaxWatts() {
        return maxWatts;
    }

    public static ApartmentConfig fromResources(ClassLoader classLoader,
                                               String pluggedIdsPropertiesResource,
                                               String searchPropertiesResource) throws IOException {
        Objects.requireNonNull(classLoader, "classLoader");

        Set<String> ids = loadIds(classLoader, pluggedIdsPropertiesResource);
        PowerRange range = loadRange(classLoader, searchPropertiesResource);
        return new ApartmentConfig(ids, range.minWatts, range.maxWatts);
    }

    private static Set<String> loadIds(ClassLoader classLoader, String resource) throws IOException {
        Properties props = loadProperties(classLoader, resource);
        String raw = props.getProperty("ids", "").trim();
        if (raw.isEmpty()) {
            return Set.of();
        }
        String[] parts = raw.split(",");
        Set<String> ids = new HashSet<>();
        for (String p : parts) {
            String id = p.trim();
            if (!id.isEmpty()) {
                ids.add(id);
            }
        }
        return ids;
    }

    private static PowerRange loadRange(ClassLoader classLoader, String resource) throws IOException {
        Properties props = loadProperties(classLoader, resource);
        int min = parseInt(props.getProperty("minWatts"), "minWatts");
        int max = parseInt(props.getProperty("maxWatts"), "maxWatts");
        return new PowerRange(min, max);
    }

    private static Properties loadProperties(ClassLoader classLoader, String resource) throws IOException {
        Objects.requireNonNull(resource, "resource");
        try (InputStream is = classLoader.getResourceAsStream(resource)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + resource);
            }
            Properties properties = new Properties();
            properties.load(is);
            return properties;
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

    private static final class PowerRange {
        private final int minWatts;
        private final int maxWatts;

        private PowerRange(int minWatts, int maxWatts) {
            this.minWatts = minWatts;
            this.maxWatts = maxWatts;
        }
    }
}


