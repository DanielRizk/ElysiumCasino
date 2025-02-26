package org.daniel.elysium.blackjack;

import org.daniel.elysium.models.BJCard;
import org.daniel.elysium.models.Card;

import java.util.ArrayList;
import java.util.List;

public class BlackjackEngine {
    private PlayerHand playerHand;
    private DealerHand dealerHand;

    public BlackjackEngine() {
        playerHand = new PlayerHand();
        dealerHand = new DealerHand();
    }

    public PlayerHand getPlayerHand() {
        return playerHand;
    }

    public DealerHand getDealerHand() {
        return dealerHand;
    }

    public boolean isAnyBlackjack(){
        boolean anyBlackjack = false;
        if (playerHand.isBlackJack()){
            playerHand.setState(HandState.BLACKJACK);
            anyBlackjack = true;
        }

        if (dealerHand.isBlackJack()){
            dealerHand.setState(HandState.BLACKJACK);
            anyBlackjack = true;
        }
        return anyBlackjack;
    }

    public boolean isInsurance(){
        return dealerHand.getHand().get(0).getValue() == 11;
    }

    public List<String> getAvailableHandOptions(){
        List<String> handOptions = new ArrayList<>();
        if (playerHand.getHandValue() < 21){
            handOptions.add("HIT");
            handOptions.add("STAND");
        }
        if (playerHand.getHand().size() <= 2){
            handOptions.add("DOUBLE");
        }
        if (playerHand.isSplittable()){
            handOptions.add("SPLIT");
        }
        return handOptions;
    }

}
