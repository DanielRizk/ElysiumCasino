package org.daniel.elysium.games.ultimateTH.center.models;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ResultAsset;
import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
import org.daniel.elysium.ultimateTH.model.UthHand;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the dealer's hand panel in Ultimate Texas Hold'em.
 * <p>
 * This panel manages the dealer's cards, including dealing, exposing, and
 * removing cards. It also displays the dealer's hand combination as an overlay.
 * </p>
 */
public class UthDealerHandPanel extends JPanel {
    private UthHand hand;
    private Image overlayImage;

    /**
     * Constructs the dealer's hand panel with a fixed size.
     */
    public UthDealerHandPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setOpaque(false);

        Dimension size = new Dimension(120, 160);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);

        this.hand = new UthHand();
    }

    /* ======================
       Hand Management
       ====================== */

    /**
     * Retrieves the dealer's hand.
     *
     * @return the {@code UthHand} representing the dealer's hand
     */
    public UthHand getHand() {
        return this.hand;
    }

    /**
     * Adds a card to the dealer's hand in a face-down position.
     *
     * @param uthCardUI the card to be added
     */
    public void addCard(UthCardUI uthCardUI) {
        uthCardUI.setFaceDown();
        add(uthCardUI);
        hand.dealCard(uthCardUI.getCard());
    }

    /**
     * Reveals all cards in the dealer's hand by turning them face-up.
     */
    public void exposeCards() {
        for (Component component : getComponents()) {
            if (component instanceof UthCardUI cardUI) {
                cardUI.setFaceUp();
            }
        }
    }

    /**
     * Removes all cards from the dealer's hand and resets it.
     */
    public void removeCards() {
        removeAll();
        hand = new UthHand();
    }

    /* ======================
       Displaying Hand Results
       ====================== */

    /**
     * Displays the dealer's hand combination using an overlay image.
     */
    public void displayHandCombination() {
        switch (hand.getEvaluatedHand().handCombination()) {
            case ROYAL_FLUSH -> showOverlay(AssetManager.getScaledImage(ResultAsset.ROYAL_FLUSH, new Dimension(600, 200)));
            case STRAIGHT_FLUSH -> showOverlay(AssetManager.getScaledImage(ResultAsset.STRAIGHT_FLUSH, new Dimension(600, 200)));
            case QUADS -> showOverlay(AssetManager.getScaledImage(ResultAsset.QUADS, new Dimension(350, 200)));
            case FULL_HOUSE -> showOverlay(AssetManager.getScaledImage(ResultAsset.FULL_HOUSE, new Dimension(500, 200)));
            case FLUSH -> showOverlay(AssetManager.getScaledImage(ResultAsset.FLUSH, new Dimension(350, 200)));
            case STRAIGHT -> showOverlay(AssetManager.getScaledImage(ResultAsset.STRAIGHT, new Dimension(500, 200)));
            case TRIPS -> showOverlay(AssetManager.getScaledImage(ResultAsset.TRIPS, new Dimension(350, 200)));
            case TWO_PAIR -> showOverlay(AssetManager.getScaledImage(ResultAsset.TWO_PAIR, new Dimension(500, 200)));
            case PAIR -> showOverlay(AssetManager.getScaledImage(ResultAsset.PAIR, new Dimension(300, 200)));
            case HIGH_CARD -> showOverlay(AssetManager.getScaledImage(ResultAsset.HIGH_CARD, new Dimension(500, 200)));
            default -> showOverlay(AssetManager.getScaledImage(ResultAsset.LOST, new Dimension(300, 200)));
        }
    }

    /**
     * Displays an overlay image (e.g., a result notification) for 3 seconds.
     * The overlay appears on top of the dealer's hand and disappears automatically.
     *
     * @param image the image to overlay on the panel
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

    /* ======================
       Rendering Methods
       ====================== */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        // Draw overlay image on top of the dealer's hand
        if (overlayImage != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelHeight = getHeight();
            int iw = overlayImage.getWidth(this);
            int ih = overlayImage.getHeight(this);
            int x = (getWidth() - iw) / 2;
            int y = (panelHeight - ih) / 2; // Center vertically

            g2.drawImage(overlayImage, x, y, this);
            g2.dispose();
        }
    }
}
