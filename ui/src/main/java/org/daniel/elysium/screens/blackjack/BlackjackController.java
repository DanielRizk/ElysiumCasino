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

import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

public class BlackjackController implements BlackjackMediator {
    private final StateManager stateManager;
    private GameState state = GameState.BET_PHASE;

    // References to subcomponents.
    private final TopPanel topPanel;
    private ChipPanel chipPanel;
    private GameAreaPanel gameAreaPanel;

    private BlackjackEngine gameEngine;

    // Game cards creation
    Shoe<UICard> shoe = Shoe.createShoe(4, UIDeck::new);
    private List<UICard> cards = shoe.getCards();

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
                //gameAreaPanel.updateBetDisplay(gameAreaPanel.getPlayerHand(0).getHand().getBet());
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
        gameAreaPanel.updateBetDisplay(0);
        updateBalanceDisplay();
    }

    @Override
    public void onDealRequested() {
        if (gameAreaPanel.getPlayerHand(0).getHand().getBet() < StateManager.MIN_BET) {
            new Toast(stateManager.getFrame(), "No bet placed yet.", 3000).setVisible(true);
            return;
        }
        state = GameState.GAME_STARTED;
        chipPanel.setVisible(false);
        gameAreaPanel.showDealButton(false);
        gameAreaPanel.clearActions();
        dealInitialCards();

        if (!isPlayerBlackjack()){
            if (!checkInsurance()){
                if (!isDealerBlackjack()){
                    calculatePlayerOptions(0);
                } else {
                    dealerTurn();
                }
            } else {
                displayInsuranceOptions();
            }
        } else {
            dealerTurn();
        }
    }

    private boolean isPlayerBlackjack(){
        if (gameAreaPanel.getPlayerHand(0).getHand().isBlackJack()){
            gameAreaPanel.getPlayerHand(0).getHand().setState(HandState.BLACKJACK);
            return true;
        }
        return false;
    }

    private boolean isDealerBlackjack(){
        if (gameAreaPanel.getDealerHand().getHand().isBlackJack()){
            gameAreaPanel.getDealerHand().getHand().setState(HandState.BLACKJACK);
            return true;
        }
        return false;
    }

    private void reset(){
        state = GameState.GAME_ENDED;
        gameAreaPanel.clearActions();
        gameAreaPanel.clearHands();
        chipPanel.setVisible(true);
        gameEngine = new BlackjackEngine();
        state = GameState.BET_PHASE;

        if (stateManager.getProfile().getBalance() < StateManager.MIN_BET){
            stateManager.switchPanel("MainMenu");
        }

        if (shoe.getCards().size() < 10){
            cards = Shoe.createShoe(4, UIDeck::new).getCards();
            new Toast(stateManager.getFrame(),
                    "Shoe Ended", 3000).setVisible(true);
        }
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

        Timer timer = new Timer(5000, e -> {
            reset();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void displayInsuranceOptions(){
        state = GameState.PLAYER_TURN;
        Map<GameActions, Integer> actions = new LinkedHashMap<>();
        actions.put(GameActions.INSURE, 0);
        actions.put(GameActions.DO_NOT_INSURE, 0);
        gameAreaPanel.updateActionButtons(actions);
    }

    private boolean checkInsurance(){
        return gameEngine.isInsurance(gameAreaPanel.getDealerHand().getHand())
                && (int)(gameAreaPanel.getPlayerHand(0).getBet() * 0.5) <=
                stateManager.getProfile().getBalance();
    }

    private void calculatePlayerOptions(int index) {
        state = GameState.PLAYER_TURN;
        gameAreaPanel.getPlayerHand(index).setHighlight(checkForSecondHand());

        Map<GameActions, Integer> actions = getOptions(index);
        gameAreaPanel.updateActionButtons(actions);
        if (actions.isEmpty()){
            handleStandOption(index);
        }
    }

    private Map<GameActions, Integer> getOptions(int index){
        Map<GameActions, Integer> actions = new LinkedHashMap<>();
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

    private void handleSplitOption(){
        gameAreaPanel.splitHand();
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHands().get(0);
        stateManager.getProfile().decreaseBalanceBy(playerHandUI.getBet());
        updateBalanceDisplay();
        playerHandUI.addCard(getCardFromShoe());
        playerHandUI.getHand().setHandSplit(true);
        calculatePlayerOptions(0);
    }

    private void handleDoubleOption(int index){
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(index);
        stateManager.getProfile().decreaseBalanceBy(playerHandUI.getBet());
        playerHandUI.addDoubleBet();
        updateBalanceDisplay();
        UICard card = getCardFromShoe();
        gameAreaPanel.addPlayerCard(index, card);

        if (checkForSecondHand() && index < 1){
            calculatePlayerOptions(index);
        } else {
            handleStandOption(index);
        }
    }

    private void handleInsureOption() {
        int insuranceBet = (int)(gameAreaPanel.getPlayerHand(0).getBet() * 0.5);
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(0);
        playerHandUI.addInsuranceBet();
        stateManager.getProfile().decreaseBalanceBy(insuranceBet);
        updateBalanceDisplay();

        Timer timer = new Timer(1000, e -> {
            if (!isDealerBlackjack()) {
                playerHandUI.clearInsuranceBet();
                calculatePlayerOptions(0);
            } else {
                playerHandUI.getHand().setState(HandState.INSURED);
                dealerTurn();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void handleDoNotInsureOption(){
        if (isDealerBlackjack()){
            dealerTurn();
        } else {
            calculatePlayerOptions(0);
        }
    }

    private void handleHitOption(int index){
        UICard card = getCardFromShoe();
        gameAreaPanel.addPlayerCard(index, card);
        calculatePlayerOptions(index);
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(index);
        if (playerHandUI.getHand().getHandValue() >= 21){
            if (checkForSecondHand() && index < 1){
                index++;
                calculatePlayerOptions(index);
            } else {
                handleStandOption(index);
            }
        }
    }

    private boolean checkForSecondHand(){
        return gameAreaPanel.getPlayerHands().size() > 1;
    }

    private void handleStandOption(int index){
        PlayerHandUI playerHandUI = gameAreaPanel.getPlayerHand(index);
        playerHandUI.setHighlight(false);
        gameAreaPanel.clearActions();
        if (checkForSecondHand() && index < 1){
            index++;
            playerHandUI.getHand().setHandSplit(true);
            gameAreaPanel.addPlayerCard(index, getCardFromShoe());
            calculatePlayerOptions(index);
        } else {
            dealerTurn();
        }
    }

    private void dealerTurn(){
        state = GameState.DEALER_TURN;
        gameAreaPanel.getDealerHand().flipCardUp();

        boolean isPlayerStillInTheGame = false;
        for (PlayerHandUI playerHandUI : gameAreaPanel.getPlayerHands()){
            if (!(playerHandUI.getHand().isBlackJack() || playerHandUI.getHand().getHandValue() > 21)){
                isPlayerStillInTheGame = true;
            }
        }
        if (isPlayerStillInTheGame){
            while (true){
                UICard card = peekCardFromShoe();
                if (gameAreaPanel.getDealerHand().addCard(card)){
                    card = getCardFromShoe();
                } else {
                    break;
                }
            }
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

    private UICard peekCardFromShoe(){
        return cards.get(0);
    }


    // Getter methods for the panels.
    public TopPanel getTopPanel() { return topPanel; }
    //public BettingPanel getBettingPanel() { return bettingPanel; }
    public ChipPanel getChipPanel() { return chipPanel; }
    public GameAreaPanel getGameAreaPanel() { return gameAreaPanel; }
}


