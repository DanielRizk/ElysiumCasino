package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.ChipAsset;
import org.daniel.elysium.models.Chip;

import javax.swing.*;

public class ChipPanel extends JPanel {
    public ChipPanel(BlackjackMediator mediator, StateManager stateManager) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        for (ChipAsset asset : ChipAsset.values()) {
            if (asset.getValue() <= stateManager.getProfile().getBalance()){
                Chip chip = new Chip(asset);
                chip.addActionListener(e -> mediator.onChipSelected(chip));
                add(chip);
            }
        }
    }
}


