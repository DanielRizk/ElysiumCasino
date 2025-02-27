package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.blackjack.DealerHand;
import org.daniel.elysium.models.UICard;

import javax.swing.*;
import java.awt.*;

public class DealerHandUI extends JPanel {
    private final DealerHand hand;

    public DealerHandUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setPreferredSize(new Dimension(getWidth(), 150));
        setOpaque(false);

        hand = new DealerHand();
    }

    public DealerHand getHand(){
        return hand;
    }

    public boolean addCard(UICard uiCard){
        if (hand.dealCard(uiCard.getCard())){
            add(uiCard);
            return true;
        }
        return false;
    }

    public void flipCardUp(){
        ((UICard)getComponents()[1]).setFaceUp();
    }

    public void flipCardDown(){
        ((UICard)getComponents()[1]).setFaceDown();
    }
}
