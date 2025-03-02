package org.daniel.elysium.assets;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the loading and caching of game assets such as images and icons.
 * This class provides utility methods to retrieve raw and scaled assets.
 */
public class AssetManager {

    /** Cache for storing loaded images. */
    private static final Map<Asset, Image> imageCache = new HashMap<>();

    // Load all assets statically into the cache
    static {
        ChipAsset.loadAssets(imageCache);
        ButtonAsset.loadAssets(imageCache);
        BackgroundAsset.loadAssets(imageCache);
        CardAsset.loadAssets(imageCache);
        BJResultAsset.loadAssets(imageCache);
    }

    /**
     * Retrieves a scaled {@link ImageIcon} from the cached assets.
     *
     * @param assetName The {@link Asset} to retrieve.
     * @param dimension The desired dimensions for scaling.
     * @return A scaled {@link ImageIcon}, or {@code null} if the asset is not found.
     */
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

    /**
     * Retrieves a scaled {@link Image} from the cached assets.
     *
     * @param assetName The {@link Asset} to retrieve.
     * @param dimension The desired dimensions for scaling.
     * @return A scaled {@link Image}, or {@code null} if the asset is not found.
     */
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

    /**
     * Retrieves the raw {@link Image} associated with the given asset.
     *
     * @param assetName The {@link Asset} to retrieve.
     * @return The {@link Image} if found, otherwise {@code null}.
     */
    public static Image getImage(Asset assetName) {
        return imageCache.get(assetName);
    }

    /**
     * Retrieves the raw {@link ImageIcon} associated with the given asset.
     *
     * @param assetName The {@link Asset} to retrieve.
     * @return The {@link ImageIcon} if found, otherwise {@code null}.
     */
    public static ImageIcon getIcon(Asset assetName) {
        return new ImageIcon(imageCache.get(assetName));
    }
}

