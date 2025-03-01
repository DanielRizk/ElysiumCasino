package org.daniel.elysium.games.blackjack.center.models;

import org.daniel.elysium.blackjack.models.DealerHand;
import org.daniel.elysium.models.UICard;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the dealer's hand in the Blackjack UI.
 * This class manages both the visual representation of the dealer's cards
 * and the backend logic through {@link DealerHand}.
 */
public class DealerHandUI extends JPanel {
    private final DealerHand hand;

    /**
     * Constructs a new dealer hand UI panel.
     * Sets up the layout and initializes the backend dealer hand logic.
     */
    public DealerHandUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setPreferredSize(new Dimension(getWidth(), 150));
        setOpaque(false);

        // Create the logical backend hand
        hand = new DealerHand();
    }

    /**
     * Retrieves the backend hand implementation of the dealer.
     *
     * @return The {@link DealerHand} instance managing game logic.
     */
    public DealerHand getHand() {
        return hand;
    }

    /**
     * Adds a card to both the UI and backend dealer hand.
     *
     * @param uiCard The UI card representation to be added.
     * @return {@code true} if the card was successfully added, {@code false} otherwise.
     */
    public boolean addCard(UICard uiCard) {
        if (hand.canDealCard(uiCard.getCard())) {
            hand.dealCard(uiCard.getCard());
            add(uiCard);
            return true;
        }
        return false;
    }

    /**
     * Flips the dealer's second card face up, revealing its value.
     * This method assumes the second card (index 1) is the face-down card.
     */
    public void flipCardUp() {
        if (getComponents().length > 1) {
            ((UICard) getComponents()[1]).setFaceUp();
        }
    }

    /**
     * Flips the dealer's second card face down, hiding its value.
     * This method assumes the second card (index 1) is the card to be flipped.
     */
    public void flipCardDown() {
        if (getComponents().length > 1) {
            ((UICard) getComponents()[1]).setFaceDown();
        }
    }
}
