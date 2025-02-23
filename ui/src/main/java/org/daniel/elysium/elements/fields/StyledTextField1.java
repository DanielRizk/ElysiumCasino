package org.daniel.elysium.elements.fields;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class StyledTextField1 extends JPanel {
    private JTextField textField;
    private Image background;
    private final String placeholder;
    private boolean showingPlaceholder = true;
    private final Color textColor = new Color(177, 177, 177);

    public StyledTextField1(String placeholder, int columns) {
        this.placeholder = placeholder;
        setLayout(new BorderLayout());
        setOpaque(false);

        textField = new JTextField(placeholder, 15);
        textField.setOpaque(false);
        textField.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        textField.setForeground(textColor);
        textField.setFont(new Font("Roboto", Font.ITALIC, 15));
        add(textField, BorderLayout.CENTER);

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
                    textField.setText(StyledTextField1.this.placeholder);
                    textField.setForeground(textColor);
                    textField.setFont(new Font("Roboto", Font.ITALIC, 15));
                    showingPlaceholder = true;
                }
            }
        });

        // Load the background image once the component is visible and on resize.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                loadBackgroundImage();
            }
            @Override
            public void componentShown(ComponentEvent e) {
                loadBackgroundImage();
                repaint();
            }
        });
    }

    private void loadBackgroundImage() {
        if (getWidth() > 0 && getHeight() > 0) {
            try {
                // Replace "/textfield_bg.png" with the path to your image.
                background = AssetManager.getScaledImage(ButtonAsset.BUTTON_DARK_BLUE_ROUND, 200, 50);
            } catch (Exception ex) {
                System.err.println("Background image not found: " + ex.getMessage());
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

    // Delegate methods to the inner text field if needed.

    public String getText() {
        return showingPlaceholder ? "" : textField.getText();
    }

    public void setText(String text) {
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
    }

    public JTextField getTextField() {
        return textField;
    }
}
