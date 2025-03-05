package org.daniel.elysium.baccarat.models;

import org.daniel.elysium.models.Card;
import org.daniel.elysium.models.CardValue;

/**
 * Represents a Baccarat-specific playing card.
 * This class extends {@link Card} and implements {@link CardValue} to determine card values for Baccarat.
 */
public class BacCard extends Card implements CardValue {

    /**
     * Constructs a Baccarat card with the specified rank and suit.
     *
     * @param rank The rank of the card (e.g., "A", "2", "K").
     * @param suit The suit of the card (e.g., "♠", "♥", "♦", "♣").
     */
    public BacCard(String rank, String suit) {
        super(rank, suit);
    }

    /**
     * Gets the Baccarat value of the card.
     * Face cards (K, Q, J) are worth 0, Aces are worth 1, and numeric cards have their face value.
     *
     * @return The Baccarat value of the card.
     */
    @Override
    public int getValue() {
        if (getRank().equals("A")) {
            return 1;
        } else if (getRank().equals("K") || getRank().equals("Q") || getRank().equals("J")) {
            return 0;
        } else {
            return Integer.parseInt(getRank());
        }
    }

    /**
     * Gets the value of a given card according to Baccarat rules.
     * This method is required by {@link CardValue}.
     *
     * @param card The card whose value is to be determined.
     * @return The Baccarat value of the card.
     */
    @Override
    public int getValue(Card card) {
        String rank = card.getRank();
        if (rank.equals("A")) {
            return 1;
        } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
            return 0;
        } else {
            return Integer.parseInt(rank);
        }
    }
}
