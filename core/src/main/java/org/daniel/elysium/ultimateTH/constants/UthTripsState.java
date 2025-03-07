package org.daniel.elysium.ultimateTH.constants;

/**
 * Represents the possible states of a Trips side bet in Ultimate Texas Hold'em,
 * along with their corresponding payout multipliers.
 */
public enum UthTripsState {

    /**
     * The hand state is undefined, typically used as a placeholder before evaluation.
     */
    UNDEFINED(0),

    /**
     * The player has lost the Trips bet.
     */
    LOST(0),

    /**
     * The player has three of a kind (Trips).
     */
    TRIPS(3),

    /**
     * The player has a straight (five sequentially ranked cards of mixed suits).
     */
    STRAIGHT(4),

    /**
     * The player has a flush (five cards of the same suit, not in sequence).
     */
    FLUSH(7),

    /**
     * The player has a full house (three of a kind and a separate pair).
     */
    FULL_HOUSE(8),

    /**
     * The player has four of a kind (Quads).
     */
    QUADS(30),

    /**
     * The player has a straight flush (five sequential cards of the same suit).
     */
    STRAIGHT_FLUSH(40),

    /**
     * The player has a royal flush (A-K-Q-J-10 of the same suit).
     */
    ROYAL_FLUSH(50);

    private final double value;

    /**
     * Constructs an {@code UthTripsState} with its corresponding payout multiplier.
     *
     * @param value the multiplier associated with this Trips bet result
     */
    UthTripsState(double value) {
        this.value = value;
    }

    /**
     * Retrieves the payout multiplier for this Trips bet state.
     *
     * @return the multiplier value
     */
    public double getValue() {
        return value;
    }

    /**
     * Returns the name of the Trips hand state as a string.
     *
     * @return the name of the hand state
     */
    @Override
    public String toString() {
        return name();
    }
}

