package org.daniel.elysium.games.ultimateTH.center;

import org.daniel.elysium.games.ultimateTH.models.UthCardUI;

import javax.swing.*;
import java.awt.*;

public class UthCardHolderPanel extends JPanel {

    public UthCardHolderPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setOpaque(false);
        Dimension size = new Dimension(120, 160);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
    }

    public void addCard(UthCardUI card){
        add(card);
    }

    public UthCardUI getCard(){
        if (getComponents().length > 0){
            return (UthCardUI) getComponent(0);
        }
        return null;
    }

    public void removeCard(){
        removeAll();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        // Enable anti-aliasing for smooth edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the stroke color and thickness
        g2.setColor(Color.WHITE); // Change as needed
        g2.setStroke(new BasicStroke(3)); // 2-pixel thick stroke

        // Draw a rounded rectangle. Adjust the arc width and height as needed.
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        g2.dispose();
    }
}
