package ua.ivanka.oop.lab1.domain;

public final class WashingMachine extends Appliance {
    private final double drumKg;
    private final boolean hasDryer;

    public WashingMachine(String id, String name, int ratedPowerWatts, double drumKg, boolean hasDryer) {
        super(id, name, ratedPowerWatts);
        this.drumKg = validateDrum(drumKg);
        this.hasDryer = hasDryer;
    }

    public double getDrumKg() {
        return drumKg;
    }

    public boolean hasDryer() {
        return hasDryer;
    }

    @Override
    public String getType() {
        return "WashingMachine";
    }

    @Override
    public String describe() {
        return super.describe().replace("}", ", drumKg=" + drumKg + ", hasDryer=" + hasDryer + '}');
    }

    private static double validateDrum(double drumKg) {
        if (drumKg <= 0.0) {
            throw new IllegalArgumentException("drumKg must be > 0");
        }
        return drumKg;
    }
}


