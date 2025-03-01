package org.daniel.elysium.elements.panels;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.debugUtils.DebugPrint;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private boolean doNotScale = false;

    public BackgroundPanel(Asset imageName) {
        try {
            backgroundImage =  AssetManager.getImage(imageName);
        } catch (Exception e) {
            DebugPrint.println("Failed to load asset: "  + imageName.toString(), true);
        }
        setLayout(new BorderLayout());
    }

    public BackgroundPanel(Asset imageName, boolean doNotScale) {
        this.doNotScale = doNotScale;
        try {
            backgroundImage =  AssetManager.getImage(imageName);
        } catch (Exception e) {
            DebugPrint.println("Failed to load asset: "  + imageName.toString(), true);
        }
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imgWidth = backgroundImage.getWidth(this);
            int imgHeight = backgroundImage.getHeight(this);

            double scale = Math.max((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
            int newWidth;
            int newHeight;
            if (doNotScale){
                newWidth = imgWidth * 40;
                newHeight = imgHeight * 40;
            } else {
                newWidth = (int) (imgWidth * scale);
                newHeight = (int) (imgHeight * scale);
            }
            int x = (panelWidth - newWidth) / 2;
            int y = (panelHeight - newHeight) / 2;
            g.drawImage(backgroundImage, x, y, newWidth, newHeight, this);
        }
    }
}