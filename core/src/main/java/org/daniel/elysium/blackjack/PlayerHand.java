package org.daniel.elysium.blackjack;

import org.daniel.elysium.models.BJCard;

import java.util.ArrayList;
import java.util.List;

public class PlayerHand extends BJHand{

    private boolean isHandSplit = false;

    private int bet = 0;

    private int insuranceBet = 0;

    public PlayerHand() {
    }

    @Override
    public boolean dealCard(BJCard card) {
        if (getHandValue() < 21){
            getHand().add(card);
            return true;
        }
        return false;
    }

    @Override
    public boolean isBlackJack() {
        return getHand().size() <= 2 && getHandValue() == 21 && !isHandSplit;
    }

    @Override
    public int getHandValue() {
        int handValue = 0;
        int aces = 0;

        for (BJCard card : getHand()) {
            int cardValue = card.getValue();
            if (cardValue == 11){
                handValue += 11;
                aces ++;
            } else {
                handValue += cardValue;
            }
        }

        while (handValue > 21 && aces > 0) {
            handValue -= 10;
            aces--;
        }

        return handValue;
    }

    public boolean isSplittable(){
        return getHand().size() == 2 && getHand().get(0).getValue() == getHand().get(1).getValue();
    }

    public void setHandSplit(boolean handSplit) {
        this.isHandSplit = handSplit;
    }

    public boolean isHandSplit() {
        return isHandSplit;
    }

    public boolean splittingAces(){
        return this.getHand().get(0).getValue() == 11 && this.getHand().get(1).getValue() == 11;
    }

    public void newHand(BJCard card1){
        List<BJCard> newHand = new ArrayList<>();
        newHand.add(card1);
        this.hand = newHand;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getInsuranceBet() {
        return insuranceBet;
    }

    public void setInsuranceBet(int insuranceBet) {
        this.insuranceBet = insuranceBet;
    }
}