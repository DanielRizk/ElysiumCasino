package org.daniel.elysium.games.baccarat;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.games.baccarat.center.BacGameAreaPanel;
import org.daniel.elysium.games.baccarat.center.PlayerHand;
import org.daniel.elysium.games.baccarat.constants.BaccaratGameState;
import org.daniel.elysium.interfaces.ChipPanelConsumer;
import org.daniel.elysium.interfaces.GameActions;
import org.daniel.elysium.interfaces.Mediator;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.models.cards.UICard;
import org.daniel.elysium.models.cards.UIDeck;
import org.daniel.elysium.models.chips.Chip;
import org.daniel.elysium.models.panels.ChipPanel;
import org.daniel.elysium.models.panels.ChipPanelUtil;
import org.daniel.elysium.models.panels.TopPanel;

import java.util.List;

/** Controls the Baccarat game flow, handling state transitions, UI updates, and interactions. */
public class BaccaratController implements Mediator, ChipPanelConsumer {
    // State managers
    private final StateManager stateManager;
    private BaccaratGameState state = BaccaratGameState.BET_PHASE;

    // References to subcomponents.
    private final TopPanel topPanel;
    private ChipPanel chipPanel;
    private final BacGameAreaPanel gameAreaPanel;

    // Define the player's betting hand
    private final PlayerHand playerHand;

    // Define the game logic engine
    //private BlackjackEngine gameEngine;

    /** The minimum bet allowed in the game. */
    public static final int MIN_BET = 100;

    // Game cards creation
    Shoe<UICard> shoe = Shoe.createShoe(4, UIDeck::new);
    private List<UICard> cards = shoe.cards();

    public BaccaratController(StateManager stateManager) {
        this.stateManager = stateManager;
        // gameEngine
        this.topPanel = new TopPanel(this, stateManager);
        this.chipPanel = new ChipPanel(this, stateManager);
        this.gameAreaPanel = new BacGameAreaPanel(this, stateManager);
        this.playerHand = new PlayerHand();
    }

    /*======================
        Initial actions
    ======================*/

    /**
     * Handles the event when a chip is selected from the chip panel.
     * <p>
     * This method checks if the selected chip's value does not exceed the player's current balance.
     * If the player has selected a betting area and if it allows for more chips, the chip is added to the bet.
     * The player's balance is then updated accordingly. If the player has sufficient balance
     * and the bet is valid, the "Deal" and "Clear Bet" buttons are displayed.
     * If the bet exceeds the allowed chip limit, a warning message is displayed.
     * If the balance is insufficient, an error message is shown.
     *
     * @param chip The selected chip to be placed as a bet.
     */
    @Override
    public void onChipSelected(Chip chip) {
        // Check if user have enough balance for the bet
        if (!(chip.getValue() <= stateManager.getProfile().getBalance())){
            new Toast(stateManager.getFrame(), "Not enough balance.", 3000).setVisible(true);
            return;
        }

        // Check if any of the bet boxes is selected
        if (!gameAreaPanel.isAnyBetSelected()) {
            new Toast(stateManager.getFrame(), "Please select a side first", 3000).setVisible(true);
            return;
        }

        // Check if the betPanel can have more chips on it
        if (!gameAreaPanel.getSelectedBox().canAddChip()){
            new Toast(stateManager.getFrame(), "Max number of chips reached.", 3000).setVisible(true);
            return;
        }

        // Continue the login normally
        gameAreaPanel.addChip(chip);
        stateManager.getProfile().decreaseBalanceBy(chip.getValue());
        playerHand.setBet(chip.getValue());
        gameAreaPanel.showClearBetButton(true);
        gameAreaPanel.showDealButton(true);
        updateBalanceDisplay();
    }

    /**
     * Handles the event when the "Clear Bet" button is clicked.
     * <p>
     * This method clears all chips from the betting area and refunds the amount back to the player's balance.
     * It also hides the "Clear Bet" and "Deal" buttons, ensuring that the game does not start without a valid bet.
     * Finally, the player's balance display is updated.
     */
    @Override
    public void onClearBet() {
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.showClearBetButton(false);
        stateManager.getProfile().increaseBalanceBy(playerHand.getBet());
        playerHand.setBet(0);
        gameAreaPanel.resetSelection();
        gameAreaPanel.clearChips();
        updateBalanceDisplay();
    }

    /**
     * Handles the event when the "Deal" button is clicked.
     * <p>
     * This method validates the player's bet before starting the game.
     * If the bet is below the minimum required amount, an error message is displayed.
     * Otherwise, the game state is updated, the betting panel is hidden,
     * and the initial cards are dealt to both the player and the dealer.
     * The game logic is then processed.
     */
    @Override
    public void onDealRequested() {
        // Handle invalid bet
        if (playerHand.getBet() < MIN_BET) {
            new Toast(stateManager.getFrame(), "Min bet is 100$", 3000).setVisible(true);
            return;
        }

        // Set tha game area to proper setup
        state = BaccaratGameState.GAME_STARTED;
        chipPanel.setVisible(false);
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.showClearBetButton(false);
        gameAreaPanel.resetSelection();

        // Deal the cards to the players
        dealInitialCards();

    }

    /**
     * Deals the initial cards to both the player and the banker.
     * <p>
     * This method updates the game state to "Dealing Cards" and distributes two cards each
     * to the player and the banker.
     */
    @Override
    public void dealInitialCards() {
        state = BaccaratGameState.DEALING_CARDS;
        gameAreaPanel.addPlayerCard(getCardFromShoe());
        gameAreaPanel.addBankerCard(getCardFromShoe());
        gameAreaPanel.addPlayerCard(getCardFromShoe());
        gameAreaPanel.addBankerCard(getCardFromShoe());
    }

    @Override
    public void updateBalanceDisplay() {
        topPanel.setBalance(stateManager.getProfile().getBalance());
    }

    @Override
    public void onActionSelected(GameActions action, int index) {

    }

    /**
     * Handles the event when the player chooses to return to the main menu.
     * <p>
     * If the player exits in the middle of a game, they will lose their bet.
     * A confirmation dialog is displayed to inform the player of this consequence.
     * If the player confirms the exit, the game switches to the main menu.
     * If the game has not started, the bet is cleared before exiting.
     */
    @Override
    public void returnToMainMenu() {
        if (state.ordinal() > BaccaratGameState.GAME_STARTED.ordinal()) {
            StyledConfirmDialog dialog = new StyledConfirmDialog(stateManager.getFrame(),
                    "If you exit now you will lose your bet, Continue?");
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                stateManager.switchPanel("MainMenu");
            }
        } else {
            onClearBet();
            stateManager.switchPanel("MainMenu");
        }
    }

    /*======================
        Reset
    ======================*/

    /** Protected API for the {@link BaccaratPanel} to revert to initial state when exiting */
    protected void resetScreen(){
        state = BaccaratGameState.BET_PHASE;
        chipPanel.setVisible(false);
        //gameAreaPanel.clearActions();
        //gameAreaPanel.clearHands();
        //gameEngine = new BlackjackEngine();
        ChipPanelUtil.removeChipPanel(this, stateManager);
        cards = Shoe.createShoe(4, UIDeck::new).cards();
    }

    /** Protected API for the {@link BaccaratPanel} to restart fresh and updated screen */
    protected void restartScreen(){
        ChipPanelUtil.revealChipPanel(this, stateManager);
        updateBalanceDisplay();
    }

    /*======================
        Helper methods
    ======================*/

    /**
     * Draws and removes the top card from the shoe.
     * <p>
     * This method retrieves the top card from the shoe and removes it from the deck,
     * simulating the process of dealing a card in the game.
     *
     * @return The top card from the shoe.
     */
    private UICard getCardFromShoe() {
        return cards.remove(0);
    }


    /**
     * Returns the top panel containing game status and return button.
     *
     * @return The {@link TopPanel} UI component.
     */
    public TopPanel getTopPanel() {
        return topPanel;
    }

    /**
     * Updates the chip selection panel.
     * @param panel new panel.
     */
    @Override
    public void setChipPanel(ChipPanel panel) {
        chipPanel = panel;
    }

    /**
     * Returns the chip selection panel.
     *
     * @return The {@link ChipPanel} UI component.
     */
    @Override
    public ChipPanel getChipPanel() {
        return chipPanel;
    }

    /**
     * Returns the game area panel containing the dealer and player hands.
     *
     * @return The {@link BacGameAreaPanel} UI component.
     */
    public BacGameAreaPanel getGameAreaPanel() {
        return gameAreaPanel;
    }
}
