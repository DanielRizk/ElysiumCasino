package org.daniel.elysium.games.blackjack.constants;

import org.daniel.elysium.interfaces.GameActions;

/**
 * Enum representing the possible actions a player can take in Blackjack.
 */
public enum BlackjackActions implements GameActions {
    HIT, STAND, DOUBLE, SPLIT, INSURE, DO_NOT_INSURE;

    /**
     * Returns a formatted string representation of the action.
     *
     * @return A user-friendly string for the game action.
     */
    @Override
    public String toString() {
        return switch (this) {
            case HIT -> "Hit";
            case STAND -> "Stand";
            case DOUBLE -> "Double";
            case SPLIT -> "Split";
            case INSURE -> "Insure";
            case DO_NOT_INSURE -> "Do not insure";
        };
    }
}
