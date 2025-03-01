package org.daniel.elysium.screens.blackjack.constants;

public enum GameActions {
    HIT, STAND, DOUBLE, SPLIT, INSURE, DO_NOT_INSURE;

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