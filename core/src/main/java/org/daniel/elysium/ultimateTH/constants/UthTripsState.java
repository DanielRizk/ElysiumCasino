package org.daniel.elysium.ultimateTH.constants;

public enum UthTripsState {
    UNDEFINED(0),
    LOST(0),
    Trips(3),
    STRAIGHT(4),
    FLUSH(7),
    FULL_HOUSE(8),
    QUADS(30),
    STRAIGHT_FLUSH(40),
    ROYAL_FLUSH(50);

    private final double value;

    UthTripsState(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name();
    }
}
