package org.daniel.elysium.games.blackjack.models;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.blackjack.models.BJCard;
import org.daniel.elysium.models.cards.UICard;

/**
 * Represents a visual representation of a Blackjack card in the UI.
 * Each {@code BJCardUI} is linked to a logical {@link BJCard} instance.
 */
public class BJCardUI extends UICard {

    /** The logical backend representation of a blackjack card. */
    private final BJCard card;

    /**
     * Constructs a {@code BJCardUI} with a given rank, suit, and image asset.
     *
     * @param rank The rank of the card (e.g., "A", "2", "K").
     * @param suit The suit of the card (e.g., "♠", "♥", "♦", "♣").
     * @param icon The asset representing the card's face image.
     */
    public BJCardUI(String rank, String suit, Asset icon) {
        super(rank, suit, icon);
        card = new BJCard(rank, suit);
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
     * @return The corresponding {@link BJCard} instance.
     */
    public BJCard getCard() {
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
