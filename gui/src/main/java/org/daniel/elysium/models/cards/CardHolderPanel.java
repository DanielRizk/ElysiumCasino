package org.daniel.elysium.models.cards;

import org.daniel.elysium.games.ultimateTH.models.UthCardUI;

import javax.swing.*;
import java.awt.*;

/**
 * A panel that holds a single card in Ultimate Texas Hold'em.
 * <p>
 * This panel is designed to display a card, remove it, and provide
 * a visually distinct rounded rectangle border.
 * </p>
 */
public class CardHolderPanel extends JPanel {

    /**
     * Constructs a cardholder panel with a fixed size and transparent background.
     */
    public CardHolderPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setOpaque(false);
        Dimension size = new Dimension(120, 160);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
    }

    /**
     * Adds a card to the panel.
     *
     * @param card the {@code UICard} to be added
     */
    public void addCard(UICard card) {
        add(card);
    }

    /**
     * Retrieves the first card from the panel.
     *
     * @return the first {@code UICard} in the panel, or {@code null} if empty
     */
    public UICard getCard() {
        if (getComponents().length > 0) {
            return (UthCardUI) getComponent(0);
        }
        return null;
    }

    /**
     * Removes all cards from the panel.
     */
    public void removeCard() {
        removeAll();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable antialiasing for smooth edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the stroke color and thickness
        g2.setColor(Color.WHITE); // Change as needed
        g2.setStroke(new BasicStroke(3)); // 3-pixel thick stroke

        // Draw a rounded rectangle with a border
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        g2.dispose();
    }
}
