package org.daniel.elysium.assets;

import org.daniel.elysium.debugUtils.DebugPrint;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

public enum ChipAsset implements Asset {
    CHIP_1000(1000),
    CHIP_500(500),
    CHIP_100(100),
    CHIP_50(50),
    CHIP_10(10);

    private final int value;

    ChipAsset(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static void loadAssets(Map<Asset, Image> imageCache){
        for (ChipAsset asset: ChipAsset.values()){
            try {
                ImageIcon icon = new ImageIcon(
                        Objects.requireNonNull(AssetManager.class.getClassLoader().
                                getResource("assets/chips/" + asset.toString())));
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
