package org.daniel.elysium.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Deck} implementation that uses single-letter codes
 * for suits (S, H, D, C) to represent Spades, Hearts, Diamonds, and Clubs.
 * <p>
 * This class creates a standard 52-card deck with ranks
 * {"A", "2", "3", ..., "10", "J", "Q", "K"}
 * and suits {"S", "H", "D", "C"}.
 */
public class LetterDeck extends Deck<Card> {

    /**
     * Generates a standard deck of 52 playing cards
     * where suits are denoted by letters (S, H, D, C).
     *
     * @return a list of {@link Card} objects representing
     *         a full deck of playing cards with letter-based suits
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

