package ua.ivanka.oop.lab1.service;

import ua.ivanka.oop.lab1.domain.Appliance;

import java.io.IOException;
import java.util.List;

public interface ApplianceSource {
    List<Appliance> loadAppliances() throws IOException;
}


