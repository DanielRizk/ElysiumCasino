package org.daniel.elysium.games.ultimateTH.center.models;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ResultAsset;
import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
import org.daniel.elysium.ultimateTH.model.UthPlayerHand;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the player's hand panel in Ultimate Texas Hold'em.
 * <p>
 * This panel manages the player's hand, including dealing, removing,
 * and displaying hand results as overlays.
 * </p>
 */
public class UthPlayerHandPanel extends JPanel {
    private UthPlayerHand hand;
    private Image overlayImage;

    /**
     * Constructs the player's hand panel with a fixed size.
     */
    public UthPlayerHandPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setOpaque(false);

        Dimension size = new Dimension(120, 160);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);

        this.hand = new UthPlayerHand();
    }

    /* ======================
       Hand Management
       ====================== */

    /**
     * Retrieves the player's hand.
     *
     * @return the {@code UthPlayerHand} representing the player's hand
     */
    public UthPlayerHand getHand() {
        return hand;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param uthCardUI the card to be added
     */
    public void addCard(UthCardUI uthCardUI) {
        add(uthCardUI);
        hand.dealCard(uthCardUI.getCard());
    }

    /**
     * Removes all cards from the player's hand and resets it.
     */
    public void removeCards() {
        removeAll();
        hand = new UthPlayerHand();
    }

    /* ======================
       Displaying Hand Results
       ====================== */

    /**
     * Displays the player's hand combination using an overlay image.
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
     * The overlay appears on top of the player's hand and disappears automatically.
     *
     * @param image the image to overlay on the panel
     */
    private void showOverlay(Image image) {
        this.overlayImage = image;
        repaint();

        Timer timer = new Timer(3000, e -> {
            overlayImage = null;
            repaint();
            ((Timer) e.getSource()).stop();
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

        // Draw overlay image on top of the player's hand
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
