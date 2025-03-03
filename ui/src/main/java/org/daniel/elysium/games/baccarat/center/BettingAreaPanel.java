package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.baccarat.HandType;
import org.daniel.elysium.models.cards.UICard;
import org.daniel.elysium.models.chips.Chip;

import javax.swing.*;
import java.awt.*;

public class BettingAreaPanel extends JPanel implements BetBox.SelectionListener {
    private final BetBox playerBetBox;
    private final BetBox bankerBetBox;
    private final BetBox tieBetBox;
    private BetBox selectedBox = null; // Track selected box

    public BettingAreaPanel() {
        setLayout(new GridLayout(3, 1, 0, 5)); // Stack boxes with no spacing
        setOpaque(false);

        // Create individual bet boxes
        tieBetBox = new BetBox("TIE");
        bankerBetBox = new BetBox("BANKER");
        playerBetBox = new BetBox("PLAYER");


        // Assign selection listener
        playerBetBox.setSelectionListener(this);
        bankerBetBox.setSelectionListener(this);
        tieBetBox.setSelectionListener(this);

        // Add to panel
        add(tieBetBox);
        add(bankerBetBox);
        add(playerBetBox);

    }

    @Override
    public void onSelected(BetBox selectedBox) {
        if (playerBetBox.getChipsCount() == 0
                && bankerBetBox.getChipsCount() == 0
                && tieBetBox.getChipsCount() == 0)
        {
            // Deselect previous selection
            if (this.selectedBox != null) {
                this.selectedBox.setSelected(false);
            }

            // Select new box
            this.selectedBox = selectedBox;
            this.selectedBox.setSelected(true);
        }
    }

    // Reset selection
    public void resetAllSelections() {
        if (selectedBox != null) {
            selectedBox.setSelected(false);
            selectedBox = null;
        }
    }

    // Get selected bet
    public BetBox getSelectedBet() {
        return selectedBox;
    }

    public HandType getBoxType(){
        if (selectedBox.getLabel().equals("BANKER")){
            return HandType.BANKER;
        } else if (selectedBox.getLabel().equals("PLAYER")){
            return HandType.PLAYER;
        } else {
            return HandType.TIE;
        }
    }

    public void clearChips(){
        playerBetBox.clearChips();
        bankerBetBox.clearChips();
        tieBetBox.clearChips();
    }

    public void addChip(Chip chip){
        if (selectedBox != null){
            selectedBox.addChip(chip);
        }
    }

}


