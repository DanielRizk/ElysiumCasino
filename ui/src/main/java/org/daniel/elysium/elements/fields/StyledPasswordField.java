package org.daniel.elysium.elements.fields;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.debugUtils.DebugPrint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;

public class StyledPasswordField extends JPanel {
    private final Dimension defaultDimension = new Dimension(250, 50);
    private final int defaultColumns = 15;
    private final String placeholder;
    private final Color textColor = new Color(177, 177, 177);
    private boolean showingPlaceholder = true;
    private JPasswordField passwordField;
    private Image background;

    public StyledPasswordField(String placeholder) {
        this.placeholder = placeholder;
        initializePanel(defaultDimension);
        initializeBackground(defaultDimension);
        initializePasswordField(placeholder, defaultColumns);
        registerPlaceholderBehaviour();
    }

    public StyledPasswordField(String placeholder, Dimension dimension, int columns) {
        this.placeholder = placeholder;
        initializePanel(dimension);
        initializeBackground(dimension);
        initializePasswordField(placeholder, columns);
        registerPlaceholderBehaviour();
    }

    /** Set common configs for base panel */
    private void initializePanel(Dimension dimension){
        setLayout(new BorderLayout());
        setPreferredSize(dimension);
        setOpaque(false);
    }

    /** Set common configs for the password field */
    private void initializePasswordField(String placeholder, int columns){
        passwordField = new JPasswordField(placeholder, columns);
        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        passwordField.setForeground(textColor);
        passwordField.setFont(new Font("Roboto", Font.ITALIC, 15));
        add(passwordField, BorderLayout.CENTER);

        /* optional to disable cursor display
        setCaret(new DefaultCaret() {
            @Override
            public void paint(Graphics g) {
                // Do nothing (hide the caret)
            }
        });
        */
    }

    /** Initialize background image and insure it is painted */
    private void initializeBackground(Dimension dimension){
        // Load the background image when resized or shown.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                loadBackgroundImage(dimension);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                loadBackgroundImage(dimension);
                repaint();
            }
        });

        // Force load background after layout is complete.
        SwingUtilities.invokeLater(() -> {
            loadBackgroundImage(dimension);
            repaint();
        });
    }

    /** Load the background image from asset manager image cache */
    private void loadBackgroundImage(Dimension dimension) {
        if (getWidth() > 0 && getHeight() > 0) {
            try {
                background = AssetManager.getScaledImage(ButtonAsset.BUTTON_DARK_BLUE_ROUND, dimension);
            } catch (Exception ex) {
                DebugPrint.println("Background image not found: " + ex.getMessage(), true);
            }
        }
    }

    /** Register disappearing placeholder text effect */
    private void registerPlaceholderBehaviour() {
        // Disable echo char initially to show placeholder text
        passwordField.setEchoChar((char) 0);

        // Add focus listeners to handle placeholder behavior
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    passwordField.setText(""); // Clear placeholder text
                    passwordField.setEchoChar('*'); // Enable echo char (show ****)
                    passwordField.setForeground(textColor); // Switch to normal text color
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setEchoChar((char) 0); // Disable echo char (show plain text)
                    passwordField.setForeground(textColor); // Switch to placeholder text color
                    passwordField.setText(placeholder);
                    showingPlaceholder = true;
                }
            }
        });
    }

    /** Getter for the password field, return empty string if it has the placeholder displayed */
    public String getPassword() {
        return showingPlaceholder ? "" : Arrays.toString(passwordField.getPassword());
    }

    /** Setter for the password field,
     * If the text to set is empty display the placeholder, otherwise set the text
     */
    public void setText(String text) {
        if (text == null || text.isEmpty()) {
            passwordField.setText(placeholder);
            passwordField.setForeground(textColor);
            passwordField.setFont(new Font("Roboto", Font.ITALIC, 15));
            showingPlaceholder = true;
        } else {
            passwordField.setText(text);
            passwordField.setForeground(textColor);
            passwordField.setFont(new Font("Roboto", Font.PLAIN, 15));
            showingPlaceholder = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw background image scaled to the panel.
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}
