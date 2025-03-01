package org.daniel.elysium.models;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.blackjack.models.BJCard;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a visual representation of a Blackjack card in the UI.
 * Each {@code UICard} is linked to a logical {@link BJCard} instance.
 */
public class UICard extends JLabel {

    /** The logical backend representation of the card. */
    private final BJCard card;

    /** The icon representing the front face of the card. */
    private final ImageIcon icon;

    /** The default dimensions for a card. */
    public static final Dimension defaultDimension = new Dimension(110, 150);

    /**
     * Constructs a {@code UICard} with a given rank, suit, and image asset.
     *
     * @param rank The rank of the card (e.g., "A", "2", "K").
     * @param suit The suit of the card (e.g., "♠", "♥", "♦", "♣").
     * @param icon The asset representing the card's face image.
     */
    public UICard(String rank, String suit, Asset icon) {
        // Create logical backend card
        this.card = new BJCard(rank, suit);
        this.icon = AssetManager.getScaledIcon(icon, defaultDimension);
        setIcon(this.icon);
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
     * Sets the card to be face up, displaying its front image.
     */
    public void setFaceUp() {
        setIcon(icon);
    }

    /**
     * Sets the card to be face down, displaying a generic back cover.
     */
    public void setFaceDown() {
        setIcon(AssetManager.getScaledIcon(CardAsset.BC, defaultDimension));
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
