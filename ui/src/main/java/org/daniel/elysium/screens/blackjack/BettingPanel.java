package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.screens.blackjack2.BetCircle;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class BettingPanel extends JPanel {
    private BetCircle betCircle;
    private StyledButton currentBetLabel;
    private StyledButton clearBetButton;
    private JPanel actionButtonsPanel;
    private Runnable onToggleChipPanel;
    private Runnable onClearBet;
    private Consumer<Integer> onChipBet;

    public BettingPanel(Runnable onToggleChipPanel, Runnable onClearBet) {
        this.onToggleChipPanel = onToggleChipPanel;
        this.onClearBet = onClearBet;
        this.onChipBet = onChipBet;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initializeComponents();
    }

    private void initializeComponents() {
        // Panel to hold BetCircle and current bet label
        JPanel betPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        betPanel.setOpaque(false);

        betCircle = new BetCircle();
        betCircle.setPreferredSize(new Dimension(120, 120));
        betCircle.setMaximumSize(new Dimension(120, 120));
        betCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Only allow toggling if game not started
        betCircle.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onToggleChipPanel.run();
            }
        });
        betPanel.add(betCircle);

        currentBetLabel = new StyledButton("Bet: $0", ButtonAsset.BUTTON_DARK_BLUE_ROUND);
        betPanel.add(currentBetLabel);

        add(betPanel);
        add(Box.createVerticalStrut(20));

        clearBetButton = new StyledButton("Clear Bet", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        clearBetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearBetButton.addActionListener(e -> onClearBet.run());
        clearBetButton.setVisible(false);
        add(clearBetButton);

        // Action buttons panel (Hit, Stand, etc.)
        actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionButtonsPanel.setOpaque(false);
        StyledButton hitButton = new StyledButton("Hit", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        StyledButton standButton = new StyledButton("Stand", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        StyledButton doubleButton = new StyledButton("Double", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        StyledButton splitButton = new StyledButton("Split", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        actionButtonsPanel.add(hitButton);
        actionButtonsPanel.add(standButton);
        actionButtonsPanel.add(doubleButton);
        actionButtonsPanel.add(splitButton);
        actionButtonsPanel.setVisible(false);
        add(actionButtonsPanel);
    }

    public void updateBetLabel(int bet) {
        currentBetLabel.setText("Bet: $" + bet);
    }

    public void showClearBetButton(boolean visible) {
        clearBetButton.setVisible(visible);
    }

    // Convenience method to hide the clear bet button.
    public void hideClearBetButton() {
        clearBetButton.setVisible(false);
    }

    public void addChip(Chip chip) {
        betCircle.addChip(chip);
    }

    public boolean canHaveMoreBets(){
        return betCircle.getChipsCount() < betCircle.getMaxChips();
    }

    public void clearChips() {
        betCircle.clearChips();
    }

    public void showActionButtons(boolean visible) {
        actionButtonsPanel.setVisible(visible);
    }
}
