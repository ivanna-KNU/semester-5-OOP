package ua.ivanka.oop.lab1.service;

import ua.ivanka.oop.lab1.apartment.Apartment;
import ua.ivanka.oop.lab1.domain.Appliance;

import java.util.List;
import java.util.Objects;

public final class ApartmentScenarioResult {
    private final Apartment apartment;
    private final int totalPowerWatts;
    private final List<Appliance> sortedByRatedPower;
    private final List<Appliance> foundInRange;

    public ApartmentScenarioResult(Apartment apartment,
                                  int totalPowerWatts,
                                  List<Appliance> sortedByRatedPower,
                                  List<Appliance> foundInRange) {
        this.apartment = Objects.requireNonNull(apartment, "apartment");
        this.totalPowerWatts = totalPowerWatts;
        this.sortedByRatedPower = List.copyOf(Objects.requireNonNull(sortedByRatedPower, "sortedByRatedPower"));
        this.foundInRange = List.copyOf(Objects.requireNonNull(foundInRange, "foundInRange"));
    }

    public Apartment getApartment() {
        return apartment;
    }

    public int getTotalPowerWatts() {
        return totalPowerWatts;
    }

    public List<Appliance> getSortedByRatedPower() {
        return sortedByRatedPower;
    }

    public List<Appliance> getFoundInRange() {
        return foundInRange;
    }
}


