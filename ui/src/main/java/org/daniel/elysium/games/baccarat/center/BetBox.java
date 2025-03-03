package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.models.chips.Chip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class BetBox extends JPanel {
    private final String label;
    private SelectionListener selectionListener;

    private final List<Chip> chips;
    private final int maxUserChips = 10;
    private final int maxChips = 20;

    public BetBox(String label) {
        this.label = label;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 7));

        this.chips = new ArrayList<>();

        setMinimumSize(new Dimension(800, 150));
        setPreferredSize(new Dimension(1000, 150));

        JLabel textLabel = new JLabel(label, SwingConstants.CENTER);
        textLabel.setFont(new Font("Roboto", Font.BOLD, 60));
        textLabel.setForeground(Color.WHITE);
        add(textLabel, BorderLayout.CENTER);

        // Add click listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectionListener != null) {
                    selectionListener.onSelected(BetBox.this);
                }
            }
        });
    }


    public String getLabel(){
        return label;
    }

    // Toggle selection
    public void setSelected(boolean selected) {
        setBorder(selected
                ? BorderFactory.createLineBorder(Color.YELLOW, 7) // Highlight selected box
                : BorderFactory.createLineBorder(Color.WHITE, 7)); // Default
        repaint();
    }

    // Assign selection listener
    public void setSelectionListener(SelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    // Interface for callback
    public interface SelectionListener {
        void onSelected(BetBox selectedBox);
    }


    public void payWin(){
        List<Chip> chipList = new ArrayList<>(chips);
        chips.addAll(chipList);
    }

    public void payTie(){
        List<Chip> chipList = Chip.getChipCombination(chips.stream()
                .mapToInt(Chip::getValue) // Extract values
                .sum() * 9);
        chips.addAll(chipList);
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
     * Checks if a chip can be added to the main bet circle.
     *
     * @return True if the number of chips in the main bet is below the maximum limit, false otherwise.
     */
    public boolean canAddChip() {
        return getChipsCount() < getMaxChips();
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw chips relative to the circle's position
        int chipSize = 100;  // Adjust as needed.
        int offset = 2;      // Smaller offset between chips.
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
}


