package org.daniel.elysium.ultimateTH.model;

import org.daniel.elysium.models.Card;

/**
 * Represents an Ultimate_TH-specific playing card.
 * This class extends {@link Card} for the basic card implementation.
 */
public class UthCard extends Card {

    /**
     * Constructs an Ultimate_TH card with the specified rank and suit.
     *
     * @param rank The rank of the card (e.g., "A", "2", "K").
     * @param suit The suit of the card (e.g., "♠", "♥", "♦", "♣").
     */
    public UthCard(String rank, String suit) {
        super(rank, suit);
    }

    /**
     * Gets the Ultimate_TH value of the card.
     * Face cards (K, Q, J) are worth 11, 12, 13, Aces are worth 14, and numeric cards have their face value.
     *
     * @return The Ultimate_TH value of the card.
     */
    @Override
    public int getValue() {
        return switch (getRank()) {
            case "A" -> 14;
            case "K" -> 13;
            case "Q" -> 12;
            case "J" -> 11;
            case "10" -> 10;
            case "9" -> 9;
            case "8" -> 8;
            case "7" -> 7;
            case "6" -> 6;
            case "5" -> 5;
            case "4" -> 4;
            case "3" -> 3;
            case "2" -> 2;
            default -> throw new IllegalArgumentException("Invalid rank: " + getRank());
        };
    }
}
