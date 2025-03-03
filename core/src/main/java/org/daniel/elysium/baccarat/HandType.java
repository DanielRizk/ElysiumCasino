package org.daniel.elysium.baccarat;

public enum HandType {
    UNDEFINED(0),
    BANKER(0.95),
    PLAYER(1),
    TIE(9);

    private final double value; // Store the associated value

    // Constructor to initialize the enum value
    HandType(double value) {
        this.value = value;
    }

    // Getter method to retrieve the value
    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name(); // Returns "BANKER", "PLAYER", or "TIE"
    }
}
