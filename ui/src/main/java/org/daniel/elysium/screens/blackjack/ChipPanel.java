package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.ChipAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.models.Chip;

import javax.swing.*;

public class ChipPanel extends JPanel {

    public ChipPanel(ChipSelectedListener chipSelectedListener, StateManager stateManager) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (ChipAsset asset : ChipAsset.values()) {
            if (asset.getValue() <= stateManager.getProfile().getBalance()){
                Chip chip = new Chip(asset);
                chip.addActionListener(e -> chipSelectedListener.chipSelected(chip));
                add(chip);
            }
        }
    }
}
