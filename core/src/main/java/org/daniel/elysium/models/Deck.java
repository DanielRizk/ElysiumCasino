package org.daniel.elysium.models;

import java.util.List;

/**
 * Represents a generic deck of cards.
 * This class provides a base structure for creating different types of decks.
 *
 * @param <T> The type of cards stored in the deck.
 */
public abstract class Deck<T> {
    private final List<T> cards;

    /**
     * Constructs a deck and initializes it with generated cards.
     */
    public Deck() {
        this.cards = generateDeck();
    }

    /**
     * Retrieves the list of cards in the deck.
     *
     * @return A list containing all cards in the deck.
     */
    public List<T> getCards() {
        return cards;
    }

    /**
     * Generates and returns a list of cards for the deck.
     * This method must be implemented by subclasses to define the specific card structure.
     *
     * @return A list of cards representing the deck.
     */
    protected abstract List<T> generateDeck();
}

