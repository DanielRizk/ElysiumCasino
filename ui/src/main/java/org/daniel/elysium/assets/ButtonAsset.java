package org.daniel.elysium.assets;

import java.awt.*;
import java.util.Map;

/**
 * Enum representing button assets used in the game.
 * Each asset corresponds to a different button style or color variation.
 */
public enum ButtonAsset implements Asset {

    /** A sharp-edged dark blue button. */
    BUTTON_DARK_BLUE_SHARP,

    /** A round dark blue button. */
    BUTTON_DARK_BLUE_ROUND,

    /** A sharp-edged blue button. */
    BUTTON_BLUE_SHARP,

    /** A round blue button. */
    BUTTON_BLUE_ROUND,

    /** A sharp-edged grey button. */
    BUTTON_GREY_SHARP,

    /** A round grey button. */
    BUTTON_GREY_ROUND;

    /**
     * Loads all button assets from the respective resource directory.
     *
     * @param imageCache The cache where the loaded images will be stored.
     */
    public static void loadAssets(Map<Asset, Image> imageCache) {
        AssetUtility.loadAssets(imageCache, "buttons", ButtonAsset.class);
    }

    /**
     * Returns the filename of the asset in lowercase with a ".png" extension.
     *
     * @return The filename of the asset (e.g., "button_dark_blue_sharp.png").
     */
    @Override
    public String toString() {
        return name().toLowerCase().concat(".png");
    }
}
