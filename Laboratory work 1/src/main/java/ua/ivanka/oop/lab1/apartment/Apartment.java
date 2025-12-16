package ua.ivanka.oop.lab1.apartment;

import ua.ivanka.oop.lab1.domain.Appliance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Apartment {
    private final List<Appliance> appliances;
    private final Map<String, Appliance> applianceById;

    public Apartment(List<Appliance> appliances) {
        Objects.requireNonNull(appliances, "appliances");
        this.appliances = List.copyOf(appliances);
        this.applianceById = this.appliances.stream()
                .collect(Collectors.toMap(Appliance::getId, Function.identity(), (a, b) -> {
                    throw new IllegalArgumentException("Duplicate appliance id: " + a.getId());
                }));
    }

    public List<Appliance> getAppliances() {
        return new ArrayList<>(appliances);
    }

    public void plugIn(Set<String> applianceIds) {
        Objects.requireNonNull(applianceIds, "applianceIds");
        for (String id : new HashSet<>(applianceIds)) {
            if (id == null || id.trim().isEmpty()) {
                continue;
            }
            Appliance appliance = applianceById.get(id.trim());
            if (appliance == null) {
                throw new IllegalArgumentException("Unknown appliance id: " + id);
            }
            appliance.plugIn();
        }
    }

    public int getTotalPowerWatts() {
        int total = 0;
        for (Appliance appliance : appliances) {
            total += appliance.getCurrentPowerWatts();
        }
        return total;
    }

    public List<Appliance> getAppliancesSortedByRatedPower() {
        return appliances.stream()
                .sorted(Comparator
                        .comparingInt(Appliance::getRatedPowerWatts)
                        .thenComparing(Appliance::getName)
                        .thenComparing(Appliance::getId))
                .collect(Collectors.toList());
    }

    public List<Appliance> findByRatedPowerRange(int minWattsInclusive, int maxWattsInclusive) {
        if (minWattsInclusive < 0 || maxWattsInclusive < 0) {
            throw new IllegalArgumentException("Power range must be >= 0");
        }
        if (minWattsInclusive > maxWattsInclusive) {
            throw new IllegalArgumentException("minWattsInclusive must be <= maxWattsInclusive");
        }
        return appliances.stream()
                .filter(a -> a.getRatedPowerWatts() >= minWattsInclusive && a.getRatedPowerWatts() <= maxWattsInclusive)
                .collect(Collectors.toList());
    }
}


