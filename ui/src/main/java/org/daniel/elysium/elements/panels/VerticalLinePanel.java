package org.daniel.elysium.elements.panels;

import javax.swing.*;
import java.awt.*;

public class VerticalLinePanel extends JPanel {
    public VerticalLinePanel(int height) {
        setPreferredSize(new Dimension(5, height)); // Set width (3px) and height dynamically
        setMinimumSize(new Dimension(5, height));
        setMaximumSize(new Dimension(5, height));
        setOpaque(false); // Transparent background
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE); // Set line color
        g.fillRect(0, 0, getWidth(), getHeight()); // Draw vertical line
    }
}
