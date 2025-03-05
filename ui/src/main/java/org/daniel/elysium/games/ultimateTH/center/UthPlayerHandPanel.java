package org.daniel.elysium.games.ultimateTH.center;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ResultAsset;
import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
import org.daniel.elysium.ultimateTH.model.UthPlayerHand;

import javax.swing.*;
import java.awt.*;

public class UthPlayerHandPanel extends JPanel {
    private UthPlayerHand hand;
    private Image overlayImage;

    public UthPlayerHandPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setOpaque(false);

        Dimension size = new Dimension(120, 160);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);

        this.hand = new UthPlayerHand();
    }

    public UthPlayerHand getHand() {
        return hand;
    }

    public void addCard(UthCardUI uthCardUI){
        add(uthCardUI);
        hand.dealCard(uthCardUI.getCard());
    }

    public void removeCards(){
        removeAll();
        hand = new UthPlayerHand();
    }



    /**
     * Displays the hand result image based on the hand combination.
     */
    public void displayHandCombination(){
        switch (hand.getEvaluatedHand().handCombination()){
            case ROYAL_FLUSH -> showOverlay(AssetManager.getScaledImage(ResultAsset.ROYAL_F, new Dimension(500, 200)));
            case STRAIGHT_FLUSH -> showOverlay(AssetManager.getScaledImage(ResultAsset.STRAIGHT_F, new Dimension(500, 200)));
            case QUADS -> showOverlay(AssetManager.getScaledImage(ResultAsset.QUADS, new Dimension(350, 200)));
            case FULL_HOUSE -> showOverlay(AssetManager.getScaledImage(ResultAsset.FULL_HOUSE, new Dimension(500, 200)));
            case FLUSH -> showOverlay(AssetManager.getScaledImage(ResultAsset.FLUSH, new Dimension(350, 200)));
            case STRAIGHT -> showOverlay(AssetManager.getScaledImage(ResultAsset.STRAIGHT, new Dimension(500, 200)));
            case TRIPS -> showOverlay(AssetManager.getScaledImage(ResultAsset.TRIPS, new Dimension(350, 200)));
            case TWO_PAIR -> showOverlay(AssetManager.getScaledImage(ResultAsset.TWO_PAIR, new Dimension(500, 200)));
            case PAIR -> showOverlay(AssetManager.getScaledImage(ResultAsset.PAIR, new Dimension(300, 200)));
            case HIGH_CARD -> showOverlay(AssetManager.getScaledImage(ResultAsset.HIGH_CARD, new Dimension(500, 200)));
            default -> showOverlay(AssetManager.getScaledImage(ResultAsset.LOST, new Dimension(300, 200)));
        }
    }

    /**
     * Displays an overlay image (e.g., a trophy or result notification) for 3 seconds.
     * The overlay appears on top of the cards and disappears automatically.
     *
     * @param image The image to overlay on the panel.
     */
    private void showOverlay(Image image) {
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
