package org.daniel.elysium.assets;

import org.daniel.elysium.debugUtils.DebugPrint;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

public enum CardAsset implements Asset {
    S2, S3, S4, S5, S6, S7, S8, S9, S10, SJ, SQ, SK, SA,
    H2, H3, H4, H5, H6, H7, H8, H9, H10, HJ, HQ, HK, HA,
    D2, D3, D4, D5, D6, D7, D8, D9, D10, DJ, DQ, DK, DA,
    C2, C3, C4, C5, C6, C7, C8, C9, C10, CJ, CQ, CK, CA;

    public static void loadAssets(Map<Asset, Image> imageCache){
        for (CardAsset asset: CardAsset.values()){
            try {
                ImageIcon icon = new ImageIcon(
                        Objects.requireNonNull(AssetManager.class.getClassLoader().
                                getResource("assets/cards/" + asset.toString())));
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
