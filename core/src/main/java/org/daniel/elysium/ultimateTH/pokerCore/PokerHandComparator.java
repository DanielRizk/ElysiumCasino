package org.daniel.elysium.ultimateTH.pokerCore;

import org.daniel.elysium.ultimateTH.model.UthCard;
import org.daniel.elysium.ultimateTH.model.UthHand;
import org.daniel.elysium.ultimateTH.pokerCore.models.PokerEvaluatedHandModel;

import java.util.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Compares poker hands in Ultimate Texas Hold'em.
 * <p>
 * This class is responsible for determining the stronger hand between two evaluated hands.
 * It compares hand rankings, kickers, and high cards when necessary.
 * </p>
 */
public class PokerHandComparator {

    /* ======================
       Hand Comparison
       ====================== */

    /**
     * Compares two evaluated poker hands to determine the stronger hand.
     *
     * @param hand1 the first evaluated hand
     * @param hand2 the second evaluated hand
     * @return a positive integer if {@code hand1} is stronger,
     *         a negative integer if {@code hand2} is stronger,
     *         or 0 if the hands are equal
     */
    protected static int compareHands(PokerEvaluatedHandModel hand1, PokerEvaluatedHandModel hand2) {
        // Compare hand combinations first
        int combinationComparison = hand1.handCombination().compareTo(hand2.handCombination());
        if (combinationComparison != 0) {
            return combinationComparison;
        }

        // If hand combinations are the same, compare based on specific hand rules
        return switch (hand1.handCombination()) {
            case TWO_PAIR -> compareTwoPairHands(hand1.cardCombination(), hand2.cardCombination());
            case PAIR -> comparePairHands(hand1.cardCombination(), hand2.cardCombination());
            case HIGH_CARD, TRIPS -> compareKickers(hand1.cardCombination(), hand2.cardCombination());
            default -> compareCardValues(hand1.cardCombination(), hand2.cardCombination());
        };
    }

    /* ======================
       Card Value & Kicker Comparison
       ====================== */

    /**
     * Compares two hands based on their card values.
     *
     * @param hand1 the first hand
     * @param hand2 the second hand
     * @return the comparison result
     */
    private static int compareCardValues(List<UthCard> hand1, List<UthCard> hand2) {
        return compareHands(hand1, hand2);
    }

    /**
     * Compares two hands based on their kickers.
     *
     * @param hand1 the first hand
     * @param hand2 the second hand
     * @return the comparison result
     */
    private static int compareKickers(List<UthCard> hand1, List<UthCard> hand2) {
        return compareHands(hand1, hand2);
    }

    /**
     * Compares two hands by evaluating the highest-ranking cards.
     *
     * @param hand1 the first hand
     * @param hand2 the second hand
     * @return a positive integer if {@code hand1} is stronger,
     *         a negative integer if {@code hand2} is stronger,
     *         or 0 if the hands are equal
     */
    private static int compareHands(List<UthCard> hand1, List<UthCard> hand2) {
        List<UthCard> sortedHand1 = hand1.stream()
                .sorted(Comparator.comparingInt((UthCard card) -> card.getValue()).reversed())
                .toList();
        List<UthCard> sortedHand2 = hand2.stream()
                .sorted(Comparator.comparingInt((UthCard card) -> card.getValue()).reversed())
                .toList();

        for (int i = 0; i < sortedHand1.size(); i++) {
            int comparison = sortedHand1.get(i).getValue() - sortedHand2.get(i).getValue();
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0;
    }

    /* ======================
       Winner Determination
       ====================== */

    /**
     * Determines the winner between a player and dealer hand.
     *
     * @param playerHand the player's evaluated hand
     * @param dealerHand the dealer's evaluated hand
     * @return {@code true} if the player wins, {@code false} if the dealer wins, or {@code null} if it's a tie
     */
    public static Boolean determineWinner(UthHand playerHand, UthHand dealerHand) {
        PokerEvaluatedHandModel playerEvaluatedHand = playerHand.getEvaluatedHand();
        PokerEvaluatedHandModel dealerEvaluatedHand = dealerHand.getEvaluatedHand();

        int comparison = compareHands(playerEvaluatedHand, dealerEvaluatedHand);

        if (comparison > 0) {
            return true; // Player wins
        } else if (comparison < 0) {
            return false; // Dealer wins
        } else {
            return null; // Tie
        }
    }

    /* ======================
       Special Hand Comparisons
       ====================== */

    /**
     * Compares two hands containing TWO_PAIR.
     *
     * @param hand1 the first hand
     * @param hand2 the second hand
     * @return the comparison result
     */
    private static int compareTwoPairHands(List<UthCard> hand1, List<UthCard> hand2) {
        List<Integer> hand1PairRanks = getPairRanks(hand1);
        List<Integer> hand2PairRanks = getPairRanks(hand2);

        // Compare the highest pair
        int highestPairComparison = hand1PairRanks.get(0).compareTo(hand2PairRanks.get(0));
        if (highestPairComparison != 0) {
            return highestPairComparison;
        }

        // Compare the second pair
        int secondPairComparison = hand1PairRanks.get(1).compareTo(hand2PairRanks.get(1));
        if (secondPairComparison != 0) {
            return secondPairComparison;
        }

        // Compare the kicker
        int hand1Kicker = getKicker(hand1, hand1PairRanks);
        int hand2Kicker = getKicker(hand2, hand2PairRanks);
        return Integer.compare(hand1Kicker, hand2Kicker);
    }

    /**
     * Compares two hands containing a PAIR.
     *
     * @param hand1 the first hand
     * @param hand2 the second hand
     * @return the comparison result
     */
    private static int comparePairHands(List<UthCard> hand1, List<UthCard> hand2) {
        List<Integer> hand1PairRanks = getPairRanks(hand1);
        List<Integer> hand2PairRanks = getPairRanks(hand2);

        int highestPairComparison = hand1PairRanks.get(0).compareTo(hand2PairRanks.get(0));
        if (highestPairComparison != 0) {
            return highestPairComparison;
        }

        return compareHands(hand1, hand2);
    }

    /* ======================
       Helper Methods
       ====================== */

    /**
     * Extracts the ranks of the pairs in a hand.
     *
     * @param hand the hand to analyze
     * @return a list of ranks that form pairs, sorted in descending order
     */
    private static List<Integer> getPairRanks(List<UthCard> hand) {
        Map<Integer, Integer> valueCounts = new HashMap<>();
        for (UthCard card : hand) {
            valueCounts.put(card.getValue(), valueCounts.getOrDefault(card.getValue(), 0) + 1);
        }

        List<Integer> pairRanks = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : valueCounts.entrySet()) {
            if (entry.getValue() == 2) {
                pairRanks.add(entry.getKey());
            }
        }

        pairRanks.sort(Comparator.reverseOrder());
        return pairRanks;
    }

    /**
     * Determines the kicker in a TWO_PAIR hand.
     *
     * @param hand the hand to evaluate
     * @param pairRanks the ranks of the pairs in the hand
     * @return the kicker card value
     */
    private static int getKicker(List<UthCard> hand, List<Integer> pairRanks) {
        int kicker = 0;
        for (UthCard card : hand) {
            if (!pairRanks.contains(card.getValue())) {
                if (card.getValue() > kicker) {
                    kicker = card.getValue();
                }
            }
        }
        return kicker;
    }
}

