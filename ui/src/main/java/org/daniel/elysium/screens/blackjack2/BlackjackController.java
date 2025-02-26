package org.daniel.elysium.screens.blackjack2;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.blackjack.BlackjackEngine;
import org.daniel.elysium.blackjack.HandState;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.models.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlackjackController implements BlackjackMediator {
    private final StateManager stateManager;
    private GameState state = GameState.BET_PHASE;
    private int currentBet = 0;

    // References to subcomponents.
    private final TopPanel topPanel;
    private final BettingPanel bettingPanel;
    private final ChipPanel chipPanel;
    private final GameAreaPanel gameAreaPanel;

    private BlackjackEngine gameEngine;

    // Game cards creation
    Shoe<UICard> shoe = Shoe.createShoe(4, UIDeck::new);
    private final List<UICard> cards = shoe.getCards();

    public BlackjackController(StateManager stateManager) {
        this.stateManager = stateManager;
        this.gameEngine = new BlackjackEngine();
        this.topPanel = new TopPanel(this, stateManager);
        this.bettingPanel = new BettingPanel(this, stateManager);
        this.chipPanel = new ChipPanel(this, stateManager);
        this.gameAreaPanel = new GameAreaPanel(this);
    }

    @Override
    public void onChipSelected(Chip chip) {
        if (chip.getValue() <= stateManager.getProfile().getBalance()) {
            if (bettingPanel.canAddChip()){
                bettingPanel.addChip(chip);
                currentBet += chip.getValue();
                bettingPanel.updateBetDisplay(currentBet);
                stateManager.getProfile().decreaseBalanceBy(chip.getValue());
                updateBalanceDisplay();
                gameAreaPanel.showDealButton(true);
            } else {
                new Toast(stateManager.getFrame(), "Max number of chips reached.", 3000).setVisible(true);
            }
        } else {
            new Toast(stateManager.getFrame(), "Not enough balance.", 3000).setVisible(true);
        }
    }

    @Override
    public void onClearBet() {
        stateManager.getProfile().increaseBalanceBy(currentBet);
        currentBet = 0;
        bettingPanel.clearChips();
        gameAreaPanel.showDealButton(false);
        bettingPanel.updateBetDisplay(currentBet);
        updateBalanceDisplay();
    }

    @Override
    public void onDealRequested() {
        if (currentBet == 0) {
            new Toast(stateManager.getFrame(), "No bet placed yet.", 3000).setVisible(true);
            return;
        }
        chipPanel.setVisible(false);
        gameAreaPanel.showDealButton(false);
        bettingPanel.clearActions();
        dealInitialCards();

        if (checkInsurance()){
            displayInsuranceOptions();
        } else if (!checkForBlackJack()){
            calculatePlayerOptions();
        } else {

        }
    }

    private void displayInsuranceOptions(){
        state = GameState.PLAYER_TURN;
        List<GameActions> actions = Arrays.asList(GameActions.INSURE, GameActions.DO_NOT_INSURE);
        bettingPanel.updateActionButtons(actions);
    }

    private boolean checkInsurance(){
        return gameEngine.isInsurance();
    }

    private boolean checkForBlackJack(){
        return gameEngine.isAnyBlackjack();
    }

    private void calculatePlayerOptions() {
        state = GameState.PLAYER_TURN;
        bettingPanel.updateActionButtons(getOptions());
    }

    private List<GameActions> getOptions(){
        List<GameActions> actions = new ArrayList<>();
        for (String option: gameEngine.getAvailableHandOptions()){
            switch (option){
                case "HIT" -> actions.add(GameActions.HIT);
                case "STAND" -> actions.add(GameActions.STAND);
                case "DOUBLE" -> {
                    if (currentBet <= stateManager.getProfile().getBalance()) {
                        actions.add(GameActions.DOUBLE);
                    }
                }
                case "SPLIT" -> {
                    if (currentBet <= stateManager.getProfile().getBalance()) {
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
        gameEngine.getPlayerHand().dealCard(new BJCard(playerCard1.getRank(), playerCard1.getSuit()));
        UICard dealerCard1 = getCardFromShoe();
        gameEngine.getDealerHand().dealCard(new BJCard(dealerCard1.getRank(), dealerCard1.getSuit()));

        UICard playerCard2 = getCardFromShoe();
        gameEngine.getPlayerHand().dealCard(new BJCard(playerCard2.getRank(), playerCard2.getSuit()));
        UICard dealerCard2 = getCardFromShoe();
        gameEngine.getDealerHand().dealCard(new BJCard(dealerCard2.getRank(), dealerCard2.getSuit()));
        dealerCard2.setFaceDown();

        gameAreaPanel.addPlayerCard(playerCard1);
        gameAreaPanel.addDealerCard(dealerCard1);
        gameAreaPanel.addPlayerCard(playerCard2);
        gameAreaPanel.addDealerCard(dealerCard2);
    }

    @Override
    public void onActionSelected(GameActions action) {
        switch (action){
            case HIT -> handleHitOption();
            case STAND -> handleStandOption();
        }
    }

    private void handleHitOption(){
        UICard card = getCardFromShoe();
        gameEngine.getPlayerHand().dealCard(new BJCard(card.getRank(), card.getSuit()));
        gameAreaPanel.addPlayerCard(card);
        bettingPanel.updateActionButtons(getOptions());
    }

    private void handleStandOption(){
        bettingPanel.clearActions();
        dealerTurn();
    }

    private void dealerTurn(){
        state = GameState.DEALER_TURN;
        gameAreaPanel.exposeDealer();
        while (true){
            UICard card = getCardFromShoe();
            if (!gameEngine.getDealerHand().dealCard(new BJCard(card.getRank(), card.getSuit()))){
                break;
            }
            gameAreaPanel.addDealerCard(card);
        }
    }

    @Override
    public void returnToMainMenu() {
        if (state.ordinal() > GameState.BET_PHASE.ordinal()){
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

    // Getter methods for the panels.
    public TopPanel getTopPanel() { return topPanel; }
    public BettingPanel getBettingPanel() { return bettingPanel; }
    public ChipPanel getChipPanel() { return chipPanel; }
    public GameAreaPanel getGameAreaPanel() { return gameAreaPanel; }
}


