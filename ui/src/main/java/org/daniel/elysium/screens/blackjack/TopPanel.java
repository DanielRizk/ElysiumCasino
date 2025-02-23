package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.constants.Asset;
import org.daniel.elysium.elements.StyledButton;
import org.daniel.elysium.screens.ScreenManager;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    private StyledButton returnButton;
    private StyledButton balanceLabel;

    public TopPanel(ScreenManager screenManager, Runnable onReturn, int initialBalance) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        returnButton = new StyledButton("Back to Main Menu", Asset.BUTTON_DB_SHARP);
        returnButton.addActionListener(e -> {
            onReturn.run();
            screenManager.showScreen("MainMenu");
        });
        add(returnButton, BorderLayout.WEST);

        balanceLabel = new StyledButton("Balance: $" + initialBalance, Asset.BUTTON_DB_ROUND);
        balanceLabel.setFocusable(false);
        add(balanceLabel, BorderLayout.EAST);
    }

    public void setBalance(int balance) {
        balanceLabel.setText("Balance: $" + balance);
    }
}
