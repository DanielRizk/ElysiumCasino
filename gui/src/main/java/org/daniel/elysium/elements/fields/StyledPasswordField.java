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

/**
 * A custom password input field with a placeholder and background styling.
 * Supports dynamically loading background images and hiding/showing placeholder text.
 */
public class StyledPasswordField extends JPanel {

    /** Default field dimensions (250x50). */
    private final Dimension defaultDimension = new Dimension(250, 50);

    /** Default number of columns in the password field. */
    private final int defaultColumns = 15;

    /** Placeholder text for the password field. */
    private final String placeholder;

    /** Color used for the placeholder text. */
    private final Color textColor = new Color(177, 177, 177);

    /** Indicates whether the placeholder text is currently displayed. */
    private boolean showingPlaceholder = true;

    /** The actual password input field. */
    private JPasswordField passwordField;

    /** Background image for the password field. */
    private Image background;

    /**
     * Constructs a StyledPasswordField with a placeholder and default dimensions.
     *
     * @param placeholder The placeholder text to be displayed when the field is empty.
     */
    public StyledPasswordField(String placeholder) {
        this.placeholder = placeholder;
        initializePanel(defaultDimension);
        initializeBackground(defaultDimension);
        initializePasswordField(placeholder, defaultColumns);
        registerPlaceholderBehaviour();
    }

    /**
     * Constructs a StyledPasswordField with a placeholder, custom dimensions, and column count.
     *
     * @param placeholder The placeholder text to be displayed when the field is empty.
     * @param dimension   The custom size of the field.
     * @param columns     The number of columns in the text field.
     */
    public StyledPasswordField(String placeholder, Dimension dimension, int columns) {
        this.placeholder = placeholder;
        initializePanel(dimension);
        initializeBackground(dimension);
        initializePasswordField(placeholder, columns);
        registerPlaceholderBehaviour();
    }

    /**
     * Sets common configurations for the main panel.
     *
     * @param dimension The preferred size of the panel.
     */
    private void initializePanel(Dimension dimension) {
        setLayout(new BorderLayout());
        setPreferredSize(dimension);
        setOpaque(false);
    }

    /**
     * Initializes the password field with styling and placeholder text.
     *
     * @param placeholder The placeholder text for the field.
     * @param columns     The number of columns in the text field.
     */
    private void initializePasswordField(String placeholder, int columns) {
        passwordField = new JPasswordField(placeholder, columns);
        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        passwordField.setForeground(textColor);
        passwordField.setFont(new Font("Roboto", Font.ITALIC, 15));
        add(passwordField, BorderLayout.CENTER);
    }

    /**
     * Initializes and loads the background image for the password field dynamically.
     *
     * @param dimension The size of the background image to be loaded.
     */
    private void initializeBackground(Dimension dimension) {
        // Load the background image when resized or shown
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

        // Force background load after layout is complete
        SwingUtilities.invokeLater(() -> {
            loadBackgroundImage(dimension);
            repaint();
        });
    }

    /**
     * Loads the background image from the asset manager and scales it to fit the panel.
     *
     * @param dimension The size of the background to be loaded.
     */
    private void loadBackgroundImage(Dimension dimension) {
        if (getWidth() > 0 && getHeight() > 0) {
            try {
                background = AssetManager.getScaledImage(ButtonAsset.BUTTON_DARK_BLUE_ROUND, dimension);
            } catch (Exception ex) {
                DebugPrint.println("Background image not found: " + ex.getMessage(), true);
            }
        }
    }

    /**
     * Registers focus listeners to handle placeholder behavior in the password field.
     * The placeholder disappears when the field gains focus and reappears when it is empty.
     */
    private void registerPlaceholderBehaviour() {
        // Disable echo char initially to show placeholder text
        passwordField.setEchoChar((char) 0);

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    passwordField.setText(""); // Clear placeholder text
                    passwordField.setEchoChar('*'); // Enable masking (****)
                    passwordField.setForeground(textColor);
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setEchoChar((char) 0); // Disable masking
                    passwordField.setForeground(textColor); // Placeholder color
                    passwordField.setText(placeholder);
                    showingPlaceholder = true;
                }
            }
        });
    }

    /**
     * Retrieves the password entered by the user.
     *
     * @return The password as a string, or an empty string if the placeholder is displayed.
     */
    public String getPassword() {
        return showingPlaceholder ? "" : Arrays.toString(passwordField.getPassword());
    }

    /**
     * Sets the text of the password field.
     * If the provided text is empty, the placeholder will be displayed.
     *
     * @param text The text to set in the password field.
     */
    public void setText(String text) {
        if (text == null || text.isEmpty()) {
            passwordField.setText(placeholder);
            passwordField.setEchoChar((char) 0); // Disable masking
            passwordField.setForeground(textColor);
            passwordField.setFont(new Font("Roboto", Font.ITALIC, 15));
            showingPlaceholder = true;
        } else {
            passwordField.setText(text);
            passwordField.setEchoChar('*'); // Enable masking
            passwordField.setForeground(textColor);
            passwordField.setFont(new Font("Roboto", Font.PLAIN, 15));
            showingPlaceholder = false;
        }
    }

    /**
     * Paints the background image behind the password field.
     *
     * @param g The {@link Graphics} object used to paint the background.
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}
