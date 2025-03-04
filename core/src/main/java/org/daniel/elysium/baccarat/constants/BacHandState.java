package org.daniel.elysium.baccarat.constants;

/**
 * Enumerates the possible states of a hand in Baccarat.
 * This enum defines the outcome of a hand after it has been evaluated against the rules of the game.
 */
public enum BacHandState {
    /**
     * Indicates that the hand's state has not yet been defined or the game round is still in progress.
     */
    UNDEFINED,

    /**
     * Indicates that the hand has won against the opposing hand.
     */
    WON,

    /**
     * Indicates that the hand has lost to the opposing hand.
     */
    LOST,

    /**
     * Indicates that the hand has tied with the opposing hand.
     */
    TIE
}

