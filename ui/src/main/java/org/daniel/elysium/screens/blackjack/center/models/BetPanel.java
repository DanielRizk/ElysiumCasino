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

public class BetPanel extends JPanel {
    private final BetCircle mainBet;
    private final BetCircle extraBet;
    private final StyledTextField currentBetLabel;

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

    /** Checks if the chip count on the main bet did not exceed the max limit of chips */
    public boolean canAddChip() {
        return mainBet.getChipsCount() < mainBet.getMaxChips();
    }

    /** Add a chip to the main bet circle */
    public void addChipMain(Chip chip) {
        mainBet.addChip(chip);
    }

    /** Add a chip to the extra bet circle */
    public void addChipExtra(Chip chip) {
        extraBet.addChip(chip);
    }

    /** Returns a list of the chips on the main bet */
    public List<Chip> getChipsMain(){
        return mainBet.getChips();
    }

    /** Returns a list of the chips on the extra bet */
    public List<Chip> getChipsExtra(){
        return extraBet.getChips();
    }

    /** Removes all the chips on the main bet */
    public void clearMainChips(){
        mainBet.clearChips();
    }

    /** Removes all the chips on the extra bet */
    public void clearExtraChips(){
        extraBet.clearChips();
    }

    /** Removes all the chips on both the main and the extra bet */
    public void clearChips() {
        mainBet.clearChips();
        extraBet.clearChips();
    }

    /** Helper method to update the bet display text field */
    public void updateBetDisplay(int bet) {
        currentBetLabel.setText(String.valueOf(bet));
    }

    /** Helper method to generate a possible combination of the chips available from an integer */
    public List<Chip> getChipCombination(int bet){
        List<ChipAsset> assetsSorted = Arrays.stream(ChipAsset.values())
                .sorted(Comparator.comparingInt(ChipAsset::getValue).reversed())
                .toList();

        List<Chip> combination = new ArrayList<>();

        for (ChipAsset chipAsset : assetsSorted){
            while (bet >= chipAsset.getValue()){
                combination.add(new Chip(chipAsset));
                bet -= chipAsset.getValue();
            }
        }
        return combination;
    }
}
