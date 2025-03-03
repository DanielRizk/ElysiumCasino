package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.models.cards.UICard;

import javax.swing.*;
import java.awt.*;

public class BankerCardsUI extends JPanel {
    public BankerCardsUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setOpaque(false);
        setPreferredSize(new Dimension(200, 150));

        add(new UICard("K", "H", CardAsset.HK)); // Example Card
        add(new UICard("K", "H", CardAsset.HK)); // Example Card
        add(new UICard("K", "H", CardAsset.HK)); // Example Card
    }
}
