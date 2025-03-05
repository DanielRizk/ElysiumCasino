package org.daniel.elysium.ultimateTH.model;

import org.daniel.elysium.ultimateTH.constants.UthHandState;
import org.daniel.elysium.ultimateTH.pokerCore.models.PokerEvaluatedHandModel;

import java.util.ArrayList;
import java.util.List;

public class UthHand {
    private final List<UthCard> hand;
    private UthHandState state = UthHandState.UNDEFINED;
    private PokerEvaluatedHandModel evaluatedHand = null;

    private boolean kickerCard = false;

    public UthHand() {
        this.hand = new ArrayList<>();
    }

    public List<UthCard> getHand(){
        return hand;
    }

    public UthHandState getState() {
        return state;
    }

    public PokerEvaluatedHandModel getEvaluatedHand() {
        return evaluatedHand;
    }

    public void setEvaluatedHand(PokerEvaluatedHandModel evaluatedHand) {
        this.evaluatedHand = evaluatedHand;
    }

    public void setState(UthHandState state) {
        this.state = state;
    }

    public boolean isKickerCard() {
        return kickerCard;
    }

    public void setKickerCard(boolean kickerCard) {
        this.kickerCard = kickerCard;
    }

    public void dealCard(UthCard card) {
        hand.add(card);
    }
}
