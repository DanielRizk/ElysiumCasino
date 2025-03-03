package org.daniel.elysium.models.panels;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.interfaces.ChipPanelConsumer;
import org.daniel.elysium.interfaces.Mediator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

// TODO: JAVADOC
public class ChipPanelUtil {

    /**
     * Regenerates the chip panel by first removing the existing one and then revealing a new instance.
     * <p>
     * This ensures that the chip panel is always freshly created and dynamically updated.
     */
    public static void regenerateChipPanel(ChipPanelConsumer consumer, StateManager stateManager){
        removeChipPanel(consumer, stateManager);
        revealChipPanel(consumer, stateManager);
    }

    /**
     * Creates and displays a new instance of the chip panel.
     * <p>
     * This method runs on the Swing Event Dispatch Thread (EDT) to ensure thread safety.
     * It removes any existing chip panel, creates a new one, and adds it to the JLayeredPane.
     * The chip panel is then repositioned and made visible.
     * A component listener is also added to adjust its position when the main frame is resized.
     */
    public static void revealChipPanel(ChipPanelConsumer consumer, StateManager stateManager) {
        SwingUtilities.invokeLater(() -> {
            if (stateManager.getFrame() != null) {
                JLayeredPane layeredPane = stateManager.getFrame().getRootPane().getLayeredPane();

                // Create a new ChipPanel instance
                if (consumer instanceof Mediator mediator){
                    consumer.setChipPanel(new ChipPanel(mediator, stateManager));
                }

                // Add to the layered pane
                layeredPane.add(consumer.getChipPanel(), JLayeredPane.POPUP_LAYER);

                // Re-display the chip panel
                repositionChipPanel(consumer, stateManager);
                consumer.getChipPanel().setVisible(true);

                // Ensure UI refresh
                layeredPane.revalidate();
                layeredPane.repaint();

                // Adjust the location of the chip panel when the parent is resized
                stateManager.getFrame().addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        repositionChipPanel(consumer, stateManager);
                    }
                });
            }
        });
    }

    /**
     * Removes the currently displayed chip panel from the layered pane.
     * <p>
     * This method ensures that the chip panel is properly removed from the UI before a new one is created.
     * It runs on the Swing Event Dispatch Thread (EDT) to prevent concurrency issues and forces a UI update.
     */
    public static void removeChipPanel(ChipPanelConsumer consumer, StateManager stateManager) {
        SwingUtilities.invokeLater(() -> {
            if (stateManager.getFrame() != null) {
                JLayeredPane layeredPane = stateManager.getFrame().getRootPane().getLayeredPane();

                // Ensure the panel exists before trying to remove it
                if (consumer.getChipPanel() != null) {
                    layeredPane.remove(consumer.getChipPanel());
                    layeredPane.revalidate();
                    layeredPane.repaint();
                }
            }
        });
    }

    /**
     * Adjusts the position of the chip panel within the main frame.
     * <p>
     * The panel is positioned near the bottom left of the screen with a fixed margin.
     * This method ensures the chip panel is correctly placed after being added or when the window is resized.
     */
    public static void repositionChipPanel(ChipPanelConsumer consumer, StateManager stateManager) {
        if (stateManager.getFrame() != null) {
            Dimension pref = consumer.getChipPanel().getPreferredSize();
            int chipPanelWidth = pref.width;
            int chipPanelHeight = pref.height;
            int yPos = stateManager.getFrame().getHeight() - chipPanelHeight - 60;  // Margin from bottom
            int xPos = 20; // Margin from left
            consumer.getChipPanel().setBounds(xPos, yPos, chipPanelWidth, chipPanelHeight);
            consumer.getChipPanel().revalidate();
            consumer.getChipPanel().repaint();
        }
    }
}
