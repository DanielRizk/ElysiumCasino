package org.daniel.elysium.baccarat;

import org.daniel.elysium.baccarat.constants.BacHandAction;
import org.daniel.elysium.baccarat.constants.BacHandState;
import org.daniel.elysium.baccarat.constants.HandType;
import org.daniel.elysium.baccarat.models.BacHand;
import org.daniel.elysium.baccarat.models.BetHand;

/**
 * Manages the rules and logic for a Baccarat game, evaluating player and banker hands and determining the outcome of bets.
 */
public class BaccaratGameEngine {

    /**
     * Evaluates the player's actions based on the current values of the player's and banker's hands.
     * Decides whether the player should draw a card or stand.
     *
     * @param banker The current hand of the banker.
     * @param player The current hand of the player.
     */
    public void evaluatePlayer(BacHand banker, BacHand player){
        if (banker.getHandValue() != 8 && banker.getHandValue() != 9)
        {
            if (player.getHandValue() <= 5){
                player.setAction(BacHandAction.DRAW);
            } else {
                player.setAction(BacHandAction.STAND);
            }
        }
    }

    /**
     * Evaluates the banker's actions based on the banker's and player's hands,
     * particularly considering the value of the player's third card if drawn.
     *
     * @param banker The current hand of the banker.
     * @param player The current hand of the player.
     */
    public void evaluateBanker(BacHand banker, BacHand player){
        if (player.getHand().size() > 2 && player.getHandValue() != 8 && player.getHandValue() != 9)
        {
            if (banker.getHandValue() <= 2){
                banker.setAction(BacHandAction.DRAW);
            } else if (banker.getHandValue() == 3
                    && player.getThirdCard().getValue() != 8){
                banker.setAction(BacHandAction.DRAW);
            } else if (banker.getHandValue() == 4
                    && player.getThirdCard().getValue() >= 2
                    && player.getThirdCard().getValue() <= 7){
                banker.setAction(BacHandAction.DRAW);
            } else if (banker.getHandValue() == 5
                    && player.getThirdCard().getValue() >= 4
                    && player.getThirdCard().getValue() <= 7){
                banker.setAction(BacHandAction.DRAW);
            } else if (banker.getHandValue() == 6
                    && player.getThirdCard().getValue() >= 6
                    && player.getThirdCard().getValue() <= 7){
                banker.setAction(BacHandAction.DRAW);
            } else {
                banker.setAction(BacHandAction.STAND);
            }
        }
    }

    /**
     * Evaluates both hands to determine the winner of the round.
     * Sets the state of each hand to WON, LOST, or TIE based on their values.
     *
     * @param banker The banker's hand.
     * @param player The player's hand.
     */
    public void evaluateHands(BacHand banker, BacHand player){
        if (player.getHandValue() > banker.getHandValue()){
            player.setState(BacHandState.WON);
            banker.setState(BacHandState.LOST);
        } else if (player.getHandValue() < banker.getHandValue()){
            player.setState(BacHandState.LOST);
            banker.setState(BacHandState.WON);
        } else {
            player.setState(BacHandState.TIE);
            banker.setState(BacHandState.TIE);
        }
    }

    /**
     * Calculates the result of the game based on the final states of the player and banker hands.
     * Adjusts the bet based on the outcome.
     *
     * @param banker The banker's hand.
     * @param player The player's hand.
     * @param hand The betting hand which will be adjusted according to the game's outcome.
     */
    public void calculateResult(BacHand banker, BacHand player, BetHand hand){
        if (player.getState() == BacHandState.WON){
            if (hand.getHandType() == HandType.PLAYER){
                hand.setState(BacHandState.WON);
                hand.setBet(hand.getBet() + (hand.getBet() * hand.getHandType().getValue()));
            } else {
                hand.setState(BacHandState.LOST);
                hand.setBet(0);
            }
        } else if (banker.getState() == BacHandState.WON) {
            if (hand.getHandType() == HandType.BANKER){
                hand.setState(BacHandState.WON);
                hand.setBet(hand.getBet() + (hand.getBet() * hand.getHandType().getValue()));
            } else {
                hand.setState(BacHandState.LOST);
                hand.setBet(0);
            }
        } else {
            if (hand.getHandType() == HandType.TIE){
                hand.setState(BacHandState.TIE);
                hand.setBet(hand.getBet() + (hand.getBet() * hand.getHandType().getValue()));
            } else {
                hand.setState(BacHandState.LOST);
                hand.setBet(0);
            }
        }
    }
}
