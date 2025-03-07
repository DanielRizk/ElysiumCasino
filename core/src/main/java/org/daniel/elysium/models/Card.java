package org.daniel.elysium.models;

/**
 * Represents a standard playing card with a rank and suit.
 */
public class Card {
    private final String rank;
    private final String suit;

    /**
     * Constructs a card with the specified rank and suit.
     *
     * @param rank The rank of the card (e.g., "A", "2", "K").
     * @param suit The suit of the card (e.g., "♠", "♥", "♦", "♣").
     */
    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Returns the rank of the card.
     *
     * @return The rank as a string.
     */
    public String getRank() {
        return rank;
    }

    /**
     * Returns the suit of the card.
     *
     * @return The suit as a string.
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Returns the default value of the card.
     * This method should be overridden in subclasses for specific card games.
     *
     * @return The default value of the card (0 by default).
     */
    public int getValue() {
        return 0;
    }

    /**
     * Returns a string representation of the card in the format "RankSuit".
     *
     * @return The formatted string representing the card.
     */
    @Override
    public String toString() {
        return rank + suit;
    }
}

