package org.daniel.elysium.screens.blackjack.center.models;

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

        // Create the logical backend hand
        hand = new DealerHand();
    }

    /** Get the logical backend hand implementation */
    public DealerHand getHand(){
        return hand;
    }

    /** Add a card to UI hand and backend hand, return true if possible, false if not */
    public boolean addCard(UICard uiCard){
        if (hand.canDealCard(uiCard.getCard())){
            hand.dealCard(uiCard.getCard());
            add(uiCard);
            return true;
        }
        return false;
    }

    /** API method to flip dealer's second card up,
     * by accessing the second component (UICard) and invoke the flip face up implementation
     */
    public void flipCardUp(){
        if (getComponents().length > 1){
            ((UICard)getComponents()[1]).setFaceUp();
        }
    }

    /** API method to flip dealer's second card down,
     * by accessing the second component (UICard) and invoke the flip face down implementation
     */
    public void flipCardDown(){
        if (getComponents().length > 1){
            ((UICard)getComponents()[1]).setFaceDown();
        }
    }
}
