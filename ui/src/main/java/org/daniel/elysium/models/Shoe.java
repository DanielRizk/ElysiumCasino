package org.daniel.elysium.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shoe {
    public static List<Card> getShoe(int numOfDecks){
        Deck deck = new Deck();
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < numOfDecks; i++){
            cards.addAll(deck.getCards());
        }
        Collections.shuffle(cards);
        return cards;
    }
}
