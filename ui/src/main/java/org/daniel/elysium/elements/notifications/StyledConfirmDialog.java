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
        super(owner, "Confirm", true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        // Create the background panel with the image.
        BackgroundPanel backgroundPanel = new BackgroundPanel(BackgroundAsset.DIALOG_BACKGROUND);
        backgroundPanel.setPreferredSize(new Dimension(620, 360));
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setOpaque(false);

        // Message label with transparent background.
        JLabel msgLabel = new JLabel(message, SwingConstants.CENTER);
        msgLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        msgLabel.setForeground(Color.WHITE);
        msgLabel.setOpaque(false);
        msgLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backgroundPanel.add(msgLabel, BorderLayout.CENTER);

        // Button panel at the bottom.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        buttonPanel.setOpaque(false);

        // Create yes button
        StyledButton yesButton = new StyledButton("Yes", ButtonAsset.BUTTON_DARK_BLUE_SHARP);

        // Create no button
        StyledButton noButton = new StyledButton("No", ButtonAsset.BUTTON_DARK_BLUE_SHARP);

        // Add action listener to yes button -> set confirm
        yesButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        // Add action listener to no button -> do nothing
        noButton.addActionListener(e -> {
            dispose();
        });

        // Create an empty space between the buttons
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(50,50));

        buttonPanel.add(yesButton);
        buttonPanel.add(spacer);
        buttonPanel.add(noButton);

        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(backgroundPanel);
        pack();
        // Has to be last statement to center the dialog
        setLocationRelativeTo(owner);
    }

    /** Return user choice */
    public boolean isConfirmed() {
        return confirmed;
    }
}

