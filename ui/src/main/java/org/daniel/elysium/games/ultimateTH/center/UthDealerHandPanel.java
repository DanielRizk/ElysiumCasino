package org.daniel.elysium.games.ultimateTH.center;

import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
import org.daniel.elysium.ultimateTH.model.UthHand;

import javax.swing.*;
import java.awt.*;

public class UthDealerHandPanel extends JPanel {
    private UthHand hand;

    public UthDealerHandPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setOpaque(false);

        Dimension size = new Dimension(120, 160);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);

        this.hand = new UthHand();
    }

    public UthHand getHand(){
        return this.hand;
    }

    public void addCard(UthCardUI uthCardUI){
        uthCardUI.setFaceDown();
        add(uthCardUI);
        hand.dealCard(uthCardUI.getCard());
    }

    public void exposeCards(){
        for (Component component : getComponents()){
            if (component instanceof UthCardUI cardUI){
                cardUI.setFaceUp();
            }
        }
    }

    public void removeCards(){
        removeAll();
        hand = new UthHand();
    }
}
