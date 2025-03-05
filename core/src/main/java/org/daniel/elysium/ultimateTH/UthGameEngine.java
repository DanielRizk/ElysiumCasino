package org.daniel.elysium.ultimateTH;

import org.daniel.elysium.blackjack.models.PlayerHand;
import org.daniel.elysium.ultimateTH.constants.UthGameStage;
import org.daniel.elysium.ultimateTH.constants.UthHandState;
import org.daniel.elysium.ultimateTH.constants.UthTripsState;
import org.daniel.elysium.ultimateTH.model.UthCard;
import org.daniel.elysium.ultimateTH.model.UthHand;
import org.daniel.elysium.ultimateTH.model.UthPlayerHand;
import org.daniel.elysium.ultimateTH.pokerCore.PokerHandEvaluator;
import org.daniel.elysium.ultimateTH.pokerCore.models.PokerEvaluatedHandModel;

import java.util.ArrayList;
import java.util.List;

import static org.daniel.elysium.ultimateTH.pokerCore.PokerHandComparator.determineWinner;

public class UthGameEngine {

    public List<String> getPlayerOptions(UthGameStage stage){
        List<String> handOptions = new ArrayList<>();
        switch (stage){
            case START -> {
                handOptions.add("X4");
                handOptions.add("X3");
                handOptions.add("CHECK");
                handOptions.add("FOLD");
            }
            case FLOP -> {
                handOptions.add("X2");
                handOptions.add("CHECK");
                handOptions.add("FOLD");
            }
            case RIVER -> {
                handOptions.add("X1");
                handOptions.add("FOLD");
            }
        }
        return handOptions;
    }

    public void evaluateHand(List<UthCard> communityCards, UthHand hand){
        PokerEvaluatedHandModel model = PokerHandEvaluator.evaluateHand(communityCards, hand);
        hand.setEvaluatedHand(model);
    }

    public void evaluateTrips(UthPlayerHand hand){
        if (hand.getState() != UthHandState.FOLD && hand.getTrips() != 0){
            switch (hand.getEvaluatedHand().handCombination()){
                case TRIPS -> {
                    hand.setTrips((int) (hand.getTrips() + (hand.getTrips() * UthTripsState.Trips.getValue())));
                    hand.setTripsState(UthTripsState.Trips);
                }
                case STRAIGHT -> {
                    hand.setTrips((int) (hand.getTrips() + (hand.getTrips() * UthTripsState.STRAIGHT.getValue())));
                    hand.setTripsState(UthTripsState.STRAIGHT);
                }
                case FLUSH -> {
                    hand.setTrips((int) (hand.getTrips() + (hand.getTrips() * UthTripsState.FLUSH.getValue())));
                    hand.setTripsState(UthTripsState.FLUSH);
                }
                case FULL_HOUSE -> {
                    hand.setTrips((int) (hand.getTrips() + (hand.getTrips() * UthTripsState.FULL_HOUSE.getValue())));
                    hand.setTripsState(UthTripsState.FULL_HOUSE);
                }
                case STRAIGHT_FLUSH -> {
                    hand.setTrips((int) (hand.getTrips() + (hand.getTrips() * UthTripsState.STRAIGHT_FLUSH.getValue())));
                    hand.setTripsState(UthTripsState.STRAIGHT_FLUSH);
                }
                case ROYAL_FLUSH -> {
                    hand.setTrips((int) (hand.getTrips() + (hand.getTrips() * UthTripsState.ROYAL_FLUSH.getValue())));
                    hand.setTripsState(UthTripsState.ROYAL_FLUSH);
                }
                default -> {
                    hand.setTrips(0);
                    hand.setTripsState(UthTripsState.LOST);
                }
            }
        }
    }

    public void determineGameResults(List<UthCard> communityCards, UthPlayerHand playerHand, UthHand dealerHand){
        if (playerHand.getState() != UthHandState.FOLD){
            Boolean result = determineWinner(communityCards, playerHand, dealerHand);
            if(Boolean.TRUE.equals(result)){
                playerHand.setState(UthHandState.WON);
                dealerHand.setState(UthHandState.LOST);
            } else if (Boolean.FALSE.equals(result)) {
                playerHand.setState(UthHandState.LOST);
                dealerHand.setState(UthHandState.WON);
            } else {
                playerHand.setState(UthHandState.TIE);
                dealerHand.setState(UthHandState.TIE);
            }
        }
    }

    public void processResults(UthPlayerHand playerHand, UthHand dealerHand){
        if (playerHand.getState() != UthHandState.FOLD){
            if (playerHand.getState() == UthHandState.WON){
                // Check and Handle Ante
                if (dealerHand.getEvaluatedHand().handCombination().getValue() > -2){
                    playerHand.setAnte(playerHand.getAnte() * 2);
                }
                // Check and Handle Blind
                if (playerHand.getEvaluatedHand().handCombination().getValue() > 0){
                    playerHand.setBlind((int) (playerHand.getBlind() + (playerHand.getBlind() * playerHand.getEvaluatedHand().handCombination().getValue())));
                }
                // Handle Play bet
                playerHand.setPlay(playerHand.getPlay() * 2);
            } else if (playerHand.getState() == UthHandState.LOST) {
                // Handle Ante
                if (dealerHand.getEvaluatedHand().handCombination().getValue() > -2){
                    playerHand.setAnte(0);
                }
                // Handle Blind
                playerHand.setBlind(0);
                // Handle Play bet
                playerHand.setPlay(0);
            }
        }
    }
}
