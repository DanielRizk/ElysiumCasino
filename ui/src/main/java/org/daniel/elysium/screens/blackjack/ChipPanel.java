package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.assets.ChipAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.assets.AssetManager;

import javax.swing.*;

public class ChipPanel extends JPanel {

    public ChipPanel(ChipSelectedListener chipSelectedListener) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (ChipAsset asset : ChipAsset.values()) {

            ImageIcon chipIcon = AssetManager.getScaledIcon(asset, 80, 80);
            StyledButton chipButton  = new StyledButton(80, 80, null, asset);
            chipButton.addActionListener(e -> chipSelectedListener.chipSelected(asset.getValue(), chipIcon));
            add(chipButton);
        }
    }
}
