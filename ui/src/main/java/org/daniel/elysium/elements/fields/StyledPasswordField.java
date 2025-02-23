package org.daniel.elysium.elements.fields;

import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.AssetManager;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class StyledPasswordField extends JPasswordField {
    private Image background = null;
    private final Asset backgroundAsset;
    private final String placeholder;
    private boolean showingPlaceholder;
    private final Color textColor = new Color(177, 177, 177);

    public StyledPasswordField(String placeholder, int columns) {
        super(placeholder, columns);
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        setFont(new Font("Roboto", Font.PLAIN, 15));
        basicPasswordFieldConfig();
        this.backgroundAsset = ButtonAsset.BUTTON_DARK_BLUE_ROUND;
        loadBackgroundImage();
        initPlaceholderBehavior();
        preventInitialFocus();
    }

    public StyledPasswordField(String placeholder, int columns, Font font, Asset backgroundAsset) {
        super(placeholder, columns);
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        setFont(font);
        basicPasswordFieldConfig();
        this.backgroundAsset = backgroundAsset;
        loadBackgroundImage();
        initPlaceholderBehavior();
    }

    private void basicPasswordFieldConfig(){
        setOpaque(false);
        setPreferredSize(new Dimension(200, 50));
        setForeground(textColor);

        // Remove the border completely
        setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // Add padding only (no border)

        setCaret(new DefaultCaret() {
            @Override
            public void paint(Graphics g) {
                // Do nothing (hide the caret)
            }
        });

        // Add a component listener to reload the background image when the component is resized
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
        if (backgroundAsset != null && getWidth() > 0 && getHeight() > 0) {
            this.background = AssetManager.getScaledImage(backgroundAsset, getWidth(), getHeight());
        }
    }

    private void initPlaceholderBehavior() {
        // Disable echo char initially to show placeholder text
        setEchoChar((char) 0);

        // Add focus listeners to handle placeholder behavior
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    setText(""); // Clear placeholder text
                    setEchoChar('*'); // Enable echo char (show ****)
                    setForeground(textColor); // Switch to normal text color
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getPassword().length == 0) {
                    setEchoChar((char) 0); // Disable echo char (show plain text)
                    setForeground(textColor); // Switch to placeholder text color
                    setText(placeholder);
                    showingPlaceholder = true;
                }
            }
        });
    }

    private void preventInitialFocus() {
        // Temporarily disable focus
        setFocusable(false);

        // Re-enable focus after the window is displayed
        SwingUtilities.invokeLater(() -> setFocusable(true));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}
