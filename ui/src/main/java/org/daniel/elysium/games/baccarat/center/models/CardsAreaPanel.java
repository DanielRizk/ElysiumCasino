package org.daniel.elysium.games.baccarat.center.models;

import org.daniel.elysium.elements.panels.VerticalLinePanel;
import org.daniel.elysium.games.baccarat.center.models.banker.BankerAreaUI;
import org.daniel.elysium.games.baccarat.center.models.player.PlayerAreaUI;
import org.daniel.elysium.games.baccarat.models.BacCardUI;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel that manages the layout and functionality of the card areas for both the player and the banker
 * in a Baccarat game interface.
 * <p>
 * This panel organizes the player and banker areas, separated by a vertical line, and handles the addition
 * and removal of cards within each area.
 */
public class CardsAreaPanel extends JPanel {
    private final PlayerAreaUI playerAreaUI;
    private final BankerAreaUI bankerAreaUI;

    /**
     * Constructs a new CardsAreaPanel with a specific layout for Baccarat game.
     * It sets up the player and banker areas with appropriate spacing and alignment, separated by a vertical line.
     */
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

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to be added to the player area UI.
     */
    public void addPlayerCard(BacCardUI card){
        playerAreaUI.addCard(card);
    }

    /**
     * Adds a card to the banker's hand.
     *
     * @param card The card to be added to the banker area UI.
     */
    public void addBankerCard(BacCardUI card){
        bankerAreaUI.addCard(card);
    }

    /**
     * Removes all cards from both the player's and banker's hands.
     * This method is typically called at the end of a game round to clear the table.
     */
    public void removeCards(){
        playerAreaUI.removeCards();
        bankerAreaUI.removeCards();
    }

    /**
     * Gets the player area UI component.
     *
     * @return The player area UI handling the player's cards.
     */
    public PlayerAreaUI getPlayerHand(){
        return playerAreaUI;
    }

    /**
     * Gets the banker area UI component.
     *
     * @return The banker area UI handling the banker's cards.
     */
    public BankerAreaUI getBankerHand(){
        return bankerAreaUI;
    }
}
