package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ChipAsset;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.models.Chip;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BetPanel extends JPanel {
    private final BetCircle mainBet;
    private final BetCircle extraBet;
    private final StyledTextField currentBetLabel;

    public BetPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        setOpaque(false);
        mainBet = new BetCircle(true);
        extraBet = new BetCircle(false);
        add(extraBet);
        add(mainBet);
        currentBetLabel = new StyledTextField("0", 150, 50, 9, false);
        add(currentBetLabel);
    }

    public List<Chip> getChipsMain(){
        return mainBet.getChips();
    }

    public List<Chip> getChipsExtra(){
        return extraBet.getChips();
    }

    public void updateBetDisplay(int bet) {
        currentBetLabel.setText(String.valueOf(bet));
    }

    public boolean canAddChip() {
        return mainBet.getChipsCount() < mainBet.getMaxChips();
    }

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

    public void addChipMain(Chip chip) {
        mainBet.addChip(chip);
    }

    public void addChipExtra(Chip chip) {
        extraBet.addChip(chip);
    }

    public void clearInsuranceChips(){
        extraBet.clearChips();
    }

    public void clearMainChips(){
        mainBet.clearChips();
    }

    public void clearChips() {
        mainBet.clearChips();
        extraBet.clearChips();
    }
}
