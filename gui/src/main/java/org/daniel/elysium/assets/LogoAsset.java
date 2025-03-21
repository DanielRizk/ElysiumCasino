package org.daniel.elysium.assets;

import java.awt.*;
import java.util.Map;

/**
 * Enum representing logos and icons assets used in the game.
 * Each asset corresponds to a logo or an icon image resource.
 */
public enum LogoAsset implements Asset {

    /** The main game icon. */
    APP_ICON,

    /** The raw version of the game logo. */
    //LOGO_RAW,

    /** The shaded version of the game logo. */
    LOGO_SHADE,

    /** The Blackjack rules background. */
    BLACKJACK_RULES;

    /**
     * Loads all logos and icons assets from the respective resource directory.
     *
     * @param imageCache The cache where the loaded images will be stored.
     */
    public static void loadAssets(Map<Asset, Image> imageCache) {
        AssetUtility.loadAssets(imageCache, "logos", LogoAsset.class);
    }

    /**
     * Returns the filename of the asset in lowercase with a ".png" extension.
     *
     * @return The filename of the asset (e.g., "logo.png").
     */
    @Override
    public String toString() {
        return name().toLowerCase().concat(".png");
    }
}
