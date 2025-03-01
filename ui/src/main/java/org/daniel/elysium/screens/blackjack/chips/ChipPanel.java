package org.daniel.elysium.screens.blackjack.chips;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.ChipAsset;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.screens.blackjack.BlackjackMediator;

import javax.swing.*;

public class ChipPanel extends JPanel {
    public ChipPanel(BlackjackMediator mediator, StateManager stateManager) {
        // Set the layout to be vertical
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // Iterate over the chip assets to get all available chips
        for (ChipAsset asset : ChipAsset.values()) {
            // If the current chip value is bigger that the current balance, do not include it.
            if (asset.getValue() <= stateManager.getProfile().getBalance()){
                // Create and add chip to panel
                Chip chip = new Chip(asset);
                add(chip);
                // Add chip action listener -> when click it will be added to bet panel
                chip.addActionListener(e -> mediator.onChipSelected(chip));

            }
        }
    }
}


