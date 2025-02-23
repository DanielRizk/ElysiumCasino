package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.screens.ScreenManager;
import org.daniel.elysium.elements.panels.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BlackjackPanel extends JPanel {

    // Global game state
    private int balance = 10000;
    private int currentBet = 0;
    private boolean gameStarted = false;

    // Subpanels
    private TopPanel topPanel;
    private GameAreaPanel gameAreaPanel;
    private BettingPanel bettingPanel;
    private ChipPanel chipPanel; // Floating chip selection panel

    // Controllers
    private CardDealer cardDealer; // Deals cards into the game area

    public BlackjackPanel(ScreenManager screenManager) {
        setLayout(new BorderLayout());

        // Create background panel
        BackgroundPanel background = new BackgroundPanel(BackgroundAsset.BACKGROUND);
        background.setLayout(new BorderLayout());

        // Create subpanels
        topPanel = new TopPanel(screenManager, this::resetEverything, balance);
        gameAreaPanel = new GameAreaPanel(this::startGame);
        bettingPanel = new BettingPanel(
                this::toggleChipPanel,
                this::clearBet,
                this::handleChipBet
        );

        // Compose the overall layout
        background.add(topPanel, BorderLayout.NORTH);
        background.add(gameAreaPanel, BorderLayout.CENTER);
        background.add(bettingPanel, BorderLayout.SOUTH);
        add(background);

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


    // Called when a chip is selected from the chip panel.
    private void handleChipBet(int chipValue, ImageIcon chipIcon) {
        if (!gameStarted && placeBet(chipValue, bettingPanel.canHaveMoreBets())) {
            // Update betting panel: update bet label and show clear bet button
            bettingPanel.updateBetLabel(currentBet);
            bettingPanel.showClearBetButton(true);
            // Add the specific chip icon to the BetCircle
            bettingPanel.addChip(chipIcon);
        }
    }


    // Place a bet if enough balance exists
    private boolean placeBet(int amount, boolean canHaveMoreBets) {
        if (balance >= amount && canHaveMoreBets) {
            currentBet += amount;
            balance -= amount;
            topPanel.setBalance(balance);
            return true;
        }
        System.out.println("Insufficient balance");
        return false;
    }

    // Called when the "Clear Bet" button is pressed.
    private void clearBet() {
        balance += currentBet;
        currentBet = 0;
        bettingPanel.updateBetLabel(currentBet);
        topPanel.setBalance(balance);
        bettingPanel.clearChips();
        bettingPanel.showClearBetButton(false);
    }

    // Called when the "Deal" button is pressed.
    private void startGame(java.awt.event.ActionEvent e) {
        if (currentBet == 0) return;
        gameStarted = true;
        gameAreaPanel.hideDealButton();
        bettingPanel.hideClearBetButton();
        if (chipPanel != null) {
            chipPanel.setVisible(false);
        }
        bettingPanel.showActionButtons(true);
        // Use CardDealer to simulate dealing cards with delay.
        cardDealer = new CardDealer(gameAreaPanel);
        cardDealer.dealCards();
    }

    // Shows the chip panel (unless the game has started).
    private void toggleChipPanel() {
        if (gameStarted) return;
        if (chipPanel == null) {
            chipPanel = new ChipPanel(this::handleChipBet);
            chipPanel.setBounds(5, getHeight() / 4 - 120, 80, getHeight());
            JLayeredPane layeredPane = getRootPane().getLayeredPane();
            layeredPane.add(chipPanel, JLayeredPane.POPUP_LAYER);
        }
        chipPanel.setVisible(true);
        chipPanel.revalidate();
        chipPanel.repaint();
    }

    // Reset the entire UI to the initial state.
    private void resetEverything() {
        // Remove chip panel
        if (chipPanel != null) {
            chipPanel.setVisible(false);
            JLayeredPane layeredPane = getRootPane().getLayeredPane();
            layeredPane.remove(chipPanel);
            chipPanel = null;
        }
        // Reset card areas
        gameAreaPanel.clearCards();
        // Reset bet circle
        bettingPanel.clearChips();
        // Reset state variables
        currentBet = 0;
        gameStarted = false;
        balance = 1000;
        topPanel.setBalance(balance);
        bettingPanel.updateBetLabel(currentBet);
        bettingPanel.showClearBetButton(false);
        bettingPanel.showActionButtons(false);
        gameAreaPanel.showDealButton(true);
        revalidate();
        repaint();
    }
}
