package org.daniel.elysium.screens.models;

import javax.swing.*;

public class Chip {
    private final int value;
    private final ImageIcon icon;

    public Chip(int value, ImageIcon icon) {
        this.value = value;
        this.icon = icon;
    }

    public int getValue() {
        return value;
    }

    public ImageIcon getIcon() {
        return icon;
    }
}
