package org.daniel.elysium.assets;

import java.awt.*;
import java.util.Map;
//TODO: comment JAVADOC
/**
 * Enum representing games result assets.
 * These assets are used to display the outcomes of all games.
 */
public enum ResultAsset implements Asset {

    /** The asset representing a win result. */
    WIN,

    /** The asset representing a lost result. */
    LOST,

    FOLD,

    /** The asset representing a push result. */
    PUSH,

    /** The asset representing a tie result. */
    TIE,

    /** The asset representing an insured result. */
    INSURED,

    /** The asset representing a blackjack result. */
    BLACKJACK,

    ROYAL_F,
    STRAIGHT_F,
    QUADS,
    FULL_HOUSE,
    FLUSH,
    STRAIGHT,
    TRIPS,
    TWO_PAIR,
    PAIR,
    HIGH_CARD,
    X1,
    X1_5,
    X3,
    X4,
    X7,
    X8,
    X10,
    X30,
    X40,
    X50,
    X500;


    /**
     * Loads all Blackjack result assets from the respective resource directory.
     *
     * @param imageCache The cache where the loaded images will be stored.
     */
    public static void loadAssets(Map<Asset, Image> imageCache) {
        AssetUtility.loadAssets(imageCache, "results", ResultAsset.class);
    }

    /**
     * Returns the filename of the asset in lowercase with a ".png" extension.
     *
     * @return The filename of the asset (e.g., "win.png").
     */
    @Override
    public String toString() {
        return name().toLowerCase().concat(".png");
    }
}
