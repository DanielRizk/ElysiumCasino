package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.elements.panels.VerticalLinePanel;

import javax.swing.*;
import java.awt.*;

public class CardsAreaPanel extends JPanel {
    public CardsAreaPanel() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 40, 0, 40);

        // Add Player Area
        add(new PlayerAreaUI(), gbc);

        // Add Vertical Line
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;  // Prevent stretching
        gbc.weighty = 0; // Prevent it from taking full height
        gbc.anchor = GridBagConstraints.CENTER; // Keep it in the center
        add(new VerticalLinePanel(300), gbc); // Set height to 300px

        // Add Banker Area
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(new BankerAreaUI(), gbc);
    }
}
