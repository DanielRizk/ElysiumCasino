package org.daniel.elysium.elements.notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NotificationDialog extends JDialog {
    public NotificationDialog(JFrame parent, String message, int displayTimeMillis) {
        super(parent, false); // false for non-modal
        setUndecorated(true);  // Optional: removes title bar
        JLabel label = new JLabel(message);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(label, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(parent);

        // Auto-close timer
        Timer timer = new Timer(displayTimeMillis, (ActionEvent e) -> dispose());
        timer.setRepeats(false);
        timer.start();
    }

    // USAGE: new NotificationDialog(frame, "Your notification message", 3000).setVisible(true);
}
