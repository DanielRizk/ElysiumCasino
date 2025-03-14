package org.daniel.elysium.games.baccarat.center.models.banker;

import org.daniel.elysium.games.baccarat.models.BacCardUI;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel designed to display banker cards and overlay images in a Baccarat game.
 * This panel supports functionalities for adding cards, removing all cards, and displaying a temporary overlay image.
 */
public class BacBankerCardsUI extends JPanel {
    private Image overlayImage;

    /**
     * Constructs a BankerCardsUI with specific layout and opacity settings.
     * The panel uses a FlowLayout to manage card positioning and is set to be transparent.
     */
    public BacBankerCardsUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setOpaque(false);
        setPreferredSize(new Dimension(200, 150));
    }

    /**
     * Adds a card to the panel.
     * @param card The BacCardUI to add to the panel.
     */
    public void addCard(BacCardUI card){
        add(card);
    }

    /**
     * Removes all cards from the panel.
     */
    public void removeCards(){
        removeAll();
    }

    /**
     * Displays an overlay image over the cards for a limited time.
     * The image is shown for 3 seconds and then automatically removed.
     * @param image The image to be displayed as an overlay.
     */
    public void showOverlay(Image image) {
        this.overlayImage = image;
        repaint();

        // Create a timer to remove the image after 3 seconds
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
