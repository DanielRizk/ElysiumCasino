package org.daniel.elysium.elements.notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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

        // Timer to auto-close the toast.
        new Timer(displayTimeMillis, e -> dispose()).start();
    }

    private void repositionToast(JFrame owner) {
        Dimension ownerSize = owner.getSize();
        Point ownerLocation = owner.getLocation();
        int x = ownerLocation.x + ownerSize.width - getWidth() - 50;
        int y = ownerLocation.y + ownerSize.height - getHeight() - 50;
        setLocation(x, y);
    }
}

