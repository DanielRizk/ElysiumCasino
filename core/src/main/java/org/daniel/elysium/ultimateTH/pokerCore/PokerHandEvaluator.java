package org.daniel.elysium.ultimateTH.pokerCore;

import org.daniel.elysium.ultimateTH.constants.UthHandCombination;
import org.daniel.elysium.ultimateTH.model.UthCard;
import org.daniel.elysium.ultimateTH.model.UthHand;
import org.daniel.elysium.ultimateTH.pokerCore.models.PokerEvaluatedHandModel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Evaluates poker hands in Ultimate Texas Hold'em.
 * <p>
 * This class determines the best possible 5-card hand from the available cards,
 * evaluates its ranking, and selects a kicker if necessary.
 * </p>
 */
public class PokerHandEvaluator {

    /* ======================
       Hand Evaluation
       ====================== */

    /**
     * Evaluates the best poker hand possible using the player's and community cards.
     *
     * @param communityCards the shared community cards
     * @param hand the player's or dealer's hand
     * @return a {@code PokerEvaluatedHandModel} containing the best hand combination, kicker, and ranking
     */
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

    /* ======================
       Combination Generation
       ====================== */

    /**
     * Generates all possible 5-card combinations from the available cards.
     *
     * @param cards the full list of available cards
     * @param k the number of cards per combination
     * @return a list of all possible 5-card combinations
     */
    private static List<List<UthCard>> generateCombinations(List<UthCard> cards, int k) {
        List<List<UthCard>> combinations = new ArrayList<>();
        generateCombinationsHelper(cards, k, 0, new ArrayList<>(), combinations);
        return combinations;
    }

    /**
     * Helper method to generate card combinations recursively.
     *
     * @param cards the full list of available cards
     * @param k the number of cards per combination
     * @param start the index to start from
     * @param current the current combination being formed
     * @param combinations the list of generated combinations
     */
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

    /* ======================
       Hand Ranking
       ====================== */

    /**
     * Determines the hand ranking for a given 5-card hand.
     *
     * @param hand the hand to evaluate
     * @return the corresponding {@code UthHandCombination}
     */
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

    /* ======================
       Hand Type Verification
       ====================== */

    /**
     * Checks if the hand is a Royal Flush.
     *
     * @param hand the hand to evaluate
     * @return {@code true} if the hand is a Royal Flush, otherwise {@code false}
     */
    private static boolean isRoyalFlush(List<UthCard> hand) {
        if (!isFlush(hand)) return false;
        Set<String> requiredRanks = new HashSet<>(Arrays.asList("A", "K", "Q", "J", "10"));
        Set<String> handRanks = hand.stream().map(UthCard::getRank).collect(Collectors.toSet());
        return handRanks.containsAll(requiredRanks);
    }

    /**
     * Checks if the hand is a Straight Flush.
     */
    private static boolean isStraightFlush(List<UthCard> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    /**
     * Checks if the hand is Four of a Kind.
     */
    private static boolean isFourOfAKind(List<UthCard> hand) {
        return getRankCounts(hand).containsValue(4);
    }

    /**
     * Checks if the hand is a Full House.
     */
    private static boolean isFullHouse(List<UthCard> hand) {
        Map<String, Integer> rankCounts = getRankCounts(hand);
        return rankCounts.containsValue(3) && rankCounts.containsValue(2);
    }

    /**
     * Checks if the hand is a Flush.
     */
    private static boolean isFlush(List<UthCard> hand) {
        return hand.stream().map(UthCard::getSuit).collect(Collectors.toSet()).size() == 1;
    }

    /**
     * Checks if the hand is a Straight.
     */
    private static boolean isStraight(List<UthCard> hand) {
        List<Integer> values = hand.stream()
                .map(UthCard::getValue)
                .sorted()
                .toList();
        if (values.equals(Arrays.asList(2, 3, 4, 5, 14))) return true;
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) != values.get(i - 1) + 1) return false;
        }
        return true;
    }

    /**
     * Checks if the hand is Three of a Kind.
     */
    private static boolean isThreeOfAKind(List<UthCard> hand) {
        return getRankCounts(hand).containsValue(3);
    }

    /**
     * Checks if the hand is Two Pair.
     */
    private static boolean isTwoPair(List<UthCard> hand) {
        return getRankCounts(hand).values().stream().filter(count -> count == 2).count() == 2;
    }

    /**
     * Checks if the hand is One Pair.
     */
    private static boolean isOnePair(List<UthCard> hand) {
        return getRankCounts(hand).containsValue(2);
    }

    /* ======================
       Helper Methods
       ====================== */

    /**
     * Counts the occurrences of each card rank in a hand.
     */
    private static Map<String, Integer> getRankCounts(List<UthCard> hand) {
        Map<String, Integer> rankCounts = new HashMap<>();
        for (UthCard card : hand) {
            rankCounts.put(card.getRank(), rankCounts.getOrDefault(card.getRank(), 0) + 1);
        }
        return rankCounts;
    }

    /**
     * Determines the kicker card based on the hand ranking.
     */
    private static UthCard getKicker(List<UthCard> hand, UthHandCombination handCombination) {
        List<UthCard> sortedHand = hand.stream()
                .sorted(Comparator.comparingInt(UthCard::getValue).reversed())
                .toList();
        return switch (handCombination) {
            case HIGH_CARD, PAIR, TWO_PAIR, TRIPS -> sortedHand.get(0);
            default -> null;
        };
    }
}
