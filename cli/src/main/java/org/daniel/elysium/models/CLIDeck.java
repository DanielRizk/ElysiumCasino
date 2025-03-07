package org.daniel.elysium.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command-line interface (CLI) deck of playing cards.
 * <p>
 * This class extends {@code Deck<Card>} and generates a standard 52-card deck,
 * containing four suits (Spades, Hearts, Diamonds, Clubs) and thirteen ranks (Ace to King).
 * </p>
 */
public class CLIDeck extends Deck<Card> {

    /**
     * Generates a standard deck of 52 playing cards.
     *
     * @return a list of {@code Card} objects representing a full deck
     */
    @Override
    protected List<Card> generateDeck() {
        List<String> suits = List.of("S", "H", "D", "C"); // Spades, Hearts, Diamonds, Clubs
        List<String> ranks = List.of("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K");

        List<Card> cards = new ArrayList<>();
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }
        return cards;
    }
}

