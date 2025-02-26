package org.daniel.elysium.blackjack;

import org.daniel.elysium.models.BJCard;

import java.util.ArrayList;
import java.util.List;

public abstract class BJHand {
    protected List<BJCard> hand;
    private HandState state = HandState.UNDEFINED;

    public BJHand() {
        this.hand = new ArrayList<>();
    }

    public List<BJCard> getHand(){
        return hand;
    }

    public HandState getState() {
        return state;
    }

    public void setState(HandState state) {
        this.state = state;
    }

    public abstract boolean dealCard(BJCard card);

    public abstract boolean isBlackJack();

    public abstract int getHandValue();
}
