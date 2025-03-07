package org.daniel.elysium.games.baccarat;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.baccarat.BaccaratGameEngine;
import org.daniel.elysium.baccarat.constants.BacHandAction;
import org.daniel.elysium.baccarat.constants.BacHandState;
import org.daniel.elysium.baccarat.models.BacHand;
import org.daniel.elysium.baccarat.models.BacBetHand;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.StyledNotificationDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.games.baccarat.center.BacGameAreaPanel;
import org.daniel.elysium.games.baccarat.constants.BaccaratGameState;
import org.daniel.elysium.games.baccarat.models.BacCardUI;
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

import javax.swing.*;
import java.util.ArrayList;
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
    private BacBetHand hand;

    /** The minimum bet allowed in the game. */
    public static final int MIN_BET = 100;

    // Game cards creation
    Shoe<UICard> shoe = Shoe.createShoe(4, UIDeck::new);
    private List<UICard> cards = shoe.cards();

    /**
     * Constructs the BaccaratController and initializes game components.
     *
     * @param stateManager The state manager handling panel switching and user profile data.
     */
    public BaccaratController(StateManager stateManager) {
        this.stateManager = stateManager;
        this.topPanel = new TopPanel(this, stateManager);
        this.chipPanel = new ChipPanel(this, stateManager);
        this.gameAreaPanel = new BacGameAreaPanel(this, stateManager);
        this.hand = new BacBetHand();
    }

    /*======================
        Initial actions
    ======================*/

    /**
     * Handles the event when a chip is selected from the chip panel.
     * <p>
     * This method checks if the selected chip's value does not exceed the player's current balance.
     * If the player has selected a betting area and if it allows for more chips, the chip is added to the bet.
     * If the player has sufficient balance and the bet is valid, the "Deal" and "Clear Bet" buttons are displayed.
     * If the bet exceeds the allowed chip limit, a warning message is displayed.
     * If the balance is insufficient, an error message is shown.
     *
     * @param chip The selected chip to be placed as a bet.
     */
    @Override
    public void onChipSelected(Chip chip) {
        // Check if user have enough balance for the bet
        if (!(hand.getBet() + chip.getValue() <= stateManager.getProfile().getBalance())){
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
        hand.setBet(hand.getBet() + chip.getValue());
        gameAreaPanel.updateBetLabel(hand.getBet());
        gameAreaPanel.showClearBetButton(true);
        gameAreaPanel.showDealButton(true);
    }

    /**
     * Handles the event when the "Clear Bet" button is clicked.
     * <p>
     * This method clears all chips from the betting area.
     * It also hides the "Clear Bet" and "Deal" buttons, ensuring that the game does not start without a valid bet.
     * Finally, the player's balance display is updated.
     */
    @Override
    public void onClearBet() {
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.showClearBetButton(false);
        hand.setBet(0);
        gameAreaPanel.updateBetLabel(0);
        gameAreaPanel.resetSelection();
        gameAreaPanel.clearChips();
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
        if (hand.getBet() < MIN_BET) {
            new Toast(stateManager.getFrame(), "Min bet is 100$", 3000).setVisible(true);
            return;
        }

        // Set tha game area to proper setup
        state = BaccaratGameState.GAME_STARTED;
        stateManager.getProfile().decreaseBalanceBy(hand.getBet());
        chipPanel.setVisible(false);
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.showClearBetButton(false);

        hand.setHandType(gameAreaPanel.getSelectedBoxType());

        // Deal the cards to the players
        dealInitialCards();

        // Evaluate the hand against each other
        evaluateHands();
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

    /**
     * Handles the player's selected action from the action buttons.
     * <p>
     * This method maps each selected action to its corresponding handler,
     * ensuring the correct game logic is executed.
     *
     * @param action The selected game action.
     * @param index  The index of the player's hand affected by the action.
     */
    @Override
    public void onActionSelected(GameActions action, int index) {
        // No action needed in baccarat
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
        if (state.ordinal() > BaccaratGameState.GAME_STARTED.ordinal() && state.ordinal() < BaccaratGameState.PAYOUT.ordinal()) {
            StyledConfirmDialog dialog = new StyledConfirmDialog(stateManager.getFrame(),
                    "If you exit now you will lose your bet, Continue?");
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                stateManager.switchPanel("MainMenu");
                gameAreaPanel.clearChips();
            }
        } else {
            onClearBet();
            stateManager.switchPanel("MainMenu");
        }
    }

    /*======================
     Evaluation and payouts
    ======================*/

    /**
     * Evaluates the hands of both the player and the banker in the current state of the Baccarat game.
     * This method orchestrates the checking of game rules, execution of player and banker actions,
     * and ultimately determines the outcome of the current round based on the rules of Baccarat.
     */
    private void evaluateHands(){
        state = BaccaratGameState.EVALUATION_PHASE;

        BacHand banker = gameAreaPanel.getBankerHand().getHand();
        BacHand player = gameAreaPanel.getPlayerHand().getHand();

        BaccaratGameEngine.evaluatePlayer(banker, player);
        executePlayerAction(player);
        BaccaratGameEngine.evaluateBanker(banker, player);
        executeBankerAction(banker);
        BaccaratGameEngine.evaluateHands(banker ,player);

        proceedTopPayouts();
    }

    /**
     * Executes the action for the player's hand based on the determined next action.
     * If the action is to draw a card, this method retrieves a card from the shoe and adds it to the player's hand.
     *
     * @param hand The player's hand to evaluate and possibly modify based on the game rules.
     */
    private void executePlayerAction(BacHand hand){
        if (hand.getAction() == BacHandAction.DRAW){
            gameAreaPanel.addPlayerCard(getCardFromShoe());
        }
    }

    /**
     * Executes the action for the banker's hand based on the determined next action.
     * If the action is to draw a card, this method retrieves a card from the shoe and adds it to the banker's hand.
     *
     * @param hand The banker's hand to evaluate and possibly modify based on the game rules.
     */
    private void executeBankerAction(BacHand hand){
        if (hand.getAction() == BacHandAction.DRAW){
            gameAreaPanel.addBankerCard(getCardFromShoe());
        }
    }

    /**
     * Processes the payouts based on the results of the game round.
     * This method calculates the game result, updates the player's balance based on the bet outcome,
     * and updates the UI components to reflect the result.
     * <ul>
     *     <li>Calculates the result of the game round.</li>
     *     <li>Adjusts the player's balance based on the result of their bet.</li>
     *     <li>Pays out wins or ties as appropriate.</li>
     *     <li>Updates the balance display on the UI.</li>
     *     <li>Displays the results of the game round.</li>
     * </ul>
     */
    private void proceedTopPayouts(){
        state = BaccaratGameState.PAYOUT;

        BaccaratGameEngine.calculateResult(gameAreaPanel.getBankerHand().getHand(), gameAreaPanel.getPlayerHand().getHand(), hand);
        stateManager.getProfile().increaseBalanceBy(hand.getBet());

        // Determine the outcome and update UI accordingly
        if (hand.getState() == BacHandState.WON){
            gameAreaPanel.getSelectedBox().payWin();
        } else if (hand.getState() == BacHandState.TIE) {
            gameAreaPanel.getSelectedBox().payTie();
        } else {
            gameAreaPanel.clearChips();
            gameAreaPanel.updateBetLabel(0);
        }

        updateBalanceDisplay();
        displayResults();
    }

    /**
     * Displays the results of the current Baccarat game round and schedules a game reset.
     * This method sets the game state to display results, updates the game area to show each hand's results,
     * and sets a timer to reset the game after a brief pause.
     * The reset will trigger the start of a new game round, providing a seamless flow from one round to the next.
     */
    private void displayResults(){
        state = BaccaratGameState.DISPLAY_RESULT;

        // Trigger visual display of results for both hands
        gameAreaPanel.getPlayerHand().showHandResult();
        gameAreaPanel.getBankerHand().showHandResult();

        if (stateManager.isAutoStartNewGame()){
            Timer timer = new Timer(5000, e -> reset());
            timer.setRepeats(false);
            timer.start();
        } else {
            gameAreaPanel.showNewGameButton(true);
        }
    }

    /*======================
        Reset
    ======================*/

    /**
     * Resets and starts a new game.
     */
    @Override
    public void startNewGame() {
        reset();
    }

    /**
     * Resets the game state and prepares everything for a new round.
     * <p>
     * This method clears all previous game actions and hands,
     * and transitions the state back to the betting phase. It also ensures that the player
     * has enough balance to continue playing. If the player's balance falls below the minimum
     * bet, they are redirected to the main menu. Additionally, if the shoe has fewer than
     * 15 cards remaining, a new shoe is created.
     */
    private void reset(){
        state = BaccaratGameState.GAME_ENDED;
        gameAreaPanel.clearHands();
        gameAreaPanel.resetSelection();
        gameAreaPanel.clearActions();
        gameAreaPanel.clearChips();
        gameAreaPanel.updateBetLabel(0);
        hand = new BacBetHand();
        state = BaccaratGameState.BET_PHASE;
        ChipPanelUtil.regenerateChipPanel(this, stateManager);

        // If player has no enough money, Player then escorted to main menu
        if (stateManager.getProfile().getBalance() < MIN_BET){
            stateManager.switchPanel("MainMenu");

            StyledNotificationDialog dialog = new StyledNotificationDialog(
                    stateManager.getFrame(),
                    "You don't have enough balance to continue playing. "
            );

            dialog.setVisible(true);
        }

        // If the shoe has less than 15 cards, start a new shoe
        if (cards.size() < 15){
            cards = Shoe.createShoe(4, UIDeck::new).cards();

            StyledNotificationDialog dialog = new StyledNotificationDialog(
                    stateManager.getFrame(),
                    "Shoe ended, Starting a new Shoe. "
            );

            dialog.setVisible(true);
        }
    }

    /** Protected API for the {@link BaccaratPanel} to revert to initial state when exiting */
    protected void resetScreen(){
        state = BaccaratGameState.BET_PHASE;
        chipPanel.setVisible(false);
        gameAreaPanel.clearHands();
        gameAreaPanel.resetSelection();
        gameAreaPanel.updateBetLabel(0);
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
     * Updates the balance display with the current balance.
     * <p>
     * Retrieves the player's current balance from the profile and updates
     * the UI's balance display accordingly.
     */
    @Override
    public void updateBalanceDisplay() {
        topPanel.setBalance(stateManager.getProfile().getBalance());
    }

    /**
     * Draws and removes the top card from the shoe.
     * <p>
     * This method retrieves the top card from the shoe and removes it from the deck,
     * simulating the process of dealing a card in the game.
     *
     * @return The top card from the shoe as {@link BacCardUI}.
     */
    private BacCardUI getCardFromShoe() {
        UICard card = cards.remove(0);
        return new BacCardUI(card.getRank(), card.getSuit(), card.getAsset());
    }

    /*======================
        Getters
    ======================*/

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

    //TODO: remove before production
    @SuppressWarnings("Unused")
    private List<UICard> getCustomDeck(){
        List<UICard> cards = new ArrayList<>();
        cards.add(new BacCardUI("10", "S", CardAsset.S10));
        cards.add(new BacCardUI("7", "S", CardAsset.S7));
        cards.add(new BacCardUI("7", "H", CardAsset.H7));
        cards.add(new BacCardUI("Q", "S", CardAsset.SQ));
        cards.add(new BacCardUI("10", "C", CardAsset.C10));
        cards.add(new BacCardUI("2", "H", CardAsset.H2));

        cards.add(new BacCardUI("9", "C", CardAsset.C9));
        cards.add(new BacCardUI("Q", "S", CardAsset.SQ));
        cards.add(new BacCardUI("K", "S", CardAsset.SK));
        cards.add(new BacCardUI("8", "S", CardAsset.S8));
        cards.add(new BacCardUI("K", "S", CardAsset.SK));
        cards.add(new BacCardUI("A", "S", CardAsset.SA));
        cards.add(new BacCardUI("K", "S", CardAsset.SK));
        cards.add(new BacCardUI("K", "S", CardAsset.SK));
        cards.add(new BacCardUI("K", "S", CardAsset.SK));
        cards.add(new BacCardUI("K", "S", CardAsset.SK));
        return cards;
    }
}
