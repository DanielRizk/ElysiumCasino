package org.daniel.elysium.elements.fields;

import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.debugUtils.DebugPrint;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;

public class StyledPasswordField extends JPanel {
    private final int defaultWidth = 250;
    private final int defaultHeight = 50;
    private final int defaultColumns = 15;
    private final String placeholder;
    private final Color textColor = new Color(177, 177, 177);
    private boolean showingPlaceholder = true;
    private JPasswordField passwordField;
    private Image background;

    public StyledPasswordField(String placeholder) {
        this.placeholder = placeholder;
        initializePanel(defaultWidth, defaultHeight);
        initializeBackground(defaultWidth, defaultHeight);
        initializePasswordField(placeholder, defaultColumns);
        registerPlaceholderBehaviour();
    }

    public StyledPasswordField(String placeholder, int width, int height, int columns) {
        this.placeholder = placeholder;
        initializePanel(width, height);
        initializeBackground(width, height);
        initializePasswordField(placeholder, columns);
        registerPlaceholderBehaviour();
    }

    private void initializePanel(int width, int height){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
    }

    private void initializePasswordField(String placeholder, int columns){
        passwordField = new JPasswordField(placeholder, columns);
        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        passwordField.setForeground(textColor);
        passwordField.setFont(new Font("Roboto", Font.ITALIC, 15));
        add(passwordField, BorderLayout.CENTER);

        /*setCaret(new DefaultCaret() {
            @Override
            public void paint(Graphics g) {
                // Do nothing (hide the caret)
            }
        });*/
    }

    private void initializeBackground(int width, int height){
        // Load the background image when resized or shown.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                loadBackgroundImage(width, height);
            }
            @Override
            public void componentShown(ComponentEvent e) {
                loadBackgroundImage(width, height);
                repaint();
            }
        });

        // Force load background after layout is complete.
        SwingUtilities.invokeLater(() -> {
            loadBackgroundImage(width, height);
            repaint();
        });
    }

    private void loadBackgroundImage(int width, int height) {
        if (getWidth() > 0 && getHeight() > 0) {
            try {
                background = AssetManager.getScaledImage(ButtonAsset.BUTTON_DARK_BLUE_ROUND, width, height);
            } catch (Exception ex) {
                DebugPrint.println("Background image not found: " + ex.getMessage(), true);
            }
        }
    }

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

    @Override
    protected void paintComponent(Graphics g) {
        // Draw background image scaled to the panel.
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }

    public String getPassword() {
        return showingPlaceholder ? "" : Arrays.toString(passwordField.getPassword());
    }

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

    public JPasswordField getTextField() {
        return passwordField;
    }
}
