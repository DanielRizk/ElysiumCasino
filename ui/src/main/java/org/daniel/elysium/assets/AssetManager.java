package org.daniel.elysium.assets;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    // Cache for loaded images;
    private static final Map<Asset, Image> imageCache = new HashMap<>();

    // load all assets statically into the cache
    static {
        ChipAsset.loadAssets(imageCache);
        ButtonAsset.loadAssets(imageCache);
        BackgroundAsset.loadAssets(imageCache);
        CardAsset.loadAssets(imageCache);
    }

    /** Utility method to get a scaled icon from a cached image */
    public static ImageIcon getScaledIcon(Asset assetName, Dimension dimension) {
        if (dimension.width == 0 || dimension.height == 0) {
            dimension.width = 1920; // Default width
            dimension.height = 1080; // Default height
        }

        Image image = getImage(assetName);
        if (image != null) {
            Image scaled = image.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } else {
            return null;
        }
    }

    /** Utility method to get a scaled Image from a cached image */
    public static Image getScaledImage(Asset assetName, Dimension dimension) {
        if (dimension.width == 0 || dimension.height == 0) {
            dimension.width = 1920; // Default width
            dimension.height = 1080; // Default height
        }

        Image image = getImage(assetName);
        if (image != null) {
            return image.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);
        } else {
            return null;
        }
    }

    /** Utility method to get a raw Image from a cached image */
    public static Image getImage(Asset assetName) {
        return imageCache.get(assetName);
    }
}
