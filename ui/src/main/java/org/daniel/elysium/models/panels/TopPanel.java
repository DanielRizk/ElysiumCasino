package org.daniel.elysium.models.panels;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.interfaces.Mediator;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code TopPanel} class represents the top section of the Blackjack UI.
 * It contains a return button to navigate back to the main menu and a balance display.
 */
public class TopPanel extends JPanel {
    private final StyledTextField balanceLabel;

    /**
     * Constructs the top panel for the Blackjack game.
     *
     * @param mediator     The {@link Mediator} that handles communication between UI components.
     * @param stateManager The {@link StateManager} that manages application states.
     */
    public TopPanel(Mediator mediator, StateManager stateManager) {
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

    /**
     * Updates the balance label to reflect the current balance.
     *
     * @param balanceText The new balance text to display.
     */
    public void setBalance(double balance) {
        balanceLabel.setText("Balance: " + balance);
    }
}
