package org.daniel.elysium.ultimateTH.pokerCore;

import org.daniel.elysium.ultimateTH.model.UthCard;
import org.daniel.elysium.ultimateTH.model.UthHand;
import org.daniel.elysium.ultimateTH.pokerCore.models.PokerEvaluatedHandModel;

import java.util.*;

import static org.daniel.elysium.ultimateTH.pokerCore.PokerHandEvaluator.evaluateHand;

public class PokerHandComparator {

    // Compare two PokerEvaluatedHandModel objects
    protected static int compareHands(PokerEvaluatedHandModel hand1, PokerEvaluatedHandModel hand2) {
        // Compare hand combinations first
        int combinationComparison = hand1.handCombination().compareTo(hand2.handCombination());
        if (combinationComparison != 0) {
            return combinationComparison;
        }

        // If hand combinations are the same, compare the hands based on their specific rules
        switch (hand1.handCombination()) {
            case TWO_PAIR:
                return compareTwoPairHands(hand1.cardCombination(), hand2.cardCombination());
            case PAIR:
                return comparePairHands(hand1.cardCombination(), hand2.cardCombination());
            case HIGH_CARD:
            case TRIPS:
                return compareKickers(hand1.cardCombination(), hand2.cardCombination());
            default:
                return compareCardValues(hand1.cardCombination(), hand2.cardCombination());
        }
    }

    // Compare two hands based on their card values
    private static int compareCardValues(List<UthCard> hand1, List<UthCard> hand2) {
        return compareHands(hand1, hand2);
    }

    // Compare two hands based on their kickers
    private static int compareKickers(List<UthCard> hand1, List<UthCard> hand2) {
        return compareHands(hand1, hand2);
    }

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

    // Determine the winner among a list of hands
    public static Boolean determineWinner(List<UthCard> communityCards, UthHand playerHand, UthHand dealerHand) {
        // Evaluate the player's and dealer's hands using the community cards
        PokerEvaluatedHandModel playerEvaluatedHand = evaluateHand(communityCards, playerHand);
        PokerEvaluatedHandModel dealerEvaluatedHand = evaluateHand(communityCards, dealerHand);

        // Compare the two hands
        int comparison = compareHands(playerEvaluatedHand, dealerEvaluatedHand);

        // Return true if the player's hand is better, false otherwise
        if (comparison > 0) {
            return true; // Player wins
        } else if (comparison < 0) {
            return false; // Dealer wins
        } else {
            return null; // Tie
        }
    }

    // Helper method to compare two hands with TWO_PAIR
    private static int compareTwoPairHands(List<UthCard> hand1, List<UthCard> hand2) {
        // Get the ranks of the pairs and the kicker for each hand
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

    // Helper method to compare two hands with TWO_PAIR
    private static int comparePairHands(List<UthCard> hand1, List<UthCard> hand2) {
        // Get the ranks of the pairs and the kicker for each hand
        List<Integer> hand1PairRanks = getPairRanks(hand1);
        List<Integer> hand2PairRanks = getPairRanks(hand2);

        // Compare the highest pair
        int highestPairComparison = hand1PairRanks.get(0).compareTo(hand2PairRanks.get(0));
        if (highestPairComparison != 0) {
            return highestPairComparison;
        }

        // Compare the kicker
        int hand1Kicker = getKicker(hand1, hand1PairRanks);
        int hand2Kicker = getKicker(hand2, hand2PairRanks);
        return Integer.compare(hand1Kicker, hand2Kicker);
    }

    // Helper method to get the ranks of the pairs in a hand
    private static List<Integer> getPairRanks(List<UthCard> hand) {
        Map<Integer, Integer> valueCounts = new HashMap<>();
        for (UthCard card : hand) {
            valueCounts.put(card.getValue(), valueCounts.getOrDefault(card.getValue(), 0) + 1);
        }

        // Extract the ranks of the pairs
        List<Integer> pairRanks = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : valueCounts.entrySet()) {
            if (entry.getValue() == 2) {
                pairRanks.add(entry.getKey());
            }
        }

        // Sort in descending order
        pairRanks.sort(Comparator.reverseOrder());
        return pairRanks;
    }

    // Helper method to get the kicker in a TWO_PAIR hand
    private static int getKicker(List<UthCard> hand, List<Integer> pairRanks) {
        for (UthCard card : hand) {
            if (!pairRanks.contains(card.getValue())) {
                return card.getValue();
            }
        }
        throw new IllegalStateException("No kicker found in TWO_PAIR hand");
    }
}
