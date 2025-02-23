package org.daniel.elysium.managers;

import org.daniel.elysium.constants.Asset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssetManager {

    // Cache for loaded images; keys are asset names (e.g., "background.jpg")
    private static final Map<Asset, Image> imageCache = new HashMap<>();

    static {
        // Pre-load your assets here.
        // You can list all assets you know you'll use:
        loadAsset("bg.png", Asset.BACKGROUND);
        loadAsset("logo.png", Asset.LOGO);
        loadAsset("logo_shade.png", Asset.LOGO_SHADE);
        loadAsset("btn_db_s.png", Asset.BUTTON_DB_SHARP);
        loadAsset("btn_db_r.png", Asset.BUTTON_DB_ROUND);
        loadAsset("btn_b_s.png", Asset.BUTTON_B_SHARP);
        loadAsset("btn_b_r.png", Asset.BUTTON_B_ROUND);
        loadAsset("btn_g_s.png", Asset.BUTTON_G_SHARP);
        loadAsset("btn_g_r.png", Asset.BUTTON_G_ROUND);
        loadAsset("BlackjackStencil.png", Asset.BLACKJACK);
        loadAsset("Chip_10.png", Asset.CHIP_10);
        loadAsset("Chip_50.png", Asset.CHIP_50);
        loadAsset("Chip_100.png", Asset.CHIP_100);
        loadAsset("Chip_500.png", Asset.CHIP_500);
        loadAsset("Chip_1000.png", Asset.CHIP_1000);
        loadAsset("CardFront.png", Asset.CARD_FRONT);
        loadAsset("CardBack.png", Asset.CARD_BACK);
        // Add more assets as needed
    }

    private static void loadAsset(String assetPath, Asset assetName) {
        try {
            // Load the image from the assets folder in the resources directory.
            ImageIcon icon = new ImageIcon(
                    Objects.requireNonNull(AssetManager.class.getClassLoader().getResource("assets/" + assetPath))
            );
            imageCache.put(assetName, icon.getImage());
        } catch (Exception e) {
            System.err.println("Failed to load asset: " + assetName);
            e.printStackTrace();
        }
    }

    public static Image getImage(Asset assetName) {
        return imageCache.get(assetName);
    }

    // Utility method to get a scaled icon from a cached image
    public static ImageIcon getScaledIcon(Asset assetName, int width, int height) {
        if (width == 0 || height == 0) {
            width = 1920; // Default width
            height = 1080; // Default height
        }

        Image image = getImage(assetName);
        if (image != null) {
            Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } else {
            return null;
        }
    }

    public static Image getScaledImage(Asset assetName, int width, int height) {
        if (width == 0 || height == 0) {
            width = 1920; // Default width
            height = 1080; // Default height
        }

        Image image = getImage(assetName);
        if (image != null) {
            Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return scaled;
        } else {
            return null;
        }
    }
}
