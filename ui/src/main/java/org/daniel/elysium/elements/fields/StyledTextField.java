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
    private final int defaultWidth = 250;
    private final int defaultHeight = 50;
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
        initializePanel(defaultWidth, defaultHeight);
        initializeBackground(defaultWidth, defaultHeight);
        initializeTextField(placeholder, defaultColumns, true);
        registerPlaceholderBehaviour();
    }

    public StyledTextField(String placeholder, boolean editable) {
        this.placeholder = placeholder;
        this.editable = editable;
        initializePanel(defaultWidth, defaultHeight);
        initializeBackground(defaultWidth, defaultHeight);
        initializeTextField(placeholder, defaultColumns, editable);
        if (editable){
            registerPlaceholderBehaviour();
        }
    }

    public StyledTextField(String placeholder, int width, int height, int columns, boolean editable) {
        this.placeholder = placeholder;
        this.editable = editable;
        initializePanel(width, height);
        initializeBackground(width, height);
        initializeTextField(placeholder, columns, editable);
        if (editable){
            registerPlaceholderBehaviour();
        }
    }

    private void initializePanel(int width, int height){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
    }

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

    private void loadBackgroundImage(int width, int height) {
        if (getWidth() > 0 && getHeight() > 0) {
            try {
                background = AssetManager.getScaledImage(ButtonAsset.BUTTON_DARK_BLUE_ROUND, width, height);
            } catch (Exception ex) {
                DebugPrint.println("Background image not found: " + ex.getMessage(), true);
            }
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

    public String getText() {
        return showingPlaceholder ? "" : textField.getText();
    }

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

    public JTextField getTextField() {
        return textField;
    }
}
