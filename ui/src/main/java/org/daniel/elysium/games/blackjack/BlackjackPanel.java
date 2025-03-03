package org.daniel.elysium.games.blackjack;

import org.daniel.elysium.interfaces.Resettable;
import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.elements.panels.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code BlackjackPanel} class represents the main panel for the Blackjack game.
 */
public class BlackjackPanel extends JPanel implements Resettable {
    private final BlackjackController controller;

    /**
     * Constructs the Blackjack game panel.
     *
     * @param stateManager The {@link StateManager} that manages application states and transitions.
     */
    public BlackjackPanel(StateManager stateManager) {
        setLayout(new BorderLayout());
        controller = new BlackjackController(stateManager);

        // Create the background container.
        BackgroundPanel background = new BackgroundPanel(BackgroundAsset.BACKGROUND);
        background.setLayout(new BorderLayout());

        // Assemble sub-panels from the controller.
        background.add(controller.getTopPanel(), BorderLayout.NORTH);
        background.add(controller.getGameAreaPanel(), BorderLayout.CENTER);

        add(background, BorderLayout.CENTER);
    }

    @Override
    public void reset() {
        controller.resetScreen();
    }

    @Override
    public void onRestart() {
        controller.restartScreen();
    }
}
