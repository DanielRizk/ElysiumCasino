package org.daniel.elysium.assets;

import org.daniel.elysium.debugUtils.DebugPrint;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

public enum BackgroundAsset implements Asset {
    BACKGROUND,
    LOGO_RAW,
    LOGO_SHADE,
    BLACKJACK_RULES,
    DIALOG_BACKGROUND,
    TOAST_BG;

    public static void loadAssets(Map<Asset, Image> imageCache){
        for (BackgroundAsset asset: BackgroundAsset.values()){
            try {
                ImageIcon icon = new ImageIcon(
                        Objects.requireNonNull(AssetManager.class.getClassLoader().
                                getResource("assets/backgrounds/" + asset.toString())));
                imageCache.put(asset, icon.getImage());
            } catch (Exception e) {
                DebugPrint.println("Failed to load asset: " + asset, true);
            }
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase().concat(".png");
    }
}
