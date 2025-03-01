package org.daniel.elysium.elements.notifications;

import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.elements.panels.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * A custom toast notification window that displays a temporary message to the user.
 * The notification fades away after a specified duration.
 */
public class Toast extends JWindow {

    /**
     * Constructs a toast notification with a given message and display duration.
     *
     * @param owner            The parent JFrame that owns this toast.
     * @param message          The message to be displayed in the toast.
     * @param displayTimeMillis The duration in milliseconds before the toast disappears.
     */
    public Toast(JFrame owner, String message, int displayTimeMillis) {
        super(owner);
        setBackground(new Color(0, 0, 0, 0));

        // A background panel that shows the toast texture/image.
        BackgroundPanel backgroundPanel = new BackgroundPanel(BackgroundAsset.TOAST_BG);
        backgroundPanel.setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 80));
        backgroundPanel.setOpaque(false);

        // Create text area for the message, use JTextArea to enable line wrap.
        JTextArea textArea = createTextArea(message);

        // Add the text area to the background panel.
        backgroundPanel.add(textArea, BorderLayout.CENTER);
        add(backgroundPanel);
        pack();

        // Initial positioning relative to the owner.
        repositionToast(owner);

        // Add a listener to update the toast's position when the owner is moved or resized.
        owner.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                repositionToast(owner);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                repositionToast(owner);
            }
        });

        // Timer to auto-close the toast after the specified duration.
        new Timer(displayTimeMillis, e -> dispose()).start();
    }

    /**
     * Creates and initializes a text area for displaying the message.
     * Ensures proper formatting and word wrapping.
     *
     * @param message The message to be displayed in the toast.
     * @return A configured {@link JTextArea} component.
     */
    private static JTextArea createTextArea(String message) {
        if (message.length() <= 60) {
            JTextArea textArea = new JTextArea(message);
            textArea.setEditable(false);
            textArea.setOpaque(false);
            textArea.setForeground(Color.WHITE);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            textArea.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
            textArea.setPreferredSize(new Dimension(280, 90));
            return textArea;
        } else {
            DebugPrint.println("Toast message length exceeded the limit of 60 characters", true);
            return new JTextArea();
        }
    }

    /**
     * Repositions the toast notification relative to the owner window.
     * Ensures that the toast appears in the bottom-right corner of the parent frame.
     *
     * @param owner The parent JFrame to position the toast relative to.
     */
    private void repositionToast(JFrame owner) {
        Dimension ownerSize = owner.getSize();
        Point ownerLocation = owner.getLocation();
        int x = ownerLocation.x + ownerSize.width - getWidth() - 50;
        int y = ownerLocation.y + ownerSize.height - getHeight() - 50;
        setLocation(x, y);
    }
}
