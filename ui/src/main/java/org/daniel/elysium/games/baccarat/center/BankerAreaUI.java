package org.daniel.elysium.games.baccarat.center;

import javax.swing.*;
import java.awt.*;

public class BankerAreaUI extends JPanel {
    public BankerAreaUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // Ensure components are centered
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setPreferredSize(new Dimension(500, 400));

        BankerTextUI textUI = new BankerTextUI();
        textUI.setAlignmentX(CENTER_ALIGNMENT);

        BankerCardsUI cardsUI = new BankerCardsUI();
        cardsUI.setAlignmentX(CENTER_ALIGNMENT);

        add(Box.createVerticalGlue()); // Push content towards center
        add(textUI);
        add(Box.createRigidArea(new Dimension(0, 30))); // Space between text & cards
        add(cardsUI);
        add(Box.createVerticalGlue()); // Push content towards center
    }
}
