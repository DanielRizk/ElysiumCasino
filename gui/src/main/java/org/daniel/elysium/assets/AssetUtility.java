package org.daniel.elysium.assets;

import org.daniel.elysium.debugUtils.DebugPrint;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for loading assets into a cache.
 * This class provides a generic method to load assets from an {@link Enum} that implements {@link Asset}.
 */
public class AssetUtility {

    /**
     * Loads assets from a specified directory and stores them in the provided cache.
     * This method is designed for use with enumerations that implement the {@link Asset} interface.
     *
     * @param <E>        The type of enum implementing {@link Asset}.
     * @param imageCache The cache where loaded images will be stored.
     * @param dir        The directory where the assets are located.
     * @param enumClass  The enumeration class containing asset definitions.
     */
    public static <E extends Enum<E> & Asset> void loadAssets(Map<Asset, Image> imageCache, String dir, Class<E> enumClass) {
        for (E asset : enumClass.getEnumConstants()) {
            try {
                String resourcePath = "assets/" + dir + "/" + asset.toString();
                ImageIcon icon = new ImageIcon(
                        Objects.requireNonNull(
                                AssetManager.class.getClassLoader().getResource(resourcePath)));
                imageCache.put(asset, icon.getImage());
            } catch (Exception e) {
                DebugPrint.println("Failed to load asset: " + asset + " from " + dir);
            }
        }
    }
}

