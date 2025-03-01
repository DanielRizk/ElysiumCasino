package org.daniel.elysium.models;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.CardAsset;

import javax.swing.*;
import java.awt.*;

public class UICard extends JLabel{
    private final BJCard card;
    private final ImageIcon icon;
    public static final Dimension defaultDimension = new Dimension(110, 150);

    public UICard(String rank, String suit, Asset icon) {
        // Create logical backend card
        this.card = new BJCard(rank, suit);
        this.icon = AssetManager.getScaledIcon(icon, defaultDimension);
        setIcon(this.icon);
    }

    /** Get the rank of the card */
    public String getRank() {
        return card.getRank();
    }

    /** Get the suit of the card */
    public String getSuit() {
        return card.getSuit();
    }

    /** Get the card as the logical backend implementation */
    public BJCard getCard(){
        return card;
    }

    /** Set the card face up */
    public void setFaceUp(){
        setIcon(icon);
    }

    /** Set the card face down */
    public void setFaceDown(){
        setIcon(AssetManager.getScaledIcon(CardAsset.BC, defaultDimension));
    }

    @Override
    public String toString() {
        return getRank() + getSuit();
    }
}
