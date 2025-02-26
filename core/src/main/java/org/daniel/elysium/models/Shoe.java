package org.daniel.elysium.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class Shoe<T> {
    private final List<T> cards;

    public Shoe(List<T> cards) {
        this.cards = cards;
    }

    public List<T> getCards() {
        return cards;
    }

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
