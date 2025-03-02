package org.daniel.elysium.games.blackjack;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.assets.ChipAsset;
import org.daniel.elysium.blackjack.BlackjackEngine;
import org.daniel.elysium.blackjack.constants.HandState;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.StyledNotificationDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.models.UICard;
import org.daniel.elysium.models.UIDeck;
import org.daniel.elysium.games.blackjack.center.GameAreaPanel;
import org.daniel.elysium.games.blackjack.center.models.PlayerHandUI;
import org.daniel.elysium.games.blackjack.chips.ChipPanel;
import org.daniel.elysium.games.blackjack.constants.GameActions;
import org.daniel.elysium.games.blackjack.constants.GameState;
import org.daniel.elysium.games.blackjack.top.TopPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls the Blackjack game flow, handling state transitions, UI updates, and interactions.
 */
public class BlackjackController implements BlackjackMediator {
    // State managers
    private final StateManager stateManager;
    private GameState state = GameState.BET_PHASE;

    // References to subcomponents.
    private final TopPanel topPanel;
    private ChipPanel chipPanel;
    private final GameAreaPanel gameAreaPanel;

    // Define the game logic engine
    private BlackjackEngine gameEngine;

    // Game cards creation
    Shoe<UICard> shoe = Shoe.createShoe(4, UIDeck::new);
    private List<UICard> cards = shoe.cards();

    /**
     * Constructs the BlackjackController and initializes game components.
     *
     * @param stateManager The state manager handling panel switching and user profile data.
     */
    public BlackjackController(StateManager stateManager) {
        this.stateManager = stateManager;
        this.gameEngine = new BlackjackEngine();
        this.topPanel = new TopPanel(this, stateManager);
        this.chipPanel = new ChipPanel(this, stateManager);
        this.gameAreaPanel = new GameAreaPanel(this, stateManager);
    }

    /*======================
        Initial actions
    ======================*/

    /**
     * Handles the event when a chip is selected from the chip panel.
     * <p>
     * This method checks if the selected chip's value does not exceed the player's current balance.
     * If the player's betting area allows for more chips, the chip is added to the bet.
     * The player's balance is then updated accordingly. If the player has sufficient balance
     * and the bet is valid, the "Deal" and "Clear Bet" buttons are displayed.
     * If the bet exceeds the allowed chip limit, a warning message is displayed.
     * If the balance is insufficient, an error message is shown.
     *
     * @param chip The selected chip to be placed as a bet.
     */
    @Override
    public void onChipSelected(Chip chip) {
        if (chip.getValue() <= stateManager.getProfile().getBalance()) {
            PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(PlayerHandUI.FIRST_HAND);
            if (playerHandUI.canAddChip()) {
                playerHandUI.addChip(chip);
                stateManager.getProfile().decreaseBalanceBy(chip.getValue());
                gameAreaPanel.showDealButton(true);
                gameAreaPanel.showClearBetButton(true);
                updateBalanceDisplay();
            } else {
                new Toast(stateManager.getFrame(), "Max number of chips reached.", 3000).setVisible(true);
            }
        } else {
            new Toast(stateManager.getFrame(), "Not enough balance.", 3000).setVisible(true);
        }
    }

    /**
     * Handles the event when the "Clear Bet" button is clicked.
     * <p>
     * This method clears all chips from the betting panel and refunds the amount back to the player's balance.
     * It also hides the "Clear Bet" and "Deal" buttons, ensuring that the game does not start without a valid bet.
     * Finally, the player's balance display is updated.
     */
    @Override
    public void onClearBet() {
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(PlayerHandUI.FIRST_HAND);
        stateManager.getProfile().increaseBalanceBy(playerHandUI.getHand().getBet());
        playerHandUI.clearChips(); // Clear chips after refunding the balance
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.showClearBetButton(false);
        updateBalanceDisplay();
    }

    /**
     * Handles the event when the "Deal" button is clicked.
     * <p>
     * This method validates the player's bet before starting the game.
     * If no bet is placed or if the bet is below the minimum required amount,
     * an error message is displayed.
     * Otherwise, the game state is updated, the betting panel is hidden,
     * and the initial cards are dealt to both the player and the dealer.
     * The game logic is then processed based on whether the player or dealer has a blackjack,
     * or if insurance options should be displayed.
     */
    @Override
    public void onDealRequested() {
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(PlayerHandUI.FIRST_HAND);

        // Handle invalid bet
        if (playerHandUI.getHand().getBet() == 0) {
            new Toast(stateManager.getFrame(), "No bet placed yet.", 3000).setVisible(true);
            return;
        } else if (playerHandUI.getHand().getBet() < StateManager.MIN_BET) {
            new Toast(stateManager.getFrame(), "Min bet is 10$", 3000).setVisible(true);
            return;
        }

        // Set tha game area to proper setup
        state = GameState.GAME_STARTED;
        chipPanel.setVisible(false);
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.clearActions();


        // Deal the cards to the players
        dealInitialCards();

        // The logical flow of the game
        // If player has a blackjack go to dealer turn
        if (isPlayerBlackjack()) {
            dealerTurn();
            return;
        }

        // If dealer has ace as first card, go to insurance
        if (checkInsurance()) {
            displayInsuranceOptions();
            return;
        }

        // If dealer has a blackjack, go to dealer turn
        if (isDealerBlackjack()) {
            dealerTurn();
            return;
        }

        // Otherwise start the game normally
        calculatePlayerOptions(PlayerHandUI.FIRST_HAND);
    }

    /**
     * Deals the initial cards to both the player and the dealer.
     * <p>
     * This method updates the game state to "Dealing Cards" and distributes two cards each
     * to the player and the dealer. The dealer's second card is hidden from view until further gameplay decisions are made.
     */
    @Override
    public void dealInitialCards() {
        state = GameState.DEALING_CARDS;
        gameAreaPanel.addPlayerCard(PlayerHandUI.FIRST_HAND, getCardFromShoe());
        gameAreaPanel.addDealerCard(getCardFromShoe());
        gameAreaPanel.addPlayerCard(PlayerHandUI.FIRST_HAND, getCardFromShoe());
        gameAreaPanel.addDealerCard(getCardFromShoe());
        gameAreaPanel.getDealerHand().flipCardDown(); // Hide dealer's second card
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
        if (state.ordinal() > GameState.GAME_STARTED.ordinal()) {
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
        Blackjack methods
    ======================*/

    /**
     * Checks if the player has a blackjack.
     * <p>
     * Retrieves the player's hand and determines whether it qualifies as a blackjack.
     *
     * @return true if the player's hand is a blackjack, false otherwise.
     */
    private boolean isPlayerBlackjack(){
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(PlayerHandUI.FIRST_HAND);
        return playerHandUI.getHand().isBlackJack();
    }

    /**
     * Checks if the dealer has a blackjack.
     * <p>
     * Retrieves the dealer's hand and determines whether it qualifies as a blackjack.
     *
     * @return true if the dealer's hand is a blackjack, false otherwise.
     */
    private boolean isDealerBlackjack(){
        return gameAreaPanel.getDealerHand().getHand().isBlackJack();
    }

    /*======================
        Insurance methods
    ======================*/

    /**
     * Determines if insurance should be offered to the player.
     * <p>
     * Checks whether the dealer's first card is an ace and if the player has
     * enough balance to afford the insurance bet (half of the original bet amount).
     *
     * @return true if the player qualifies for insurance, false otherwise.
     */
    private boolean checkInsurance(){
        return gameEngine.isInsurance(gameAreaPanel.getDealerHand().getHand())
                && (int)(gameAreaPanel.getPlayerHand(0).getBet() * 0.5) <=
                stateManager.getProfile().getBalance();
    }

    /**
     * Displays insurance options to the player.
     * <p>
     * Updates the game state and generates the necessary UI elements to allow the player
     * to choose between insuring their bet or declining insurance.
     */
    private void displayInsuranceOptions(){
        state = GameState.PLAYER_TURN;
        Map<GameActions, Integer> actions = new LinkedHashMap<>();
        actions.put(GameActions.INSURE, PlayerHandUI.FIRST_HAND);
        actions.put(GameActions.DO_NOT_INSURE, PlayerHandUI.FIRST_HAND);
        gameAreaPanel.updateActionButtons(actions);
    }

    /**
     * Handles the insurance option selected by the player.
     * <p>
     * If the player chooses to insure their bet, the insurance amount is deducted from their balance
     * and added to the extra bet area. A timer is set to evaluate the insurance bet:
     * If the dealer has blackjack, the hand is marked as insured and the dealer wins.
     * Otherwise, the insurance bet is lost, and the game continues.
     */
    private void handleInsureOption() {
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(PlayerHandUI.FIRST_HAND);
        playerHandUI.addInsuranceBet();
        stateManager.getProfile().decreaseBalanceBy(playerHandUI.getInsuranceBet());
        updateBalanceDisplay();

        // Wait for a second for the user to see the outcome of the insurance
        Timer timer = new Timer(1000, e -> {
            if (!isDealerBlackjack()) { // If dealer is not blackjack, insurance is lost, game continues
                playerHandUI.clearInsuranceBet();
                calculatePlayerOptions(0);
            } else { // If dealer has blackjack, the dealer wins, and the hand is marked as INSURED
                playerHandUI.getHand().setState(HandState.INSURED);
                dealerTurn();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Handles the player's decision to decline insurance.
     * <p>
     * If the dealer has blackjack, the dealer wins immediately.
     * Otherwise, the game proceeds as usual.
     */
    private void handleDoNotInsureOption(){
        if (isDealerBlackjack()){ // If dealer has blackjack, dealer wins
            dealerTurn();
        } else { // Otherwise, continue the game
            calculatePlayerOptions(PlayerHandUI.FIRST_HAND);
        }
    }

    /*======================
        Player actions
    ======================*/

    /**
     * Updates game state and retrieves available options for the player's hand.
     * <p>
     * This method highlights the current hand if necessary and determines the available actions
     * based on backend logic. If no actions are available, the player is forced to stand.
     *
     * @param index The index of the player's hand.
     */
    private void calculatePlayerOptions(int index) {
        state = GameState.PLAYER_TURN;

        // checkForSecond hand is true, Highlight is applied to the current hand
        gameAreaPanel.getPlayerHand(index).setHighlight(checkForSecondHand());

        // Get the available action from the backend logic.
        Map<GameActions, Integer> actions = getOptions(index);
        gameAreaPanel.updateActionButtons(actions);

        // If there are no more available options, treat it as a stand
        if (actions.isEmpty()){
            handleStandOption(index);
        }
    }

    /**
     * Retrieves the available actions for a player's hand based on backend logic.
     * <p>
     * It checks the game engine for allowed actions and maps them to the corresponding
     * game area panel buttons. The map preserves the order of actions for consistency.
     *
     * @param index The index of the player's hand.
     * @return A map of available game actions for the specified hand.
     */
    private Map<GameActions, Integer> getOptions(int index){
        Map<GameActions, Integer> actions = new LinkedHashMap<>(); // LinkedHashMap is used to preserve the order
        for (String option: gameEngine.getAvailableHandOptions(gameAreaPanel.getPlayerHand(index).getHand())){
            switch (option){
                case "HIT" -> actions.put(GameActions.HIT, index);
                case "STAND" -> actions.put(GameActions.STAND, index);
                case "DOUBLE" -> {
                    if (gameAreaPanel.getPlayerHand(index).getHand().getBet() <= stateManager.getProfile().getBalance()) {
                        actions.put(GameActions.DOUBLE, index);
                    }
                }
                case "SPLIT" -> {
                    if (gameAreaPanel.getPlayerHand(index).getHand().getBet() <= stateManager.getProfile().getBalance()) {
                        actions.put(GameActions.SPLIT, index);
                    }
                }
            }
        }
        return actions;
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
        switch (action){
            case HIT -> handleHitOption(index);
            case STAND -> handleStandOption(index);
            case DOUBLE -> handleDoubleOption(index);
            case SPLIT -> handleSplitOption();
            case INSURE -> handleInsureOption();
            case DO_NOT_INSURE -> handleDoNotInsureOption();
        }
    }

    /**
     * Handles the "Hit" action by drawing a card and updating the game flow.
     * <p>
     * This method validates whether the player can draw another card. If they can,
     * it adds the card to their hand and updates their available actions.
     * If the player's total hand value reaches or exceeds 21, the turn either moves
     * to the second hand (if applicable) or stands by default.
     *
     * @param index The index of the player's hand.
     */
    private void handleHitOption(int index){
        // Check if player can draw another card
        if (gameAreaPanel.addPlayerCard(index, peekCardFromShoe())){
            burnCard(); // remove the added card from the shoe
        }

        // Calculate available options
        calculatePlayerOptions(index);

        // If the player bust, move to next hand if any, or stand by default
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(index);
        if (playerHandUI.getHand().getHandValue() >= 21){
            // If there is another hand (split) and this the first hand, go to the second hand
            if (checkForSecondHand() && index < 1){
                index++; // increment to second hand index
                calculatePlayerOptions(index);
            } else {
                handleStandOption(index);
            }
        }
    }

    /**
     * Handles the "Stand" action, transitioning the game to the appropriate state.
     * <p>
     * If the player has multiple hands (due to a split), the game moves to the next hand.
     * Otherwise, the dealer's turn begins.
     *
     * @param index The index of the player's hand.
     */
    private void handleStandOption(int index){
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(index);
        playerHandUI.setHighlight(false); // turns the highlight off at all cases
        gameAreaPanel.clearActions();

        // If there is another hand (split) and this the first hand, go to the second hand
        if (checkForSecondHand() && index < 1){
            index++; // increment to second hand index
            PlayerHandUI playerHandUI2 = gameAreaPanel.getPlayerHand(index);
            playerHandUI2.getHand().setHandSplit(true); // to prevent black for split hands
            gameAreaPanel.addPlayerCard(index, getCardFromShoe()); // second split hand has one card after split.
            calculatePlayerOptions(index);
        } else {
            dealerTurn();
        }
    }

    /**
     * Handles the "Double" action by doubling the player's bet, drawing a final card,
     * and then standing by default.
     * <p>
     * The method ensures that the player's balance is updated before adding the double bet.
     * After drawing one additional card, the player's turn ends automatically.
     *
     * @param index The index of the player's hand.
     */
    private void handleDoubleOption(int index){
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(index);
        stateManager.getProfile().decreaseBalanceBy(playerHandUI.getBet());
        // Add the double bet after decreasing the balance, otherwise you will decrease the double
        playerHandUI.addDoubleChip();
        updateBalanceDisplay();
        gameAreaPanel.addPlayerCard(index, getCardFromShoe());

        // Stand comes after the double by default
        handleStandOption(index);
    }

    /**
     * Handles the "Split" action, allowing the player to divide their hand into two separate hands.
     * <p>
     * This method updates the UI to reflect the split, decreases the player's balance
     * accordingly, and assigns a second card to the first split hand. The game then proceeds
     * with the first split hand.
     */
    private void handleSplitOption(){
        gameAreaPanel.splitHand(); // The game area panel is responsible for the UI splitting of the hands
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHands().get(PlayerHandUI.FIRST_HAND);
        stateManager.getProfile().decreaseBalanceBy(playerHandUI.getBet()); // decrease the same bet amount
        updateBalanceDisplay();

        // Add the second card to the first split hand
        playerHandUI.addCard(getCardFromShoe());
        playerHandUI.getHand().setHandSplit(true); // To prevent the blackjack for split hand
        calculatePlayerOptions(PlayerHandUI.FIRST_HAND); // Start always with the first split hand
    }

    /*======================
        Dealer actions
    ======================*/

    /**
     * Updates the game state and handles the dealer's turn.
     * <p>
     * This method transitions the game state to the dealer's turn, revealing the dealer's second card.
     * It determines whether the dealer should continue drawing cards based on the state of the player's hands.
     * If at least one player hand is still in the game (not busted or already a blackjack),
     * the dealer will continue drawing cards according to the game rules.
     * Once the dealer's turn is completed, the game results are evaluated.
     */
    private void dealerTurn(){
        state = GameState.DEALER_TURN;
        gameAreaPanel.getDealerHand().flipCardUp(); // expose dealer's second card

        // This flags tells, if the dealer should just expose (all player hands bust)
        // or dealer should draw till 17 (One or more of the player hands are still in the game)
        boolean isPlayerStillInTheGame = false;

        // check if any of the player hand is in the game
        for (PlayerHandUI playerHandUI : gameAreaPanel.getPlayerHands()){
            if (!(playerHandUI.getHand().isBlackJack() || playerHandUI.getHand().getHandValue() > 21)){
                isPlayerStillInTheGame = true;
            }
        }

        if (isPlayerStillInTheGame){
            while (true){
                if (gameAreaPanel.addDealerCard(peekCardFromShoe())){
                    burnCard(); // remove the added card from the shoe
                } else {
                    break;
                }
            }
        }
        evaluateGameResults();
    }

    /*======================
     Evaluation and payouts
    ======================*/

    /**
     * Evaluates the player's hands against the dealer's hand.
     * <p>
     * This method transitions the game state to the evaluation phase and processes each player's
     * hand using the backend game logic to determine the outcome. After evaluation, it proceeds
     * to display the results.
     */
    private void evaluateGameResults(){
        state = GameState.EVALUATION_PHASE;
        for (PlayerHandUI playerHandUI : gameAreaPanel.getPlayerHands()){
            gameEngine.resolvePlayerResult(playerHandUI.getHand(),
                    gameAreaPanel.getDealerHand().getHand());
        }
        displayResults();
    }

    /**
     * Displays the game results for each player's hand.
     * <p>
     * This method updates the game state to indicate that the results are being displayed.
     * It triggers the UI to show the outcome for each player's hand and then proceeds to
     * the payout phase.
     */
    private void displayResults(){
        state = GameState.DISPLAY_RESULT;
        for (PlayerHandUI playerHandUI : gameAreaPanel.getPlayerHands()){
            playerHandUI.displayHandResult();
        }
        proceedToPayouts();
    }

    /**
     * Updates the game state and processes payouts for the player based on hand results.
     * <p>
     * This method iterates through all player hands and determines the appropriate payout
     * based on the final state of each hand. Winnings are added to the player's balance
     * accordingly. The UI is updated to reflect the payouts, and the game transitions
     * to a reset routine after a delay to start a new round.
     */
    private void proceedToPayouts(){
        state = GameState.PAYOUT;
        List<PlayerHandUI> allHands = gameAreaPanel.getPlayerHands();
        for (PlayerHandUI playerHandUI : allHands) {
            if (playerHandUI.getHand().getState() == HandState.BLACKJACK){
                stateManager.getProfile().increaseBalanceBy(playerHandUI.getBet());
                playerHandUI.payBlackjackWin();
            } else if (playerHandUI.getHand().getState() == HandState.INSURED){
                stateManager.getProfile().increaseBalanceBy(playerHandUI.getInsuranceBet());
                playerHandUI.payInsurance();
                playerHandUI.clearMainBet();
            } else if (playerHandUI.getHand().getState() == HandState.WON) {
                stateManager.getProfile().increaseBalanceBy(playerHandUI.getBet());
                playerHandUI.payWin();
            } else if (playerHandUI.getHand().getState() == HandState.PUSH) {
                stateManager.getProfile().increaseBalanceBy(playerHandUI.getBet());
            }
        }

        updateBalanceDisplay();

        // Go to reset to start a new game after 5 seconds
        Timer timer = new Timer(5000, e -> reset());
        timer.setRepeats(false);
        timer.start();
    }

    /*======================
        Reset
    ======================*/

    /**
     * Resets the game state and prepares everything for a new round.
     * <p>
     * This method clears all previous game actions and hands, resets the game engine,
     * and transitions the state back to the betting phase. It also ensures that the player
     * has enough balance to continue playing. If the player's balance falls below the minimum
     * bet, they are redirected to the main menu. Additionally, if the shoe has fewer than
     * 10 cards remaining, a new shoe is created.
     */
    private void reset(){
        state = GameState.GAME_ENDED;
        gameAreaPanel.clearActions();
        gameAreaPanel.clearHands();
        gameEngine = new BlackjackEngine();
        state = GameState.BET_PHASE;
        regenerateChipPanel();

        // If player has no enough money, Player then escorted to main menu
        if (stateManager.getProfile().getBalance() < StateManager.MIN_BET){
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

    /** Protected API for the {@link BlackjackPanel} to revert to initial state when exiting */
    protected void resetScreen(){
        state = GameState.BET_PHASE;
        chipPanel.setVisible(false);
        gameAreaPanel.clearActions();
        gameAreaPanel.clearHands();
        gameEngine = new BlackjackEngine();
        removeChipPanel();
        cards = Shoe.createShoe(4, UIDeck::new).cards();
    }

    /** Protected API for the BlackjackPanel to restart fresh and updated screen */
    protected void restartScreen(){
        revealChipPanel();
        updateBalanceDisplay();
    }

    /*======================
        Helper methods
    ======================*/

    /**
     * Regenerates the chip panel by first removing the existing one and then revealing a new instance.
     * <p>
     * This ensures that the chip panel is always freshly created and dynamically updated.
     */
    private void regenerateChipPanel(){
        removeChipPanel();
        revealChipPanel();
    }

    /**
     * Creates and displays a new instance of the chip panel.
     * <p>
     * This method runs on the Swing Event Dispatch Thread (EDT) to ensure thread safety.
     * It removes any existing chip panel, creates a new one, and adds it to the JLayeredPane.
     * The chip panel is then repositioned and made visible.
     * A component listener is also added to adjust its position when the main frame is resized.
     */
    private void revealChipPanel() {
        SwingUtilities.invokeLater(() -> {
            if (stateManager.getFrame() != null) {
                JLayeredPane layeredPane = stateManager.getFrame().getRootPane().getLayeredPane();

                // Remove any existing chip panel before creating a new one
                //removeChipPanel();

                // Create a new ChipPanel instance
                chipPanel = new ChipPanel(this, stateManager);

                // Add to the layered pane
                layeredPane.add(chipPanel, JLayeredPane.POPUP_LAYER);

                // Re-display the chip panel
                repositionChipPanel();
                chipPanel.setVisible(true);

                // Ensure UI refresh
                layeredPane.revalidate();
                layeredPane.repaint();

                // Adjust the location of the chip panel when the parent is resized
                stateManager.getFrame().addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        repositionChipPanel();
                    }
                });
            }
        });
    }

    /**
     * Removes the currently displayed chip panel from the layered pane.
     * <p>
     * This method ensures that the chip panel is properly removed from the UI before a new one is created.
     * It runs on the Swing Event Dispatch Thread (EDT) to prevent concurrency issues and forces a UI update.
     */
    private void removeChipPanel() {
        SwingUtilities.invokeLater(() -> {
            if (stateManager.getFrame() != null) {
                JLayeredPane layeredPane = stateManager.getFrame().getRootPane().getLayeredPane();

                // Ensure the panel exists before trying to remove it
                if (chipPanel != null) {
                    layeredPane.remove(chipPanel);
                    layeredPane.revalidate();
                    layeredPane.repaint();
                }
            }
        });
    }

    /**
     * Adjusts the position of the chip panel within the main frame.
     * <p>
     * The panel is positioned near the bottom left of the screen with a fixed margin.
     * This method ensures the chip panel is correctly placed after being added or when the window is resized.
     */
    private void repositionChipPanel() {
        if (stateManager.getFrame() != null) {
            Dimension pref = getChipPanel().getPreferredSize();
            int chipPanelWidth = pref.width;
            int chipPanelHeight = pref.height;
            int yPos = stateManager.getFrame().getHeight() - chipPanelHeight - 60;  // Margin from bottom
            int xPos = 20; // Margin from left
            getChipPanel().setBounds(xPos, yPos, chipPanelWidth, chipPanelHeight);
            getChipPanel().revalidate();
            getChipPanel().repaint();
        }
    }

    /**
     * Checks if the player has split hands.
     * <p>
     * This method determines whether the player has more than one hand in play,
     * indicating that a split action has been performed.
     *
     * @return true if the player has multiple hands, false otherwise.
     */
    private boolean checkForSecondHand(){
        return gameAreaPanel.getPlayerHands().size() > 1;
    }

    /**
     * Updates the balance display with the current balance.
     * <p>
     * Retrieves the player's current balance from the profile and updates
     * the UI's balance display accordingly.
     */
    @Override
    public void updateBalanceDisplay() {
        topPanel.setBalance("Balance: " + stateManager.getProfile().getBalance());
    }

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
     * Retrieves the top card from the shoe without removing it.
     * <p>
     * This method allows the game logic to check the next card in the shoe
     * without altering the deck order.
     *
     * @return The top card from the shoe.
     */
    private UICard peekCardFromShoe(){
        return cards.get(0);
    }

    /**
     * Removes the top card from the shoe without using it.
     * <p>
     * This method is typically used in blackjack to discard a card,
     * simulating the burning process used in some game variations.
     */
    private void burnCard(){
        getCardFromShoe();
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
     * Returns the chip selection panel.
     *
     * @return The {@link ChipPanel} UI component.
     */
    public ChipPanel getChipPanel() {
        return chipPanel;
    }

    /**
     * Returns the game area panel containing the dealer and player hands.
     *
     * @return The {@link GameAreaPanel} UI component.
     */
    public GameAreaPanel getGameAreaPanel() {
        return gameAreaPanel;
    }

    //TODO: remove before production
    @SuppressWarnings("Unused")
    private List<UICard> getCustomDeck(){
        List<UICard> cards = new ArrayList<>();
        cards.add(new UICard("10", "S", CardAsset.S10));
        cards.add(new UICard("10", "S", CardAsset.S10));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("10", "H", CardAsset.H10));
        cards.add(new UICard("10", "S", CardAsset.S10));
        cards.add(new UICard("8", "C", CardAsset.C8));
        cards.add(new UICard("4", "H", CardAsset.H4));

        cards.add(new UICard("K", "C", CardAsset.CK));
        cards.add(new UICard("7", "S", CardAsset.S7));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("8", "S", CardAsset.S8));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("A", "S", CardAsset.SA));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("K", "S", CardAsset.SK));
        return cards;
    }
}


