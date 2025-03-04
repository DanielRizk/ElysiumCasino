package org.daniel.elysium.games.baccarat.center.models.banker;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ResultAsset;
import org.daniel.elysium.baccarat.models.BacHand;
import org.daniel.elysium.games.baccarat.models.BacCardUI;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the banker area in the Baccarat game UI. This area includes both textual and card display components,
 * managing interactions and visual updates related to the banker's hand.
 */
public class BankerAreaUI extends JPanel {
    private final BankerCardsUI cardsUI;
    public BacHand hand;

    /**
     * Constructs the BankerAreaUI with layout and component initialization tailored for displaying banker-related information.
     * This includes setting up the banker text label and card UI, and ensuring proper alignment and spacing.
     */
    public BankerAreaUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // Ensure components are centered
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setPreferredSize(new Dimension(500, 400));

        // Banker label setup
        BankerTextUI textUI = new BankerTextUI();
        textUI.setAlignmentX(CENTER_ALIGNMENT);

        // Banker cards setup
        cardsUI = new BankerCardsUI();
        cardsUI.setAlignmentX(CENTER_ALIGNMENT);

        // Layout adjustments
        add(Box.createVerticalGlue()); // Push content towards center
        add(textUI);
        add(Box.createRigidArea(new Dimension(0, 30))); // Space between text & cards
        add(cardsUI);
        add(Box.createVerticalGlue()); // Push content towards center

        // Initialize the Banker's hand
        this.hand = new BacHand();
    }

    /**
     * Adds a card to the banker's UI and updates the banker's hand model.
     * @param card The graphical representation of the card to be added.
     */
    public void addCard(BacCardUI card){
        cardsUI.addCard(card);
        hand.dealCard(card.getCard());
    }

    /**
     * Removes all cards from the banker's UI and resets the banker's hand model.
     */
    public void removeCards(){
        cardsUI.removeCards();
        hand = new BacHand();
    }

    /**
     * Retrieves the banker's current hand.
     * @return The banker's current hand of cards.
     */
    public BacHand getHand(){
        return hand;
    }

    /**
     * Displays an overlay on the banker's cards based on the result of the hand (win, lose, tie).
     */
    public void showHandResult(){
        switch (hand.getState()) {
            case WON:
                cardsUI.showOverlay(AssetManager.getScaledImage(ResultAsset.WON, new Dimension(300, 200)));
                break;
            case LOST:
                cardsUI.showOverlay(AssetManager.getScaledImage(ResultAsset.LOST, new Dimension(300, 200)));
                break;
            case TIE:
                cardsUI.showOverlay(AssetManager.getScaledImage(ResultAsset.TIE, new Dimension(300, 200)));
                break;
            default:
                // No action needed for other states
                break;
        }
    }
}
