package org.daniel.elysium.blackjack.models;

import org.daniel.elysium.blackjack.constants.HandState;

/**
 * Represents a player's hand in a game of Blackjack.
 * This class handles betting, hand splitting, and Blackjack hand evaluation.
 */
public class PlayerHand extends BJHand {

    private boolean isHandSplit = false;
    private boolean isSplitAces = false;
    private int bet = 0;
    private int insuranceBet = 0;

    /**
     * Determines if the player can receive another card.
     * A card can be dealt as long as the hand value is below 21.
     *
     * @param card The {@link BJCard} to be considered for dealing.
     * @return {@code true} if the hand value is below 21, otherwise {@code false}.
     */
    @Override
    public boolean canDealCard(BJCard card) {
        return getHandValue() < 21;
    }

    /**
     * Attempts to deal a card to the player's hand.
     * The player can receive a card as long as their hand value is below 21 and is not a split aces.
     *
     * @param card The {@link BJCard} to be added.
     * @return {@code true} if the card was successfully added, otherwise {@code false}.
     */
    @Override
    public boolean dealCard(BJCard card) {
        if (!(getHand().size() > 1 && isSplitAces)) {
            if (getHandValue() < 21) {
                getHand().add(card);
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the player's hand is a Blackjack.
     * A Blackjack occurs when the hand consists of exactly two cards with a total value of 21
     * and the hand has not been split.
     *
     * @return {@code true} if the player has a Blackjack, otherwise {@code false}.
     */
    @Override
    public boolean isBlackJack() {
        boolean eval = getHand().size() <= 2 && getHandValue() == 21 && !isHandSplit;
        if (eval) {
            setState(HandState.BLACKJACK);
            return true;
        }
        return false;
    }

    /**
     * Calculates the total value of the player's hand according to Blackjack rules.
     * Aces are counted as 11 unless they cause the hand value to exceed 21, in which case they count as 1.
     *
     * @return The numerical value of the player's hand.
     */
    @Override
    public int getHandValue() {
        int handValue = 0;
        int aces = 0;

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

        return handValue;
    }

    /**
     * Checks if the hand is eligible for splitting.
     * A hand is splittable if it contains exactly two cards of the same value and has not already been split.
     *
     * @return {@code true} if the hand can be split, otherwise {@code false}.
     */
    public boolean isSplittable() {
        return getHand().size() == 2 && getHand().get(0).getValue() == getHand().get(1).getValue();
    }

    /**
     * Sets whether the hand has been split.
     *
     * @param handSplit {@code true} if the hand has been split, otherwise {@code false}.
     */
    public void setHandSplit(boolean handSplit) {
        this.isHandSplit = handSplit;
    }

    /**
     * Checks if the hand is a split aces.
     * A hand is split aces if it contains exactly two cards and both are aces and did not split before.
     *
     * @return {@code true} if the hand can be a split aces, otherwise {@code false}.
     */
    public boolean isSplitAces() {
        return getHand().size() == 2 && getHand().get(0).getValue() == 11 && getHand().get(1).getValue() == 11 && !isHandSplit;
    }

    /**
     * Returns if the hand did come from a split aces.
     *
     * @return {@code true} if the hand came from split aces, otherwise {@code false}.
     */
    public boolean didComeFromSplitAces() {
        return isSplitAces;
    }

    /**
     * Sets whether the hand has been a split aces.
     *
     * @param splitAces {@code true} if the hand has been a split aces, otherwise {@code false}.
     */
    public void setSplitAces(boolean splitAces) {
        this.isSplitAces = splitAces;
    }

    /**
     * Gets the current bet amount for this hand.
     *
     * @return The bet amount.
     */
    public int getBet() {
        return bet;
    }

    /**
     * Sets the bet amount for this hand.
     *
     * @param bet The bet amount to set.
     */
    public void setBet(int bet) {
        this.bet = bet;
    }

    /**
     * Gets the current insurance bet amount.
     *
     * @return The insurance bet amount.
     */
    public int getInsuranceBet() {
        return insuranceBet;
    }

    /**
     * Sets the insurance bet amount.
     *
     * @param insuranceBet The insurance bet amount to set.
     */
    public void setInsuranceBet(int insuranceBet) {
        this.insuranceBet = insuranceBet;
    }
}
