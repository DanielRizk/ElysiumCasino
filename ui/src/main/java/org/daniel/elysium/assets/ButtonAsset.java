package org.daniel.elysium.assets;

import java.awt.*;
import java.util.Map;

public enum ButtonAsset implements Asset {
    BUTTON_DARK_BLUE_SHARP,
    BUTTON_DARK_BLUE_ROUND,
    BUTTON_BLUE_SHARP,
    BUTTON_BLUE_ROUND,
    BUTTON_GREY_SHARP,
    BUTTON_GREY_ROUND;

    /** Loads all assets from the respective recourse directory */
    public static void loadAssets(Map<Asset, Image> imageCache){
        AssetUtility.loadAssets(imageCache, "buttons", ButtonAsset.class);
    }

    @Override
    public String toString() {
        return name().toLowerCase().concat(".png");
    }
}