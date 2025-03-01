package org.daniel.elysium.models;

import org.daniel.elysium.assets.CardAsset;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a deck of UI cards, generating a full deck of visible playing cards.
 * This class extends {@link Deck} with {@link UICard} objects.
 */
public class UIDeck extends Deck<UICard> {

    /**
     * Generates a standard deck of UI cards by iterating over all defined {@link CardAsset} values.
     * The back of the card ({@code CardAsset.BC}) is excluded.
     *
     * @return A list containing all {@link UICard} objects in the deck.
     */
    @Override
    protected List<UICard> generateDeck() {
        List<UICard> cards = new ArrayList<>();
        for (CardAsset card : CardAsset.values()) {
            if (card != CardAsset.BC) { // Exclude the card back
                String name = card.name();
                String suit = name.substring(0, 1);  // Extract suit (first character)
                String rank = name.substring(1);    // Extract rank (remaining characters)
                cards.add(new UICard(rank, suit, card));
            }
        }
        return cards;
    }
}
