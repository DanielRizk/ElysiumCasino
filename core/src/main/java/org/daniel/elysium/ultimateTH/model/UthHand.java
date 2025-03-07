package org.daniel.elysium.ultimateTH.model;

import org.daniel.elysium.ultimateTH.constants.UthHandState;
import org.daniel.elysium.ultimateTH.pokerCore.models.PokerEvaluatedHandModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic hand in Ultimate Texas Hold'em.
 * <p>
 * This class manages the hand's state, dealt cards, and evaluated results.
 * It also tracks whether a kicker card is relevant for hand ranking.
 * </p>
 */
public class UthHand {
    private final List<UthCard> hand;
    private UthHandState state = UthHandState.UNDEFINED;
    private PokerEvaluatedHandModel evaluatedHand = null;
    private boolean kickerCard = false;

    /**
     * Constructs an empty hand for a player or dealer.
     */
    public UthHand() {
        this.hand = new ArrayList<>();
    }

    /**
     * Retrieves the list of cards in the hand.
     *
     * @return a list of {@code UthCard} objects representing the hand
     */
    public List<UthCard> getHand() {
        return hand;
    }

    /**
     * Adds a card to the hand.
     *
     * @param card the {@code UthCard} to be added
     */
    public void dealCard(UthCard card) {
        hand.add(card);
    }

    /**
     * Retrieves the current state of the hand.
     *
     * @return the {@code UthHandState} representing the hand's outcome
     */
    public UthHandState getState() {
        return state;
    }

    /**
     * Sets the state of the hand.
     *
     * @param state the new state of the hand
     */
    public void setState(UthHandState state) {
        this.state = state;
    }

    /**
     * Retrieves the evaluated poker hand.
     *
     * @return the evaluated hand model containing hand ranking and relevant details
     */
    public PokerEvaluatedHandModel getEvaluatedHand() {
        return evaluatedHand;
    }

    /**
     * Sets the evaluated poker hand.
     *
     * @param evaluatedHand the evaluated hand model containing hand ranking and relevant details
     */
    public void setEvaluatedHand(PokerEvaluatedHandModel evaluatedHand) {
        this.evaluatedHand = evaluatedHand;
    }

    /**
     * Checks whether a kicker card is relevant for determining the hand's strength.
     *
     * @return {@code true} if a kicker card is relevant, otherwise {@code false}
     */
    public boolean isKickerCard() {
        return kickerCard;
    }

    /**
     * Sets whether a kicker card is relevant for the hand's ranking.
     *
     * @param kickerCard {@code true} if a kicker card is considered, otherwise {@code false}
     */
    public void setKickerCard(boolean kickerCard) {
        this.kickerCard = kickerCard;
    }
}
