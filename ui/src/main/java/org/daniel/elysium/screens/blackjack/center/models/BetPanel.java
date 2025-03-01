package org.daniel.elysium.screens.blackjack.center.models;

import org.daniel.elysium.assets.ChipAsset;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.models.Chip;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Represents the betting panel where the player can place main and extra bets.
 * It includes two betting circles and a display to show the current bet amount.
 */
public class BetPanel extends JPanel {
    private final BetCircle mainBet;
    private final BetCircle extraBet;
    private final StyledTextField currentBetLabel;

    /**
     * Constructs the bet panel with a main bet circle, an extra bet circle, and a bet display.
     */
    public BetPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setOpaque(false);

        // Create and add the bet display text field
        currentBetLabel = new StyledTextField("0", new Dimension(150, 50), 9, false);
        add(currentBetLabel);

        // Create and add the main bet circle, set to visible
        mainBet = new BetCircle(true);
        add(mainBet);

        // Create and add the extra bet circle, set to invisible
        extraBet = new BetCircle(false);
        add(extraBet);
    }

    /**
     * Checks if a chip can be added to the main bet circle.
     *
     * @return True if the number of chips in the main bet is below the maximum limit, false otherwise.
     */
    public boolean canAddChip() {
        return mainBet.getChipsCount() < mainBet.getMaxChips();
    }

    /**
     * Adds a chip to the main bet circle.
     *
     * @param chip The chip to be added.
     */
    public void addChipMain(Chip chip) {
        mainBet.addChip(chip);
    }

    /**
     * Adds a chip to the extra bet circle.
     *
     * @param chip The chip to be added.
     */
    public void addChipExtra(Chip chip) {
        extraBet.addChip(chip);
    }

    /**
     * Retrieves the list of chips placed in the main bet circle.
     *
     * @return A list of chips in the main bet.
     */
    public List<Chip> getChipsMain() {
        return mainBet.getChips();
    }

    /**
     * Retrieves the list of chips placed in the extra bet circle.
     *
     * @return A list of chips in the extra bet.
     */
    public List<Chip> getChipsExtra() {
        return extraBet.getChips();
    }

    /**
     * Removes all chips from the main bet circle.
     */
    public void clearMainChips() {
        mainBet.clearChips();
    }

    /**
     * Removes all chips from the extra bet circle.
     */
    public void clearExtraChips() {
        extraBet.clearChips();
    }

    /**
     * Removes all chips from both the main and extra bet circles.
     */
    public void clearChips() {
        mainBet.clearChips();
        extraBet.clearChips();
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
     * Generates a possible combination of chips from the available denominations to match the given bet amount.
     *
     * @param bet The total bet amount to be represented in chips.
     * @return A list of chips representing the given bet amount.
     */
    public List<Chip> getChipCombination(int bet) {
        List<ChipAsset> assetsSorted = Arrays.stream(ChipAsset.values())
                .sorted(Comparator.comparingInt(ChipAsset::getValue).reversed())
                .toList();

        List<Chip> combination = new ArrayList<>();

        for (ChipAsset chipAsset : assetsSorted) {
            while (bet >= chipAsset.getValue()) {
                combination.add(new Chip(chipAsset));
                bet -= chipAsset.getValue();
            }
        }
        return combination;
    }
}
