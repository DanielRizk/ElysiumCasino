package org.daniel.elysium.screens.blackjack2;

import org.daniel.elysium.models.Chip;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BetCircle extends JPanel {
    private final List<Chip> chips = new ArrayList<>();
    private final int maxChips = 20;

    public BetCircle() {
        setOpaque(false);
        // Set the panel's preferred size larger than the circle size.
        setPreferredSize(new Dimension(130, 220));
    }

    public void addChip(Chip chip) {
        if (chips.size() < maxChips) {
            chips.add(chip);
            repaint();
        }
    }

    public void clearChips() {
        chips.clear();
        repaint();
    }

    public int getChipsCount(){
        return chips.size();
    }

    public int getMaxChips(){
        return maxChips;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Create a copy of the Graphics for proper rendering.
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.WHITE);

        // Use a fixed diameter for the circle (200px)
        int circleDiameter = 120;
        int circleX = (getWidth() - circleDiameter) / 2;
        int circleY = (getHeight() - circleDiameter) / 2;
        g2.drawOval(circleX, circleY, circleDiameter, circleDiameter);

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



