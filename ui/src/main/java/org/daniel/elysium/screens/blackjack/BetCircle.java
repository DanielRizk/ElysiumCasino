package org.daniel.elysium.screens.blackjack;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BetCircle extends JPanel {
    private final List<ImageIcon> chipIcons = new ArrayList<>();
    private final int maxChips = 10;  // Limit to 20 chips

    public BetCircle() {
        setOpaque(false);
        //setBounds(new Rectangle(getWidth() + 50, getHeight() + 50));
    }

    public void addChip(ImageIcon chip) {
        if (chipIcons.size() >= maxChips) {
            System.out.println("Maximum number of chips reached.");
            return;
        }
        chipIcons.add(chip);
        repaint();
    }

    public void clearChips() {
        chipIcons.clear();
        repaint();
    }

    public int getChipsCount(){
        return chipIcons.size();
    }

    public int getMaxChips(){
        return maxChips;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the circle outline
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.WHITE);
        int diameter = Math.min(getWidth(), getHeight()) - 10;
        int circleX = (getWidth() - diameter) / 2;
        int circleY = (getHeight() - diameter) / 2;
        g2.drawOval(circleX, circleY, diameter, diameter);

        // Adjust chip drawing values:
        int chipSize = 70;  // Larger chip size
        int offset = 3;     // Smaller offset between chips
        int baseX = (getWidth() - chipSize) / 2;
        int baseY = (getHeight() - chipSize) / 2;

        for (int i = 0; i < chipIcons.size(); i++) {
            int chipY = baseY - (i * offset);
            g2.drawImage(chipIcons.get(i).getImage(), baseX, chipY, chipSize, chipSize, null);
        }
        g2.dispose();
    }
}


