package org.daniel.elysium.games.ultimateTH.constants;

import org.daniel.elysium.interfaces.GameActions;

/**
 * Enum representing the possible actions a player can take in Ultimate_TH.
 */
public enum UthActions implements GameActions {
    X4, X3, X2, X1, CHECK, FOLD;

    /**
     * Returns a formatted string representation of the action.
     *
     * @return A user-friendly string for the game action.
     */
    @Override
    public String toString() {
        return switch (this) {
            case X4 -> "x4";
            case X3 -> "x3";
            case X2 -> "x2";
            case X1 -> "x1";
            case CHECK -> "Check";
            case FOLD -> "Fold";
        };
    }
}
