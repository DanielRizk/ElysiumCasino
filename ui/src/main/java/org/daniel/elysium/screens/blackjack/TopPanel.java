package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledTextField;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    private final BlackjackMediator mediator;
    private final StateManager stateManager;
    private final StyledTextField balanceLabel;

    public TopPanel(BlackjackMediator mediator, StateManager stateManager) {
        this.mediator = mediator;
        this.stateManager = stateManager;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        StyledButton returnButton = new StyledButton("Return to Main Menu", 250, 50);
        add(returnButton, BorderLayout.WEST);
        returnButton.addActionListener(e -> mediator.returnToMainMenu());

        balanceLabel = new StyledTextField("Balance: " + stateManager.getProfile().getBalance(), false);
        add(balanceLabel, BorderLayout.EAST);
    }

    public void setBalance(String balanceText) {
        balanceLabel.setText(balanceText);
    }
}


