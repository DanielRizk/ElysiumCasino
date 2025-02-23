package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.constants.Asset;
import org.daniel.elysium.elements.StyledButton;
import org.daniel.elysium.managers.AssetManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ChipPanel extends JPanel {
    private static final int[] CHIP_VALUES = {10, 50, 100, 500, 1000};

    public ChipPanel(ChipSelectedListener chipSelectedListener) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (int chip : CHIP_VALUES) {
            // Create a chip button – here width/height and icon are set as needed.
            Asset asset = null;
            switch (chip){
                case 10 ->  asset =  Asset.CHIP_10;
                case 50 ->  asset = Asset.CHIP_50;
                case 100 -> asset = Asset.CHIP_100;
                case 500 -> asset = Asset.CHIP_500;
                case 1000 -> asset = Asset.CHIP_1000;
            }
            ImageIcon chipIcon = AssetManager.getScaledIcon(asset, 80, 80);
            StyledButton chipButton = chipButton = new StyledButton(80, 80, null, asset);
            chipButton.addActionListener(e -> chipSelectedListener.chipSelected(chip, chipIcon));
            add(chipButton);
        }
    }
}
