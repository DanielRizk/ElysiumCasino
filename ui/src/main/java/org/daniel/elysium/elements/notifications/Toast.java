package org.daniel.elysium.elements.notifications;

import javax.swing.*;
import java.awt.*;

public class Toast extends JWindow {
    public Toast(JFrame owner, String message, int displayTimeMillis) {
        super(owner);
        JLabel label = new JLabel(message);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 170));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(label);
        pack();

        // Position at the bottom center of the owner frame.
        Dimension ownerSize = owner.getSize();
        Point ownerLocation = owner.getLocation();
        int x = ownerLocation.x + (ownerSize.width - getWidth()) / 2;
        int y = ownerLocation.y + ownerSize.height - getHeight() - 50;
        setLocation(x, y);

        // Timer to auto-close the toast.
        new Timer(displayTimeMillis, e -> dispose()).start();
    }
    // USAGE: new Toast(frame, "This is a toast notification", 3000).setVisible(true);
}
