package org.daniel.elysium.ultimateTH;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the game logic for Ultimate Texas Hold'em.
 * <p>
 * This class handles evaluating hands, determining game results,
 * processing payouts, and retrieving available player options at different game stages.
 * </p>
 */
public class UthGameEngine {

    /**
     * Retrieves the available betting options for the player based on the game stage.
     *
     * @param stage the current stage of the game
     * @return a list of available actions as strings
     */
    public List<String> getPlayerOptions(UthGameStage stage) {
        List<String> handOptions = new ArrayList<>();
        switch (stage) {
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

    /**
     * Evaluates the player's or dealer's hand by determining the best poker combination
     * using the given community cards.
     *
     * @param communityCards the shared community cards
     * @param hand the player's or dealer's hand to evaluate
     */
    public void evaluateHand(List<UthCard> communityCards, UthHand hand) {
        if (hand.getState() != UthHandState.FOLD) {
            PokerEvaluatedHandModel model = PokerHandEvaluator.evaluateHand(communityCards, hand);
            hand.setEvaluatedHand(model);
        }
    }

    /**
     * Evaluates the player's Trips side bet based on their final hand combination.
     *
     * @param hand the player's hand containing the Trips bet
     */
    public void evaluateTrips(UthPlayerHand hand) {
        if (hand.getState() != UthHandState.FOLD && hand.getTrips() != 0) {
            switch (hand.getEvaluatedHand().handCombination()) {
                case TRIPS -> {
                    hand.setTrips((int) (hand.getTrips() + (hand.getTrips() * UthTripsState.TRIPS.getValue())));
                    hand.setTripsState(UthTripsState.TRIPS);
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

    /**
     * Determines the game result by comparing the player's hand with the dealer's hand.
     *
     * @param playerHand the player's hand
     * @param dealerHand the dealer's hand
     */
    public void determineGameResults(UthPlayerHand playerHand, UthHand dealerHand) {
        if (playerHand.getState() != UthHandState.FOLD) {
            Boolean result = determineWinner(playerHand, dealerHand);
            if (Boolean.TRUE.equals(result)) {
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

    /**
     * Processes the payout results for the player based on the game outcome.
     * Updates the player's bets based on whether they won, lost, or tied.
     *
     * @param playerHand the player's hand
     * @param dealerHand the dealer's hand
     */
    public void processResults(UthPlayerHand playerHand, UthHand dealerHand) {
        if (playerHand.getState() != UthHandState.FOLD) {
            if (playerHand.getState() == UthHandState.WON) {
                // Handle Ante bet payout if the dealer qualifies
                if (dealerHand.getEvaluatedHand().handCombination().getValue() > -2) {
                    playerHand.setAnte(playerHand.getAnte() * 2);
                }
                // Handle Blind bet payout if the player qualifies
                if (playerHand.getEvaluatedHand().handCombination().getValue() > 0) {
                    playerHand.setBlind((int) (playerHand.getBlind() + (playerHand.getBlind() * playerHand.getEvaluatedHand().handCombination().getValue())));
                }
                // Double the Play bet payout
                playerHand.setPlay(playerHand.getPlay() * 2);
            } else if (playerHand.getState() == UthHandState.LOST) {
                // Handle lost bets
                if (dealerHand.getEvaluatedHand().handCombination().getValue() > -2) {
                    playerHand.setAnte(0);
                }
                playerHand.setBlind(0);
                playerHand.setPlay(0);
            }
        }
    }
}
