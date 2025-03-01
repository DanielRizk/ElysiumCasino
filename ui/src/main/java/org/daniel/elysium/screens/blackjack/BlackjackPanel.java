package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.elements.panels.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class BlackjackPanel extends JPanel {
    private final BlackjackController controller;

    public BlackjackPanel(StateManager stateManager) {
        setLayout(new BorderLayout());
        controller = new BlackjackController(stateManager);

        // Create the background container.
        BackgroundPanel background = new BackgroundPanel(BackgroundAsset.BACKGROUND);
        background.setLayout(new BorderLayout());

        // Assemble sub_panels from the controller.
        background.add(controller.getTopPanel(), BorderLayout.NORTH);
        background.add(controller.getGameAreaPanel(), BorderLayout.CENTER);

        add(background, BorderLayout.CENTER);
    }

    /** Add the chip panel to the frame's layered pane. */
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

                // Adjust the location of the chip panel when the parent moves
                frame.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        repositionChipPanel();
                    }
                });
            }
        });
    }

    /** Hide and remove the chip panel to the frame's layered pane. */
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

    /** Adjust the location of the chip panel relative to the parent */
    private void repositionChipPanel() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            Dimension pref = controller.getChipPanel().getPreferredSize();
            int chipPanelWidth = pref.width;
            int chipPanelHeight = pref.height;
            int yPos = frame.getHeight() - chipPanelHeight - 60;  // margin from bottom
            int xPos = 20; // margin from left
            controller.getChipPanel().setBounds(xPos, yPos, chipPanelWidth, chipPanelHeight);
            controller.getChipPanel().revalidate();
            controller.getChipPanel().repaint();
        }
    }
}


