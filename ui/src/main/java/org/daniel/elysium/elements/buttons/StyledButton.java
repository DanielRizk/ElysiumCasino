package org.daniel.elysium.elements.buttons;


import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ButtonAsset;

import javax.swing.*;
import java.awt.*;

/** Custom JButton with background and size functionalities */
public class StyledButton extends JButton {
    private final Dimension defaultDimensions = new Dimension(200, 50);

    public StyledButton(String text) {
        setPreferredSize(defaultDimensions);
        setIcon(AssetManager.getScaledIcon(ButtonAsset.BUTTON_DARK_BLUE_SHARP, defaultDimensions));
        basicButtonConfig(text);
    }

    public StyledButton(String text, Asset asset) {
        setPreferredSize(defaultDimensions);
        setIcon(AssetManager.getScaledIcon(asset, defaultDimensions));
        basicButtonConfig(text);
    }

    public StyledButton (String text, int width, int height) {
        Dimension customDimension = new Dimension(width, height);
        setPreferredSize(customDimension);
        setIcon(AssetManager.getScaledIcon(ButtonAsset.BUTTON_DARK_BLUE_SHARP, customDimension));
        basicButtonConfig(text);
    }

    public StyledButton (String text, int width, int height, Asset asset) {
        Dimension customDimension = new Dimension(width, height);
        setPreferredSize(customDimension);
        setIcon(AssetManager.getScaledIcon(asset, customDimension));
        basicButtonConfig(text);
    }

    /** Apply common button configurations */
    private void basicButtonConfig(String text){
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);

        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);
        setForeground(Color.WHITE);
        setFont(new Font("Serif", Font.BOLD, 18));
        setText(text);
    }
}
