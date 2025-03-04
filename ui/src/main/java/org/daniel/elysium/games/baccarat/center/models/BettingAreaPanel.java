package org.daniel.elysium.games.baccarat.center.models;

import org.daniel.elysium.baccarat.constants.HandType;
import org.daniel.elysium.models.chips.Chip;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the betting area panel in a Baccarat game interface.
 * It manages three main bet boxes for player, banker, and tie bets, handling bet selection and chip management.
 */
public class BettingAreaPanel extends JPanel implements BetBox.SelectionListener {
    private final BetBox playerBetBox;
    private final BetBox bankerBetBox;
    private final BetBox tieBetBox;
    private BetBox selectedBox; // Track the currently selected bet box

    /**
     * Constructs a new BettingAreaPanel with specific layout and initialization for bet boxes.
     */
    public BettingAreaPanel() {
        setLayout(new GridLayout(3, 1, 0, 5));
        setOpaque(false);

        // Initialize bet boxes
        tieBetBox = new BetBox("TIE", Color.YELLOW);
        bankerBetBox = new BetBox("BANKER", Color.RED);
        playerBetBox = new BetBox("PLAYER", Color.WHITE);

        // Set this panel as the selection listener for all bet boxes
        playerBetBox.setSelectionListener(this);
        bankerBetBox.setSelectionListener(this);
        tieBetBox.setSelectionListener(this);

        // Add bet boxes to the panel
        add(tieBetBox);
        add(bankerBetBox);
        add(playerBetBox);
    }

    /**
     * Handles the event when a BetBox is selected. Ensures only one BetBox is selected at a time if no chips have been placed yet.
     *
     * @param selectedBox The BetBox that was just selected.
     */
    @Override
    public void onSelected(BetBox selectedBox) {
        if (playerBetBox.getChipsCount() == 0 && bankerBetBox.getChipsCount() == 0 && tieBetBox.getChipsCount() == 0) {
            // Deselect the previous selection if no chips are placed
            if (this.selectedBox != null) {
                this.selectedBox.setSelected(false);
            }

            // Select the new box
            this.selectedBox = selectedBox;
            this.selectedBox.setSelected(true);
        }
    }

    /**
     * Resets all selections in the betting area.
     */
    public void resetAllSelections() {
        if (selectedBox != null) {
            selectedBox.setSelected(false);
            selectedBox = null;
        }
    }

    /**
     * Returns the currently selected BetBox.
     *
     * @return The selected BetBox or null if no selection has been made.
     */
    public BetBox getSelectedBet() {
        return selectedBox;
    }

    /**
     * Returns the type of bet associated with the selected BetBox.
     *
     * @return The type of hand (PLAYER, BANKER, or TIE) corresponding to the selected bet.
     */
    public HandType getBoxType() {
        if (selectedBox != null) {
            return HandType.valueOf(selectedBox.getLabel().toUpperCase());
        }
        return null; // Return null if no box is selected
    }

    /**
     * Adds a chip to the currently selected BetBox.
     *
     * @param chip The chip to be added to the selected bet box.
     */
    public void addChip(Chip chip) {
        if (selectedBox != null) {
            selectedBox.addChip(chip);
        }
    }

    /**
     * Clears all chips from all BetBoxes in the panel.
     */
    public void clearChips() {
        playerBetBox.clearChips();
        bankerBetBox.clearChips();
        tieBetBox.clearChips();
    }
}
