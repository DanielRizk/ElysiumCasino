package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.models.cards.UICard;

import javax.swing.*;
import java.awt.*;

public class PlayerCardsUI extends JPanel {
    public PlayerCardsUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setOpaque(false);
        setPreferredSize(new Dimension(200, 150)); // Ensure size consistency

        add(new UICard("A", "S", CardAsset.SA));
        add(new UICard("A", "S", CardAsset.SA));
        add(new UICard("A", "S", CardAsset.SA));
    }
}
