package org.daniel.elysium.games.blackjack;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.elements.panels.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * The {@code BlackjackPanel} class represents the main panel for the Blackjack game.
 * It manages the game layout, background, and chip panel positioning.
 */
public class BlackjackPanel extends JPanel {
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

    /**
     * Adds the chip panel to the frame's layered pane after the component is added to the UI.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                JLayeredPane layeredPane = frame.getRootPane().getLayeredPane();
                if (controller.getChipPanel().getParent() != layeredPane) {
                    layeredPane.add(controller.getChipPanel(), JLayeredPane.POPUP_LAYER);
                }

                // Re-display the chip panel
                repositionChipPanel();
                controller.getChipPanel().setVisible(true);

                // Adjust the location of the chip panel when the parent is resized
                frame.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        repositionChipPanel();
                    }
                });
            }
        });
    }

    /**
     * Removes and hides the chip panel from the frame's layered pane when the panel is removed.
     */
    @Override
    public void removeNotify() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            JLayeredPane layeredPane = frame.getRootPane().getLayeredPane();
            layeredPane.remove(controller.getChipPanel());
            layeredPane.repaint();
        }
        super.removeNotify();
    }

    /**
     * Adjusts the position of the chip panel relative to the game window.
     * It ensures the chip panel remains in the bottom-left corner of the window.
     */
    private void repositionChipPanel() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            Dimension pref = controller.getChipPanel().getPreferredSize();
            int chipPanelWidth = pref.width;
            int chipPanelHeight = pref.height;
            int yPos = frame.getHeight() - chipPanelHeight - 60;  // Margin from bottom
            int xPos = 20; // Margin from left
            controller.getChipPanel().setBounds(xPos, yPos, chipPanelWidth, chipPanelHeight);
            controller.getChipPanel().revalidate();
            controller.getChipPanel().repaint();
        }
    }
}
