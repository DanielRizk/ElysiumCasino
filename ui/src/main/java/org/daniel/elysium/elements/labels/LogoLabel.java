package org.daniel.elysium.elements.labels;

import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;

import javax.swing.*;
import java.awt.*;

public class LogoLabel extends JLabel {
    public LogoLabel(Asset asset, Dimension dimension) {
        setIcon(AssetManager.getScaledIcon(asset, dimension));
    }
}
