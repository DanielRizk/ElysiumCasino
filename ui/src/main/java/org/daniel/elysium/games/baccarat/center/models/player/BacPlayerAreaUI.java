package org.daniel.elysium.games.baccarat.center.models.player;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ResultAsset;
import org.daniel.elysium.baccarat.models.BacHand;
import org.daniel.elysium.games.baccarat.models.BacCardUI;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the player area in the Baccarat game UI. This area includes both textual and card display components,
 * managing interactions and visual updates related to the player's hand.
 */
public class BacPlayerAreaUI extends JPanel {
    private final BacPlayerCardsUI cardsUI;
    private BacHand hand;

    /**
     * Constructs the PlayerAreaUI with layout and component initialization tailored for displaying player-related information.
     * This includes setting up the player text label and card UI, and ensuring proper alignment and spacing.
     */
    public BacPlayerAreaUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // Ensure components are centered
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setPreferredSize(new Dimension(500, 400));

        // Player label setup
        BacPlayerTextUI textUI = new BacPlayerTextUI();
        textUI.setAlignmentX(CENTER_ALIGNMENT);

        // Player cards setup
        cardsUI = new BacPlayerCardsUI();
        cardsUI.setAlignmentX(CENTER_ALIGNMENT);

        // Layout adjustments
        add(Box.createVerticalGlue()); // Push content towards center
        add(textUI);
        add(Box.createRigidArea(new Dimension(0, 30))); // Space between text & cards
        add(cardsUI);
        add(Box.createVerticalGlue()); // Push content towards center

        // Initialize the player's hand
        this.hand = new BacHand();
    }

    /**
     * Adds a card to the player's UI and updates the player's hand model.
     * @param card The graphical representation of the card to be added.
     */
    public void addCard(BacCardUI card){
        cardsUI.addCard(card);
        hand.dealCard(card.getCard());
    }

    /**
     * Removes all cards from the player's UI and resets the player's hand model.
     */
    public void removeCards(){
        cardsUI.removeCards();
        hand = new BacHand();
    }

    /**
     * Retrieves the player's current hand.
     * @return The player's current hand of cards.
     */
    public BacHand getHand(){
        return hand;
    }

    /**
     * Displays an overlay on the player's cards based on the result of the hand (win, lose, tie).
     */
    public void showHandResult() {
        switch (hand.getState()) {
            case WON -> cardsUI.showOverlay(AssetManager.getScaledImage(ResultAsset.WON, new Dimension(300, 200)));
            case TIE -> cardsUI.showOverlay(AssetManager.getScaledImage(ResultAsset.TIE, new Dimension(300, 200)));
            default -> cardsUI.showOverlay(AssetManager.getScaledImage(ResultAsset.LOST, new Dimension(300, 200)));
        }
    }
}
