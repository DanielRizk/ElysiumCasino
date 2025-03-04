package org.daniel.elysium.baccarat.models;

import org.daniel.elysium.baccarat.constants.BacHandAction;
import org.daniel.elysium.baccarat.constants.BacHandState;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single hand in a game of Baccarat, tracking the cards, the hand's state, and any actions taken.
 */
public class BacHand {

    private final List<BacCard> hand;
    private BacHandState state = BacHandState.UNDEFINED;
    private BacHandAction action = BacHandAction.UNDEFINED;

    /**
     * Constructs a new, empty Baccarat hand.
     */
    public BacHand() {
        this.hand = new ArrayList<>();
    }

    /**
     * Gets the cards currently in the hand.
     * @return A list of {@link BacCard} objects representing the cards in the hand.
     */
    public List<BacCard> getHand(){
        return hand;
    }

    /**
     * Gets the current state of the hand.
     * @return The current {@link BacHandState}, such as WON, LOST, TIE, or UNDEFINED.
     */
    public BacHandState getState() {
        return state;
    }

    /**
     * Sets the state of the hand.
     * @param state The new state of the hand, from the {@link BacHandState} enum.
     */
    public void setState(BacHandState state) {
        this.state = state;
    }

    /**
     * Gets the current action for this hand.
     * @return The current {@link BacHandAction}, such as DRAW or STAND.
     */
    public BacHandAction getAction() {
        return action;
    }

    /**
     * Sets the action to be taken for this hand.
     * @param action The action to set, from the {@link BacHandAction} enum.
     */
    public void setAction(BacHandAction action) {
        this.action = action;
    }

    /**
     * Adds a card to this hand.
     * @param card The {@link BacCard} to add to the hand.
     */
    public void dealCard(BacCard card) {
        hand.add(card);
    }

    /**
     * Retrieves the third card in the hand, if it exists.
     * This is typically used in Baccarat game rules to determine the banker's response.
     * @return The third {@link BacCard} in the hand, or null if less than three cards are present.
     */
    public BacCard getThirdCard(){
        if (hand.size() > 2){
            return hand.get(2);
        }
        return null;
    }

    /**
     * Calculates the total value of the hand according to Baccarat rules, where the sum of card values is taken modulo 10.
     * @return The value of the hand, reduced modulo 10.
     */
    public int getHandValue(){
        int value = 0;
        for (BacCard card : hand) {
            value += card.getValue();
        }
        return value % 10;
    }
}
