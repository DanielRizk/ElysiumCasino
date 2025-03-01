package org.daniel.elysium.screens.blackjack.top;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.screens.blackjack.BlackjackMediator;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    private final StyledTextField balanceLabel;

    public TopPanel(BlackjackMediator mediator, StateManager stateManager) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create and add return to main menu button on the left side
        StyledButton returnButton = new StyledButton("Return to Main Menu", 250, 50);
        add(returnButton, BorderLayout.WEST);
        // Add action listener for return button -> go to main menu
        returnButton.addActionListener(e -> mediator.returnToMainMenu());

        // Add balance text field to display the current balance
        balanceLabel = new StyledTextField("Balance: " + stateManager.getProfile().getBalance(), false);
        add(balanceLabel, BorderLayout.EAST);
    }

    /** Helper method to set the current balance */
    public void setBalance(String balanceText) {
        balanceLabel.setText(balanceText);
    }
}


