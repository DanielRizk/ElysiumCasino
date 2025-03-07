package org.daniel.elysium.ultimateTH.constants;

/**
 * Represents the possible states of a player's hand in Ultimate Texas Hold'em.
 */
public enum UthHandState {

    /**
     * The hand state is undefined, typically used as a placeholder before evaluation.
     */
    UNDEFINED,

    /**
     * The player has lost the hand.
     */
    LOST,

    /**
     * The player has folded their hand.
     */
    FOLD,

    /**
     * The hand resulted in a tie (push) with the dealer.
     */
    TIE,

    /**
     * The player has won the hand.
     */
    WON
}

