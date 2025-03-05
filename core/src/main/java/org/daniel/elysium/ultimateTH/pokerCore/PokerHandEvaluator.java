package org.daniel.elysium.ultimateTH.pokerCore;

import org.daniel.elysium.ultimateTH.constants.UthHandCombination;
import org.daniel.elysium.ultimateTH.model.UthCard;
import org.daniel.elysium.ultimateTH.model.UthHand;
import org.daniel.elysium.ultimateTH.pokerCore.models.PokerEvaluatedHandModel;

import java.util.*;
import java.util.stream.Collectors;

public class PokerHandEvaluator {

    // Evaluate the strength of a 5-card hand and return a PokerEvaluatedHandModel
    public static PokerEvaluatedHandModel evaluateHand(List<UthCard> communityCards, UthHand hand) {
        List<UthCard> allCards = new ArrayList<>(communityCards);
        allCards.addAll(hand.getHand());
        List<List<UthCard>> combinations = generateCombinations(allCards, 5);

        // Track the best hand
        PokerEvaluatedHandModel bestHand = null;

        for (List<UthCard> combination : combinations) {
            UthHandCombination handCombination = determineHandCombination(combination);
            UthCard kicker = getKicker(combination, handCombination);

            // Create a PokerEvaluatedHandModel for this combination
            PokerEvaluatedHandModel currentHand = new PokerEvaluatedHandModel(combination, kicker, handCombination);

            // Update the best hand if this one is better
            if (bestHand == null || PokerHandComparator.compareHands(currentHand, bestHand) > 0) {
                bestHand = currentHand;
            }
        }

        return bestHand;
    }

    // Helper method to generate all combinations of size k from a list of cards
    private static List<List<UthCard>> generateCombinations(List<UthCard> cards, int k) {
        List<List<UthCard>> combinations = new ArrayList<>();
        generateCombinationsHelper(cards, k, 0, new ArrayList<>(), combinations);
        return combinations;
    }

    private static void generateCombinationsHelper(List<UthCard> cards, int k, int start, List<UthCard> current, List<List<UthCard>> combinations) {
        if (k == 0) {
            combinations.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i <= cards.size() - k; i++) {
            current.add(cards.get(i));
            generateCombinationsHelper(cards, k - 1, i + 1, current, combinations);
            current.remove(current.size() - 1);
        }
    }

    // Determine the hand combination for a 5-card hand
    private static UthHandCombination determineHandCombination(List<UthCard> hand) {
        if (isRoyalFlush(hand)) return UthHandCombination.ROYAL_FLUSH;
        if (isStraightFlush(hand)) return UthHandCombination.STRAIGHT_FLUSH;
        if (isFourOfAKind(hand)) return UthHandCombination.QUADS;
        if (isFullHouse(hand)) return UthHandCombination.FULL_HOUSE;
        if (isFlush(hand)) return UthHandCombination.FLUSH;
        if (isStraight(hand)) return UthHandCombination.STRAIGHT;
        if (isThreeOfAKind(hand)) return UthHandCombination.TRIPS;
        if (isTwoPair(hand)) return UthHandCombination.TWO_PAIR;
        if (isOnePair(hand)) return UthHandCombination.PAIR;
        return UthHandCombination.HIGH_CARD;
    }

    // Helper methods to check for specific hand types
    private static boolean isRoyalFlush(List<UthCard> hand) {
        if (!isFlush(hand)) return false;

        Set<String> requiredRanks = new HashSet<>(Arrays.asList("A", "K", "Q", "J", "10"));
        Set<String> handRanks = hand.stream().map(UthCard::getRank).collect(Collectors.toSet());

        return handRanks.containsAll(requiredRanks);
    }

    private static boolean isStraightFlush(List<UthCard> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    private static boolean isFourOfAKind(List<UthCard> hand) {
        Map<String, Integer> rankCounts = getRankCounts(hand);
        return rankCounts.containsValue(4);
    }

    private static boolean isFullHouse(List<UthCard> hand) {
        Map<String, Integer> rankCounts = getRankCounts(hand);
        return rankCounts.containsValue(3) && rankCounts.containsValue(2);
    }

    private static boolean isFlush(List<UthCard> hand) {
        Set<String> suits = hand.stream().map(UthCard::getSuit).collect(Collectors.toSet());
        return suits.size() == 1;
    }

    private static boolean isStraight(List<UthCard> hand) {
        // Extract and sort the card ranks
        List<Integer> values = hand.stream()
                .map(UthCard::getValue) // Map ranks to values
                .sorted()
                .toList();

        // Handle low straight (A, 2, 3, 4, 5)
        if (values.equals(Arrays.asList(2, 3, 4, 5, 14))) {
            return true;
        }

        // Check for consecutive values
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) != values.get(i - 1) + 1) {
                return false;
            }
        }

        // If all cards are consecutive, it's a straight
        return true;
    }

    private static boolean isThreeOfAKind(List<UthCard> hand) {
        Map<String, Integer> rankCounts = getRankCounts(hand);
        return rankCounts.containsValue(3);
    }

    private static boolean isTwoPair(List<UthCard> hand) {
        Map<String, Integer> rankCounts = getRankCounts(hand);
        int pairCount = 0;
        for (int count : rankCounts.values()) {
            if (count == 2) {
                pairCount++;
            }
        }
        return pairCount == 2;
    }

    private static boolean isOnePair(List<UthCard> hand) {
        Map<String, Integer> rankCounts = getRankCounts(hand);
        return rankCounts.containsValue(2);
    }

    // Helper method to count occurrences of each card rank
    private static Map<String, Integer> getRankCounts(List<UthCard> hand) {
        Map<String, Integer> rankCounts = new HashMap<>();
        for (UthCard card : hand) {
            rankCounts.put(card.getRank(), rankCounts.getOrDefault(card.getRank(), 0) + 1);
        }
        return rankCounts;
    }

    // Helper method to determine the kicker card
    private static UthCard getKicker(List<UthCard> hand, UthHandCombination handCombination) {
        // Sort the hand based on the mapped integer value of the rank
        List<UthCard> sortedHand = hand.stream()
                .sorted(Comparator.comparingInt((UthCard card) -> card.getValue()).reversed())
                .toList();

        return switch (handCombination) {
            case HIGH_CARD, PAIR, TWO_PAIR, TRIPS -> sortedHand.get(0);
            default -> null;
        };
    }
}
