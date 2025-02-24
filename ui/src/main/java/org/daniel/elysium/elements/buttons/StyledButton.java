package org.daniel.elysium.elements.buttons;


import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ButtonAsset;

import javax.swing.*;
import java.awt.*;

public class StyledButton extends JButton {

    public StyledButton(String text) {
        setPreferredSize(new Dimension(200, 50));
        setIcon(AssetManager.getScaledIcon(ButtonAsset.BUTTON_DARK_BLUE_SHARP, 200, 50));
        basicButtonConfig(text);
    }

    public StyledButton(String text, Asset asset) {
        setPreferredSize(new Dimension(200, 50));
        setIcon(AssetManager.getScaledIcon(asset, 200, 50));
        basicButtonConfig(text);
    }

    public StyledButton (String text, int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setIcon(AssetManager.getScaledIcon(ButtonAsset.BUTTON_DARK_BLUE_SHARP, width, height));
        basicButtonConfig(text);
    }

    public StyledButton (String text, int width, int height, Asset asset) {
        setPreferredSize(new Dimension(width, height));
        setIcon(AssetManager.getScaledIcon(asset, width, height));
        basicButtonConfig(text);
    }

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
