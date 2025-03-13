package org.daniel.elysium.baccarat;

import org.daniel.elysium.baccarat.constants.BacHandAction;
import org.daniel.elysium.baccarat.constants.BacHandState;
import org.daniel.elysium.baccarat.constants.BacHandType;
import org.daniel.elysium.baccarat.models.BacBetHand;
import org.daniel.elysium.baccarat.models.BacHand;

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
    public static void evaluatePlayer(BacHand banker, BacHand player){
        if (banker.getHandValue() != 8 && banker.getHandValue() != 9) {
            if (player.getHandValue() <= 5){
                player.setAction(BacHandAction.DRAW);
            } else {
                player.setAction(BacHandAction.STAND);
            }
        } else {
            player.setAction(BacHandAction.STAND);
        }
    }

    /**
     * Evaluates the banker's actions based on the banker's and player's hands,
     * particularly considering the value of the player's third card if drawn.
     *
     * @param banker The current hand of the banker.
     * @param player The current hand of the player.
     */
    public static void evaluateBanker(BacHand banker, BacHand player) {
        // 1) If either side has a natural 8 or 9, no more draws:
        boolean playerNatural = (player.getHand().size() == 2
                && (player.getHandValue() == 8 || player.getHandValue() == 9));
        boolean bankerNatural = (banker.getHand().size() == 2
                && (banker.getHandValue() == 8 || banker.getHandValue() == 9));

        if (playerNatural || bankerNatural) {
            banker.setAction(BacHandAction.STAND);
            return;
        }

        // 2) If the player has only 2 cards (player stood pat),
        //    use the simpler rule: banker draws if ≤5, otherwise stands.
        if (player.getHand().size() == 2) {
            if (banker.getHandValue() <= 5) {
                banker.setAction(BacHandAction.DRAW);
            } else {
                // 6 or 7 => stand
                banker.setAction(BacHandAction.STAND);
            }
            return;
        }

        // 3) Otherwise, the player must have taken a third card, so the banker
        //    follows the more complex draw rules:
        int bankerTotal = banker.getHandValue();
        int playerThird = player.getThirdCard().getValue(); // Value of player's 3rd card

        if (bankerTotal <= 2) {
            // Always draw if banker total is 0,1,2
            banker.setAction(BacHandAction.DRAW);
        } else if (bankerTotal == 3 && playerThird != 8) {
            // Draw unless player's third card = 8
            banker.setAction(BacHandAction.DRAW);
        } else if (bankerTotal == 4 && (playerThird >= 2 && playerThird <= 7)) {
            // Draw if player's third card is 2..7
            banker.setAction(BacHandAction.DRAW);
        } else if (bankerTotal == 5 && (playerThird >= 4 && playerThird <= 7)) {
            // Draw if player's third card is 4..7
            banker.setAction(BacHandAction.DRAW);
        } else if (bankerTotal == 6 && (playerThird == 6 || playerThird == 7)) {
            // Draw if player's third card is 6..7
            banker.setAction(BacHandAction.DRAW);
        } else {
            // Stand for 7, or any case not matched above
            banker.setAction(BacHandAction.STAND);
        }
    }

    /**
     * Evaluates both hands to determine the winner of the round.
     * Sets the state of each hand to WON, LOST, or TIE based on their values.
     *
     * @param banker The banker's hand.
     * @param player The player's hand.
     */
    public static void evaluateHands(BacHand banker, BacHand player){
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
    public static void calculateResult(BacHand banker, BacHand player, BacBetHand hand){
        if (player.getState() == BacHandState.WON){
            if (hand.getHandType() == BacHandType.PLAYER){
                hand.setState(BacHandState.WON);
                hand.setBet(hand.getBet() + (hand.getBet() * hand.getHandType().getValue()));
            } else {
                hand.setState(BacHandState.LOST);
                hand.setBet(0);
            }
        } else if (banker.getState() == BacHandState.WON) {
            if (hand.getHandType() == BacHandType.BANKER){
                hand.setState(BacHandState.WON);
                hand.setBet(hand.getBet() + (hand.getBet() * hand.getHandType().getValue()));
            } else {
                hand.setState(BacHandState.LOST);
                hand.setBet(0);
            }
        } else {
            if (hand.getHandType() == BacHandType.TIE){
                hand.setState(BacHandState.WON);
                hand.setBet(hand.getBet() + (hand.getBet() * hand.getHandType().getValue()));
            } else {
                hand.setState(BacHandState.LOST);
                hand.setBet(0);
            }
        }
    }
}
