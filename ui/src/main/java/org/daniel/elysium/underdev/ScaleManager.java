package org.daniel.elysium.underdev;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static org.daniel.elysium.underdev.Resizable.*;

public class ScaleManager {

    // Current scale factor starts at medium.
    private static float scaleFactor = HIGH_SCALE;

    private static final List<Runnable> scaleListeners = new ArrayList<>();

    public static float getScaleFactor() {
        return scaleFactor;
    }

    private static final Timer debounceTimer = new Timer(100, e -> notifyListeners());
    static {
        debounceTimer.setRepeats(false);
    }

    private static float quantizeScale(float rawScale) {
        // You can define boundaries as halfway between your intervals.
        float lowMediumBoundary = (LOW_SCALE + MEDIUM_SCALE) / 2.0f;
        float mediumHighBoundary = (MEDIUM_SCALE + HIGH_SCALE) / 2.0f;

        if (rawScale < lowMediumBoundary) {
            return LOW_SCALE;
        } else if (rawScale < mediumHighBoundary) {
            return MEDIUM_SCALE;
        } else {
            return HIGH_SCALE;
        }
    }

    public static void setScaleFactor(float rawFactor) {
        float newFactor = quantizeScale(rawFactor);
        if (Math.abs(scaleFactor - newFactor) > 0.01f) {
            scaleFactor = newFactor;
            debounceTimer.restart();
        }
    }

    public static void addScaleListener(Runnable listener) {
        scaleListeners.add(listener);
    }

    public static void removeScaleListener(Runnable listener) {
        scaleListeners.remove(listener);
    }

    private static void notifyListeners() {
        SwingUtilities.invokeLater(() -> {
            for (Runnable listener : scaleListeners) {
                listener.run();
            }
        });
    }

    /*scaleUpdateTask = () -> {
        if (!this.isShowing()){
            return;
        }

        if (faceUp) {
            setIcon(AssetManager.getScaledIcon(asset, getScaledDimension()));
        } else {
            setIcon(AssetManager.getScaledIcon(CardAsset.BC, getScaledDimension()));
        }
    };

    // Register for scale changes
        ScaleManager.addScaleListener(scaleUpdateTask);

    @Override
    public Dimension getScaledDimension() {
        float scaleFactor = ScaleManager.getScaleFactor();
        if (scaleFactor == LOW_SCALE){
            return LOW_DIMENSION;
        } else if (scaleFactor == MEDIUM_SCALE){
            return MED_DIMENSION;
        } else {
            return HIGH_DIMENSION;
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        // Unregister listeners when card is removed
        ScaleManager.removeScaleListener(scaleUpdateTask);
    }

    frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Calculate the scale factor based on a base resolution
                float scaleFactorWidth = (float) frame.getWidth() / 1600;
                float scaleFactorHeight = (float) frame.getHeight() / 1200;

                ScaleManager.setScaleFactor(Math.min(scaleFactorWidth, scaleFactorHeight));
            }
        });


    */
}
