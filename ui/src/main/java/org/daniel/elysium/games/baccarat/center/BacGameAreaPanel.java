package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.interfaces.Mediator;

import javax.print.attribute.standard.Media;
import javax.swing.*;
import java.awt.*;

public class BacGameAreaPanel extends JPanel {

    private final CardsAreaPanel cardsAreaPanel;
    private final StyledButton dealButton;
    private final Mediator mediator;

    public BacGameAreaPanel(Mediator mediator, StateManager stateManager) {
        this.mediator = mediator;
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Define layout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Cards area
        gbc.gridy = 1;
        gbc.weighty = 0.10;
        cardsAreaPanel = new CardsAreaPanel();
        add(cardsAreaPanel, gbc);

        // Deal Button
        gbc.gridy = 2;
        gbc.weighty = 0.05;
        JPanel dealButtonContainer = new JPanel(new BorderLayout());
        dealButtonContainer.setOpaque(false);
        dealButton = new StyledButton("DEAL", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        dealButton.setHorizontalAlignment(SwingConstants.CENTER);
        dealButton.setVisible(false);
        dealButton.addActionListener(e -> mediator.onDealRequested());
        dealButtonContainer.add(dealButton, BorderLayout.CENTER);
        add(dealButtonContainer, gbc);
    }
}
