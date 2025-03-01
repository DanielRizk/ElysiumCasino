package org.daniel.elysium.screens.blackjack.center.models;

import org.daniel.elysium.models.Chip;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BetCircle extends JPanel {
    private final List<Chip> chips;
    private final int maxUserChips = 10;
    private final int maxChips = 20;
    private final boolean visible;

    public BetCircle(boolean visible ) {
        setOpaque(false);
        setPreferredSize(new Dimension(130, 220));
        this.chips = new ArrayList<>();
        this.visible = visible;
    }

    /** Adds a chip to the circle if not exceeded max limit of chips */
    public void addChip(Chip chip) {
        if (chips.size() < maxChips) {
            chips.add(chip);
            repaint();
        }
    }

    /** Clears all chips on the circle */
    public void clearChips() {
        chips.clear();
        repaint();
    }

    /** Get the number of chip added */
    public int getChipsCount(){
        return chips.size();
    }

    /** Get max number of chips user can add */
    public int getMaxChips(){
        return maxUserChips;
    }

    /** Get the list of chips added */
    public List<Chip> getChips(){
        return chips;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // If set to visible, draw the stroke of the circle, otherwise keep it invisible
        if (visible){
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.WHITE);
        }

        // Use a fixed diameter for the circle (120px)
        int circleDiameter = 120;
        int circleX = (getWidth() - circleDiameter) / 2;
        int circleY = (getHeight() - circleDiameter) / 2;

        // If set to visible, draw the stroke of the circle, otherwise keep it invisible
        if (visible){
            g2.drawOval(circleX, circleY, circleDiameter, circleDiameter);
        }

        // Draw chips relative to the circle's position.
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



