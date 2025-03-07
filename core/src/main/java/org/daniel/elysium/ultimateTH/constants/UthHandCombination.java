package org.daniel.elysium.ultimateTH.constants;

/**
 * Represents the possible hand combinations in Ultimate Texas Hold'em,
 * along with their corresponding payout multipliers.
 */
public enum UthHandCombination {

    /**
     * Undefined hand, typically used as a placeholder.
     */
    UNDEFINED(-2),

    /**
     * A high card hand, the lowest-ranking hand.
     */
    HIGH_CARD(-2),

    /**
     * A hand containing a single pair of matching cards.
     */
    PAIR(-1),

    /**
     * A hand containing two pairs of matching cards.
     */
    TWO_PAIR(-1),

    /**
     * A hand containing three of a kind (Trips).
     */
    TRIPS(0),

    /**
     * A hand consisting of five sequentially ranked cards of mixed suits.
     */
    STRAIGHT(1),

    /**
     * A hand consisting of five cards of the same suit, not in sequence.
     */
    FLUSH(1.5),

    /**
     * A hand consisting of three of a kind and a separate pair.
     */
    FULL_HOUSE(3),

    /**
     * A hand consisting of four cards of the same rank.
     */
    QUADS(10),

    /**
     * A straight where all five cards are of the same suit.
     */
    STRAIGHT_FLUSH(50),

    /**
     * The best possible hand: a straight flush with an Ace-high.
     */
    ROYAL_FLUSH(500);

    private final double value;

    /**
     * Constructs an {@code UthHandCombination} with its corresponding payout multiplier.
     *
     * @param value the multiplier associated with this hand combination
     */
    UthHandCombination(double value) {
        this.value = value;
    }

    /**
     * Retrieves the payout multiplier for this hand combination.
     *
     * @return the multiplier value
     */
    public double getValue() {
        return value;
    }

    /**
     * Returns the name of the hand combination as a string.
     *
     * @return the name of the hand combination
     */
    @Override
    public String toString() {
        return name();
    }
}
