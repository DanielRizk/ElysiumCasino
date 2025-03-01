package org.daniel.elysium.uiUtils;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class GlowBorder extends AbstractBorder {
    private final Color glowColor;
    private final int thickness;

    public GlowBorder(Color glowColor, int thickness) {
        this.glowColor = glowColor;
        this.thickness = thickness;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a thick rectangle (or round-rect) for a "glow"
        g2.setColor(glowColor);
        g2.setStroke(new BasicStroke(thickness));
        // For a round-rect:
        g2.drawRoundRect(x + thickness/2, y + thickness/2,
                width - thickness, height - thickness,
                thickness * 2, thickness * 2);

        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }
}
