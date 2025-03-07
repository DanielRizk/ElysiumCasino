package org.daniel.elysium.blackjack;

import org.daniel.elysium.blackjack.constants.BJHandState;
import org.daniel.elysium.blackjack.models.BJDealerHand;
import org.daniel.elysium.blackjack.models.BJPlayerHand;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the core game logic for Blackjack, including evaluating hand states,
 * determining available actions, and resolving player results.
 */
public class BlackjackEngine {

    /**
     * Determines if the player can take insurance based on the dealer's upcard.
     * Insurance is available if the dealer's first card is an Ace.
     *
     * @param hand The dealer's {@link BJDealerHand}.
     * @return {@code true} if insurance is available, otherwise {@code false}.
     */
    public static boolean isInsurance(BJDealerHand hand) {
        return hand.getHand().get(0).getValue() == 11;
    }

    /**
     * Determines the available actions a player can take based on their hand.
     *
     * @param hand The player's {@link BJPlayerHand}.
     * @return A list of available actions as strings (e.g., "HIT", "STAND", "DOUBLE", "SPLIT").
     */
    public static List<String> getAvailableHandOptions(BJPlayerHand hand) {
        List<String> handOptions = new ArrayList<>();
        if (!hand.didComeFromSplitAces()){
            if (hand.getHandValue() < 21) {
                handOptions.add("HIT");
                handOptions.add("STAND");
            }
            if (hand.getHand().size() <= 2 && hand.getHandValue() != 21) {
                handOptions.add("DOUBLE");
            }
            if (hand.isSplittable()) {
                handOptions.add("SPLIT");
            }
        }
        return handOptions;
    }

    /**
     * Resolves the outcome of the player's hand by comparing it with the dealer's hand.
     * Adjusts the player's bet based on the result.
     *
     * @param hand        The player's {@link BJPlayerHand}.
     * @param dealerHand  The dealer's {@link BJDealerHand}.
     */
    public static void resolvePlayerResult(BJPlayerHand hand, BJDealerHand dealerHand) {
        float bet = hand.getBet();

        // Handle insurance payout
        if (hand.getState() == BJHandState.INSURED) {
            hand.setInsuranceBet(hand.getInsuranceBet() * 3);

            // Blackjack payout (unless the dealer also has Blackjack)
        } else if (hand.isBlackJack() && !dealerHand.isBlackJack()) {
            hand.setBet((int) (bet * 2.5f));
            hand.setState(BJHandState.BLACKJACK);

            // Win conditions: Player has a higher valid hand OR dealer busts
        } else if ((hand.getHandValue() > dealerHand.getHandValue() && hand.getHandValue() <= 21)
                || (dealerHand.getHandValue() > 21 && hand.getHandValue() <= 21)) {
            hand.setBet((int) (bet * 2));
            hand.setState(BJHandState.WON);

            // Push (tie) condition
        } else if (hand.getHandValue() <= 21 && hand.getHandValue() == dealerHand.getHandValue()) {
            hand.setBet((int) bet);
            hand.setState(BJHandState.PUSH);

            // Loss condition
        } else {
            hand.setState(BJHandState.LOST);
            hand.setBet(0);
        }
    }
}
