package ua.ivanka.oop.lab1.domain;

import java.util.Objects;

public abstract class Appliance {
    private final String id;
    private final String name;
    private final int ratedPowerWatts;
    private boolean pluggedIn;

    protected Appliance(String id, String name, int ratedPowerWatts) {
        this.id = validateId(id);
        this.name = validateName(name);
        this.ratedPowerWatts = validatePower(ratedPowerWatts);
        this.pluggedIn = false;
    }

    public final String getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final int getRatedPowerWatts() {
        return ratedPowerWatts;
    }

    public final boolean isPluggedIn() {
        return pluggedIn;
    }

    public void plugIn() {
        pluggedIn = true;
    }

    public void unplug() {
        pluggedIn = false;
    }

    public int getCurrentPowerWatts() {
        return pluggedIn ? ratedPowerWatts : 0;
    }

    public abstract String getType();

    public String describe() {
        return getType()
                + "{id='" + id + '\''
                + ", name='" + name + '\''
                + ", ratedPowerWatts=" + ratedPowerWatts
                + ", pluggedIn=" + pluggedIn
                + '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appliance)) {
            return false;
        }
        Appliance appliance = (Appliance) o;
        return id.equals(appliance.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    private static String validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id must be non-empty");
        }
        return id.trim();
    }

    private static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name must be non-empty");
        }
        return name.trim();
    }

    private static int validatePower(int ratedPowerWatts) {
        if (ratedPowerWatts <= 0) {
            throw new IllegalArgumentException("ratedPowerWatts must be > 0");
        }
        return ratedPowerWatts;
    }
}


