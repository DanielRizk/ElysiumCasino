package org.daniel.elysium.blackjack;

import org.daniel.elysium.models.BJCard;
import org.daniel.elysium.models.Card;

import java.util.ArrayList;
import java.util.List;

public class BlackjackEngine {

    public BlackjackEngine() {

    }

    public boolean isInsurance(DealerHand hand){
        return hand.getHand().get(0).getValue() == 11;
    }

    public void resolvePlayerResult(PlayerHand hand, DealerHand dealerHand) {
        float bet = hand.getBet();
        if (hand.getState() == HandState.INSURED) {
            hand.setInsuranceBet(hand.getInsuranceBet() + (hand.getInsuranceBet() * 2));
        } else if (hand.isBlackJack() && !dealerHand.isBlackJack()) {
            hand.setBet((int) (bet * 2.5f));
            hand.setState(HandState.WON);
        } else if ((hand.getHandValue() > dealerHand.getHandValue() && hand.getHandValue() <= 21)
                || (dealerHand.getHandValue() > 21 && hand.getHandValue() <= 21)) {
            hand.setBet((int) (bet * 2));
            hand.setState(HandState.WON);
        } else if (hand.getHandValue() == dealerHand.getHandValue()) {
            hand.setBet((int) bet);
            hand.setState(HandState.PUSH);
        } else {
            hand.setState(HandState.LOST);
            hand.setBet(0);
        }
    }

    public List<String> getAvailableHandOptions(PlayerHand hand){
        List<String> handOptions = new ArrayList<>();
        if (hand.getHandValue() < 21){
            handOptions.add("HIT");
            handOptions.add("STAND");
        }
        if (hand.getHand().size() <= 2){
            handOptions.add("DOUBLE");
        }
        if (hand.isSplittable()){
            handOptions.add("SPLIT");
        }
        return handOptions;
    }

}
