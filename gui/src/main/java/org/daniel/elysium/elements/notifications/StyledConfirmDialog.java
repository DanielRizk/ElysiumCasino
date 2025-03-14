package org.daniel.elysium.elements.notifications;

import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.panels.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

/**
 * A custom-styled confirmation dialog with a background image and styled buttons.
 * Displays a confirmation message with "Yes" and "No" buttons.
 */
public class StyledConfirmDialog extends JDialog {

    /** Indicates whether the user confirmed the dialog. */
    private boolean confirmed = false;

    /**
     * Constructs a StyledConfirmDialog with a given message.
     *
     * @param owner   The parent JFrame that owns this dialog.
     * @param message The message to be displayed in the dialog.
     */
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

        // Create "Yes" button
        StyledButton yesButton = new StyledButton("Yes", ButtonAsset.BUTTON_DARK_BLUE_SHARP);

        // Create "No" button
        StyledButton noButton = new StyledButton("No", ButtonAsset.BUTTON_DARK_BLUE_SHARP);

        // Add action listener to "Yes" button -> set confirmed and close dialog
        yesButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        // Add action listener to "No" button -> close dialog without confirming
        noButton.addActionListener(e -> {
            dispose();
        });

        // Create an empty space between the buttons
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(50, 50));

        // Add buttons to the panel
        buttonPanel.add(yesButton);
        buttonPanel.add(spacer);
        buttonPanel.add(noButton);

        // Add button panel to the background panel
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set content pane and finalize setup
        setContentPane(backgroundPanel);
        pack();

        // Center the dialog relative to the owner
        // Has to be last statement to center the dialog
        setLocationRelativeTo(owner);
    }

    /**
     * Returns whether the user confirmed the action.
     *
     * @return {@code true} if the user clicked "Yes", otherwise {@code false}.
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}
