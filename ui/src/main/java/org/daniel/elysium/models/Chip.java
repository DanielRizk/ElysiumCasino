package org.daniel.elysium.models;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ChipAsset;

import javax.swing.*;
import java.awt.*;

public class Chip extends JButton {
    private final int value;
    private final ImageIcon icon;
    public static final Dimension chipDimension = new Dimension(100, 100);

    public Chip(ChipAsset icon) {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        // Get the icon and the value of the chip
        this.icon = AssetManager.getScaledIcon(icon, chipDimension);
        this.value = icon.getValue();

        // Set default size and icon of the chip
        setPreferredSize(chipDimension);
        setIcon(this.icon);
    }

    /** Getter for the chip value */
    public int getValue() {
        return value;
    }

    /** Getter for the chip icon */
    public ImageIcon getIcon() {
        return icon;
    }
}
