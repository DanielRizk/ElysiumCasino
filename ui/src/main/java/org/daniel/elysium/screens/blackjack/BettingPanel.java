package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.models.Chip;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class BettingPanel extends JPanel {
    private final BlackjackMediator mediator;
    private final StateManager stateManager;
    private final BetCircle betCircle;
    private final StyledTextField currentBetLabel;
    private final JPanel buttonSwitcherPanel;
    private final CardLayout cardLayout;
    private final JPanel actionButtonsPanel;

    public BettingPanel(BlackjackMediator mediator, StateManager stateManager) {
        this.mediator = mediator;
        this.stateManager = stateManager;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Bet panel: contains the bet circle and current bet label.
        JPanel betPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        betPanel.setOpaque(false);
        betCircle = new BetCircle();
        betPanel.add(betCircle);
        currentBetLabel = new StyledTextField("0", 150, 50, 9, false);
        betPanel.add(currentBetLabel);
        add(betPanel);
        add(Box.createVerticalStrut(10));

        // Button switcher panel using CardLayout.
        cardLayout = new CardLayout();
        buttonSwitcherPanel = new JPanel(cardLayout);
        buttonSwitcherPanel.setOpaque(false);
        Dimension fixedSize = new Dimension(1000, 60);
        buttonSwitcherPanel.setPreferredSize(fixedSize);
        buttonSwitcherPanel.setMinimumSize(fixedSize);
        buttonSwitcherPanel.setMaximumSize(fixedSize);

        // Create the "clear bet" panel.
        JPanel clearButtonPanel = new JPanel(new BorderLayout());
        clearButtonPanel.setOpaque(false);
        StyledButton clearBetButton = new StyledButton("Clear bet");
        clearBetButton.addActionListener(e -> mediator.onClearBet());
        clearButtonPanel.add(clearBetButton, BorderLayout.CENTER);
        buttonSwitcherPanel.add(clearButtonPanel, "clear");

        // Create an initially empty panel for action buttons.
        actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionButtonsPanel.setOpaque(false);
        buttonSwitcherPanel.add(actionButtonsPanel, "action");

        // Create an empty panel for when nothing should be visible.
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);
        buttonSwitcherPanel.add(emptyPanel, "hide");

        // Start with the "hide" card.
        cardLayout.show(buttonSwitcherPanel, "hide");

        add(buttonSwitcherPanel);
    }

    // This method updates the current bet display.
    public void updateBetDisplay(int bet) {
        currentBetLabel.setText(String.valueOf(bet));
        cardLayout.show(buttonSwitcherPanel, bet > 0 ? "clear" : "hide");
    }

    // Call this method to update the action buttons dynamically.
    public void updateActionButtons(Collection<GameActions> availableActions) {
        actionButtonsPanel.removeAll();
        for (GameActions action : availableActions) {
            StyledButton button = new StyledButton(action.toString());
            // Each button notifies the mediator which action was selected.
            button.addActionListener(e -> mediator.onActionSelected(action));
            actionButtonsPanel.add(button);
        }
        actionButtonsPanel.revalidate();
        actionButtonsPanel.repaint();
        // Show the action card.
        cardLayout.show(buttonSwitcherPanel, "action");
    }

    public void clearActions(){
        cardLayout.show(buttonSwitcherPanel, "hide");
    }

    public boolean canAddChip() {
        return betCircle.getChipsCount() < betCircle.getMaxChips();
    }

    public void addChip(Chip chip) {
        betCircle.addChip(chip);
    }

    public void clearChips() {
        betCircle.clearChips();
    }
}



