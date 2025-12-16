package ua.ivanka.oop.lab1.domain;

public final class Lamp extends Appliance {
    private final int brightnessLumens;

    public Lamp(String id, String name, int ratedPowerWatts, int brightnessLumens) {
        super(id, name, ratedPowerWatts);
        this.brightnessLumens = validateBrightness(brightnessLumens);
    }

    public int getBrightnessLumens() {
        return brightnessLumens;
    }

    @Override
    public String getType() {
        return "Lamp";
    }

    @Override
    public String describe() {
        return super.describe().replace("}", ", brightnessLumens=" + brightnessLumens + '}');
    }

    private static int validateBrightness(int brightnessLumens) {
        if (brightnessLumens <= 0) {
            throw new IllegalArgumentException("brightnessLumens must be > 0");
        }
        return brightnessLumens;
    }
}


