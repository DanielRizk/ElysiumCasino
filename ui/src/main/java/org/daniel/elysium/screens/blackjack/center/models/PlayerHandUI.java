package org.daniel.elysium.screens.blackjack.center.models;

import org.daniel.elysium.blackjack.PlayerHand;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.models.UICard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerHandUI extends JPanel {
    private final PlayerCardsPanel playerCards;
    private final BetPanel betPanel;
    private final PlayerHand hand;

    public static final int FIRST_HAND = 0;

    public PlayerHandUI() {
        // Use BoxLayout for vertical stacking
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Configure the player's hand panel
        playerCards = new PlayerCardsPanel();
        playerCards.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(playerCards);

        // Configure the bet panel
        betPanel = new BetPanel();
        betPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(betPanel);

        // Create the logical backend hand
        hand = new PlayerHand();
    }

    /*========================
      Fields getter
     ========================*/

    /** Get the player cards panel - UI */
    public PlayerCardsPanel getPlayerCards(){
        return playerCards;
    }

    /** Get the player bet panel - UI */
    public BetPanel getBetPanel(){
        return betPanel;
    }

    /** Get the player hand - Backend */
    public PlayerHand getHand(){
        return hand;
    }

    /*========================
      Bet setters and getters
     ========================*/

    /** Get the player main bet */
    public int getBet(){
        return hand.getBet();
    }

    /** Get the player insurance bet */
    public int getInsuranceBet(){
        return hand.getInsuranceBet();
    }

    /** Set the player main bet */
    public void setBet(int bet){
        hand.setBet(bet);
    }

    /** Set the player insurance bet */
    public void setInsuranceBet(int insuranceBet){
        hand.setInsuranceBet(insuranceBet);
    }

    /*========================
      Cards methods
     ========================*/

    /** Add a card to UI hand and backend hand, return true if possible, false if not */
    public boolean addCard(UICard uiCard){
        if (hand.canDealCard(uiCard.getCard())){
            hand.dealCard(uiCard.getCard());
            playerCards.addCard(uiCard);
            return true;
        }
        return false;
    }

    /** Toggle UI cards highlight effect */
    public void setHighlight(boolean highlight){
        playerCards.setHighlight(highlight);
    }

    /*========================
      Chips and bet methods
     ========================*/

    /** Checks if the bet panel reached max limit of chips */
    public boolean canAddChip() {
        return betPanel.canAddChip();
    }

    /** Add chip as UI to bet panel and as value to the hand, then update display */
    public void addChip(Chip chip) {
        betPanel.addChipMain(chip);
        setBet(getBet() + chip.getValue());
        updateBetDisplay(getBet());
    }

    /** Duplicate the chip on the hand bet panel as UI and value, then update display */
    public void addDoubleChip(){
        List<Chip> chips = new ArrayList<>(getBetPanel().getChipsMain());
        for (Chip chip : chips) {
            getBetPanel().addChipExtra(chip);
        }
        setBet(getBet() * 2);
        updateBetDisplay(getBet());
    }

    /** Set the insurance bet as half of the original bet */
    public void addInsuranceBet(){
        List<Chip> chips = getBetPanel().getChipCombination((int) (getBet() * 0.5));
        for (Chip chip : chips) {
            getBetPanel().addChipExtra(chip);
        }
        setInsuranceBet((int) (getBet() * 0.5));
    }

    /** Add the profit to the bet panel for blackjack as 1.5 times of the original bet */
    public void payBlackjackWin(){
        // The bet is multiplied by 3/5 to return the profit from the revenue
        // as at this point, the game engine updated the bet with the total revenue
        // if bet 100, blackjack profit 150, revenue 250, to get 150 from 250
        // revenue [250] = 100 * (3/2 + 2/2) = 250
        // profit = revenue (250) * ((2/3) + (2/2)) = 150, 2/3 + 2/2 = 3/5#
        List<Chip> chips = getBetPanel().getChipCombination((getBet() * 3 / 5));
        for (Chip chip : chips) {
            getBetPanel().addChipMain(chip);
        }
    }

    /** Add the profit to the bet panel for insurance as 2 times of the original bet */
    public void payInsurance(){
        // The bet is multiplied by 2/3 to return the profit from the revenue
        // as at this point, the game engine updated the bet with the total revenue
        // if bet 50, insurance profit 100, revenue 150, to get 100 from 150
        // revenue [150] = 50 * (4/2 + 2/2) = 150
        // profit is always 2/3 of the revenue, 150 * 2/3 = 100
        List<Chip> chips = getBetPanel().getChipCombination((getInsuranceBet() * 2 / 3));
        for (Chip chip : chips) {
            getBetPanel().addChipExtra(chip);
        }
    }

    /** Add the profit to the main bet and the extra bet panel */
    public void payWin(){
        List<Chip> chips = new ArrayList<>(getBetPanel().getChipsMain());
        for (Chip chip : chips) {
            getBetPanel().addChipMain(chip);
        }

        chips = new ArrayList<>(getBetPanel().getChipsExtra());
        for (Chip chip : chips) {
            getBetPanel().addChipExtra(chip);
        }
    }

    /** Remove the UI chips from main bet panel and set main bet to 0 */
    public void clearMainBet(){
        hand.setBet(0);
        betPanel.clearMainChips();
        updateBetDisplay(getBet());
    }

    /** Remove the UI chips from extra bet panel and set insurance bet to 0 */
    public void clearInsuranceBet(){
        hand.setInsuranceBet(0);
        betPanel.clearExtraChips();
    }

    /** Remove the UI chips from main and extra bet panel and set main bet to 0 */
    public void clearChips() {
        betPanel.clearChips();
        hand.setBet(0);
        updateBetDisplay(getBet());
    }

    /*========================
      Helper methods
     ========================*/

    /** Refreshes and updates the bet display */
    private void updateBetDisplay(int bet){
        betPanel.updateBetDisplay(bet);
    }
}

