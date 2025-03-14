package org.daniel.elysium.assets;

import java.awt.*;
import java.util.Map;

/**
 * Enum representing background assets used in the game.
 * Each asset corresponds to a background image resource.
 */
public enum BackgroundAsset implements Asset {

    /** The main game background. */
    BACKGROUND_MAIN,

    /** The background of blackjack game. */
    BACKGROUND_BLACKJACK,

    /** The background of baccarat game. */
    BACKGROUND_BACCARAT,

    /** The background of ultimate_th game. */
    BACKGROUND_ULTIMATE,

    /** The background for dialog windows. */
    DIALOG_BACKGROUND,

    /** The background for toast notifications. */
    TOAST_BG,

    /** The background for the splash screen*/
    SPLASH_SCREEN;

    /**
     * Loads all background assets from the respective resource directory.
     *
     * @param imageCache The cache where the loaded images will be stored.
     */
    public static void loadAssets(Map<Asset, Image> imageCache) {
        AssetUtility.loadAssets(imageCache, "backgrounds", BackgroundAsset.class);
    }

    /**
     * Returns the filename of the asset in lowercase with a ".png" extension.
     *
     * @return The filename of the asset (e.g., "background.png").
     */
    @Override
    public String toString() {
        return name().toLowerCase().concat(".png");
    }
}

