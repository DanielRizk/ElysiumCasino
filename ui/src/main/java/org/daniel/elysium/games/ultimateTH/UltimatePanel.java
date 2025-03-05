package org.daniel.elysium.games.ultimateTH;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.interfaces.Resettable;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code UltimatePanel} class represents the main panel for the Ultimate Texas Hold'em game.
 */
public class UltimatePanel extends JPanel implements Resettable {
    private final UltimateController controller;

    /**
     * Constructs the Ultimate_TH game panel.
     *
     * @param stateManager The {@link StateManager} that manages application states and transitions.
     */
    public UltimatePanel(StateManager stateManager) {
        setLayout(new BorderLayout());
        controller = new UltimateController(stateManager);

        // Create the background container.
        BackgroundPanel background = new BackgroundPanel(BackgroundAsset.BACKGROUND_ULTIMATE);
        background.setLayout(new BorderLayout());

        // Assemble sub-panels from the controller.
        //background.add(controller.getTopPanel(), BorderLayout.NORTH);
        //background.add(controller.getGameAreaPanel(), BorderLayout.CENTER);

        add(background, BorderLayout.CENTER);
    }

    @Override
    public void reset() {
        //controller.resetScreen();
    }

    @Override
    public void onRestart() {
        //controller.restartScreen();
    }
}
