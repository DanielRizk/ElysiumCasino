package org.daniel.elysium.models;

import javax.swing.*;

public class Card {
    private final String rank;
    private final String suit;
    private final ImageIcon icon;

    public Card(String rank, String suit, ImageIcon icon) {
        this.rank = rank;
        this.suit = suit;
        this.icon = icon;
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
