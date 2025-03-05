package org.daniel.elysium.models.chips;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ChipAsset;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Represents a casino chip as a styled button.
 * The chip has a specific value and an associated image icon.
 */
public class Chip extends JButton {

    /** The monetary value of the chip. */
    private final int value;

    /** The image icon representing the chip. */
    private final ImageIcon icon;

    /** The default dimensions for all chips. */
    public static final Dimension chipDimension = new Dimension(100, 100);

    /**
     * Constructs a Chip with the specified icon.
     * The icon represents the chip's appearance and value.
     *
     * @param icon The {@link ChipAsset} enum representing the chip's image and value.
     */
    public Chip(ChipAsset icon) {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        // Get the icon and the value of the chip
        this.icon = AssetManager.getScaledIcon(icon, chipDimension);
        this.value = icon.getValue();

        // Set default size and icon of the chip
        setPreferredSize(chipDimension);
        setIcon(this.icon);
    }

    /**
     * Returns the monetary value of the chip.
     *
     * @return The integer value of the chip.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the image icon representing the chip.
     *
     * @return The {@link ImageIcon} of the chip.
     */
    @Override
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Generates a possible combination of chips from the available denominations to match the given bet amount.
     *  <p>
     *  Max is 30 chips to enhance performance.
     * @param bet The total bet amount to be represented in chips.
     * @return A list of chips representing the given bet amount.
     */
    public static List<Chip> getChipCombination(int bet) {
        List<ChipAsset> assetsSorted = Arrays.stream(ChipAsset.values())
                .sorted(Comparator.comparingInt(ChipAsset::getValue).reversed())
                .toList();

        List<Chip> combination = new ArrayList<>(30);

        for (ChipAsset chipAsset : assetsSorted) {
            if (combination.size() >= 30) break;  // Stop if we already have 30 chips

            int chipValue = chipAsset.getValue();
            int count = bet / chipValue;

            if (count > 0) {
                // Determine how many chips can be added without exceeding the limit
                int availableSlots = 30 - combination.size();
                int chipsToAdd = Math.min(count, availableSlots);

                // Add the chips in bulk; note that if Chip is immutable, sharing instances is acceptable.
                combination.addAll(Collections.nCopies(chipsToAdd, new Chip(chipAsset)));

                bet -= chipsToAdd * chipValue;
            }
        }

        return combination;
    }
}
