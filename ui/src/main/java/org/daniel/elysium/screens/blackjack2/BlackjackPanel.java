package org.daniel.elysium.screens.blackjack2;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.assets.ChipAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.screens.blackjack.BetCircle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BlackjackPanel extends JPanel {
    private final StateManager stateManager;
    private int currentBet = 0;
    private GameState state = GameState.BET_PHASE;

    // For chip panel visibility control
    private JPanel chipPanel;
    private JPanel topPanel;
    private JPanel gameAreaPanel;
    private JPanel bettingPanel;

    StyledTextField balanceLabel;
    StyledButton dealButton;
    BetCircle betCircle;
    StyledTextField currentBetLabel;
    StyledButton clearBetButton;

    public BlackjackPanel(StateManager stateManager) {
        this.stateManager = stateManager;
        setLayout(new BorderLayout());

        BackgroundPanel background = new BackgroundPanel(BackgroundAsset.BACKGROUND);
        background.setLayout(new BorderLayout());

        // -------------------
        // Create Top Panel
        // -------------------
        topPanel = createTopPanel();

        // -------------------
        // Create Game Area Panel (center)
        // -------------------
        gameAreaPanel = createGameAreaPanel();

        // -------------------
        // Create Betting Panel (bottom)
        // -------------------
        bettingPanel = createBettingPanel();

        // -------------------
        // Create Chip Panel (left)
        // -------------------
        chipPanel = createChipPanel();

        // -------------------
        // Add all subpanels to the background panel
        // -------------------
        background.add(topPanel, BorderLayout.NORTH);
        background.add(gameAreaPanel, BorderLayout.CENTER);
        background.add(bettingPanel, BorderLayout.SOUTH);
        background.add(chipPanel, BorderLayout.WEST);

        // Finally, add the background panel to this panel.
        add(background, BorderLayout.CENTER);

        // Optional: Hide chip panel when clicking outside it.
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (chipPanel != null && chipPanel.isVisible()) {
                    Rectangle bounds = SwingUtilities.convertRectangle(chipPanel.getParent(), chipPanel.getBounds(), BlackjackPanel.this);
                    if (!bounds.contains(e.getPoint())) {
                        chipPanel.setVisible(false);
                    }
                }
            }
        });
    }

    private JPanel createTopPanel(){
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        StyledButton returnButton = new StyledButton("Return to Main Menu", 250, 50);
        topPanel.add(returnButton, BorderLayout.WEST);
        // Balance label shows the current balance from the state manager's profile.
        balanceLabel = new StyledTextField("Balance: " + stateManager.getProfile().getBalance(), false);
        topPanel.add(balanceLabel, BorderLayout.EAST);
        returnButton.addActionListener(e -> stateManager.switchPanel("MainMenu"));
        return topPanel;
    }

    private JPanel createGameAreaPanel(){
        JPanel gameAreaPanel = new JPanel(new GridBagLayout());
        gameAreaPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Dealer Hand Panel
        gbc.gridy = 0;
        gbc.weighty = 0.3;
        JPanel dealerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dealerHandPanel.setOpaque(false);
        dealerHandPanel.setPreferredSize(new Dimension(600, 150));
        gameAreaPanel.add(dealerHandPanel, gbc);

        // Filler panel between dealer and logo
        gbc.gridy = 1;
        gbc.weighty = 0.05;
        JPanel filler1 = new JPanel();
        filler1.setOpaque(false);
        filler1.setPreferredSize(new Dimension(600, 75));
        gameAreaPanel.add(filler1, gbc);

        // Logo / Rules Label
        gbc.gridy = 2;
        gbc.weighty = 0.2;
        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(BackgroundAsset.BLACKJACK_RULES, 600, 230));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameAreaPanel.add(logoLabel, gbc);

        // Deal Button
        gbc.gridy = 3;
        gbc.weighty = 0.1;
        dealButton = new StyledButton("DEAL", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        dealButton.setHorizontalAlignment(SwingConstants.CENTER);
        gameAreaPanel.add(dealButton, gbc);

        // Filler panel between deal button and player hand
        gbc.gridy = 4;
        gbc.weighty = 0.1;
        JPanel filler2 = new JPanel();
        filler2.setOpaque(false);
        filler2.setPreferredSize(new Dimension(600, 40));
        gameAreaPanel.add(filler2, gbc);

        // Player Hand Panel
        gbc.gridy = 5;
        gbc.weighty = 0.35;
        JPanel playerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        playerHandPanel.setOpaque(false);
        playerHandPanel.setPreferredSize(new Dimension(600, 300));
        gameAreaPanel.add(playerHandPanel, gbc);
        return gameAreaPanel;
    }

    private JPanel createBettingPanel(){
        JPanel bettingPanel = new JPanel();
        bettingPanel.setLayout(new BoxLayout(bettingPanel, BoxLayout.Y_AXIS));
        bettingPanel.setOpaque(false);
        bettingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Bet panel: contains bet circle and current bet label.
        JPanel betPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        betPanel.setOpaque(false);
        betCircle = new BetCircle();
        betPanel.add(betCircle);
        currentBetLabel = new StyledTextField(String.valueOf(currentBet), 150, 50, 9, false);
        betPanel.add(currentBetLabel);
        bettingPanel.add(betPanel);
        bettingPanel.add(Box.createVerticalStrut(20));

        // Clear Bet Button
        clearBetButton = new StyledButton("Clear bet");
        clearBetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearBetButton.setVisible(false);
        clearBetButton.addActionListener(e -> {
            stateManager.getProfile().increaseBalanceBy(currentBet);
            currentBet = 0;
            currentBetLabel.setText(String.valueOf(currentBet));
            betCircle.clearChips();
            balanceLabel.setText("Balance: " + stateManager.getProfile().getBalance());
            clearBetButton.setVisible(false);
        });
        bettingPanel.add(clearBetButton);

        // Action Buttons Panel (Hit, Stand, etc.)
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionButtonsPanel.setOpaque(false);
        StyledButton hitButton = new StyledButton("Hit");
        StyledButton standButton = new StyledButton("Stand");
        StyledButton doubleButton = new StyledButton("Double");
        StyledButton splitButton = new StyledButton("Split");
        actionButtonsPanel.add(hitButton);
        actionButtonsPanel.add(standButton);
        actionButtonsPanel.add(doubleButton);
        actionButtonsPanel.add(splitButton);
        actionButtonsPanel.setVisible(false);
        bettingPanel.add(actionButtonsPanel);

        dealButton.addActionListener(e -> {
            if (currentBet == 0) {
                new Toast((JFrame) SwingUtilities.getWindowAncestor(this), "No bet placed", 3000).setVisible(true);
                return;
            }
            state = GameState.GAME_STARTED;
            dealButton.setVisible(false);
            clearBetButton.setVisible(false);
            // ... additional game logic here.
        });
        return bettingPanel;
    }

    private JPanel createChipPanel(){
        JPanel chipPanel = new JPanel();
        chipPanel.setLayout(new BoxLayout(chipPanel, BoxLayout.Y_AXIS));
        chipPanel.setOpaque(false);
        for (ChipAsset asset : ChipAsset.values()) {
            if (asset.getValue() <= stateManager.getProfile().getBalance()){
                Chip chip = new Chip(asset);
                chip.addActionListener(e -> {
                    if (chip.getValue() <= stateManager.getProfile().getBalance() && betCircle.getChipsCount() < betCircle.getMaxChips()){
                        betCircle.addChip(chip);
                        currentBet += chip.getValue();
                        currentBetLabel.setText(String.valueOf(currentBet));
                        clearBetButton.setVisible(true);
                        stateManager.getProfile().decreaseBalanceBy(chip.getValue());
                        balanceLabel.setText("Balance: " + stateManager.getProfile().getBalance());
                    } else {
                        new Toast((JFrame) SwingUtilities.getWindowAncestor(this),
                                "Maximum number of chips reached.", 3000).setVisible(true);
                    }
                });
                chipPanel.add(chip);
            }
        }
        return chipPanel;
    }

    enum GameState {
        BET_PHASE,
        GAME_STARTED,
        DEALING_CARDS,
        PLAYER_TURN,
        DEALER_TURN,
        EVALUATION_PHASE,
        DISPLAY_RESULT,
        PAYOUT,
        GAME_ENDED
    }
}
