package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.baccarat.BacHand;
import org.daniel.elysium.elements.panels.VerticalLinePanel;

import javax.swing.*;
import java.awt.*;

public class CardsAreaPanel extends JPanel {
    private final PlayerAreaUI playerAreaUI;
    private final BankerAreaUI bankerAreaUI;

    public CardsAreaPanel() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 40, 0, 40);

        // Add Player Area
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        playerAreaUI = new PlayerAreaUI();
        add(playerAreaUI, gbc);

        // Add Vertical Line
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;  // Prevent stretching
        gbc.weighty = 0; // Prevent it from taking full height
        gbc.anchor = GridBagConstraints.CENTER; // Keep it in the center
        add(new VerticalLinePanel(300), gbc); // Set height to 300px

        // Add Banker Area
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        bankerAreaUI = new BankerAreaUI();
        add(bankerAreaUI, gbc);
    }

    public void addPlayerCard(BacCardUI card){
        playerAreaUI.addCard(card);
    }

    public void addBankerCard(BacCardUI card){
        bankerAreaUI.addCard(card);
    }

    public void removeCards(){
        playerAreaUI.removeCards();
        bankerAreaUI.removeCards();
    }

    public PlayerAreaUI getPlayerHand(){
        return playerAreaUI;
    }

    public BankerAreaUI getBankerHand(){
        return bankerAreaUI;
    }
}
