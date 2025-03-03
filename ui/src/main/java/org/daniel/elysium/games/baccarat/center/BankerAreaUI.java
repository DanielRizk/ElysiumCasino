package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.models.cards.UICard;

import javax.swing.*;
import java.awt.*;

public class BankerAreaUI extends JPanel {
    private final BankerCardsUI cardsUI;

    public BankerAreaUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // Ensure components are centered
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setPreferredSize(new Dimension(500, 400));

        BankerTextUI textUI = new BankerTextUI();
        textUI.setAlignmentX(CENTER_ALIGNMENT);

        cardsUI = new BankerCardsUI();
        cardsUI.setAlignmentX(CENTER_ALIGNMENT);

        add(Box.createVerticalGlue()); // Push content towards center
        add(textUI);
        add(Box.createRigidArea(new Dimension(0, 30))); // Space between text & cards
        add(cardsUI);
        add(Box.createVerticalGlue()); // Push content towards center
    }

    public void addCard(UICard card){
        cardsUI.add(card);
    }

    public void removeCards(){
        cardsUI.removeAll();
    }
}
