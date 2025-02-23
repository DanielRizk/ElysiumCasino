package org.daniel.elysium.elements.notifications;

import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.panels.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

public class StyledConfirmDialog extends JDialog {
    private boolean confirmed = false;

    public StyledConfirmDialog(JFrame owner, String message) {
        super(owner, "Quit", true);
        setUndecorated(true); // Optional: borderless look

        Font font = new Font("Serif", Font.BOLD, 18); //new Font("Segoe UI", Font.BOLD, 14)

        // Create the background panel with the image.
        BackgroundPanel backgroundPanel = new BackgroundPanel(BackgroundAsset.DIALOG_BACKGROUND);
        backgroundPanel.setPreferredSize(new Dimension(600, 300));
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        // Message label with transparent background.
        JLabel msgLabel = new JLabel(message, SwingConstants.CENTER);
        msgLabel.setFont(font);
        msgLabel.setForeground(Color.WHITE);
        msgLabel.setOpaque(false);
        msgLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backgroundPanel.add(msgLabel, BorderLayout.CENTER);

        // Button panel at the bottom.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        StyledButton yesButton = new StyledButton("Yes", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        StyledButton noButton = new StyledButton("No", ButtonAsset.BUTTON_DARK_BLUE_SHARP);

        yesButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        noButton.addActionListener(e -> dispose());

        buttonPanel.add(yesButton);
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(50,50));
        buttonPanel.add(spacer);
        buttonPanel.add(noButton);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(backgroundPanel);
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

