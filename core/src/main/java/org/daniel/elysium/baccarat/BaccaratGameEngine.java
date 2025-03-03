package org.daniel.elysium.baccarat;

public class BaccaratGameEngine {

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
            hand.setState(BacHandState.TIE);
            hand.setBet(hand.getBet() + (hand.getBet() * hand.getHandType().getValue()));
        }
    }
}
