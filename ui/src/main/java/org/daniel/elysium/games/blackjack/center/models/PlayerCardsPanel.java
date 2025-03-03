package org.daniel.elysium.games.blackjack.center.models;

import org.daniel.elysium.models.cards.UICard;
import org.daniel.elysium.uiUtils.GlowBorder;

import javax.swing.*;
import java.awt.*;

/**
 * A panel that holds and displays the player's cards.
 * Provides additional functionalities such as highlighting and overlay effects.
 */
public class PlayerCardsPanel extends JPanel {
    private Image overlayImage;

    /**
     * Constructs a new player card panel with a centered layout.
     */
    public PlayerCardsPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        setOpaque(false);
        setMinimumSize(new Dimension(getWidth(), 150));
    }

    /**
     * Adds a card to the panel.
     *
     * @param card The {@link UICard} to be added.
     */
    public void addCard(UICard card) {
        add(card);
    }

    /**
     * Toggles a glowing highlight border for all cards in the panel.
     *
     * @param glow {@code true} to enable highlighting, {@code false} to disable.
     */
    public void setHighlight(boolean glow) {
        for (Component component : getComponents()) {
            if (component instanceof UICard uiCard) {
                if (glow) {
                    uiCard.setBorder(new GlowBorder(Color.RED, 4));
                } else {
                    uiCard.setBorder(null);
                }
            }
        }
        revalidate();
        repaint();
    }

    /**
     * Displays an overlay image (e.g., a trophy or result notification) for 3 seconds.
     * The overlay appears on top of the cards and disappears automatically.
     *
     * @param image The image to overlay on the panel.
     */
    public void showOverlay(Image image) {
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

