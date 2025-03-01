package org.daniel.elysium.assets;

import java.awt.*;
import java.util.Map;

/**
 * Enum representing card assets used in the game.
 * Each asset corresponds to an image file of a standard playing card.
 *
 * <p>Cards are represented using the following format:</p>
 * <ul>
 *     <li><strong>S</strong> - Spades</li>
 *     <li><strong>H</strong> - Hearts</li>
 *     <li><strong>D</strong> - Diamonds</li>
 *     <li><strong>C</strong> - Clubs</li>
 *     <li>Ranks: 2-10, J (Jack), Q (Queen), K (King), A (Ace)</li>
 * </ul>
 * <p>For example:</p>
 * <ul>
 *     <li><code>S10</code> represents the 10 of Spades</li>
 *     <li><code>HA</code> represents the Ace of Hearts</li>
 *     <li><code>DJ</code> represents the Jack of Diamonds</li>
 * </ul>
 */
public enum CardAsset implements Asset {

    // Spades
    S2, S3, S4, S5, S6, S7, S8, S9, S10, SJ, SQ, SK, SA,

    // Hearts
    H2, H3, H4, H5, H6, H7, H8, H9, H10, HJ, HQ, HK, HA,

    // Diamonds
    D2, D3, D4, D5, D6, D7, D8, D9, D10, DJ, DQ, DK, DA,

    // Clubs
    C2, C3, C4, C5, C6, C7, C8, C9, C10, CJ, CQ, CK, CA,

    /** The back of a card (used for hidden dealer cards). */
    BC;

    /**
     * Loads all card assets from the respective resource directory.
     *
     * @param imageCache The cache where the loaded images will be stored.
     */
    public static void loadAssets(Map<Asset, Image> imageCache) {
        AssetUtility.loadAssets(imageCache, "cards", CardAsset.class);
    }

    /**
     * Returns the filename of the asset in lowercase with a ".png" extension.
     *
     * @return The filename of the asset (e.g., "s10.png", "ha.png").
     */
    @Override
    public String toString() {
        return name().toLowerCase().concat(".png");
    }
}

