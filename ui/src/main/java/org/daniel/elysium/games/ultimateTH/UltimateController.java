package org.daniel.elysium.games.ultimateTH;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.Asset;
import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.StyledNotificationDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.games.CustomDebugDeck;
import org.daniel.elysium.games.ultimateTH.center.UthGameAreaPanel;
import org.daniel.elysium.games.ultimateTH.constants.UthActions;
import org.daniel.elysium.games.ultimateTH.constants.UthGameState;
import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
import org.daniel.elysium.interfaces.ChipPanelConsumer;
import org.daniel.elysium.interfaces.GameActions;
import org.daniel.elysium.interfaces.Mediator;
import org.daniel.elysium.models.Card;
import org.daniel.elysium.models.LetterDeck;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.models.chips.Chip;
import org.daniel.elysium.models.panels.ChipPanel;
import org.daniel.elysium.models.panels.ChipPanelUtil;
import org.daniel.elysium.models.panels.TopPanel;
import org.daniel.elysium.ultimateTH.UthGameEngine;
import org.daniel.elysium.ultimateTH.constants.UthGameStage;
import org.daniel.elysium.ultimateTH.constants.UthHandState;
import org.daniel.elysium.ultimateTH.model.UthHand;
import org.daniel.elysium.ultimateTH.model.UthPlayerHand;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls the Ultimate TH game flow, handling state transitions, UI updates, and interactions.
 */
public class UltimateController implements Mediator, ChipPanelConsumer {
    // State managers
    private final StateManager stateManager;
    private UthGameState state = UthGameState.BET_PHASE;
    private UthGameStage stage = UthGameStage.START;

    // References to subcomponents.
    private final TopPanel topPanel;
    private ChipPanel chipPanel;
    private final UthGameAreaPanel gameAreaPanel;

    /** The minimum bet allowed in the game. */
    public static final int MIN_BET = 10;

    // Game cards creation
    Shoe<Card> shoe = Shoe.createShoe(1, LetterDeck::new);
    private List<Card> cards = CustomDebugDeck.getCustomUTHDeck();//shoe.cards();

    /**
     * Constructs the UltimateController and initializes game components.
     *
     * @param stateManager The state manager handling panel switching and user profile data.
     */
    public UltimateController(StateManager stateManager) {
        this.stateManager = stateManager;
        this.topPanel = new TopPanel(this, stateManager);
        this.chipPanel = new ChipPanel(this, stateManager);
        this.gameAreaPanel = new UthGameAreaPanel(this, stateManager);
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
        // Check if any of the bet circle is selected
        if (!gameAreaPanel.isAnyBetSelected()) {
            new Toast(stateManager.getFrame(), "Please select a bet type first", 3000).setVisible(true);
            return;
        }

        UthPlayerHand hand = gameAreaPanel.getPlayerHand();

        // Handles the balance check in case of trips bet or ante/blind bet
        if (gameAreaPanel.getSelectedCircle().getLabel().equals("TRIPS")){
            // Check if user have enough balance for the bet
            if (!(hand.getTrips() + chip.getValue() <= stateManager.getProfile().getBalance())){
                new Toast(stateManager.getFrame(), "Not enough balance.", 3000).setVisible(true);
                return;
            }
        } else {
            if (!(hand.getAnte() + chip.getValue() <= stateManager.getProfile().getBalance() / 2)){
                new Toast(stateManager.getFrame(), "Not enough balance.", 3000).setVisible(true);
                return;
            }
        }

        // Check if the betPanel can have more chips on it
        if (!gameAreaPanel.getSelectedCircle().canAddChip()){
            new Toast(stateManager.getFrame(), "Max number of chips reached.", 3000).setVisible(true);
            return;
        }

        gameAreaPanel.addChip(chip);

        // Sets the corresponding bet [trips - ante/blind]
        if (gameAreaPanel.getSelectedCircle().getLabel().equals("ANTE") || gameAreaPanel.getSelectedCircle().getLabel().equals("BLIND")){
            hand.setBet(hand.getAnte() + chip.getValue());
            gameAreaPanel.updateBetDisplay(hand.getAnte());
        } else {
            hand.setTrips(hand.getTrips() + chip.getValue());
            gameAreaPanel.updateTripsDisplay(hand.getTrips());
        }
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
        gameAreaPanel.getPlayerHand().setBet(0);
        gameAreaPanel.getPlayerHand().setTrips(0);
        gameAreaPanel.updateBetDisplay(0);
        gameAreaPanel.updateTripsDisplay(0);
        gameAreaPanel.resetSelection();
        gameAreaPanel.clearAllChips();
    }

    /**
     * Handles player deal request by validating bets/balance and progressing game state.
     * <p>
     * Key actions:
     * - Validates ante meets min bet and balance sufficiency for future stages
     * - Deducts ante and Trips bets from player balance
     * - Hides betting UI and shows game interface
     * - Deals initial cards and calculates available player actions
     */
    @Override
    public void onDealRequested() {
        UthPlayerHand hand = gameAreaPanel.getPlayerHand();

        // Handle invalid bet
        if (hand.getAnte() < MIN_BET) {
            new Toast(stateManager.getFrame(), "Min bet is 10$", 3000).setVisible(true);
            return;
        }

        if (hand.getAnte() * 3 > stateManager.getProfile().getBalance() - (hand.getAnte() + hand.getBlind() + hand.getTrips())){
            new Toast(stateManager.getFrame(), "Not enough balance for later stages in the game", 3000).setVisible(true);
            return;
        }

        // Update game state
        state = UthGameState.GAME_STARTED;

        // Update user balance
        stateManager.getProfile().decreaseBalanceBy(hand.getAnte() * 2);
        stateManager.getProfile().decreaseBalanceBy(hand.getTrips());
        updateBalanceDisplay();

        // Set tha game area to proper setup
        chipPanel.setVisible(false);
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.showClearBetButton(false);
        gameAreaPanel.resetSelection();

        // Deal the cards to the players
        dealInitialCards();

        // Display user actions
        calculatePlayerOptions();
    }

    /**
     * Deals the initial set of cards for the game round.
     * <p>
     * Adds community cards, two player cards, and two dealer cards
     * from the shoe to the game area.
     * </p>
     */
    @Override
    public void dealInitialCards() {
        state = UthGameState.DEALING_CARDS;

        gameAreaPanel.addCommunityCard(getCommunityCardsFromShoe());

        gameAreaPanel.addPlayerCard(getCardFromShoe());
        gameAreaPanel.addPlayerCard(getCardFromShoe());

        gameAreaPanel.addDealerCard(getCardFromShoe());
        gameAreaPanel.addDealerCard(getCardFromShoe());
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
        if (state.ordinal() > UthGameState.GAME_STARTED.ordinal() && state.ordinal() < UthGameState.PAYOUT.ordinal()) {
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
        Player actions
    ======================*/

    /**
     * Updates game state and retrieves available options for the player's hand.
     */
    private void calculatePlayerOptions() {
        state = UthGameState.PLAYER_TURN;

        // Get the available action from the backend logic.
        Map<UthActions, Integer> actions = getOptions();
        gameAreaPanel.updateActionButtons(actions);
    }

    /**
     * Retrieves the available actions for a player's hand based on backend logic.
     * <p>
     * It checks the game engine for allowed actions and maps them to the corresponding
     * game area panel buttons. The map preserves the order of actions for consistency.
     *
     * @return A map of available game actions for the specified hand.
     */
    private Map<UthActions, Integer> getOptions(){
        Map<UthActions, Integer> actions = new LinkedHashMap<>(); // LinkedHashMap is used to preserve the order
        for (String option: UthGameEngine.getPlayerOptions(stage)){
            switch (option){
                case "X4" -> {
                    // Validate if the use has 4x the bet amount and not just the 3x
                    if (gameAreaPanel.getPlayerHand().getAnte() * 4 <= stateManager.getProfile().getBalance()){
                        actions.put(UthActions.X4, 0);
                    }
                }
                case "X3" -> actions.put(UthActions.X3, 0);
                case "X2" -> actions.put(UthActions.X2, 0);
                case "X1" -> actions.put(UthActions.X1, 0);
                case "CHECK" -> actions.put(UthActions.CHECK, 0);
                case "FOLD" -> actions.put(UthActions.FOLD, 0);
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
        if (action instanceof UthActions uthActions){
            switch (uthActions) {
                case X4 -> handleX4Option();
                case X3 -> handleX3Option();
                case X2 -> handleX2Option();
                case X1 -> handleX1Option();
                case CHECK -> handleCheckOption();
                case FOLD -> handleFoldOption();
            }
        }
    }

    /**
     * Handles the "X4" betting option in Ultimate Texas Hold'em.
     * <p>
     * Updates the game state, deducts 4 times the ante from the player's balance,
     * updates the play bet and UI, and reveals the community cards.
     * </p>
     */
    private void handleX4Option(){
        state = UthGameState.DEALER_TURN;
        stage = UthGameStage.FINAL;

        UthPlayerHand hand = gameAreaPanel.getPlayerHand();

        stateManager.getProfile().decreaseBalanceBy(hand.getAnte() * 4);
        hand.setPlay(hand.getAnte() * 4);
        gameAreaPanel.addPlayChips(UthActions.X4);
        gameAreaPanel.updatePlayDisplay(hand.getPlay());

        updateBalanceDisplay();
        exposeCommunityCards();
    }

    /**
     * Handles the "X3" betting option in Ultimate Texas Hold'em.
     * <p>
     * Updates the game state, deducts 3 times the ante from the player's balance,
     * updates the play bet and UI, and reveals the community cards.
     * </p>
     */
    private void handleX3Option(){
        state = UthGameState.DEALER_TURN;
        stage = UthGameStage.FINAL;

        UthPlayerHand hand = gameAreaPanel.getPlayerHand();

        stateManager.getProfile().decreaseBalanceBy(hand.getAnte() * 3);
        hand.setPlay(hand.getAnte() * 3);
        gameAreaPanel.addPlayChips(UthActions.X3);
        gameAreaPanel.updatePlayDisplay(hand.getPlay());

        updateBalanceDisplay();
        exposeCommunityCards();
    }

    /**
     * Handles the "X2" betting option in Ultimate Texas Hold'em.
     * <p>
     * Updates the game state, deducts 2 times the ante from the player's balance,
     * updates the play bet and UI, and reveals the community cards.
     * </p>
     */
    private void handleX2Option(){
        state = UthGameState.DEALER_TURN;
        stage = UthGameStage.FINAL;

        UthPlayerHand hand = gameAreaPanel.getPlayerHand();

        stateManager.getProfile().decreaseBalanceBy(hand.getAnte() * 2);
        hand.setPlay(hand.getAnte() * 2);
        gameAreaPanel.addPlayChips(UthActions.X2);
        gameAreaPanel.updatePlayDisplay(hand.getPlay());

        updateBalanceDisplay();
        exposeCommunityCards();
    }

    /**
     * Handles the "X1" betting option in Ultimate Texas Hold'em.
     * <p>
     * Updates the game state, deducts the ante amount from the player's balance,
     * updates the play bet and UI, and reveals the community cards.
     * </p>
     */
    private void handleX1Option(){
        state = UthGameState.DEALER_TURN;
        stage = UthGameStage.FINAL;

        UthPlayerHand hand = gameAreaPanel.getPlayerHand();

        stateManager.getProfile().decreaseBalanceBy(hand.getAnte());
        hand.setPlay(hand.getAnte());
        gameAreaPanel.addPlayChips(UthActions.X1);
        gameAreaPanel.updatePlayDisplay(hand.getPlay());

        updateBalanceDisplay();
        exposeCommunityCards();
    }

    /**
     * Handles the "Check" option in Ultimate Texas Hold'em.
     * <p>
     * Advances the game stage when the player checks. If at the start, it moves to the flop;
     * if at the flop, it moves to the river. Then, updates the available player options.
     * </p>
     */
    private void handleCheckOption(){
        switch (stage){
            case START -> {
                stage = UthGameStage.FLOP;
                gameAreaPanel.getCommunityCardsPanel().exposeFlop();
            }
            case FLOP -> {
                stage = UthGameStage.RIVER;
                gameAreaPanel.getCommunityCardsPanel().exposeTurnAndRiver();
            }
        }
        calculatePlayerOptions();
    }

    /**
     * Handles the "Fold" option in Ultimate Texas Hold'em.
     * <p>
     * Marks the player's hand as folded, resets all bets to zero,
     * and reveals the community cards.
     * </p>
     */
    private void handleFoldOption(){
        UthPlayerHand hand = gameAreaPanel.getPlayerHand();

        hand.setState(UthHandState.FOLD);
        hand.setAnte(0);
        hand.setBlind(0);
        hand.setPlay(0);
        hand.setTrips(0);

        exposeCommunityCards();
    }

    /*======================
     Dealer and Comm actions
    ======================*/

    /**
     * Reveals all community cards, clears player actions, and proceeds to expose the dealer's hand.
     */
    private void exposeCommunityCards(){
        gameAreaPanel.getCommunityCardsPanel().exposeAll();
        gameAreaPanel.clearActions();

        exposeDealer();
    }

    /**
     * Exposes the dealer's hand and updates the game state to the dealer's turn.
     * <p>
     * If the player has not folded, it evaluates the hands after a delay;
     * otherwise, it resets the game.
     * </p>
     */
    private void exposeDealer(){
        state = UthGameState.DEALER_TURN;
        stage = UthGameStage.FINAL;

        gameAreaPanel.getDealerHandPanel().exposeCards();

        Timer timer = new Timer(2000, e -> {
            if (gameAreaPanel.getPlayerHand().getState() != UthHandState.FOLD){
                evaluateHands();
            } else {
                if (stateManager.isUTHAutoStart()){
                    reset();
                } else {
                    gameAreaPanel.showNewGameButton(true);
                }
            }

        });
        timer.setRepeats(false);
        timer.start();
    }

    /*======================
     Evaluation and payouts
    ======================*/

    /**
     * Evaluates the player's and dealer's hands and determines the game results.
     * <p>
     * Sets the game state to the evaluation phase, evaluates both hands using the
     * community cards, determines the winner, evaluates Trips bets, processes results,
     * and displays the final outcome.
     * </p>
     */
    private void evaluateHands(){
        state = UthGameState.EVALUATION_PHASE;

        UthPlayerHand playerHand = gameAreaPanel.getPlayerHand();
        UthHand dealerHand = gameAreaPanel.getDealerHand();

        UthGameEngine.evaluateHand(gameAreaPanel.getCommunityCards(), playerHand);
        UthGameEngine.evaluateHand(gameAreaPanel.getCommunityCards(), dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        UthGameEngine.evaluateTrips(playerHand);

        UthGameEngine.processResults(playerHand, dealerHand);

        displayResults();
    }

    /**
     * Displays the results of the game round.
     * <p>
     * Updates the game state, shows the player's and dealer's hand combinations,
     * displays the results, and updates the Trips and Blind multipliers before
     * proceeding to the payout phase.
     * </p>
     */
    private void displayResults(){
        state = UthGameState.DISPLAY_RESULT;

        gameAreaPanel.getPlayerHandPanel().displayHandCombination();
        gameAreaPanel.getDealerHandPanel().displayHandCombination();

        UthPlayerHand playerHand = gameAreaPanel.getPlayerHand();
        UthHand dealerHand = gameAreaPanel.getDealerHand();

        gameAreaPanel.displayResults(playerHand.getState());
        gameAreaPanel.displayTripsMultiplier(playerHand.getTripsState());
        gameAreaPanel.displayBlindMultiplier(playerHand.getEvaluatedHand().handCombination());
        gameAreaPanel.displayAnteState(dealerHand.getEvaluatedHand().handCombination());

        gameAreaPanel.updateAnteDisplay(playerHand.getAnte());
        gameAreaPanel.updateBlindDisplay(playerHand.getBlind());
        gameAreaPanel.updateTripsDisplay(playerHand.getTrips());
        gameAreaPanel.updatePlayDisplay(playerHand.getPlay());

        proceedToPayouts();
    }

    /**
     * Processes payouts at the end of the game round.
     * <p>
     * Determines if the dealer qualifies for ante payouts, handles payouts for
     * main bets (ante, blind, and play), processes Trips side bet payouts, updates
     * the player's balance, and clears losing bets.
     * </p>
     */
    private void proceedToPayouts(){
        state = UthGameState.PAYOUT;

        UthPlayerHand playerHand = gameAreaPanel.getPlayerHand();
        UthHand dealerHand = gameAreaPanel.getDealerHand();

        // Check if the dealer qualifies to pay ante bet
        boolean dealerQualifies = dealerHand.getEvaluatedHand().handCombination().getValue() > -2;

        // Handle main bet ante/blind/play payouts
        if (playerHand.getState() == UthHandState.WON){
            gameAreaPanel.getBetPanel().payWin(playerHand, dealerQualifies);
            stateManager.getProfile().increaseBalanceBy(playerHand.getAnte());
            stateManager.getProfile().increaseBalanceBy(playerHand.getBlind());
            stateManager.getProfile().increaseBalanceBy(playerHand.getPlay());
        } else if (playerHand.getState() == UthHandState.TIE){
            stateManager.getProfile().increaseBalanceBy(playerHand.getAnte());
            stateManager.getProfile().increaseBalanceBy(playerHand.getBlind());
            stateManager.getProfile().increaseBalanceBy(playerHand.getPlay());
        } else {
            // In case dealer wins unqualified, ante pushes
            stateManager.getProfile().increaseBalanceBy(playerHand.getAnte());
            gameAreaPanel.getBetPanel().clearAnteChips();
            gameAreaPanel.getBetPanel().clearBlindChips();
            gameAreaPanel.getBetPanel().clearPlayChips();
        }

        // Handles side bet trips payouts
        if (playerHand.getTripsState().getValue() > 0){
            gameAreaPanel.getBetPanel().payTripsWin(playerHand);
            stateManager.getProfile().increaseBalanceBy(playerHand.getTrips());
        } else {
            gameAreaPanel.getBetPanel().clearTripsChips();
        }

        if (stateManager.isUTHAutoStart()){
            Timer timer = new Timer(5000, e -> reset());
            timer.setRepeats(false);
            timer.start();
        } else {
            gameAreaPanel.showNewGameButton(true);
        }

        updateBalanceDisplay();
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
     * bet, they are redirected to the main menu.
     */
    private void reset(){
        state = UthGameState.GAME_ENDED;
        gameAreaPanel.clearActions();
        gameAreaPanel.clearCards();
        gameAreaPanel.clearAllChips();
        state = UthGameState.BET_PHASE;
        stage = UthGameStage.START;
        ChipPanelUtil.regenerateChipPanel(this, stateManager);

        // If player has no enough money, Player then escorted to main menu
        if (stateManager.getProfile().getBalance() < MIN_BET * 5){
            stateManager.switchPanel("MainMenu");

            StyledNotificationDialog dialog = new StyledNotificationDialog(
                    stateManager.getFrame(),
                    "You don't have enough balance to continue playing. "
            );

            dialog.setVisible(true);
        }

        cards = Shoe.createShoe(1, LetterDeck::new).cards();
    }

    /** Protected API for the {@link UltimatePanel} to revert to initial state when exiting */
    protected void resetScreen(){
        state = UthGameState.BET_PHASE;
        stage = UthGameStage.START;
        chipPanel.setVisible(false);
        gameAreaPanel.clearActions();
        gameAreaPanel.clearCards();
        gameAreaPanel.clearAllChips();
        ChipPanelUtil.removeChipPanel(this, stateManager);
        cards = Shoe.createShoe(1, LetterDeck::new).cards();
    }

    /** Protected API for the {@link UltimatePanel} to restart fresh and updated screen */
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
     * @return The top card from the shoe as {@code UthCardUI}.
     */
    private UthCardUI getCardFromShoe() {
        Card card = cards.remove(0);
        Asset asset = CardAsset.fromString(card.getSuit() + card.getRank());
        return new UthCardUI(card.getRank(), card.getSuit(), asset);
    }

    /**
     * Draws and removes the top 5 cards from the shoe for the community cards.
     * <p>
     * This method retrieves the top 5 cards from the shoe and removes them from the deck,
     * simulating the process of dealing the community cards in the game.
     *
     * @return The top 5 cards from the shoe as {@code List<UthCardUI>}.
     */
    private List<UthCardUI> getCommunityCardsFromShoe() {
        List<UthCardUI> commCards = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            Card card = cards.remove(0);
            Asset asset = CardAsset.fromString(card.getSuit() + card.getRank());
            commCards.add(new UthCardUI(card.getRank(), card.getSuit(), asset));
        }
        return commCards;
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
     * @return The {@link UthGameAreaPanel} UI component.
     */
    public UthGameAreaPanel getGameAreaPanel() {
        return gameAreaPanel;
    }
}
