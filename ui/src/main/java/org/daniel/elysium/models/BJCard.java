package org.daniel.elysium.models;

import org.daniel.elysium.assets.Asset;

import javax.swing.*;

public class BJCard extends Card{

    public BJCard(String rank, String suit, Asset icon) {
        super(rank, suit, icon);
    }

    @Override
    public int getValue() {
        if (getRank().equals("A")) {
            return 11;
        } else if (getRank().equals("K") || getRank().equals("Q") || getRank().equals("J")) {
            return 10;
        } else {
            return Integer.parseInt(getRank());
        }
    }
}
