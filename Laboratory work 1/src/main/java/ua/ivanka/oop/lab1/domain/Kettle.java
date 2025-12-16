package ua.ivanka.oop.lab1.domain;

public final class Kettle extends Appliance {
    private final double capacityLiters;

    public Kettle(String id, String name, int ratedPowerWatts, double capacityLiters) {
        super(id, name, ratedPowerWatts);
        this.capacityLiters = validateCapacity(capacityLiters);
    }

    public double getCapacityLiters() {
        return capacityLiters;
    }

    @Override
    public String getType() {
        return "Kettle";
    }

    @Override
    public String describe() {
        return super.describe().replace("}", ", capacityLiters=" + capacityLiters + '}');
    }

    private static double validateCapacity(double capacityLiters) {
        if (capacityLiters <= 0.0) {
            throw new IllegalArgumentException("capacityLiters must be > 0");
        }
        return capacityLiters;
    }
}


