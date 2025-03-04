package org.daniel.elysium.baccarat.models;

import org.daniel.elysium.baccarat.constants.BacHandState;
import org.daniel.elysium.baccarat.constants.HandType;

/**
 * Represents a bet in a Baccarat game, holding details about the bet amount, the type of hand bet upon,
 * and the outcome state of the bet.
 */
public class BetHand {

    private double bet = 0;
    private HandType handType = HandType.UNDEFINED;
    private BacHandState state = BacHandState.UNDEFINED;

    /**
     * Retrieves the current bet amount.
     * @return The amount of money bet.
     */
    public double getBet() {
        return bet;
    }

    /**
     * Sets the bet amount.
     * @param bet The amount of money to set as the bet.
     */
    public void setBet(double bet) {
        this.bet = bet;
    }

    /**
     * Retrieves the type of hand on which the bet was placed.
     * @return The type of hand ({@link HandType}), such as PLAYER, BANKER, or TIE.
     */
    public HandType getHandType() {
        return handType;
    }

    /**
     * Sets the type of hand on which the bet is placed.
     * @param handType The hand type ({@link HandType}) to set, indicating on which hand the bet is made.
     */
    public void setHandType(HandType handType) {
        this.handType = handType;
    }

    /**
     * Retrieves the current state of the bet.
     * @return The state ({@link BacHandState}) of the bet, such as WON, LOST, or TIE.
     */
    public BacHandState getState() {
        return state;
    }

    /**
     * Sets the state of the bet.
     * @param state The state ({@link BacHandState}) to set, indicating the outcome of the bet.
     */
    public void setState(BacHandState state) {
        this.state = state;
    }
}
