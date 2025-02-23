package org.daniel.elysium.assets;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    // Cache for loaded images; keys are asset names (e.g., "background.jpg")
    private static final Map<Asset, Image> imageCache = new HashMap<>();

    static {
        ChipAsset.loadAssets(imageCache);
        ButtonAsset.loadAssets(imageCache);
        BackgroundAsset.loadAssets(imageCache);
        CardAsset.loadAssets(imageCache);
    }

    public static Image getImage(Asset assetName) {
        return imageCache.get(assetName);
    }

    // Utility method to get a scaled icon from a cached image
    public static ImageIcon getScaledIcon(Asset assetName, int width, int height) {
        if (width == 0 || height == 0) {
            width = 1920; // Default width
            height = 1080; // Default height
        }

        Image image = getImage(assetName);
        if (image != null) {
            Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } else {
            return null;
        }
    }

    public static Image getScaledImage(Asset assetName, int width, int height) {
        if (width == 0 || height == 0) {
            width = 1920; // Default width
            height = 1080; // Default height
        }

        Image image = getImage(assetName);
        if (image != null) {
            return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } else {
            return null;
        }
    }
}
