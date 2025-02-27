package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.blackjack.BlackjackEngine;
import org.daniel.elysium.blackjack.HandState;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.models.UICard;
import org.daniel.elysium.models.UIDeck;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BlackjackController implements BlackjackMediator {
    private final StateManager stateManager;
    private GameState state = GameState.BET_PHASE;

    // References to subcomponents.
    private final TopPanel topPanel;
    //private BettingPanel bettingPanel;
    private ChipPanel chipPanel;
    private GameAreaPanel gameAreaPanel;

    private BlackjackEngine gameEngine;

    // Game cards creation
    Shoe<UICard> shoe = Shoe.createShoe(4, UIDeck::new);
    private final List<UICard> cards = shoe.getCards();

    public BlackjackController(StateManager stateManager) {
        this.stateManager = stateManager;
        this.gameEngine = new BlackjackEngine();
        this.topPanel = new TopPanel(this, stateManager);
        this.chipPanel = new ChipPanel(this, stateManager);
        this.gameAreaPanel = new GameAreaPanel(this, stateManager);
    }

    @Override
    public void onChipSelected(Chip chip) {
        if (chip.getValue() <= stateManager.getProfile().getBalance()) {
            if (gameAreaPanel.getPlayerHand(0).canAddChip()){
                gameAreaPanel.getPlayerHand(0).addChip(chip);
                gameAreaPanel.updateBetDisplay(0, gameAreaPanel.getPlayerHand(0).getHand().getBet());
                stateManager.getProfile().decreaseBalanceBy(chip.getValue());
                gameAreaPanel.showDealButton(true);
                updateBalanceDisplay();
            } else {
                new Toast(stateManager.getFrame(), "Max number of chips reached.", 3000).setVisible(true);
            }
        } else {
            new Toast(stateManager.getFrame(), "Not enough balance.", 3000).setVisible(true);
        }
    }

    @Override
    public void onClearBet() {
        stateManager.getProfile().increaseBalanceBy(gameAreaPanel.getPlayerHand(0).getHand().getBet());
        gameAreaPanel.getPlayerHand(0).clearChips();
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.updateBetDisplay(0, 0);
        updateBalanceDisplay();
    }

    @Override
    public void onDealRequested() {
        if (gameAreaPanel.getPlayerHand(0).getHand().getBet() == 0) {
            new Toast(stateManager.getFrame(), "No bet placed yet.", 3000).setVisible(true);
            return;
        }
        state = GameState.GAME_STARTED;
        chipPanel.setVisible(false);
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.clearActions();
        dealInitialCards();

        if (checkInsurance()){
            displayInsuranceOptions();
        } else if (!checkForBlackJack()){
            calculatePlayerOptions();
        } else {
            evaluateGameResults();
        }
    }

    private void reset(){
        state = GameState.GAME_ENDED;
        gameAreaPanel.clearActions();
        gameAreaPanel.clearHands();
        chipPanel.setVisible(true);
        gameEngine = new BlackjackEngine();
        state = GameState.BET_PHASE;
    }

    private void evaluateGameResults(){
        state = GameState.EVALUATION_PHASE;
        for (PlayerHandUI playerHandUI : gameAreaPanel.getPlayerHands()){
            gameEngine.resolvePlayerResult(playerHandUI.getHand(),
                    gameAreaPanel.getDealerHand().getHand());
            new Toast(stateManager.getFrame(),
                    "Hand" + playerHandUI.getHand().getState(),
                    3000).setVisible(true);
        }

        proceedToPayouts();
    }

    private void proceedToPayouts(){
        state = GameState.PAYOUT;
        List<PlayerHandUI> allHands = gameAreaPanel.getPlayerHands();
        for (PlayerHandUI playerHandUI : allHands) {
            stateManager.getProfile().increaseBalanceBy(playerHandUI.getHand().getBet());
            if (playerHandUI.getHand().getState() == HandState.WON) {
                List<Chip> chips = new ArrayList<>(playerHandUI.getBetPanel().getChips());
                for (Chip chip : chips) {
                    playerHandUI.addPayoutChip(chip);
                }
            }
        }
        updateBalanceDisplay();

        Timer timer = new Timer(3000, e -> {
            reset();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void displayInsuranceOptions(){
        state = GameState.PLAYER_TURN;
        List<GameActions> actions = Arrays.asList(GameActions.INSURE, GameActions.DO_NOT_INSURE);
        gameAreaPanel.updateActionButtons(actions);
    }

    private boolean checkInsurance(){
        return gameEngine.isInsurance(gameAreaPanel.getDealerHand().getHand());
    }

    private boolean checkForBlackJack(){
        //return gameEngine.isAnyBlackjack();
        return false;
    }

    private void calculatePlayerOptions() {
        state = GameState.PLAYER_TURN;
        gameAreaPanel.updateActionButtons(getOptions(0));
    }

    private List<GameActions> getOptions(int index){
        List<GameActions> actions = new ArrayList<>();
        for (String option: gameEngine.getAvailableHandOptions(gameAreaPanel.getPlayerHand(index).getHand())){
            switch (option){
                case "HIT" -> actions.add(GameActions.HIT);
                case "STAND" -> actions.add(GameActions.STAND);
                case "DOUBLE" -> {
                    if (gameAreaPanel.getPlayerHand(index).getHand().getBet() <= stateManager.getProfile().getBalance()) {
                        actions.add(GameActions.DOUBLE);
                    }
                }
                case "SPLIT" -> {
                    if (gameAreaPanel.getPlayerHand(index).getHand().getBet() <= stateManager.getProfile().getBalance()) {
                        actions.add(GameActions.SPLIT);
                    }
                }
            }
        }
        return actions;
    }

    @Override
    public void updateBalanceDisplay() {
        topPanel.setBalance("Balance: " + stateManager.getProfile().getBalance());
    }

    @Override
    public void dealInitialCards() {
        state = GameState.DEALING_CARDS;

        UICard playerCard1 = getCardFromShoe();
        UICard dealerCard1 = getCardFromShoe();
        UICard playerCard2 = getCardFromShoe();
        UICard dealerCard2 = getCardFromShoe();
        dealerCard2.setFaceDown();

        gameAreaPanel.addPlayerCard(0, playerCard1);
        gameAreaPanel.addDealerCard(dealerCard1);
        gameAreaPanel.addPlayerCard(0, playerCard2);
        gameAreaPanel.addDealerCard(dealerCard2);
    }

    @Override
    public void onActionSelected(GameActions action) {
        switch (action){
            case HIT -> handleHitOption(0);
            case STAND -> handleStandOption();
        }
    }

    private void handleHitOption(int index){
        UICard card = getCardFromShoe();
        gameAreaPanel.addPlayerCard(index, card);
        gameAreaPanel.updateActionButtons(getOptions(index));
    }

    private void handleStandOption(){
        gameAreaPanel.clearActions();
        dealerTurn();
    }

    private void dealerTurn(){
        state = GameState.DEALER_TURN;
        gameAreaPanel.getDealerHand().flipCardUp();
        while (true){
            UICard card = getCardFromShoe();
            if (!gameAreaPanel.getDealerHand().addCard(card)){
                break;
            }
            gameAreaPanel.addDealerCard(card);
        }

        evaluateGameResults();
    }

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

    // Helper to create a UI component for a card.
    private UICard getCardFromShoe() {
        return cards.remove(0);
    }

    public int getMediatorWidth(){
        return stateManager.getFrame().getWidth();
    }

    public int getMediatorHeight(){
        return stateManager.getFrame().getHeight();
    }

    // Getter methods for the panels.
    public TopPanel getTopPanel() { return topPanel; }
    //public BettingPanel getBettingPanel() { return bettingPanel; }
    public ChipPanel getChipPanel() { return chipPanel; }
    public GameAreaPanel getGameAreaPanel() { return gameAreaPanel; }
}


