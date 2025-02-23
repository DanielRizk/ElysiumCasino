package org.daniel.elysium.assets;

import org.daniel.elysium.debugUtils.DebugPrint;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

public enum ButtonAsset implements Asset {
    BUTTON_DARK_BLUE_SHARP,
    BUTTON_DARK_BLUE_ROUND,
    BUTTON_BLUE_SHARP,
    BUTTON_BLUE_ROUND,
    BUTTON_GREY_SHARP,
    BUTTON_GREY_ROUND;

    public static void loadAssets(Map<Asset, Image> imageCache){
        for (ButtonAsset asset: ButtonAsset.values()){
            try {
                ImageIcon icon = new ImageIcon(
                        Objects.requireNonNull(AssetManager.class.getClassLoader().
                                getResource("assets/buttons/" + asset.toString())));
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