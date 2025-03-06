package org.daniel.elysium.assets;

import java.awt.*;
import java.util.Map;

/**
 * Enum representing games result assets.
 * These assets are used to display the outcomes of all games.
 */
public enum ResultAsset implements Asset {

    /** The asset representing a win result. */
    WIN,

    /** The asset representing a lost result. */
    LOST,

    /** The asset representing a folded hand or forfeited game. */
    FOLD,

    /** The asset representing a push result (tie with no loss/gain). */
    PUSH,

    /** The asset representing a tie result between players. */
    TIE,

    /** The asset representing an insured bet outcome (common in Blackjack). */
    INSURED,

    /** The asset representing a Blackjack (natural 21) result. */
    BLACKJACK,

    /** The asset representing a Royal Flush poker hand. */
    ROYAL_FLUSH,

    /** The asset representing a Straight Flush poker hand. */
    STRAIGHT_FLUSH,

    /** The asset representing a Four-of-a-Kind poker hand. */
    QUADS,

    /** The asset representing a Full House poker hand. */
    FULL_HOUSE,

    /** The asset representing a Flush poker hand. */
    FLUSH,

    /** The asset representing a Straight poker hand. */
    STRAIGHT,

    /** The asset representing a Three-of-a-Kind poker hand. */
    TRIPS,

    /** The asset representing a Two Pair poker hand. */
    TWO_PAIR,

    /** The asset representing a One Pair poker hand. */
    PAIR,

    /** The asset representing a High Card poker result. */
    HIGH_CARD,

    /** The asset representing a 1x payout multiplier. */
    X1,

    /** The asset representing a 1.5x payout multiplier. */
    X1_5,

    /** The asset representing a 3x payout multiplier. */
    X3,

    /** The asset representing a 4x payout multiplier. */
    X4,

    /** The asset representing a 7x payout multiplier. */
    X7,

    /** The asset representing a 8x payout multiplier. */
    X8,

    /** The asset representing a 10x payout multiplier. */
    X10,

    /** The asset representing a 30x payout multiplier. */
    X30,

    /** The asset representing a 40x payout multiplier. */
    X40,

    /** The asset representing a 50x payout multiplier. */
    X50,

    /** The asset representing a 500x payout multiplier. */
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
