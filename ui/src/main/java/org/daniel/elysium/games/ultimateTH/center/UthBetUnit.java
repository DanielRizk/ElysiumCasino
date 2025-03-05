package org.daniel.elysium.games.ultimateTH.center;

import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.models.chips.BetCircle;
import org.daniel.elysium.models.chips.Chip;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UthBetUnit extends JPanel {
    private final BetCircle circle;
    private final StyledTextField currentBetLabel;
    private Image overlayImage;

    public UthBetUnit(ComponentOrientation orientation, String text) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        setComponentOrientation(orientation);
        setOpaque(false);

        // Create and add the bet display text field
        currentBetLabel = new StyledTextField("0", new Dimension(150, 50), 9, false);
        add(currentBetLabel);

        // Create and add the main bet circle, set to visible
        circle = new BetCircle(true, text);
        add(circle);
    }

    /**
     * Checks if a chip can be added to the main bet circle.
     *
     * @return True if the number of chips in the main bet is below the maximum limit, false otherwise.
     */
    public boolean canAddChip() {
        return circle.getChipsCount() < circle.getMaxChips();
    }

    /**
     * Adds a chip to the main bet circle.
     *
     * @param chip The chip to be added.
     */
    public void addChip(Chip chip) {
        circle.addChip(chip);
    }

    /**
     * Adds a list of chips to the bet circle.
     *
     * @param chips The chip to be added.
     */
    public void addChips(List<Chip> chips) {
        for (Chip chip : chips){
            circle.addChip(chip);
        }
    }

    /**
     * Retrieves the list of chips placed in the main bet circle.
     *
     * @return A list of chips in the main bet.
     */
    public List<Chip> getChips() {
        return circle.getChips();
    }

    /**
     * Removes all chips from the main bet circle.
     */
    public void clearChips() {
        circle.clearChips();
    }

    public BetCircle getCircle(){
        return circle;
    }

    /**
     * Updates the displayed bet amount.
     *
     * @param bet The amount to display.
     */
    public void updateBetDisplay(int bet) {
        currentBetLabel.setText(String.valueOf(bet));
    }

    /**
     * Displays an overlay image (e.g., a trophy or result notification) for 3 seconds.
     * The overlay appears on top of the cards and disappears automatically.
     *
     * @param image The image to overlay on the panel.
     */
    void showOverlay(Image image) {
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
