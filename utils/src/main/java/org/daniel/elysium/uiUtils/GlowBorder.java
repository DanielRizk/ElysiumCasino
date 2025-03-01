package org.daniel.elysium.uiUtils;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * A custom border that renders a glowing effect around a component.
 * This effect is achieved using a thick stroke in the specified color.
 */
public class GlowBorder extends AbstractBorder {

    /** The color of the glow effect. */
    private final Color glowColor;

    /** The thickness of the glow effect. */
    private final int thickness;

    /**
     * Constructs a {@code GlowBorder} with the specified color and thickness.
     *
     * @param glowColor The color of the glowing border.
     * @param thickness The thickness of the border glow effect.
     */
    public GlowBorder(Color glowColor, int thickness) {
        this.glowColor = glowColor;
        this.thickness = thickness;
    }

    /**
     * Paints the glowing border around the component.
     *
     * @param c      The component for which this border is being painted.
     * @param g      The graphics context to use for painting.
     * @param x      The x position of the border.
     * @param y      The y position of the border.
     * @param width  The width of the border area.
     * @param height The height of the border area.
     */
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a thick rectangle (or round-rect) for a "glow"
        g2.setColor(glowColor);
        g2.setStroke(new BasicStroke(thickness));

        // For a round-rect:
        g2.drawRoundRect(x + thickness / 2, y + thickness / 2,
                width - thickness, height - thickness,
                thickness * 2, thickness * 2);

        g2.dispose();
    }

    /**
     * Returns the insets defining the space occupied by the border.
     *
     * @param c The component for which this border insets are being determined.
     * @return An {@link Insets} object specifying the thickness on all sides.
     */
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }
}
