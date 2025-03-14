package org.daniel.elysium.elements.buttons;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ButtonAsset;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JButton with background images, custom dimensions, and styling.
 * This class allows creating buttons with different sizes and asset-based icons.
 */
public class StyledButton extends JButton {

    /** Default button dimensions (200x50). */
    private final Dimension defaultDimensions = new Dimension(200, 50);

    /**
     * Constructs a StyledButton with default dimensions and a default button background.
     *
     * @param text The text displayed on the button.
     */
    public StyledButton(String text) {
        setPreferredSize(defaultDimensions);
        setIcon(AssetManager.getScaledIcon(ButtonAsset.BUTTON_DARK_BLUE_SHARP, defaultDimensions));
        basicButtonConfig(text);
    }

    /**
     * Constructs a StyledButton with default dimensions and a specified asset as a background.
     *
     * @param text  The text displayed on the button.
     * @param asset The {@link Asset} to use as the button's background.
     */
    public StyledButton(String text, Asset asset) {
        setPreferredSize(defaultDimensions);
        setIcon(AssetManager.getScaledIcon(asset, defaultDimensions));
        basicButtonConfig(text);
    }

    /**
     * Constructs a StyledButton with custom dimensions and a default button background.
     *
     * @param text  The text displayed on the button.
     * @param width The width of the button.
     * @param height The height of the button.
     */
    public StyledButton(String text, int width, int height) {
        Dimension customDimension = new Dimension(width, height);
        setPreferredSize(customDimension);
        setIcon(AssetManager.getScaledIcon(ButtonAsset.BUTTON_DARK_BLUE_SHARP, customDimension));
        basicButtonConfig(text);
    }

    /**
     * Constructs a StyledButton with custom dimensions and a specified asset as a background.
     *
     * @param text  The text displayed on the button.
     * @param width The width of the button.
     * @param height The height of the button.
     * @param asset The {@link Asset} to use as the button's background.
     */
    public StyledButton(String text, int width, int height, Asset asset) {
        Dimension customDimension = new Dimension(width, height);
        setPreferredSize(customDimension);
        setIcon(AssetManager.getScaledIcon(asset, customDimension));
        basicButtonConfig(text);
    }

    /**
     * Applies common button configurations such as text alignment, font, and transparency settings.
     *
     * @param text The text to be displayed on the button.
     */
    private void basicButtonConfig(String text) {
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
