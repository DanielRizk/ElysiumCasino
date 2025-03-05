package org.daniel.elysium.games.ultimateTH.center;

import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
import org.daniel.elysium.ultimateTH.model.UthPlayerHand;

import javax.swing.*;
import java.awt.*;

public class UthPlayerHandPanel extends JPanel {
    private UthPlayerHand hand;

    public UthPlayerHandPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setOpaque(false);

        Dimension size = new Dimension(120, 160);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);

        this.hand = new UthPlayerHand();
    }

    public UthPlayerHand getHand() {
        return hand;
    }

    public void addCard(UthCardUI uthCardUI){
        add(uthCardUI);
        hand.dealCard(uthCardUI.getCard());
    }

    public void removeCards(){
        removeAll();
        hand = new UthPlayerHand();
    }
}
