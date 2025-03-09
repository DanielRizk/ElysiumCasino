package org.daniel.elysium.models.chips;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a betting box within a Baccarat game UI. Manages betting interactions
 * and displays chips for player, banker, or tie bets.
 */
public class BetBox extends JPanel {
    private final String label;
    private final Color color;
    private boolean selected = false;
    private SelectionListener selectionListener;

    private final List<Chip> chips;
    private final int maxUserChips = 10;
    private final int maxChips = 20;

    /**
     * Constructs a BetBox with a specific label.
     * @param label The label for the betting box (e.g., PLAYER, BANKER, TIE).
     * @param color The color for the bet box text.
     */
    public BetBox(String label, Color color) {
        this.label = label;
        this.color = color;
        this.chips = new ArrayList<>();
        initializeUI();
    }

    /**
     * Initializes the user interface components of the bet box.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setMinimumSize(new Dimension(800, 150));
        setPreferredSize(new Dimension(1000, 150));
        addLabel();
        setupMouseListener();
    }

    /**
     * Adds the label to the center of the bet box.
     */
    private void addLabel() {
        JLabel textLabel = new JLabel(label, SwingConstants.CENTER);
        textLabel.setFont(new Font("Roboto", Font.BOLD, 60));
        textLabel.setForeground(color);
        add(textLabel, BorderLayout.CENTER);
    }

    /**
     * Sets up a mouse listener to handle click events for selecting this bet box.
     */
    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectionListener != null) {
                    selectionListener.onSelected(BetBox.this);
                }
            }
        });
    }

    /**
     * Sets whether this bet box is selected or not, changing its border color accordingly.
     * @param selected true to mark this bet box as selected; false otherwise.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    /**
     * Sets the selection listener for this bet box.
     * @param selectionListener The listener to be notified when this box is selected.
     */
    public void setSelectionListener(SelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    /**
     * Adds a chip to this bet box if the maximum chip limit has not been exceeded.
     * @param chip The chip to add.
     */
    public void addChip(Chip chip) {
        if (chips.size() < maxChips) {
            chips.add(chip);
            repaint();
        }
    }

    /**
     * Clears all chips from this bet box.
     */
    public void clearChips() {
        chips.clear();
        repaint();
    }

    /**
     * Gets the current list of chips placed in this bet box.
     * @return A list of chips.
     */
    public List<Chip> getChips() {
        return chips;
    }

    /**
     * Returns the count of chips currently in this bet box.
     * @return The number of chips.
     */
    public int getChipsCount() {
        return chips.size();
    }

    /**
     * Returns the maximum number of chips that can be placed in this bet box.
     * @return The maximum number of chips allowed.
     */
    public int getMaxChips() {
        return maxUserChips;
    }

    /**
     * Determines whether a chip can be added to this bet box.
     * @return true if another chip can be added; false otherwise.
     */
    public boolean canAddChip() {
        return getChipsCount() < getMaxChips();
    }

    /**
     * Doubles the chips in this bet box as part of a win payout.
     */
    public void payWin() {
        List<Chip> chipList;
        if (getLabel().equals("BANKER")){
            chipList = Chip.getChipCombination((int) (chips.stream().mapToInt(Chip::getValue).sum() * 0.95));
        } else {
            chipList = new ArrayList<>(chips);
        }
        chips.addAll(chipList);
    }

    /**
     * Calculates and adds the appropriate chips for a tie payout.
     */
    public void payTie() {
        List<Chip> chipList = Chip.getChipCombination(chips.stream().mapToInt(Chip::getValue).sum() * 8);
        chips.addAll(chipList);
    }

    /**
     * Custom painting for the bet box, including its chips.
     * @param g The graphics context.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        // Enable antialiasing for smooth edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the stroke color and thickness
        if (selected){
            g2.setColor(Color.YELLOW);
        } else {
            g2.setColor(Color.WHITE);
        }


        g2.setStroke(new BasicStroke(7)); // 7-pixel thick stroke

        // Draw a rounded rectangle with a border
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

        g2.dispose();
    }

    /**
     * Custom painting for children components, specifically chips.
     * @param g The graphics context.
     */
    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        paintChips((Graphics2D) g);
    }

    /**
     * Paints chips on the bet box.
     * @param g2 The graphics context for 2D drawing.
     */
    private void paintChips(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int chipSize = 100;
        int offset = 2;
        int baseX = (getWidth() - chipSize) / 2;
        int baseY = (getHeight() - chipSize + 30) / 2;
        for (int i = 0; i < chips.size(); i++) {
            if (i < maxChips) {
                int chipY = baseY - (i * offset);
                g2.drawImage(chips.get(i).getIcon().getImage(), baseX, chipY, chipSize, chipSize, null);
            }
        }
        g2.dispose();
    }

    /**
     * Interface for handling selection events of bet boxes.
     */
    public interface SelectionListener {
        void onSelected(BetBox selectedBox);
    }

    /**
     * Returns the label of this bet box.
     * @return The label as a string.
     */
    public String getLabel() {
        return label;
    }
}
