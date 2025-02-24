package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    private StyledButton returnButton;
    private StyledButton balanceLabel;

    public TopPanel(StateManager stateManager, Runnable onReturn, int initialBalance) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        returnButton = new StyledButton("Back to Main Menu", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        returnButton.addActionListener(e -> {
            onReturn.run();
            stateManager.switchPanel("MainMenu");
        });
        add(returnButton, BorderLayout.WEST);

        balanceLabel = new StyledButton("Balance: $" + initialBalance, ButtonAsset.BUTTON_DARK_BLUE_ROUND);
        balanceLabel.setFocusable(false);
        add(balanceLabel, BorderLayout.EAST);
    }

    public void setBalance(int balance) {
        balanceLabel.setText("Balance: $" + balance);
    }
}
