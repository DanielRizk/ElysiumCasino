package org.daniel.elysium.blackjack;

import org.daniel.elysium.models.BJCard;

public class DealerHand extends BJHand{

    @Override
    public boolean dealCard(BJCard card) {
        if (getHandValue() < 17){
            getHand().add(card);
            return true;
        }
        return false;
    }

    @Override
    public boolean isBlackJack() {
        return getHand().size() <= 2 && getHandValue() == 21;
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

        if (handValue == 17 && aces > 0){
            handValue -= 10;
        }

        return handValue;
    }
}
