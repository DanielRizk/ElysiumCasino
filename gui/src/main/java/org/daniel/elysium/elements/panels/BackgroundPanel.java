package org.daniel.elysium.elements.panels;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.debugUtils.DebugPrint;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel that displays a background image, with optional scaling behavior.
 * The image can be scaled to fit the panel or displayed at a fixed size.
 */
public class BackgroundPanel extends JPanel {

    /** The background image to be displayed. */
    private Image backgroundImage;

    /** Flag to indicate whether the image should be scaled or not. */
    private boolean doNotScale = false;

    /**
     * Constructs a BackgroundPanel with a specified background image.
     * The image is automatically scaled to fit the panel by default.
     *
     * @param imageName The asset representing the background image.
     */
    public BackgroundPanel(Asset imageName) {
        try {
            backgroundImage = AssetManager.getImage(imageName);
        } catch (Exception e) {
            DebugPrint.println("Failed to load asset: " + imageName.toString(), true);
        }
        setLayout(new BorderLayout());
    }

    /**
     * Constructs a BackgroundPanel with a specified background image and scaling option.
     *
     * @param imageName   The asset representing the background image.
     * @param doNotScale  If {@code true}, the image will not be scaled and will be drawn at its original resolution.
     */
    public BackgroundPanel(Asset imageName, boolean doNotScale) {
        this.doNotScale = doNotScale;
        try {
            backgroundImage = AssetManager.getImage(imageName);
        } catch (Exception e) {
            DebugPrint.println("Failed to load asset: " + imageName.toString(), true);
        }
        setLayout(new BorderLayout());
    }

    /**
     * Paints the background image onto the panel.
     * The image is either scaled to fit the panel or drawn at a fixed size, based on the scaling flag.
     *
     * @param g The {@link Graphics} object used for rendering.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imgWidth = backgroundImage.getWidth(this);
            int imgHeight = backgroundImage.getHeight(this);

            // Determine the scaling factor to maintain aspect ratio
            double scale = Math.max((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
            int newWidth;
            int newHeight;

            // If scaling is disabled, set a large fixed size for the image
            if (doNotScale) {
                newWidth = imgWidth * 40;
                newHeight = imgHeight * 40;
            } else {
                newWidth = (int) (imgWidth * scale);
                newHeight = (int) (imgHeight * scale);
            }

            // Center the image within the panel
            int x = (panelWidth - newWidth) / 2;
            int y = (panelHeight - newHeight) / 2;

            // Draw the image onto the panel
            g.drawImage(backgroundImage, x, y, newWidth, newHeight, this);
        }
    }
}
