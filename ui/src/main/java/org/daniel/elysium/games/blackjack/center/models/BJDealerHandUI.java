package org.daniel.elysium.games.blackjack.center.models;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ResultAsset;
import org.daniel.elysium.blackjack.constants.HandState;
import org.daniel.elysium.blackjack.models.DealerHand;
import org.daniel.elysium.games.blackjack.models.BJCardUI;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the dealer's hand in the Blackjack UI.
 * This class manages both the visual representation of the dealer's cards
 * and the backend logic through {@link DealerHand}.
 */
public class BJDealerHandUI extends JPanel {
    private final DealerHand hand;
    private Image overlayImage;

    /**
     * Constructs a new dealer hand UI panel.
     * Sets up the layout and initializes the backend dealer hand logic.
     */
    public BJDealerHandUI() {
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
    public boolean addCard(BJCardUI uiCard) {
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
            ((BJCardUI) getComponents()[1]).setFaceUp();
        }
    }

    /**
     * Flips the dealer's second card face down, hiding its value.
     * This method assumes the second card (index 1) is the card to be flipped.
     */
    public void flipCardDown() {
        if (getComponents().length > 1) {
            ((BJCardUI) getComponents()[1]).setFaceDown();
        }
    }

    /**
     * Displays the blackjack hand result image.
     */
    public void displayBlackjackResult(){
        if (hand.getState() == HandState.BLACKJACK){
            showOverlay(AssetManager.getScaledImage(ResultAsset.BLACKJACK, new Dimension(300, 200)));
        }
    }

    /**
     * Displays an overlay image (e.g., a trophy or result notification) for 3 seconds.
     * The overlay appears on top of the cards and disappears automatically.
     *
     * @param image The image to overlay on the panel.
     */
    private void showOverlay(Image image) {
        this.overlayImage = image;
        repaint();

        Timer timer = new Timer(3000, e -> {
            overlayImage = null;
            repaint();
            ((Timer) e.getSource()).stop(); // Stop the timer after execution
        });

        timer.setRepeats(false);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        // Draw overlay image on top of the cards
        if (overlayImage != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelHeight = getHeight();
            int iw = overlayImage.getWidth(this);
            int ih = overlayImage.getHeight(this);
            int x = (getWidth() - iw) / 2;

            // Use a more precise way to center vertically
            int y = (panelHeight - ih) / 2;

            g2.drawImage(overlayImage, x, y, this);
            g2.dispose();
        }
    }
}
