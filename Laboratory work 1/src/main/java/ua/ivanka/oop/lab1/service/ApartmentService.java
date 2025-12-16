package ua.ivanka.oop.lab1.service;

import ua.ivanka.oop.lab1.apartment.Apartment;
import ua.ivanka.oop.lab1.domain.Appliance;
import ua.ivanka.oop.lab1.io.ApartmentConfig;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class ApartmentService {
    private final ApplianceSource applianceSource;

    public ApartmentService(ApplianceSource applianceSource) {
        this.applianceSource = Objects.requireNonNull(applianceSource, "applianceSource");
    }

    public Apartment buildApartment() throws IOException {
        List<Appliance> appliances = applianceSource.loadAppliances();
        return new Apartment(appliances);
    }

    public ApartmentScenarioResult runScenario(ApartmentConfig config) throws IOException {
        Objects.requireNonNull(config, "config");

        Apartment apartment = buildApartment();
        apartment.plugIn(config.getPluggedInIds());

        int totalPower = apartment.getTotalPowerWatts();
        List<Appliance> sorted = apartment.getAppliancesSortedByRatedPower();
        List<Appliance> found = apartment.findByRatedPowerRange(config.getMinWatts(), config.getMaxWatts());

        return new ApartmentScenarioResult(apartment, totalPower, sorted, found);
    }
}


