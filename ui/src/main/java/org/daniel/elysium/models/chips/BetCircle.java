package org.daniel.elysium.models.chips;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a betting circle where chips are placed.
 * This panel visually displays placed chips and ensures a limit on chip additions.
 */
public class BetCircle extends JPanel {
    private final List<Chip> chips;
    private final int maxUserChips = 10;
    private final int maxChips = 20;
    private final boolean visible;
    private String text;
    private final String label;
    private SelectionListener selectionListener;
    private Color color = Color.WHITE;

    /**
     * Constructs a BetCircle with visibility control.
     *
     * @param visible If true, the circle border will be drawn, otherwise it remains hidden.
     */
    public BetCircle(boolean visible, String text) {
        setOpaque(false);
        setPreferredSize(new Dimension(130, 220));
        this.chips = new ArrayList<>();
        this.visible = visible;
        this.text = text;
        this.label = text;
        setupMouseListener();
    }

    /**
     * Adds a chip to the betting circle if the maximum chip limit is not exceeded.
     *
     * @param chip The chip to be added.
     */
    public void addChip(Chip chip) {
        if (chips.size() < maxChips) {
            chips.add(chip);
            repaint();
        }
    }

    /**
     * Clears all chips from the betting circle.
     */
    public void clearChips() {
        chips.clear();
        repaint();
    }

    /**
     * Retrieves the number of chips currently in the betting circle.
     *
     * @return The count of placed chips.
     */
    public int getChipsCount() {
        return chips.size();
    }

    /**
     * Retrieves the maximum number of chips a user can add to the betting circle.
     *
     * @return The maximum number of user-placed chips.
     */
    public int getMaxChips() {
        return maxUserChips;
    }

    /**
     * Retrieves the list of chips currently placed in the betting circle.
     *
     * @return A list of placed chips.
     */
    public List<Chip> getChips() {
        return chips;
    }

    /**
     * Determines whether a chip can be added to this bet circle.
     * @return true if another chip can be added; false otherwise.
     */
    public boolean canAddChip() {
        return getChipsCount() < getMaxChips();
    }

    /**
     * Interface for handling selection events of bet circle.
     */
    public interface SelectionListener {
        void onSelected(BetCircle selectedBox);
    }

    /**
     * Sets up a mouse listener to handle click events for selecting this bet circle.
     */
    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectionListener != null) {
                    selectionListener.onSelected(BetCircle.this);
                }
            }
        });
    }

    /**
     * Sets whether this bet circle is selected or not, changing its border color accordingly.
     * @param selected true to mark this bet circle as selected; false otherwise.
     */
    public void setSelected(boolean selected) {
        if (selected){
            color = Color.YELLOW;
        } else {
            color = Color.WHITE;
        }
        repaint();
    }

    /**
     * Sets the selection listener for this bet circle.
     * @param selectionListener The listener to be notified when this circle is selected.
     */
    public void setSelectionListener(SelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    /**
     * Returns the label of this bet circle.
     * @return The label as a string.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Custom rendering method to draw the betting circle and the text.
     *
     * @param g The Graphics context used for rendering.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // If visible, draw the stroke of the betting circle
        if (visible) {
            g2.setStroke(new BasicStroke(3));
            g2.setColor(color);
        }

        // Define the fixed diameter for the circle
        int circleDiameter = 120;
        int circleX = (getWidth() - circleDiameter) / 2;
        int circleY = (getHeight() - circleDiameter) / 2;

        // Draw the circle border if set to visible
        if (visible) {
            g2.drawOval(circleX, circleY, circleDiameter, circleDiameter);
        }

        if (text != null){
            Font font = new Font("Roboto", Font.BOLD, 30);
            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);

            // Calculate the center of the circle.
            int centerX = circleX + circleDiameter / 2;
            int centerY = circleY + circleDiameter / 2;

            // Determine x so the text is centered horizontally.
            int textX = centerX - textWidth / 2;
            // For vertical centering, adjust y to account for ascent and descent.
            int textY = centerY + (fm.getAscent() - fm.getDescent()) / 2;

            g2.setColor(Color.WHITE); // Set text color
            g2.drawString(text, textX, textY);
        }
        g2.dispose();
    }

    /**
     * Custom rendering method to draw the chips.
     *
     * @param g The Graphics context used for rendering.
     */
    @Override
    protected void paintChildren(Graphics g){
        super.paintChildren(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define the fixed diameter for the circle
        int circleDiameter = 120;
        int circleX = (getWidth() - circleDiameter) / 2;
        int circleY = (getHeight() - circleDiameter) / 2;


        // Draw chips relative to the circle's position
        int chipSize = 100;  // Adjust as needed.
        int offset = 3;      // Smaller offset between chips.
        int baseX = circleX + (circleDiameter - chipSize) / 2;
        int baseY = circleY + (circleDiameter - chipSize) / 2;
        for (int i = 0; i < chips.size(); i++) {
            int chipY = baseY - (i * offset);
            g2.drawImage(chips.get(i).getIcon().getImage(), baseX, chipY, chipSize, chipSize, null);
        }
    }
}
