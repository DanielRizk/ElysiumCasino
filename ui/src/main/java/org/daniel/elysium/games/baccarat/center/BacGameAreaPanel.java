package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.baccarat.constants.HandType;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.games.baccarat.center.models.BacBettingAreaPanel;
import org.daniel.elysium.games.baccarat.center.models.BacCardsAreaPanel;
import org.daniel.elysium.games.baccarat.center.models.banker.BacBankerAreaUI;
import org.daniel.elysium.games.baccarat.center.models.player.BacPlayerAreaUI;
import org.daniel.elysium.games.baccarat.models.BacCardUI;
import org.daniel.elysium.interfaces.Mediator;
import org.daniel.elysium.models.chips.BetBox;
import org.daniel.elysium.models.chips.Chip;

import javax.swing.*;
import java.awt.*;

/**
 * GameAreaPanel represents the main play area where banker and player hands are displayed.
 * It also handles actions like betting.
 */
public class BacGameAreaPanel extends JPanel {

    private final BacCardsAreaPanel cardsAreaPanel;
    private final BacBettingAreaPanel bettingAreaPanel;
    private final JPanel buttonSwitcherPanel;
    private final CardLayout cardLayout;
    private final StyledButton dealButton;
    private final StyledTextField currentBetLabel;

    /**
     * Constructs the main baccarat play area, initializing UI elements.
     *
     * @param mediator   The mediator handling communication between UI and logic.
     * @param stateManager The state manager tracking the game's state.
     */
    public BacGameAreaPanel(Mediator mediator, StateManager stateManager) {
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Define layout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Current  bet label
        gbc.gridy = 1;
        gbc.weighty = 0.10;
        currentBetLabel = new StyledTextField("Bet: " + 0, new Dimension(200, 50), 15, false);
        add(currentBetLabel, gbc);

        // Cards area, player and banker
        gbc.gridy = 2;
        gbc.weighty = 0.30;
        gbc.fill = GridBagConstraints.BOTH;
        cardsAreaPanel = new BacCardsAreaPanel();
        add(cardsAreaPanel, gbc);

        // Deal Button
        gbc.gridy = 3;
        gbc.weighty = 0.10;
        JPanel dealButtonContainer = new JPanel(new BorderLayout());
        dealButtonContainer.setOpaque(false);
        dealButton = new StyledButton("DEAL", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        dealButton.setHorizontalAlignment(SwingConstants.CENTER);
        dealButton.setVisible(false);
        dealButton.addActionListener(e -> mediator.onDealRequested());
        dealButtonContainer.add(dealButton, BorderLayout.CENTER);
        add(dealButtonContainer, gbc);

        // Betting Area Panel (3 Rectangles: Player, Banker, Tie)
        gbc.gridy = 4;
        gbc.weighty = 0.40; // Give it a reasonable height
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        bettingAreaPanel = new BacBettingAreaPanel();
        add(bettingAreaPanel, gbc);

        // Action Buttons Panel
        gbc.gridy = 5;
        gbc.weighty = 0.10;
        cardLayout = new CardLayout();
        buttonSwitcherPanel = new JPanel(cardLayout);
        buttonSwitcherPanel.setOpaque(false);

        // Clear Bet Button Panel
        JPanel clearButtonPanel = new JPanel(new BorderLayout());
        clearButtonPanel.setOpaque(false);
        StyledButton clearBetButton = new StyledButton("Clear bet");
        clearBetButton.addActionListener(e -> mediator.onClearBet());
        clearButtonPanel.add(clearBetButton, BorderLayout.CENTER);
        buttonSwitcherPanel.add(clearButtonPanel, "clear");

        // New game Button Panel
        JPanel newGameButtonPanel = new JPanel(new BorderLayout());
        newGameButtonPanel.setOpaque(false);
        StyledButton newGameButton = new StyledButton("Continue");
        newGameButton.addActionListener(e -> mediator.startNewGame());
        newGameButtonPanel.add(newGameButton, BorderLayout.CENTER);
        buttonSwitcherPanel.add(newGameButtonPanel, "continue");

        // Empty Panel (Hidden State)
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);
        buttonSwitcherPanel.add(emptyPanel, "hide");
        cardLayout.show(buttonSwitcherPanel, "hide");

        add(buttonSwitcherPanel, gbc);
    }

    /* ======================
       Hand Management Methods
       ====================== */

    /**
     * Returns the player's hand UI component.
     * This method provides access to the player area of the game interface,
     * allowing other parts of the application to interact with the player's hand.
     *
     * @return The {@link BacPlayerAreaUI} object representing the player's hand in the UI.
     */
    public BacPlayerAreaUI getPlayerHand(){
        return cardsAreaPanel.getPlayerHand();
    }

    /**
     * Returns the banker's hand UI component.
     * This method provides access to the banker area of the game interface,
     * allowing other parts of the application to interact with the banker's hand.
     *
     * @return The {@link BacBankerAreaUI} object representing the banker's hand in the UI.
     */
    public BacBankerAreaUI getBankerHand(){
        return cardsAreaPanel.getBankerHand();
    }

    /**
     * Clears all cards from both the player's and banker's hands.
     * This method ensures that the playing area is reset and ready for new cards.
     */
    public void clearHands(){
        cardsAreaPanel.removeCards();
    }

    /* ======================
       Game Actions
       ====================== */

    /**
     * Adds a card to the player's hand on the game interface.
     * This method delegates the addition of a new card to the player's section of the UI,
     * updating the visual representation of the player's hand.
     *
     * @param card The {@link BacCardUI} object representing the card to be added to the player's hand.
     */
    public void addPlayerCard(BacCardUI card){
        cardsAreaPanel.addPlayerCard(card);
    }

    /**
     * Adds a card to the banker's hand on the game interface.
     * This method delegates the addition of a new card to the banker's section of the UI,
     * updating the visual representation of the banker's hand.
     *
     * @param card The {@link BacCardUI} object representing the card to be added to the banker's hand.
     */
    public void addBankerCard(BacCardUI card){
        cardsAreaPanel.addBankerCard(card);
    }

    /* ======================
       Betting Actions
       ====================== */

    /**
     * Adds a chip to the betting area on the game interface.
     * This method is responsible for visually representing the addition of a bet in the form of a chip
     * to the currently selected bet box within the betting area.
     *
     * @param chip The {@link Chip} object to be added to the betting area.
     */
    public void addChip(Chip chip){
        bettingAreaPanel.addChip(chip);
    }

    /**
     * Clears all chips from the betting area.
     * This method is typically called when a game round ends or when the player chooses to clear all bets,
     * ensuring that the betting area is reset and ready for new bets.
     */
    public void clearChips(){
        bettingAreaPanel.clearChips();
    }

    /**
     * Retrieves the currently selected bet box from the betting area.
     * This method provides access to the bet box that the player has selected for placing bets.
     *
     * @return The {@link BetBox} representing the selected betting area, or null if no bet box is selected.
     */
    public BetBox getSelectedBox(){
        return bettingAreaPanel.getSelectedBet();
    }

    /**
     * Gets the type of the currently selected bet box in the betting area.
     * This method helps in determining the type of bet placed by the player, such as 'Player', 'Banker', or 'Tie'.
     *
     * @return The {@link HandType} of the selected bet box, indicating the type of bet.
     */
    public HandType getSelectedBoxType(){
        return bettingAreaPanel.getBoxType();
    }

    /**
     * Checks if any bet is currently selected in the betting area.
     * This method is useful for validating whether a player has made a selection before allowing actions like placing a bet.
     *
     * @return true if a bet is currently selected; otherwise, false.
     */
    public boolean isAnyBetSelected(){
        return bettingAreaPanel.getSelectedBet() != null;
    }

    /**
     * Resets all selections in the betting area.
     * This method is used to clear all active selections, effectively resetting the betting interface
     * to a neutral state without any bets selected.
     */
    public void resetSelection(){
        bettingAreaPanel.resetAllSelections();
    }

    /* ======================
       Button Visibility
       ====================== */

    /**
     * Toggles the visibility of the deal button.
     *
     * @param visible {@code true} to show the button, {@code false} to hide it.
     */
    public void showDealButton(boolean visible) {
        dealButton.setVisible(visible);
    }

    /**
     * Toggles the visibility of the clear bet button.
     *
     * @param visible {@code true} to show the clear bet button, {@code false} to hide it.
     */
    public void showClearBetButton(boolean visible) {
        cardLayout.show(buttonSwitcherPanel, visible ? "clear" : "hide");
    }

    /**
     * Toggles the visibility of the new game button.
     *
     * @param visible {@code true} to show the new game button, {@code false} to hide it.
     */
    public void showNewGameButton(boolean visible) {
        cardLayout.show(buttonSwitcherPanel, visible ? "continue" : "hide");
    }

    /**
     * Hides all action buttons by switching to an empty panel.
     */
    public void clearActions() {
        cardLayout.show(buttonSwitcherPanel, "hide");
    }

    /**
     * Updates the UI bet label with the current bet
     *
     * @param bet The amount of bet to display.
     */
    public void updateBetLabel(double bet) {
        currentBetLabel.setText("Bet: " + bet);
    }
}
