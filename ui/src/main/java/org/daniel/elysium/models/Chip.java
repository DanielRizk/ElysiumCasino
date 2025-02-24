package org.daniel.elysium.models;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.assets.ChipAsset;

import javax.swing.*;
import java.awt.*;

public class Chip extends JButton {
    private final int value;
    private final ImageIcon icon;

    public Chip(ChipAsset icon) {
        setOpaque(false);
        setContentAreaFilled(false); // Prevents the button from painting a background.
        setBorderPainted(false);
        setFocusPainted(false);
        setPreferredSize(new Dimension(100, 100));
        this.icon = AssetManager.getScaledIcon(icon, 100, 100);
        setIcon(this.icon);
        this.value = icon.getValue();
    }

    public int getValue() {
        return value;
    }

    public ImageIcon getIcon() {
        return icon;
    }
}
