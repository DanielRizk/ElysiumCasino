package org.daniel.elysium.models;

import org.daniel.elysium.assets.CardAsset;

import java.util.ArrayList;
import java.util.List;

public class UIDeck extends Deck<UICard> {

    @Override
    protected List<UICard> generateDeck() {
        List<UICard> cards = new ArrayList<>();
        for (CardAsset card: CardAsset.values()){
            if (card != CardAsset.BC){
                String name = card.name();
                String suit = name.substring(0, 1);
                String rank = name.substring(1);
                cards.add(new UICard(rank, suit, card));
            }
        }
        return cards;
    }
}
