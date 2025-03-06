package org.daniel.elysium.games.ultimateTH.center.models;

import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.models.chips.BetCircle;
import org.daniel.elysium.models.chips.Chip;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Represents a single betting unit in Ultimate Texas Hold'em.
 * <p>
 * This class manages a betting area, displaying the current bet amount
 * and allowing players to place and remove chips.
 * </p>
 */
public class UthBetUnit extends JPanel {
    private final BetCircle circle;
    private final StyledTextField currentBetLabel;
    private Image overlayImage;

    /**
     * Constructs a betting unit with a specified orientation and label.
     *
     * @param orientation The component orientation (left-to-right or right-to-left).
     * @param text        The label text for the betting unit.
     */
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

    /* ======================
       Chip Management
       ====================== */

    /**
     * Checks if a chip can be added to the main bet circle.
     *
     * @return {@code true} if the number of chips in the bet circle is below the maximum limit, otherwise {@code false}.
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
     * @param chips The list of chips to be added.
     */
    public void addChips(List<Chip> chips) {
        for (Chip chip : chips) {
            circle.addChip(chip);
        }
    }

    /**
     * Retrieves the list of chips placed in the main bet circle.
     *
     * @return A list of chips in the bet circle.
     */
    public List<Chip> getChips() {
        return circle.getChips();
    }

    /**
     * Removes all chips from the bet circle.
     */
    public void clearChips() {
        circle.clearChips();
    }

    /* ======================
       Display & Updates
       ====================== */

    /**
     * Updates the displayed bet amount.
     *
     * @param bet The amount to display.
     */
    public void updateBetDisplay(int bet) {
        currentBetLabel.setText(String.valueOf(bet));
    }

    /**
     * Displays an overlay image (e.g., a winning multiplier or result notification) for 3 seconds.
     * The overlay appears on top of the bet unit and disappears automatically.
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

    /* ======================
       Getters
       ====================== */

    /**
     * Returns the {@code BetCircle} associated with this betting unit.
     *
     * @return The {@code BetCircle} instance.
     */
    public BetCircle getCircle() {
        return circle;
    }

    /* ======================
       Rendering Methods
       ====================== */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        // Draw overlay image on top of the bet unit
        if (overlayImage != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelHeight = getHeight();
            int iw = overlayImage.getWidth(this);
            int ih = overlayImage.getHeight(this);
            int x = (getWidth() - iw) / 2;
            int y = (panelHeight - ih) / 2; // Center vertically

            g2.drawImage(overlayImage, x, y, this);
            g2.dispose();
        }
    }
}
