package org.daniel.elysium.screens;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.LogoAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.buttons.StyledToggleButton;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.elements.labels.LogoLabel;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.interfaces.Resettable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * Represents the settings panel where users can control game behavior, auto or manual.
 */
public class SettingsPanel extends JPanel implements Resettable {
    private final StateManager stateManager;
    private final StyledToggleButton bjToggle;
    private final StyledToggleButton bacToggle;
    private final StyledToggleButton uthToggle;
    private final StyledButton backButton;

    /**
     * Constructs a {@code SettingsPanel} with the required UI components.
     *
     * @param stateManager The application's {@link StateManager} instance.
     */
    public SettingsPanel(StateManager stateManager) {
        this.stateManager = stateManager;
        setLayout(new BorderLayout());

        // Set the background
        BackgroundPanel backgroundPanel = new BackgroundPanel(BackgroundAsset.BACKGROUND_MAIN);

        // Create an inputPanel in the center with GridBagLayout for alignment
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        // Create a grid to organize elements
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Create and add the game logo
        Dimension logoDimension = new Dimension(600, 500);
        LogoLabel logoLabel = new LogoLabel(LogoAsset.LOGO_SHADE, logoDimension);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        inputPanel.add(logoLabel, gbc);

        // Create the text label
        StyledTextField newGameLabel = new StyledTextField("New Game auto start", new Dimension(250, 50), 15, false);
        newGameLabel.setFont(newGameLabel.getFont().deriveFont(Font.BOLD, 24f));
        gbc.gridy = 1;
        inputPanel.add(newGameLabel, gbc);

        // BLACKJACK controls
        JPanel bjRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        bjRow.setOpaque(false);
        StyledTextField bjLabel = new StyledTextField("Blackjack", new Dimension(170, 50), 15, false);
        bjToggle = new StyledToggleButton("On", "OFF");
        bjRow.add(bjLabel);
        bjRow.add(bjToggle);
        gbc.gridy = 2;
        inputPanel.add(bjRow, gbc);

        // BACCARAT controls
        JPanel bacRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        bacRow.setOpaque(false);
        StyledTextField bacLabel = new StyledTextField("Baccarat", new Dimension(170, 50), 15, false);
        bacToggle = new StyledToggleButton("On", "OFF");
        bacRow.add(bacLabel);
        bacRow.add(bacToggle);
        gbc.gridy = 3;
        inputPanel.add(bacRow, gbc);

        // ULTIMATE TH controls
        JPanel uthRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        uthRow.setOpaque(false);
        StyledTextField uthLabel = new StyledTextField("Ultimate TH", new Dimension(170, 50), 15, false);
        uthToggle = new StyledToggleButton("On", "OFF");
        uthRow.add(uthLabel);
        uthRow.add(uthToggle);
        gbc.gridy = 4;
        inputPanel.add(uthRow, gbc);

        // Create and add back button
        backButton = new StyledButton("Back");
        // Increase spacing before the back button
        gbc.insets = new Insets(30, 0, 10, 0);
        gbc.gridy = 5;
        inputPanel.add(backButton, gbc);

        // Register the button actions
        registerButtonActions();

        // Add the inputPanel to the background panel
        backgroundPanel.add(inputPanel, BorderLayout.CENTER);

        // Finally add backgroundPanel to this main panel
        add(backgroundPanel, BorderLayout.CENTER);
    }

    /**
     * Registers action listeners for Blackjack toggle, Baccarat toggle, Ultimate_TH toggle and back button.
     */
    private void registerButtonActions() {
        // bjToggle action -> set the mode for BJ game
        bjToggle.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                bjToggle.setSelected();
                stateManager.setBJAutoStartMode(true);
            } else {
                bjToggle.setUnselected();
                stateManager.setBJAutoStartMode(false);
            }
        });

        // bacToggle action -> set the mode for Bac game
        bacToggle.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                bacToggle.setSelected();
                stateManager.setBacAutoStartMode(true);
            } else {
                bacToggle.setUnselected();
                stateManager.setBacAutoStartMode(false);
            }
        });

        // uthToggle action -> set the mode for UTH game
        uthToggle.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                uthToggle.setSelected();
                stateManager.setUTHAutoStartMode(true);
            } else {
                uthToggle.setUnselected();
                stateManager.setUTHAutoStartMode(false);
            }
        });

        // Back button action -> switch to login panel
        backButton.addActionListener(e -> stateManager.switchPanel("MainMenu"));
    }

    @Override
    public void reset() {
        // Do nothing
    }

    @Override
    public void onRestart() {
        if (stateManager.isBJAutoStart()){
            bjToggle.setSelected();
        } else {
            bjToggle.setUnselected();
        }

        if (stateManager.isBacAutoStart()){
            bacToggle.setSelected();
        } else {
            bacToggle.setUnselected();
        }

        if (stateManager.isUTHAutoStart()){
            uthToggle.setSelected();
        } else {
            uthToggle.setUnselected();
        }
    }
}
