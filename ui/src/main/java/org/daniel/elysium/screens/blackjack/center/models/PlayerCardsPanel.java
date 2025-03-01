package org.daniel.elysium.screens.blackjack.center.models;

import org.daniel.elysium.models.UICard;
import org.daniel.elysium.uiUtils.GlowBorder;

import javax.swing.*;
import java.awt.*;


public class PlayerCardsPanel extends JPanel {

    public PlayerCardsPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        setOpaque(false);
        setMinimumSize(new Dimension(getWidth(), 150));
    }

    /** Add a card to player cards area */
    public void addCard(UICard card){
        add(card);
    }

    /** Toggle the highlight of the cards area */
    public void setHighlight(boolean glow) {
        Component[] components = getComponents();
        for (Component component : components) {
            if (glow) {
                ((UICard) component).setBorder(new GlowBorder(Color.YELLOW, 4));
            } else {
                ((UICard) component).setBorder(null);
            }
        }
        revalidate();
        repaint();
    }
}
