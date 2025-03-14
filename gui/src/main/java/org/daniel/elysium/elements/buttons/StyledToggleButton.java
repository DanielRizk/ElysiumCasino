package org.daniel.elysium.elements.buttons;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ButtonAsset;

import javax.swing.*;
import java.awt.*;
public class StyledToggleButton extends JToggleButton {

    /** Default button dimensions (50x50). */
    private final Dimension defaultDimensions = new Dimension(80, 50);

    private final String side1Text;
    private final String side2Text;

    /**
     * Constructs a StyledToggleButton with default dimensions and a default button background.
     *
     * @param side1 The text displayed on the first side of the toggle.
     * @param side2 The text displayed on the second side of the toggle.
     */
    public StyledToggleButton(String side1, String side2) {
        this.side1Text = side1;
        this.side2Text = side2;
        setIcon(AssetManager.getScaledIcon(ButtonAsset.BUTTON_GREY_SHARP, defaultDimensions));
        basicButtonConfig();
    }

    /**
     * Constructs a StyledToggleButton with default dimensions and a specified asset as a background.
     *
     * @param side1 The text displayed on the first side of the toggle.
     * @param side2 The text displayed on the second side of the toggle.
     * @param asset The {@link Asset} to use as the button's background.
     */
    public StyledToggleButton(String side1, String side2, Asset asset) {
        this.side1Text = side1;
        this.side2Text = side2;
        setIcon(AssetManager.getScaledIcon(asset, defaultDimensions));
        basicButtonConfig();
    }

    /**
     * Constructs a StyledToggleButton with custom dimensions and a default button background.
     *
     * @param side1 The text displayed on the first side of the toggle.
     * @param side2 The text displayed on the second side of the toggle.
     * @param width The width of the toggle button.
     * @param height The height of the toggle button.
     */
    public StyledToggleButton(String side1, String side2, int width, int height) {
        this.side1Text = side1;
        this.side2Text = side2;
        Dimension dimension = new Dimension(width, height);
        setIcon(AssetManager.getScaledIcon(ButtonAsset.BUTTON_GREY_SHARP, dimension));
        basicButtonConfig();
    }

    /**
     * Constructs a StyledToggleButton with custom dimensions and a specified asset as a background.
     *
     * @param side1 The text displayed on the first side of the toggle.
     * @param side2 The text displayed on the second side of the toggle.
     * @param width The width of the toggle button.
     * @param height The height of the toggle button.
     * @param asset The {@link Asset} to use as the button's background.
     */
    public StyledToggleButton(String side1, String side2, int width, int height, Asset asset) {
        this.side1Text = side1;
        this.side2Text = side2;
        Dimension dimension = new Dimension(width, height);
        setIcon(AssetManager.getScaledIcon(asset, dimension));
        basicButtonConfig();
    }

    /**
     * Applies common toggle button configurations such as text alignment, font, and transparency settings.
     */
    private void basicButtonConfig() {
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);

        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);
        setForeground(Color.WHITE);
        setFont(new Font("Serif", Font.BOLD, 18));
        setUnselected();
    }

    /** Set the displayed text to the first side text */
    public void setSelected(){
        setText(side1Text);
    }

    /** Set the displayed text to the second side text */
    public void setUnselected(){
        setText(side2Text);
    }
}
