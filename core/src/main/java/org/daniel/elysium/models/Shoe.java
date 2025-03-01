package org.daniel.elysium.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Represents a shoe containing multiple decks of cards.
 * A shoe is commonly used in card games like Blackjack to hold and shuffle multiple decks.
 *
 * @param <T> The type of cards stored in the shoe.
 */
public record Shoe<T>(List<T> cards) {

    /**
     * Creates a shoe containing the specified number of decks.
     * Decks are supplied using a {@link Supplier}, allowing different types of decks to be used.
     * The cards from all decks are combined and shuffled.
     *
     * @param numOfDecks   The number of decks to include in the shoe.
     * @param deckSupplier A supplier that provides instances of {@link Deck} to generate cards.
     * @return A new {@code Shoe} instance containing shuffled cards from multiple decks.
     */
    public static <T> Shoe<T> createShoe(int numOfDecks, Supplier<? extends Deck<T>> deckSupplier) {
        List<T> cards = new ArrayList<>();
        for (int i = 0; i < numOfDecks; i++) {
            Deck<T> deck = deckSupplier.get();
            cards.addAll(deck.getCards());
        }
        Collections.shuffle(cards);
        return new Shoe<>(cards);
    }
}
