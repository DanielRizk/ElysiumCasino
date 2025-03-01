package org.daniel.elysium.assets;

import java.awt.*;
import java.util.Map;

/**
 * Enum representing chip assets used in the game.
 * Each chip has a corresponding monetary value and is associated with an image.
 */
public enum ChipAsset implements Asset {

    /** Chip with a value of 1000. */
    CHIP_1000(1000),

    /** Chip with a value of 500. */
    CHIP_500(500),

    /** Chip with a value of 100. */
    CHIP_100(100),

    /** Chip with a value of 50. */
    CHIP_50(50),

    /** Chip with a value of 10. */
    CHIP_10(10);

    /** The numerical value of the chip. */
    private final int value;

    /**
     * Constructs a chip asset with the specified value.
     *
     * @param value The monetary value of the chip.
     */
    ChipAsset(int value) {
        this.value = value;
    }

    /**
     * Returns the value of the chip.
     *
     * @return The integer value of the chip.
     */
    public int getValue() {
        return value;
    }

    /**
     * Loads all chip assets from the respective resource directory.
     *
     * @param imageCache The cache where the loaded images will be stored.
     */
    public static void loadAssets(Map<Asset, Image> imageCache) {
        AssetUtility.loadAssets(imageCache, "chips", ChipAsset.class);
    }

    /**
     * Returns the filename of the asset in lowercase with a ".png" extension.
     *
     * @return The filename of the asset (e.g., "chip_1000.png").
     */
    @Override
    public String toString() {
        return name().toLowerCase().concat(".png");
    }
}
