package org.daniel.elysium.elements.notifications;

import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.panels.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

/**
 * A custom-styled notification dialog with a background image and a single "Ok" button.
 * Used to display informational messages to the user.
 */
public class StyledNotificationDialog extends JDialog {

    /**
     * Constructs a StyledNotificationDialog with a given message.
     *
     * @param owner   The parent JFrame that owns this dialog.
     * @param message The message to be displayed in the dialog.
     */
    public StyledNotificationDialog(JFrame owner, String message) {
        super(owner, "Info", true);
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

        // Create "Ok" button
        StyledButton okButton = new StyledButton("Ok", ButtonAsset.BUTTON_DARK_BLUE_SHARP);

        // Add action listener to "Ok" button -> close the dialog
        okButton.addActionListener(e -> {
            dispose();
        });

        // Add button to the panel
        buttonPanel.add(okButton);

        // Add button panel to the background panel
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set content pane and finalize setup
        setContentPane(backgroundPanel);
        pack();

        // Center the dialog relative to the owner
        // Has to be last statement to center the dialog
        setLocationRelativeTo(owner);
    }
}
