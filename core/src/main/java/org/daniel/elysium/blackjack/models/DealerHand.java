package org.daniel.elysium.blackjack.models;

import org.daniel.elysium.blackjack.constants.HandState;

/**
 * Represents the dealer's hand in a game of Blackjack.
 * The dealer follows predefined rules for drawing cards.
 */
public class DealerHand extends BJHand {

    /**
     * Determines if the dealer is allowed to receive another card.
     * The dealer must hit until their hand value is at least 17.
     *
     * @param card The {@link BJCard} to be considered for dealing.
     * @return {@code true} if the dealer's hand value is less than 17, otherwise {@code false}.
     */
    @Override
    public boolean canDealCard(BJCard card) {
        return getHandValue() < 17;
    }

    /**
     * Attempts to deal a card to the dealer's hand.
     * The dealer will take a card only if their hand value is below 17.
     *
     * @param card The {@link BJCard} to be added to the dealer's hand.
     * @return {@code true} if the card was successfully added, otherwise {@code false}.
     */
    @Override
    public boolean dealCard(BJCard card) {
        if (getHandValue() < 17) {
            getHand().add(card);
            return true;
        }
        return false;
    }

    /**
     * Determines if the dealer has a Blackjack.
     * A Blackjack occurs when the hand consists of exactly two cards with a total value of 21.
     *
     * @return {@code true} if the dealer has a Blackjack, otherwise {@code false}.
     */
    @Override
    public boolean isBlackJack() {
        boolean eval = getHand().size() <= 2 && getHandValue() == 21;
        if (eval) {
            setState(HandState.BLACKJACK);
            return true;
        }
        return false;
    }

    /**
     * Calculates the total value of the dealer's hand according to Blackjack rules.
     * Aces are counted as 11 unless they cause the hand value to exceed 21, in which case they count as 1.
     * If the dealer has a soft 17 (Ace counted as 11 with a total value of 17), the value is adjusted to 7.
     *
     * @return The numerical value of the dealer's hand.
     */
    @Override
    public int getHandValue() {
        int handValue = 0;
        int aces = 0;

        // Calculate the hand value while counting Aces separately
        for (BJCard card : getHand()) {
            int cardValue = card.getValue();
            if (cardValue == 11) {
                handValue += 11;
                aces++;
            } else {
                handValue += cardValue;
            }
        }

        // Adjust for Aces if the total value exceeds 21
        while (handValue > 21 && aces > 0) {
            handValue -= 10; // Convert an Ace from 11 to 1
            aces--;
        }

        // Adjust for Soft 17 (if the dealer has a 17 with an Ace counted as 11)
        if (handValue == 17 && aces > 0) {
            handValue -= 10; // Convert the soft 17 to a hard 7
        }

        return handValue;
    }
}

