package org.daniel.elysium.models;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.CardAsset;

import javax.swing.*;

public class UICard extends JLabel{
    private final Card card;
    private final ImageIcon icon;

    public UICard(String rank, String suit, Asset icon) {
        this.card = new Card(rank, suit);
        this.icon = AssetManager.getScaledIcon(icon, 110, 150);
        setIcon(this.icon);
    }

    public void setFaceUp(){
        setIcon(icon);
    }

    public void setFaceDown(){
        setIcon(AssetManager.getScaledIcon(CardAsset.BC, 110, 150));
    }

    public String getRank() {
        return card.getRank();
    }

    public String getSuit() {
        return card.getSuit();
    }

    public Card getCard(){
        return card;
    }

    @Override
    public String toString() {
        return getRank() + getSuit();
    }
}
