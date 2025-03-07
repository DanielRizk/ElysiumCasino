package org.daniel.elysium.baccarat.constants;

/**
 * Enumerates types of hands in Baccarat, each associated with a specific payout multiplier.
 * This enum is crucial for determining the payout calculations based on the type of bet placed.
 */
public enum BacHandType {
    /**
     * Represents an undefined hand type, used primarily as a default state.
     */
    UNDEFINED(0),

    /**
     * Represents the banker's hand. Bets on the banker hand are subject to a 5% commission,
     * hence the payout multiplier is 0.95.
     */
    BANKER(0.95),

    /**
     * Represents the player's hand. Bets on the player hand pay even money, thus the multiplier is 1.
     */
    PLAYER(1),

    /**
     * Represents a tie between the player and banker hands. Bets on a tie usually pay 8:1.
     */
    TIE(8);

    private final double value; // Payout multiplier associated with each hand type.

    /**
     * Constructs a HandType with the specified payout multiplier.
     * @param value The payout multiplier for this hand type.
     */
    BacHandType(double value) {
        this.value = value;
    }

    /**
     * Retrieves the payout multiplier for the hand type.
     * @return The payout multiplier.
     */
    public double getValue() {
        return value;
    }

    /**
     * Provides the name of the enum constant, which is the string representation of the hand type.
     * @return The name of the hand type.
     */
    @Override
    public String toString() {
        return name(); // Returns "BANKER", "PLAYER", or "TIE"
    }
}

