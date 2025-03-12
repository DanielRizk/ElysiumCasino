package org.daniel.elysium.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A specialized {@link Deck} implementation that uses Unicode suit symbols
 * (♠, ♥, ♦, ♣) instead of letters like S, H, D, C.
 * <p>
 * This class creates a standard 52-card deck where ranks are
 * {"A", "2", "3", ..., "10", "J", "Q", "K"} and
 * suits are {"♠", "♥", "♦", "♣"}.
 */
public class SymbolicDeck extends Deck<Card> {

    /**
     * Generates a standard deck of 52 playing cards using
     * Unicode symbols for suits (♠, ♥, ♦, ♣).
     *
     * @return a list of {@link Card} objects representing
     *         a standard 52-card deck with symbolic suits
     */
    @Override
    protected List<Card> generateDeck() {
        List<String> suits = List.of("♠", "♥", "♦", "♣");
        List<String> ranks = List.of("A", "2", "3", "4", "5",
                "6", "7", "8", "9", "10",
                "J", "Q", "K");

        List<Card> cards = new ArrayList<>();
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }
        return cards;
    }
}

