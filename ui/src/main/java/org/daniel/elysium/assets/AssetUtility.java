package org.daniel.elysium.assets;

import org.daniel.elysium.debugUtils.DebugPrint;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

public class AssetUtility {

    /** Utility method for all Asset sub_classes to load their assets */
    public static <E extends Enum<E> & Asset> void loadAssets(Map<Asset, Image> imageCache, String dir, Class<E> enumClass) {
        for (E asset : enumClass.getEnumConstants()) {
            try {
                String resourcePath = "assets/" + dir + "/" + asset.toString();
                ImageIcon icon = new ImageIcon(
                        Objects.requireNonNull(
                                AssetManager.class.getClassLoader().getResource(resourcePath)));
                imageCache.put(asset, icon.getImage());
            } catch (Exception e) {
                DebugPrint.println("Failed to load asset: " + asset + " from " + dir);
            }
        }
    }
}

