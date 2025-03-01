package org.daniel.elysium.screens.blackjack.center.models;

import org.daniel.elysium.models.Chip;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a betting circle where chips are placed.
 * This panel visually displays placed chips and ensures a limit on chip additions.
 */
public class BetCircle extends JPanel {
    private final List<Chip> chips;
    private final int maxUserChips = 10;
    private final int maxChips = 20;
    private final boolean visible;

    /**
     * Constructs a BetCircle with visibility control.
     *
     * @param visible If true, the circle border will be drawn, otherwise it remains hidden.
     */
    public BetCircle(boolean visible) {
        setOpaque(false);
        setPreferredSize(new Dimension(130, 220));
        this.chips = new ArrayList<>();
        this.visible = visible;
    }

    /**
     * Adds a chip to the betting circle if the maximum chip limit is not exceeded.
     *
     * @param chip The chip to be added.
     */
    public void addChip(Chip chip) {
        if (chips.size() < maxChips) {
            chips.add(chip);
            repaint();
        }
    }

    /**
     * Clears all chips from the betting circle.
     */
    public void clearChips() {
        chips.clear();
        repaint();
    }

    /**
     * Retrieves the number of chips currently in the betting circle.
     *
     * @return The count of placed chips.
     */
    public int getChipsCount() {
        return chips.size();
    }

    /**
     * Retrieves the maximum number of chips a user can add to the betting circle.
     *
     * @return The maximum number of user-placed chips.
     */
    public int getMaxChips() {
        return maxUserChips;
    }

    /**
     * Retrieves the list of chips currently placed in the betting circle.
     *
     * @return A list of placed chips.
     */
    public List<Chip> getChips() {
        return chips;
    }

    /**
     * Custom rendering method to draw the betting circle and placed chips.
     *
     * @param g The Graphics context used for rendering.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // If visible, draw the stroke of the betting circle
        if (visible) {
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.WHITE);
        }

        // Define the fixed diameter for the circle
        int circleDiameter = 120;
        int circleX = (getWidth() - circleDiameter) / 2;
        int circleY = (getHeight() - circleDiameter) / 2;

        // Draw the circle border if set to visible
        if (visible) {
            g2.drawOval(circleX, circleY, circleDiameter, circleDiameter);
        }

        // Draw chips relative to the circle's position
        int chipSize = 100;  // Adjust as needed.
        int offset = 3;      // Smaller offset between chips.
        int baseX = circleX + (circleDiameter - chipSize) / 2;
        int baseY = circleY + (circleDiameter - chipSize) / 2;
        for (int i = 0; i < chips.size(); i++) {
            int chipY = baseY - (i * offset);
            g2.drawImage(chips.get(i).getIcon().getImage(), baseX, chipY, chipSize, chipSize, null);
        }
        g2.dispose();
    }
}
