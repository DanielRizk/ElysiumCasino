package org.daniel.elysium.assets;

import java.awt.*;
import java.util.Map;

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

    /** Returns the value of the chips as integer */
    public int getValue(){
        return value;
    }

    /** Loads all assets from the respective recourse directory */
    public static void loadAssets(Map<Asset, Image> imageCache){
        AssetUtility.loadAssets(imageCache, "chips", ChipAsset.class);
    }

    @Override
    public String toString() {
        return name().toLowerCase().concat(".png");
    }
}
