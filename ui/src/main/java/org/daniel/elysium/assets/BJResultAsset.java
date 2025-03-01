package org.daniel.elysium.assets;

import java.awt.*;
import java.util.Map;

/**
 * Enum representing Blackjack result assets.
 * These assets are used to display the outcomes of a Blackjack game.
 */
public enum BJResultAsset implements Asset {

    /** The asset representing a win result. */
    WIN;

    /**
     * Loads all Blackjack result assets from the respective resource directory.
     *
     * @param imageCache The cache where the loaded images will be stored.
     */
    public static void loadAssets(Map<Asset, Image> imageCache) {
        AssetUtility.loadAssets(imageCache, "unused", BJResultAsset.class);
    }

    /**
     * Returns the filename of the asset in lowercase with a ".png" extension.
     *
     * @return The filename of the asset (e.g., "win.png").
     */
    @Override
    public String toString() {
        return name().toLowerCase().concat(".png");
    }
}
