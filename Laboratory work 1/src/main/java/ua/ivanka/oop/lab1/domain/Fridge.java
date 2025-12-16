package ua.ivanka.oop.lab1.domain;

public final class Fridge extends Appliance {
    private final int volumeLiters;
    private final boolean hasFreezer;

    public Fridge(String id, String name, int ratedPowerWatts, int volumeLiters, boolean hasFreezer) {
        super(id, name, ratedPowerWatts);
        this.volumeLiters = validateVolume(volumeLiters);
        this.hasFreezer = hasFreezer;
    }

    public int getVolumeLiters() {
        return volumeLiters;
    }

    public boolean hasFreezer() {
        return hasFreezer;
    }

    @Override
    public String getType() {
        return "Fridge";
    }

    @Override
    public String describe() {
        return super.describe().replace("}", ", volumeLiters=" + volumeLiters + ", hasFreezer=" + hasFreezer + '}');
    }

    private static int validateVolume(int volumeLiters) {
        if (volumeLiters <= 0) {
            throw new IllegalArgumentException("volumeLiters must be > 0");
        }
        return volumeLiters;
    }
}


