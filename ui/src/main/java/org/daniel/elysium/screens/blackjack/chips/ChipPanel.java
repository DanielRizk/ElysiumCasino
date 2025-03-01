package org.daniel.elysium.screens.blackjack.chips;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.ChipAsset;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.screens.blackjack.BlackjackMediator;

import javax.swing.*;

/**
 * Panel that displays available betting chips for the player to select.
 */
public class ChipPanel extends JPanel {

    /**
     * Constructs the ChipPanel, displaying available chip options based on the player's balance.
     *
     * @param mediator      The mediator handling chip selection events.
     * @param stateManager  The state manager tracking the player's balance and game state.
     */
    public ChipPanel(BlackjackMediator mediator, StateManager stateManager) {
        // Set the layout to be vertical
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // Iterate over the chip assets to get all available chips
        for (ChipAsset asset : ChipAsset.values()) {
            // If the current chip value is bigger than the player's balance, do not include it.
            if (asset.getValue() <= stateManager.getProfile().getBalance()) {
                // Create and add chip to panel
                Chip chip = new Chip(asset);
                add(chip);

                // Add chip action listener -> when clicked, it will be added to the bet panel
                chip.addActionListener(e -> mediator.onChipSelected(chip));
            }
        }
    }
}
