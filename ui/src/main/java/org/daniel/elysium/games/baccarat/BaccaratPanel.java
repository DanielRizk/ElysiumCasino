package org.daniel.elysium.games.baccarat;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.interfaces.Resettable;

import javax.swing.*;
import java.awt.*;

public class BaccaratPanel extends JPanel implements Resettable {
    private final BaccaratController controller;

    public BaccaratPanel(StateManager stateManager) {
        setLayout(new BorderLayout());
        controller = new BaccaratController(stateManager);

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
