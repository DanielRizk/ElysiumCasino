package org.daniel.elysium.assets;

import java.awt.*;
import java.util.Map;

public enum BackgroundAsset implements Asset {
    BACKGROUND,
    LOGO_RAW,
    LOGO_SHADE,
    BLACKJACK_RULES,
    DIALOG_BACKGROUND,
    TOAST_BG;

    /** Loads all assets from the respective recourse directory */
    public static void loadAssets(Map<Asset, Image> imageCache){
        AssetUtility.loadAssets(imageCache, "backgrounds", BackgroundAsset.class);
    }

    @Override
    public String toString() {
        return name().toLowerCase().concat(".png");
    }
}
