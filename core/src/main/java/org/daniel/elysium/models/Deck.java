package org.daniel.elysium.models;

import java.util.List;

public abstract class Deck <T> {
    private final List<T> cards;

    public Deck(){
        this.cards = generateDeck();
    }

    public List<T> getCards(){
        return cards;
    }

    protected abstract List<T> generateDeck();
}
