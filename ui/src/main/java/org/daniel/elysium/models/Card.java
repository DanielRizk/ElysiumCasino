package org.daniel.elysium.models;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;

import javax.swing.*;
import java.awt.*;

public class Card extends JLabel{
    private final String rank;
    private final String suit;
    private final ImageIcon icon;

    public Card(String rank, String suit, Asset icon) {
        this.rank = rank;
        this.suit = suit;
        this.icon = AssetManager.getScaledIcon(icon, 110, 150);
        setIcon(this.icon);
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue(){
        return 0;
    }

    @Override
    public String toString() {
        return rank + suit;
    }
}
