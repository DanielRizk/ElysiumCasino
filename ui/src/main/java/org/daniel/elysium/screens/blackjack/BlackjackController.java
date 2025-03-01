package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.blackjack.BlackjackEngine;
import org.daniel.elysium.blackjack.HandState;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.models.UICard;
import org.daniel.elysium.models.UIDeck;
import org.daniel.elysium.screens.blackjack.center.GameAreaPanel;
import org.daniel.elysium.screens.blackjack.center.models.PlayerHandUI;
import org.daniel.elysium.screens.blackjack.chips.ChipPanel;
import org.daniel.elysium.screens.blackjack.constants.GameActions;
import org.daniel.elysium.screens.blackjack.constants.GameState;
import org.daniel.elysium.screens.blackjack.top.TopPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BlackjackController implements BlackjackMediator {
    // State managers
    private final StateManager stateManager;
    private GameState state = GameState.BET_PHASE;

    // References to subcomponents.
    private final TopPanel topPanel;
    private final ChipPanel chipPanel;
    private final GameAreaPanel gameAreaPanel;

    // Define the game logic engine
    private BlackjackEngine gameEngine;

    // Game cards creation
    Shoe<UICard> shoe = Shoe.createShoe(4, UIDeck::new);
    private List<UICard> cards = shoe.getCards();

    public BlackjackController(StateManager stateManager) {
        this.stateManager = stateManager;
        this.gameEngine = new BlackjackEngine();
        this.topPanel = new TopPanel(this, stateManager);
        this.chipPanel = new ChipPanel(this, stateManager);
        this.gameAreaPanel = new GameAreaPanel(this, stateManager);
    }

    /**
     * When a chip is selected from the chip panel, this method validates
     * if the chip value is less than the current balance, it then validates
     * if the bet circle of the player hand can have more chips stacked,
     * then it adds the chip, decrease the balance, updates the current balance,
     * show clear bet button, and display the deal button to start the game.
     */
    @Override
    public void onChipSelected(Chip chip) {
        if (chip.getValue() <= stateManager.getProfile().getBalance()) {
            PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(PlayerHandUI.FIRST_HAND);
            if (playerHandUI.canAddChip()){
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

    /*======================
        Initial actions
    ======================*/

    /**
     * When the clear bet button is clicked, it clears all the chips on the bet panel
     * and return the bet to the balance, hides the clearBet button, hides the deal button
     * and updates the balance.
     */
    @Override
    public void onClearBet() {
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(PlayerHandUI.FIRST_HAND);
        stateManager.getProfile().increaseBalanceBy(playerHandUI.getHand().getBet());
        // This has to be after the balance increase because the bet will be set to 0
        playerHandUI.clearChips();
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.showClearBetButton(false);
        updateBalanceDisplay();
    }

    /**
     * When the deal button is clicked, the bet is validated, game stated is updated,
     * then the game area is updated. The initial cards are dealt and initial,
     * hands processing is done to determine the appropriate path.
     */
    @Override
    public void onDealRequested() {
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(PlayerHandUI.FIRST_HAND);

        // Handle invalid bet
        if (playerHandUI.getHand().getBet() == 0) {
            new Toast(stateManager.getFrame(),
                    "No bet placed yet.",
                    3000).setVisible(true);
            return;
        } else if (playerHandUI.getHand().getBet() < StateManager.MIN_BET) {
            new Toast(stateManager.getFrame(),
                    "Min bet is 10$",
                    3000).setVisible(true);
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
        if (isPlayerBlackjack()){ // if player does
            dealerTurn();
            return;
        }

        // If dealer has ace as first card, go to insurance
        if (checkInsurance()){
            displayInsuranceOptions();
            return;
        }

        // If dealer has a blackjack, go to dealer turn
        if (isDealerBlackjack()){
            dealerTurn();
            return;
        }

        // Otherwise start the game normally
        calculatePlayerOptions(PlayerHandUI.FIRST_HAND);
    }

    /** Update game state and deal initial cards */
    @Override
    public void dealInitialCards() {
        state = GameState.DEALING_CARDS;
        gameAreaPanel.addPlayerCard(PlayerHandUI.FIRST_HAND, getCardFromShoe());
        gameAreaPanel.addDealerCard(getCardFromShoe());
        gameAreaPanel.addPlayerCard(PlayerHandUI.FIRST_HAND, getCardFromShoe());
        gameAreaPanel.addDealerCard(getCardFromShoe());
        gameAreaPanel.getDealerHand().flipCardDown(); // Hide dealer second card
    }

    /**
     * Return to main menu, If player is in a middle of a game
     * and leaves, her loses the bet
     */
    @Override
    public void returnToMainMenu() {
        if (state.ordinal() > GameState.GAME_STARTED.ordinal()){
            StyledConfirmDialog dialog = new StyledConfirmDialog(stateManager.getFrame(),
                    "If you exit now you will lose your bet, Continue?");
            dialog.setVisible(true);
            if (dialog.isConfirmed()){
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

    /** Check if the player has a blackjack */
    private boolean isPlayerBlackjack(){
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(PlayerHandUI.FIRST_HAND);
        return playerHandUI.getHand().isBlackJack();
    }

    /** Check if the dealer has a blackjack */
    private boolean isDealerBlackjack(){
        return gameAreaPanel.getDealerHand().getHand().isBlackJack();
    }

    /*======================
        Insurance methods
    ======================*/

    /**
     * This method passes the dealer hand to the backend logic to determine
     * if it has an ace as the first hand, then checks if the player has enough
     * balance for insurance.
     */
    private boolean checkInsurance(){
        return gameEngine.isInsurance(gameAreaPanel.getDealerHand().getHand())
                && (int)(gameAreaPanel.getPlayerHand(0).getBet() * 0.5) <=
                stateManager.getProfile().getBalance();
    }

    /**
     * Update game state, create the insurance map
     * for the game area panel to generate the insurance buttons
     */
    private void displayInsuranceOptions(){
        state = GameState.PLAYER_TURN;
        Map<GameActions, Integer> actions = new LinkedHashMap<>();
        actions.put(GameActions.INSURE, PlayerHandUI.FIRST_HAND);
        actions.put(GameActions.DO_NOT_INSURE, PlayerHandUI.FIRST_HAND);
        gameAreaPanel.updateActionButtons(actions);
    }

    /**
     * Handler for insure option, Add the insurance to the extra bet circle
     * and decreases balance. Evaluates the insurance.
     */
    private void handleInsureOption() {
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(PlayerHandUI.FIRST_HAND);
        playerHandUI.addInsuranceBet();
        stateManager.getProfile().decreaseBalanceBy(playerHandUI.getInsuranceBet());
        updateBalanceDisplay();

        // Wait for a second for the user to see the outcome of the insurance, Can we do better?
        Timer timer = new Timer(1000, e -> {
            if (!isDealerBlackjack()) { // If dealer is not blackjack, insurance lost, game continues
                playerHandUI.clearInsuranceBet();
                calculatePlayerOptions(0);
            } else { // If dealer is blackjack, Dealer wins and set hand state to INSURED
                playerHandUI.getHand().setState(HandState.INSURED);
                dealerTurn();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /** Handler for do not insure option. Evaluates the insurance and proceeds the game. */
    private void handleDoNotInsureOption(){
        if (isDealerBlackjack()){ // If dealer is blackjack dealer wins
            dealerTurn();
        } else { // Otherwise game continues
            calculatePlayerOptions(PlayerHandUI.FIRST_HAND);
        }
    }

    /*======================
        Player actions
    ======================*/

    /** Update game state, and get the available options for the player hand*/
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
     * Helper method to map between the output of the backend and the
     * creation of the map required by the game area panel to generate the action buttons.
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
     * Triggered by the actions listeners of the action buttons,
     * and maps each action to its respective handler
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
     * Handler for hit option, Validates the draw of a card
     * and determine the appropriate flow for the game
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

    /** Handler for stand option, it directs the game to the appropriate flow of the game */
    private void handleStandOption(int index){
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(index);
        playerHandUI.setHighlight(false); // turns the highlight off at all cases
        gameAreaPanel.clearActions();

        // If there is another hand (split) and this the first hand, go to the second hand
        if (checkForSecondHand() && index < 1){
            index++; // increment to second hand index
            playerHandUI.getHand().setHandSplit(true); // to prevent black for split hands
            gameAreaPanel.addPlayerCard(index, getCardFromShoe()); // second split hand has one card after split.
            calculatePlayerOptions(index);
        } else {
            dealerTurn();
        }
    }

    /** Handler for double option, Adds the bet decreases the balance and goes to stand */
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
     * Handler for split option, invokes the split method in the game area,
     * and then decrease the balance and display the available option for the first split hand
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

    /** Updates the game state, and handles the dealer turn */
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
                if (gameAreaPanel.getDealerHand().addCard(peekCardFromShoe())){
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
     * Updates the game state, Send the hand to the backend logic for
     * evaluation against the daler cards, and displays the result message
     */
    private void evaluateGameResults(){
        state = GameState.EVALUATION_PHASE;
        for (PlayerHandUI playerHandUI : gameAreaPanel.getPlayerHands()){
            gameEngine.resolvePlayerResult(playerHandUI.getHand(),
                    gameAreaPanel.getDealerHand().getHand());

            // placeholder for the final result message
            new Toast(stateManager.getFrame(),
                    "Hand " + playerHandUI.getHand().getState(),
                    3000).setVisible(true);
        }
        proceedToPayouts();
    }

    /** Updates the game state, handles the UI payout by adding/removing chips,
     * and updates the balance. Finally, it goes to the reset routine to start a new game
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
        Timer timer = new Timer(5000, e -> {
            reset();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /*======================
        Reset
    ======================*/

    /** Updates the game state, and reset everything to start a new game */
    private void reset(){
        state = GameState.GAME_ENDED;
        gameAreaPanel.clearActions();
        gameAreaPanel.clearHands();
        chipPanel.setVisible(true);
        gameEngine = new BlackjackEngine();
        state = GameState.BET_PHASE;

        // If player has no enough money, Player then escorted to main menu
        if (stateManager.getProfile().getBalance() < StateManager.MIN_BET){
            stateManager.switchPanel("MainMenu");
        }

        // If the shoe has less than 10 cards, start a new shoe
        if (shoe.getCards().size() < 10){
            cards = Shoe.createShoe(4, UIDeck::new).getCards();
            new Toast(stateManager.getFrame(),
                    "Shoe Ended", 3000).setVisible(true);
        }
    }

    /*======================
        Helper methods
    ======================*/

    /** Checks if the player has split hands */
    private boolean checkForSecondHand(){
        return gameAreaPanel.getPlayerHands().size() > 1;
    }

    /** Update the balance display with the current balance */
    @Override
    public void updateBalanceDisplay() {
        topPanel.setBalance("Balance: " + stateManager.getProfile().getBalance());
    }

    /** Returns and removes the top card from the shoe */
    private UICard getCardFromShoe() {
        return cards.remove(0);
    }

    /** Returns the top card from the shoe */
    private UICard peekCardFromShoe(){
        return cards.get(0);
    }

    /** Removes the top card from the shoe */
    private void burnCard(){
        getCardFromShoe();
    }

    /*======================
        Getters
    ======================*/

    /** Returns the reference to the top panel object */
    public TopPanel getTopPanel() { return topPanel; }

    /** Returns the reference to the chip panel object */
    public ChipPanel getChipPanel() { return chipPanel; }

    /** Returns the reference to the game area panel object */
    public GameAreaPanel getGameAreaPanel() { return gameAreaPanel; }

    //TODO: remove before production
    private List<UICard> getCustomDeck(){
        List<UICard> cards = new ArrayList<>();
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("10", "S", CardAsset.S10));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("4", "H", CardAsset.H4));
        cards.add(new UICard("A", "S", CardAsset.SA));
        cards.add(new UICard("4", "C", CardAsset.C4));
        cards.add(new UICard("4", "H", CardAsset.H4));
        cards.add(new UICard("4", "C", CardAsset.C4));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("K", "S", CardAsset.SK));
        cards.add(new UICard("K", "S", CardAsset.SK));
        return cards;
    }
}


