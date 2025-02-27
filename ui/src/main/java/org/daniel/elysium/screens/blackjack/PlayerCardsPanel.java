package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.models.UICard;

import javax.swing.*;
import java.awt.*;


public class PlayerCardsPanel extends JPanel {

    public PlayerCardsPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        setOpaque(false);
        setMinimumSize(new Dimension(getWidth(), 150));
    }

    public void addCard(UICard card){
        add(card);
    }
}
