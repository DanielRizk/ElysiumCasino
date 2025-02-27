package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.blackjack.PlayerHand;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.models.UICard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerHandUI extends JPanel {
    private final PlayerCardsPanel playerHand;
    private final BetPanel betPanel;
    private final PlayerHand hand;

    public PlayerHandUI() {
        // Use BoxLayout for vertical stacking
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Configure the player's hand panel
        playerHand = new PlayerCardsPanel();
        playerHand.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(playerHand);

        // Configure the bet panel
        betPanel = new BetPanel();
        betPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(betPanel);

        hand = new PlayerHand();
    }

    public void setHighlight(boolean highlight){
        playerHand.setHighlight(highlight);
    }

    public PlayerHand getHand(){
        return hand;
    }

    public boolean addCard(UICard uiCard){
        if (hand.dealCard(uiCard.getCard())){
            playerHand.addCard(uiCard);
            return true;
        }
        return false;
    }

    public boolean canAddChip() {
        return betPanel.canAddChip();
    }

    public void addChip(Chip chip) {
        betPanel.addChipMain(chip);
        hand.setBet(hand.getBet() + chip.getValue());
        updateBetDisplay(hand.getBet());
    }

    public void payBlackjackWin(){
        List<Chip> chips = getBetPanel().getChipCombination((hand.getBet() * 3 / 5));
        for (Chip chip : chips) {
            getBetPanel().addChipMain(chip);
        }
    }

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

    public void addInsuranceBet(){
        List<Chip> chips = getBetPanel().getChipCombination((int) (hand.getBet() * 0.5));
        for (Chip chip : chips) {
            getBetPanel().addChipExtra(chip);
        }
        hand.setInsuranceBet((int) (hand.getBet() * 0.5));
    }

    public void payInsurance(){
        List<Chip> chips = getBetPanel().getChipCombination((hand.getInsuranceBet() * 2 / 3));
        for (Chip chip : chips) {
            getBetPanel().addChipExtra(chip);
        }
    }

    public void clearInsuranceBet(){
        hand.setInsuranceBet(0);
        betPanel.clearInsuranceChips();
    }

    public void clearMainBet(){
        hand.setBet(0);
        betPanel.clearMainChips();
    }

    public int getBet(){
        return hand.getBet();
    }

    public int getInsuranceBet(){
        return hand.getInsuranceBet();
    }

    public void addDoubleBet(){
        List<Chip> chips = new ArrayList<>(getBetPanel().getChipsMain());
        for (Chip chip : chips) {
            getBetPanel().addChipExtra(chip);
        }
        hand.setBet(hand.getBet() * 2);
        updateBetDisplay(hand.getBet());
    }

    public void clearChips() {
        betPanel.clearChips();
        hand.setBet(0);
        updateBetDisplay(hand.getBet());
    }

    private void updateBetDisplay(int bet){
        betPanel.updateBetDisplay(bet);
    }


    public PlayerCardsPanel getPlayerHand(){
        return playerHand;
    }

    public BetPanel getBetPanel(){
        return betPanel;
    }


}

