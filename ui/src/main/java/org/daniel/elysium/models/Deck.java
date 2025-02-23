package org.daniel.elysium.models;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.CardAsset;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck(){
        this.cards = generateDeck();
    }
    
    public List<Card> getCards(){
        return cards;
    }

    private List<Card> generateDeck() {
        List<Card> cards = new ArrayList<>();
        for (CardAsset card: CardAsset.values()){
            String name = card.name();
            String suit = name.substring(0, 1);
            String rank = name.substring(1);
            cards.add(new Card(rank, suit, AssetManager.getScaledIcon(card, 100, 150)));
        }
        return cards;
    }
}
