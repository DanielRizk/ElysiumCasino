package org.daniel.elysium.games.ultimateTH;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.blackjack.constants.HandState;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.StyledNotificationDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.games.blackjack.constants.BlackjackActions;
import org.daniel.elysium.games.blackjack.models.BJCardUI;
import org.daniel.elysium.games.ultimateTH.center.UthGameAreaPanel;
import org.daniel.elysium.games.ultimateTH.constants.UthActions;
import org.daniel.elysium.games.ultimateTH.constants.UthGameState;
import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
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
import org.daniel.elysium.ultimateTH.UthGameEngine;
import org.daniel.elysium.ultimateTH.constants.UthGameStage;
import org.daniel.elysium.ultimateTH.constants.UthHandState;
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

    // Define the game logic engine
    private final UthGameEngine gameEngine;

    /** The minimum bet allowed in the game. */
    public static final int MIN_BET = 10;

    // Game cards creation
    Shoe<UICard> shoe = Shoe.createShoe(1, UIDeck::new);
    private List<UICard> cards = getCustomDeck();//shoe.cards();

    /**
     * Constructs the UltimateController and initializes game components.
     *
     * @param stateManager The state manager handling panel switching and user profile data.
     */
    public UltimateController(StateManager stateManager) {
        this.stateManager = stateManager;
        this.gameEngine = new UthGameEngine();
        this.topPanel = new TopPanel(this, stateManager);
        this.chipPanel = new ChipPanel(this, stateManager);
        this.gameAreaPanel = new UthGameAreaPanel(this, stateManager);
    }


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
        // Check if any of the bet circle is selected
        if (!gameAreaPanel.isAnyBetSelected()) {
            new Toast(stateManager.getFrame(), "Please select a bet type first", 3000).setVisible(true);
            return;
        }

        if (gameAreaPanel.getSelectedCircle().getLabel().equals("TRIPS")){
            // Check if user have enough balance for the bet
            if (!(chip.getValue() <= stateManager.getProfile().getBalance())){
                new Toast(stateManager.getFrame(), "Not enough balance.", 3000).setVisible(true);
                return;
            }
        } else {
            if (!(chip.getValue() <= stateManager.getProfile().getBalance() / 2)){
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
        if (gameAreaPanel.getSelectedCircle().getLabel().equals("ANTE") || gameAreaPanel.getSelectedCircle().getLabel().equals("BLIND")){
            stateManager.getProfile().decreaseBalanceBy(chip.getValue() * 2);
            gameAreaPanel.getPlayerHand().getHand().setBet(gameAreaPanel.getPlayerHand().getHand().getAnte() + chip.getValue());
            gameAreaPanel.updateBetDisplay(gameAreaPanel.getPlayerHand().getHand().getAnte());
        } else {
            stateManager.getProfile().decreaseBalanceBy(chip.getValue());
            gameAreaPanel.getPlayerHand().getHand().setTrips(gameAreaPanel.getPlayerHand().getHand().getTrips() + chip.getValue());
            gameAreaPanel.updateTripsDisplay(gameAreaPanel.getPlayerHand().getHand().getTrips());
        }
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
        stateManager.getProfile().increaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getAnte() * 2);
        stateManager.getProfile().increaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getTrips());
        gameAreaPanel.getPlayerHand().getHand().setBet(0);
        gameAreaPanel.getPlayerHand().getHand().setTrips(0);
        gameAreaPanel.updateBetDisplay(0);
        gameAreaPanel.updateTripsDisplay(0);
        gameAreaPanel.resetSelection();
        gameAreaPanel.clearAllChips();
        updateBalanceDisplay();
    }

    @Override
    public void onDealRequested() {
        // Handle invalid bet
        if (gameAreaPanel.getPlayerHand().getHand().getAnte() < MIN_BET) {
            new Toast(stateManager.getFrame(), "Min bet is 10$", 3000).setVisible(true);
            return;
        }

        if (gameAreaPanel.getPlayerHand().getHand().getAnte() * 3 > stateManager.getProfile().getBalance()){
            new Toast(stateManager.getFrame(), "Not enough balance for later stages in the game", 3000).setVisible(true);
            return;
        }

        // Set tha game area to proper setup
        state = UthGameState.GAME_STARTED;
        chipPanel.setVisible(false);
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.showClearBetButton(false);
        gameAreaPanel.resetSelection();

        // Deal the cards to the players
        dealInitialCards();

        calculatePlayerOptions();
    }


    /**
     * Updates game state and retrieves available options for the player's hand.
     * <p>
     * This method highlights the current hand if necessary and determines the available actions
     * based on backend logic.
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
        for (String option: gameEngine.getPlayerOptions(stage)){
            switch (option){
                case "X4" -> {
                    if (gameAreaPanel.getPlayerHand().getHand().getAnte() * 4 <= stateManager.getProfile().getBalance()){
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
     * Updates the balance display with the current balance.
     * <p>
     * Retrieves the player's current balance from the profile and updates
     * the UI's balance display accordingly.
     */
    @Override
    public void updateBalanceDisplay() {
        topPanel.setBalance(stateManager.getProfile().getBalance());
    }

    @Override
    public void dealInitialCards() {
        gameAreaPanel.getCommunityCards().addFlop1(getCardFromShoe());
        gameAreaPanel.getCommunityCards().addFlop2(getCardFromShoe());
        gameAreaPanel.getCommunityCards().addFlop3(getCardFromShoe());
        gameAreaPanel.getCommunityCards().addTurn(getCardFromShoe());
        gameAreaPanel.getCommunityCards().addRiver(getCardFromShoe());

        gameAreaPanel.getPlayerHand().addCard(getCardFromShoe());
        gameAreaPanel.getPlayerHand().addCard(getCardFromShoe());

        gameAreaPanel.getDealerHand().addCard(getCardFromShoe());
        gameAreaPanel.getDealerHand().addCard(getCardFromShoe());
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



    private void handleX4Option(){
        state = UthGameState.DEALER_TURN;
        stage = UthGameStage.FINAL;

        stateManager.getProfile().decreaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getAnte() * 4);
        gameAreaPanel.getPlayerHand().getHand().setPlay(gameAreaPanel.getPlayerHand().getHand().getAnte() * 4);
        gameAreaPanel.addPlayChips(UthActions.X4);
        gameAreaPanel.updatePlayDisplay(gameAreaPanel.getPlayerHand().getHand().getPlay());

        updateBalanceDisplay();
        exposeCommunityCards();
    }

    private void handleX3Option(){
        state = UthGameState.DEALER_TURN;
        stage = UthGameStage.FINAL;

        stateManager.getProfile().decreaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getAnte() * 3);
        gameAreaPanel.getPlayerHand().getHand().setPlay(gameAreaPanel.getPlayerHand().getHand().getAnte() * 3);
        gameAreaPanel.addPlayChips(UthActions.X3);
        gameAreaPanel.updatePlayDisplay(gameAreaPanel.getPlayerHand().getHand().getPlay());

        updateBalanceDisplay();
        exposeCommunityCards();
    }

    private void handleX2Option(){
        state = UthGameState.DEALER_TURN;
        stage = UthGameStage.FINAL;

        stateManager.getProfile().decreaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getAnte() * 2);
        gameAreaPanel.getPlayerHand().getHand().setPlay(gameAreaPanel.getPlayerHand().getHand().getAnte() * 2);
        gameAreaPanel.addPlayChips(UthActions.X2);
        gameAreaPanel.updatePlayDisplay(gameAreaPanel.getPlayerHand().getHand().getPlay());

        updateBalanceDisplay();
        exposeCommunityCards();
    }

    private void handleX1Option(){
        state = UthGameState.DEALER_TURN;
        stage = UthGameStage.FINAL;

        stateManager.getProfile().decreaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getAnte());
        gameAreaPanel.getPlayerHand().getHand().setPlay(gameAreaPanel.getPlayerHand().getHand().getAnte());
        gameAreaPanel.addPlayChips(UthActions.X1);
        gameAreaPanel.updatePlayDisplay(gameAreaPanel.getPlayerHand().getHand().getPlay());

        updateBalanceDisplay();
        exposeCommunityCards();
    }

    private void handleCheckOption(){
        switch (stage){
            case START -> {
                stage = UthGameStage.FLOP;
                gameAreaPanel.getCommunityCards().exposeFlop();
            }
            case FLOP -> {
                stage = UthGameStage.RIVER;
                gameAreaPanel.getCommunityCards().exposeTurnAndRiver();
            }
        }

        calculatePlayerOptions();
    }

    private void handleFoldOption(){
        gameAreaPanel.getPlayerHand().getHand().setState(UthHandState.FOLD);
        gameAreaPanel.getPlayerHand().getHand().setAnte(0);
        gameAreaPanel.getPlayerHand().getHand().setBlind(0);
        gameAreaPanel.getPlayerHand().getHand().setPlay(0);
        gameAreaPanel.getPlayerHand().getHand().setTrips(0);

        exposeCommunityCards();
    }

    private void exposeCommunityCards(){
        gameAreaPanel.getCommunityCards().exposeAll();
        gameAreaPanel.clearActions();

        exposeDealer();
    }

    private void exposeDealer(){
        state = UthGameState.DEALER_TURN;
        stage = UthGameStage.FINAL;

        gameAreaPanel.getDealerHand().exposeCards();

        Timer timer = new Timer(2000, e -> {
            if (gameAreaPanel.getPlayerHand().getHand().getState() != UthHandState.FOLD){
                evaluateHands();
            } else {
                reset();
            }

        });
        timer.setRepeats(false);
        timer.start();

    }

    private void evaluateHands(){
        state = UthGameState.EVALUATION_PHASE;

        gameEngine.evaluateHand(gameAreaPanel.getCommunityCards().getCards(), gameAreaPanel.getPlayerHand().getHand());
        gameEngine.evaluateHand(gameAreaPanel.getCommunityCards().getCards(), gameAreaPanel.getDealerHand().getHand());

        gameEngine.determineGameResults(gameAreaPanel.getCommunityCards().getCards(), gameAreaPanel.getPlayerHand().getHand(), gameAreaPanel.getDealerHand().getHand());

        gameEngine.evaluateTrips(gameAreaPanel.getPlayerHand().getHand());

        gameEngine.processResults(gameAreaPanel.getPlayerHand().getHand(), gameAreaPanel.getDealerHand().getHand());

        displayResults();
    }

    private void displayResults(){
        state = UthGameState.DISPLAY_RESULT;

        // TODO: Change TWO_PAIRS, ROYAL_F, STRAIGHT_F to TWO_PAIR, ROYAL_FLUSH, STRAIGHT_FLUSH
        gameAreaPanel.getDealerHand().displayHandCombination();

        gameAreaPanel.getPlayerHand().displayHandCombination();
        gameAreaPanel.displayResults(gameAreaPanel.getPlayerHand().getHand().getState());
        gameAreaPanel.displayBlindMultiplier(gameAreaPanel.getPlayerHand().getHand().getEvaluatedHand().handCombination());
        gameAreaPanel.displayTripsMultiplier(gameAreaPanel.getPlayerHand().getHand().getTripsState());

        proceedToPayouts();

    }

    private void proceedToPayouts(){
        state = UthGameState.PAYOUT;

        boolean dealerQualifies = gameAreaPanel.getDealerHand().getHand().getEvaluatedHand().handCombination().getValue() > -2;
        if (gameAreaPanel.getPlayerHand().getHand().getState() == UthHandState.WON){
            gameAreaPanel.getBetPanel().payWin(gameAreaPanel.getPlayerHand().getHand(), dealerQualifies);
            stateManager.getProfile().increaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getAnte());
            stateManager.getProfile().increaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getBlind());
            stateManager.getProfile().increaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getPlay());
        } else if (gameAreaPanel.getPlayerHand().getHand().getState() == UthHandState.TIE){
            stateManager.getProfile().increaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getAnte());
            stateManager.getProfile().increaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getBlind());
            stateManager.getProfile().increaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getPlay());
        } else {
            gameAreaPanel.getBetPanel().clearAnteChips();
            gameAreaPanel.getBetPanel().clearBlindChips();
            gameAreaPanel.getBetPanel().clearPlayChips();
        }

        if (gameAreaPanel.getPlayerHand().getHand().getTripsState().getValue() > 0){
            gameAreaPanel.getBetPanel().payTripsWin(gameAreaPanel.getPlayerHand().getHand());
            stateManager.getProfile().increaseBalanceBy(gameAreaPanel.getPlayerHand().getHand().getTrips());
        } else {
            gameAreaPanel.getBetPanel().clearTripsChips();
        }

        updateBalanceDisplay();

        Timer timer = new Timer(10000, e -> {
            reset();
        });
        timer.setRepeats(false);
        timer.start();

    }

    @Override
    public void returnToMainMenu() {
        if (state.ordinal() > UthGameState.GAME_STARTED.ordinal()) {
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

    private void reset(){
        state = UthGameState.GAME_ENDED;
        gameAreaPanel.clearActions();
        gameAreaPanel.clearCards();
        gameAreaPanel.clearAllChips();
        state = UthGameState.BET_PHASE;
        stage = UthGameStage.START;
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


        cards = Shoe.createShoe(1, UIDeck::new).cards();
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
        cards = Shoe.createShoe(1, UIDeck::new).cards();
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
     * Draws and removes the top card from the shoe.
     * <p>
     * This method retrieves the top card from the shoe and removes it from the deck,
     * simulating the process of dealing a card in the game.
     *
     * @return The top card from the shoe as {@link }.
     */
    private UthCardUI getCardFromShoe() {
        UICard card = cards.remove(0);
        return new UthCardUI(card.getRank(), card.getSuit(), card.getAsset());
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

    //TODO: remove before production
    @SuppressWarnings("Unused")
    private List<UICard> getCustomDeck(){
        List<UICard> cards = new ArrayList<>();
        cards.add(new UthCardUI("A", "H", CardAsset.HA));
        cards.add(new UthCardUI("K", "H", CardAsset.HK));
        cards.add(new UthCardUI("Q", "H", CardAsset.HQ));
        cards.add(new UthCardUI("J", "H", CardAsset.HJ));
        cards.add(new UthCardUI("10", "S", CardAsset.S10));
        cards.add(new UthCardUI("10", "H", CardAsset.H10));
        cards.add(new UthCardUI("2", "H", CardAsset.H2));
        cards.add(new UthCardUI("J", "D", CardAsset.DJ));
        cards.add(new UthCardUI("J", "S", CardAsset.SJ));
        cards.add(new UthCardUI("5", "H", CardAsset.H5));
        cards.add(new UthCardUI("4", "C", CardAsset.C4));
        cards.add(new UthCardUI("A", "H", CardAsset.HA));
        cards.add(new UthCardUI("8", "S", CardAsset.S8));
        cards.add(new UthCardUI("K", "S", CardAsset.SK));
        cards.add(new UthCardUI("A", "S", CardAsset.SA));
        return cards;
    }
}
