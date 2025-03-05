package org.daniel.elysium.ultimateTH.constants;

public enum UthHandCombination {
    UNDEFINED(-2),
    HIGH_CARD(-2),
    PAIR(-1),
    TWO_PAIR(-1),
    TRIPS(0),
    STRAIGHT(1),
    FLUSH(1.5),
    FULL_HOUSE(3),
    QUADS(10),
    STRAIGHT_FLUSH(50),
    ROYAL_FLUSH(500);


    private final double value;

    UthHandCombination(double value) {
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