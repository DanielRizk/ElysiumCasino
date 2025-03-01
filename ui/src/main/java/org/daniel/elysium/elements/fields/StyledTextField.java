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

public class StyledTextField extends JPanel {
    private final Dimension defaultDimension = new Dimension(250, 50);
    private final int defaultColumns = 15;
    private final String placeholder;
    private final Color textColor = new Color(177, 177, 177);
    private boolean showingPlaceholder = true;
    private JTextField textField;
    private Image background;
    private final boolean editable;

    public StyledTextField(String placeholder) {
        this.placeholder = placeholder;
        this.editable = true;
        initializePanel(defaultDimension);
        initializeBackground(defaultDimension);
        initializeTextField(placeholder, defaultColumns, true);
        registerPlaceholderBehaviour();
    }

    public StyledTextField(String placeholder, boolean editable) {
        this.placeholder = placeholder;
        this.editable = editable;
        initializePanel(defaultDimension);
        initializeBackground(defaultDimension);
        initializeTextField(placeholder, defaultColumns, editable);

        if (editable){
            registerPlaceholderBehaviour();
        }
    }

    public StyledTextField(String placeholder, Dimension dimension, int columns, boolean editable) {
        this.placeholder = placeholder;
        this.editable = editable;
        initializePanel(dimension);
        initializeBackground(dimension);
        initializeTextField(placeholder, columns, editable);

        if (editable){
            registerPlaceholderBehaviour();
        }
    }

    /** Set common configs for base panel */
    private void initializePanel(Dimension dimension){
        setLayout(new BorderLayout());
        setPreferredSize(dimension);
        setOpaque(false);
    }

    /** Set common configs for the text field */
    private void initializeTextField(String placeholder, int columns, boolean editable){
        textField = new JTextField(placeholder, columns);
        textField.setOpaque(false);
        textField.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        textField.setForeground(textColor);

        if (editable){
            textField.setFont(new Font("Roboto", Font.ITALIC, 15));
        } else {
            textField.setFont(new Font("Serif", Font.BOLD, 18));
            textField.setFocusable(false);
        }

        add(textField, BorderLayout.CENTER);
    }

    /** Initialize background image and insure it is painted */
    private void initializeBackground(Dimension dimension){
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

    /** In case of editable text field, register disappearing placeholder text effect */
    private void registerPlaceholderBehaviour(){
        // Handle placeholder behavior.
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

    /** Getter for the text field, return empty string if it has the placeholder displayed */
    public String getText() {
        return showingPlaceholder ? "" : textField.getText();
    }

    /** Setter for the text area, in case of editable text field:
     *  if the text to set is empty display the placeholder, otherwise set the text
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

    @Override
    protected void paintComponent(Graphics g) {
        // Draw background image scaled to the panel.
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}
