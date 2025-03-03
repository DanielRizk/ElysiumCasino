package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.baccarat.BacCard;
import org.daniel.elysium.models.cards.UICard;

/**
 * Represents a visual representation of a Baccarat card in the UI.
 * Each {@code UICard} is linked to a logical {@link BacCard} instance.
 */
public class BacCardUI extends UICard {
    /** The logical backend representation of the card. */
    private final BacCard card;

    /**
     * Constructs a {@code UICard} with a given rank, suit, and image asset.
     *
     * @param rank The rank of the card (e.g., "A", "2", "K").
     * @param suit The suit of the card (e.g., "♠", "♥", "♦", "♣").
     * @param icon The asset representing the card's face image.
     */
    public BacCardUI(String rank, String suit, Asset icon) {
        super(rank, suit, icon);
        card = new BacCard(rank, suit);
    }

    /**
     * Returns the rank of the card.
     *
     * @return The rank as a string.
     */
    public String getRank() {
        return card.getRank();
    }

    /**
     * Returns the suit of the card.
     *
     * @return The suit as a string.
     */
    public String getSuit() {
        return card.getSuit();
    }

    /**
     * Returns the logical backend representation of the card.
     *
     * @return The corresponding {@link BacCard} instance.
     */
    public BacCard getCard() {
        return card;
    }

    /**
     * Returns a string representation of the card in the format "RankSuit".
     *
     * @return The formatted string representing the card.
     */
    @Override
    public String toString() {
        return getRank() + getSuit();
    }
}