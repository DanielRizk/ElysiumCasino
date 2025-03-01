package org.daniel.elysium.blackjack.models;

import org.daniel.elysium.blackjack.constants.HandState;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic hand in Blackjack.
 * This abstract class provides common functionality for managing a hand of cards
 * and defining game-specific rules.
 */
public abstract class BJHand {
    protected List<BJCard> hand;
    private HandState state = HandState.UNDEFINED;

    /**
     * Constructs an empty Blackjack hand.
     */
    public BJHand() {
        this.hand = new ArrayList<>();
    }

    /**
     * Retrieves the list of cards in the hand.
     *
     * @return A list of {@link BJCard} objects in the hand.
     */
    public List<BJCard> getHand() {
        return hand;
    }

    /**
     * Gets the current state of the hand.
     *
     * @return The {@link HandState} of the hand.
     */
    public HandState getState() {
        return state;
    }

    /**
     * Sets the state of the hand.
     *
     * @param state The new {@link HandState} to be assigned.
     */
    public void setState(HandState state) {
        this.state = state;
    }

    /**
     * Checks whether a given card can be dealt to this hand.
     *
     * @param card The {@link BJCard} to check.
     * @return {@code true} if the card can be dealt, otherwise {@code false}.
     */
    public abstract boolean canDealCard(BJCard card);

    /**
     * Attempts to add a card to this hand.
     *
     * @param card The {@link BJCard} to be added.
     * @return {@code true} if the card was successfully added, otherwise {@code false}.
     */
    public abstract boolean dealCard(BJCard card);

    /**
     * Determines if this hand is a Blackjack (i.e., an Ace and a 10-value card).
     *
     * @return {@code true} if the hand is a Blackjack, otherwise {@code false}.
     */
    public abstract boolean isBlackJack();

    /**
     * Calculates and returns the total value of the hand according to Blackjack rules.
     *
     * @return The numerical value of the hand.
     */
    public abstract int getHandValue();
}

