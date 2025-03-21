package org.daniel.elysium.blackjack.models;

import org.daniel.elysium.models.Card;

/**
 * Represents a Blackjack-specific playing card.
 * This class extends {@link Card} for the basic card implementation.
 */
public class BJCard extends Card {

    /**
     * Constructs a Blackjack card with the specified rank and suit.
     *
     * @param rank The rank of the card (e.g., "A", "2", "K").
     * @param suit The suit of the card (e.g., "♠", "♥", "♦", "♣").
     */
    public BJCard(String rank, String suit) {
        super(rank, suit);
    }

    /**
     * Gets the Blackjack value of the card.
     * Face cards (K, Q, J) are worth 10, Aces are worth 11, and numeric cards have their face value.
     *
     * @return The Blackjack value of the card.
     */
    @Override
    public int getValue() {
        String rank = getRank();
        if (rank.equals("A")) {
            return 11;
        } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
            return 10;
        } else {
            return Integer.parseInt(rank);
        }
    }
}

