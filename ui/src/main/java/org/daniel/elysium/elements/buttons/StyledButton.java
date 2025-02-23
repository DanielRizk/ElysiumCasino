package org.daniel.elysium.elements.buttons;


import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;

import javax.swing.*;
import java.awt.*;

public class StyledButton extends JButton {
    public StyledButton(String text, Asset asset) {
        setPreferredSize(new Dimension(200, 50));
        setIcon(AssetManager.getScaledIcon(asset, 200, 50));
        basicButtonConfig(text);
    }

    public StyledButton(int width, int height, String text, Asset asset) {
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
