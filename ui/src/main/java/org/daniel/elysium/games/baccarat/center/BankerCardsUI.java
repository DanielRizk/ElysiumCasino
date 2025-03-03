package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.models.cards.UICard;

import javax.swing.*;
import java.awt.*;

public class BankerCardsUI extends JPanel {
    private Image overlayImage;

    public BankerCardsUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setOpaque(false);
        setPreferredSize(new Dimension(200, 150));
    }

    public void addCard(UICard card){
        add(card);
    }

    public void removeCards(){
        removeAll();
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
