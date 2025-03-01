package org.daniel.elysium.elements.fields;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.debugUtils.DebugPrint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * A custom styled text field with placeholder text and background styling.
 * Supports editable and non-editable modes.
 */
public class StyledTextField extends JPanel {

    /** Default field dimensions (250x50). */
    private final Dimension defaultDimension = new Dimension(250, 50);

    /** Default number of columns in the text field. */
    private final int defaultColumns = 15;

    /** Placeholder text for the text field. */
    private final String placeholder;

    /** Color used for the placeholder text. */
    private final Color textColor = new Color(177, 177, 177);

    /** Indicates whether the placeholder text is currently displayed. */
    private boolean showingPlaceholder = true;

    /** The actual text input field. */
    private JTextField textField;

    /** Background image for the text field. */
    private Image background;

    /** Indicates whether the text field is editable. */
    private final boolean editable;

    /**
     * Constructs a StyledTextField with a placeholder and default dimensions.
     *
     * @param placeholder The placeholder text to be displayed when the field is empty.
     */
    public StyledTextField(String placeholder) {
        this.placeholder = placeholder;
        this.editable = true;
        initializePanel(defaultDimension);
        initializeBackground(defaultDimension);
        initializeTextField(placeholder, defaultColumns, true);
        registerPlaceholderBehaviour();
    }

    /**
     * Constructs a StyledTextField with a placeholder and editability flag.
     *
     * @param placeholder The placeholder text to be displayed when the field is empty.
     * @param editable    Specifies whether the field is editable.
     */
    public StyledTextField(String placeholder, boolean editable) {
        this.placeholder = placeholder;
        this.editable = editable;
        initializePanel(defaultDimension);
        initializeBackground(defaultDimension);
        initializeTextField(placeholder, defaultColumns, editable);

        if (editable) {
            registerPlaceholderBehaviour();
        }
    }

    /**
     * Constructs a StyledTextField with a placeholder, custom dimensions, column count, and editability flag.
     *
     * @param placeholder The placeholder text to be displayed when the field is empty.
     * @param dimension   The custom size of the field.
     * @param columns     The number of columns in the text field.
     * @param editable    Specifies whether the field is editable.
     */
    public StyledTextField(String placeholder, Dimension dimension, int columns, boolean editable) {
        this.placeholder = placeholder;
        this.editable = editable;
        initializePanel(dimension);
        initializeBackground(dimension);
        initializeTextField(placeholder, columns, editable);

        if (editable) {
            registerPlaceholderBehaviour();
        }
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
     * Initializes the text field with styling and placeholder text.
     *
     * @param placeholder The placeholder text for the field.
     * @param columns     The number of columns in the text field.
     * @param editable    Specifies whether the text field is editable.
     */
    private void initializeTextField(String placeholder, int columns, boolean editable) {
        textField = new JTextField(placeholder, columns);
        textField.setOpaque(false);
        textField.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        textField.setForeground(textColor);

        if (editable) {
            textField.setFont(new Font("Roboto", Font.ITALIC, 15));
        } else {
            textField.setFont(new Font("Serif", Font.BOLD, 18));
            textField.setFocusable(false);
        }

        add(textField, BorderLayout.CENTER);
    }

    /**
     * Initializes and loads the background image for the text field dynamically.
     *
     * @param dimension The size of the background image to be loaded.
     */
    private void initializeBackground(Dimension dimension) {
        // Load the background image when resized or shown.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                loadBackgroundImage(dimension);
                repaint();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                loadBackgroundImage(dimension);
                repaint();
            }
        });

        // Force background load after layout is complete.
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
     * Registers focus listeners to handle placeholder behavior in the text field.
     * The placeholder disappears when the field gains focus and reappears when it is empty.
     */
    private void registerPlaceholderBehaviour() {
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    textField.setText("");
                    textField.setForeground(textColor);
                    textField.setFont(new Font("Roboto", Font.PLAIN, 15));
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(StyledTextField.this.placeholder);
                    textField.setForeground(textColor);
                    textField.setFont(new Font("Roboto", Font.ITALIC, 15));
                    showingPlaceholder = true;
                }
            }
        });
    }

    /**
     * Retrieves the text entered by the user.
     *
     * @return The entered text, or an empty string if the placeholder is displayed.
     */
    public String getText() {
        return showingPlaceholder ? "" : textField.getText();
    }

    /**
     * Sets the text of the text field.
     * If the provided text is empty, the placeholder will be displayed.
     *
     * @param text The text to set in the text field.
     */
    public void setText(String text) {
        if (editable) {
            if (text == null || text.isEmpty()) {
                textField.setText(placeholder);
                textField.setForeground(textColor);
                textField.setFont(new Font("Roboto", Font.ITALIC, 15));
                showingPlaceholder = true;
            } else {
                textField.setText(text);
                textField.setForeground(textColor);
                textField.setFont(new Font("Roboto", Font.PLAIN, 15));
                showingPlaceholder = false;
            }
        } else {
            textField.setText(text);
        }
    }

    /**
     * Paints the background image behind the text field.
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
