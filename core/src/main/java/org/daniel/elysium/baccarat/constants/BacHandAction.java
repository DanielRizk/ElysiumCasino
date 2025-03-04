package org.daniel.elysium.baccarat.constants;

/**
 * Enumerates possible actions for a hand in Baccarat.
 * This enum is used to dictate the course of action a player or banker should take according to Baccarat rules.
 */
public enum BacHandAction {
    /**
     * Represents an undefined action, used as a default state before an action is determined.
     */
    UNDEFINED,

    /**
     * Represents the action to draw another card.
     */
    DRAW,

    /**
     * Represents the action to stand, indicating no further cards should be drawn.
     */
    STAND
}

