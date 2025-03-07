package org.daniel.elysium.blackjack.constants;

/**
 * Represents the possible outcomes or states of a hand in a card game.
 */
public enum BJHandState {

    /**
     * The hand state is not yet determined.
     */
    UNDEFINED,

    /**
     * The hand has won the round.
     */
    WON,

    /**
     * The hand has lost the round.
     */
    LOST,

    /**
     * The hand resulted in a push (tie).
     */
    PUSH,

    /**
     * The hand has been insured against a dealer blackjack.
     */
    INSURED,

    /**
     * The hand has achieved a blackjack.
     */
    BLACKJACK
}

