package org.daniel.elysium;

import org.daniel.elysium.ultimateTH.UthGameEngine;
import org.daniel.elysium.ultimateTH.constants.UthHandCombination;
import org.daniel.elysium.ultimateTH.constants.UthHandState;
import org.daniel.elysium.ultimateTH.constants.UthTripsState;
import org.daniel.elysium.ultimateTH.model.UthCard;
import org.daniel.elysium.ultimateTH.model.UthHand;
import org.daniel.elysium.ultimateTH.model.UthPlayerHand;
import org.daniel.elysium.ultimateTH.pokerCore.models.PokerEvaluatedHandModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for verifying that the {@link UthGameEngine} correctly identifies, compares and payout
 * various poker hands (e.g., Royal Flush, Straight, Pair, etc.) in an
 * Ultimate Texas Hold'em context.
 * <p>
 * Each method sets up a scenario with specific community cards and
 * player hole cards, then checks that the engine evaluates the resulting
 * best 5-card combination as expected.
 */
public class UltimateTHTest {

    /**
     * Ensures that the engine correctly identifies a Royal Flush
     * when the player and community cards form A-K-Q-J-10 of the same suit.
     */
    @Test
    public void evaluateRoyalFlush() {
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("A", "S"));
        communityCards.add(new UthCard("K", "S"));
        communityCards.add(new UthCard("Q", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("5", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("J", "S"));
        playerHand.dealCard(new UthCard("10", "S"));

        UthGameEngine.evaluateHand(communityCards, playerHand);

        Assertions.assertEquals(UthHandCombination.ROYAL_FLUSH,
                playerHand.getEvaluatedHand().handCombination());
    }

    /**
     * Tests that the engine correctly identifies a Straight Flush
     * (K-Q-J-10-9 in the same suit).
     */
    @Test
    public void evaluateStraightFlush() {
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("K", "S"));
        communityCards.add(new UthCard("Q", "S"));
        communityCards.add(new UthCard("9", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("5", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("J", "S"));
        playerHand.dealCard(new UthCard("10", "S"));

        UthGameEngine.evaluateHand(communityCards, playerHand);

        Assertions.assertEquals(UthHandCombination.STRAIGHT_FLUSH,
                playerHand.getEvaluatedHand().handCombination());
    }

    /**
     * Verifies that four of a kind (quads) is recognized when
     * the player and community cards form K-K-K-K.
     */
    @Test
    public void evaluateQuads() {
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("K", "S"));
        communityCards.add(new UthCard("K", "D"));
        communityCards.add(new UthCard("9", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("5", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("K", "H"));
        playerHand.dealCard(new UthCard("K", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);

        Assertions.assertEquals(UthHandCombination.QUADS,
                playerHand.getEvaluatedHand().handCombination());
    }

    /**
     * Confirms that a Full House (K-K-K-9-9) is properly identified.
     */
    @Test
    public void evaluateFullHouse() {
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("K", "S"));
        communityCards.add(new UthCard("K", "D"));
        communityCards.add(new UthCard("9", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("5", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("K", "H"));
        playerHand.dealCard(new UthCard("9", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);

        Assertions.assertEquals(UthHandCombination.FULL_HOUSE,
                playerHand.getEvaluatedHand().handCombination());
    }

    /**
     * Checks that a Flush is detected when the player has five
     * cards of the same suit (e.g., D).
     */
    @Test
    public void evaluateFlush() {
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("K", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("9", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("5", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("K", "D"));
        playerHand.dealCard(new UthCard("9", "D"));

        UthGameEngine.evaluateHand(communityCards, playerHand);

        Assertions.assertEquals(UthHandCombination.FLUSH,
                playerHand.getEvaluatedHand().handCombination());
    }

    /**
     * Ensures that a Straight (10-9-8-J-Q, etc.) is recognized
     * even with mixed suits.
     */
    @Test
    public void evaluateStraight() {
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("9", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("5", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("J", "H"));
        playerHand.dealCard(new UthCard("8", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);

        Assertions.assertEquals(UthHandCombination.STRAIGHT,
                playerHand.getEvaluatedHand().handCombination());
    }

    /**
     * Verifies the detection of Trips (three of a kind),
     * here specifically with '9's.
     */
    @Test
    public void evaluateTrips() {
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("9", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("5", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("9", "H"));
        playerHand.dealCard(new UthCard("9", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);

        Assertions.assertEquals(UthHandCombination.TRIPS,
                playerHand.getEvaluatedHand().handCombination());
    }

    /**
     * Confirms that Two Pair is identified (e.g., 9-9 and 5-5).
     */
    @Test
    public void evaluateTwoPair() {
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("5", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("5", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("9", "H"));
        playerHand.dealCard(new UthCard("9", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);

        Assertions.assertEquals(UthHandCombination.TWO_PAIR,
                playerHand.getEvaluatedHand().handCombination());
    }

    /**
     * Tests that a single pair (Pair of 9s) is recognized.
     */
    @Test
    public void evaluateOnePair() {
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("5", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("9", "H"));
        playerHand.dealCard(new UthCard("9", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);

        Assertions.assertEquals(UthHandCombination.PAIR,
                playerHand.getEvaluatedHand().handCombination());
    }

    /**
     * Verifies that no combination beyond a High Card is found
     * if the player's best 5-card set does not form a pair
     * or higher rank.
     */
    @Test
    public void evaluateHighCard() {
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("5", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("A", "H"));
        playerHand.dealCard(new UthCard("9", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);

        Assertions.assertEquals(UthHandCombination.HIGH_CARD,
                playerHand.getEvaluatedHand().handCombination());
    }

    /**
     * Checks that when both player and dealer have High Card hands,
     * the player wins if their highest card (Ace) beats the dealer's (King).
     */
    @Test
    public void result_HighCard_vs_HighCard_PlayerWins(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("5", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("A", "H"));
        playerHand.dealCard(new UthCard("9", "C"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("K", "H"));
        dealerHand.dealCard(new UthCard("9", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.HIGH_CARD, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.HIGH_CARD, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Ensures that when the player has a Pair and the dealer has only High Card,
     * the player's Pair wins.
     */
    @Test
    public void result_Pair_vs_HighCard_PlayerWins(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("9", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("A", "H"));
        playerHand.dealCard(new UthCard("9", "C"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("K", "H"));
        dealerHand.dealCard(new UthCard("8", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.PAIR, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.HIGH_CARD, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Verifies that when both player and dealer have a Pair of the same rank,
     * the kicker decides the winner. In this scenario, the player's Ace
     * kicker outclasses the dealer's King.
     */
    @Test
    public void result_Pair_vs_Pair_Tie_PlayerKicker(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("4", "D"));
        communityCards.add(new UthCard("9", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("A", "H"));
        playerHand.dealCard(new UthCard("9", "C"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("K", "H"));
        dealerHand.dealCard(new UthCard("9", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.PAIR, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.PAIR, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Checks that if both player and dealer have pairs, the one
     * with the higher-ranked pair wins (Ace pair vs. King pair).
     */
    @Test
    public void result_Pair_vs_Pair_PlayerHigherPair(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("K", "D"));
        communityCards.add(new UthCard("A", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("A", "H"));
        playerHand.dealCard(new UthCard("9", "C"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("K", "H"));
        dealerHand.dealCard(new UthCard("9", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.PAIR, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.PAIR, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Confirms that when both hands have TwoPair, the player
     * with the higher pair among the two sets wins.
     */
    @Test
    public void result_TwoPair_vs_TwoPair_PlayerHigherPair(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("9", "D"));
        communityCards.add(new UthCard("A", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("A", "H"));
        playerHand.dealCard(new UthCard("10", "C"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("A", "H"));
        dealerHand.dealCard(new UthCard("9", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.TWO_PAIR, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.TWO_PAIR, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Tests that Trips (three of a kind) defeats Two Pair.
     */
    @Test
    public void result_TwoPair_vs_Trips_PlayerWins(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("9", "D"));
        communityCards.add(new UthCard("A", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("10", "H"));
        playerHand.dealCard(new UthCard("10", "C"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("A", "H"));
        dealerHand.dealCard(new UthCard("9", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.TRIPS, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.TWO_PAIR, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Checks that when both hands are Straights, the one with the
     * higher top card wins (player has 6-high vs. dealer's A-low or similar).
     */
    @Test
    public void result_Straight_vs_Straight_PlayerWins(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("5", "D"));
        communityCards.add(new UthCard("4", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("6", "H"));
        playerHand.dealCard(new UthCard("2", "C"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("A", "H"));
        dealerHand.dealCard(new UthCard("2", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.STRAIGHT, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.STRAIGHT, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Confirms that a Flush (player) beats a Straight (dealer).
     */
    @Test
    public void result_Flush_vs_Straight_PlayerWins(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("5", "D"));
        communityCards.add(new UthCard("4", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("6", "D"));
        playerHand.dealCard(new UthCard("2", "D"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("A", "H"));
        dealerHand.dealCard(new UthCard("2", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.FLUSH, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.STRAIGHT, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Tests that when both player and dealer have a Flush,
     * the dealer's flush is stronger (perhaps an Ace-high flush).
     */
    @Test
    public void result_Flush_vs_Flush_PlayerWins(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("5", "D"));
        communityCards.add(new UthCard("4", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("6", "D"));
        playerHand.dealCard(new UthCard("2", "D"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("A", "D"));
        dealerHand.dealCard(new UthCard("2", "D"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.FLUSH, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.FLUSH, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.LOST, playerHand.getState());
        Assertions.assertEquals(UthHandState.WON, dealerHand.getState());
    }

    /**
     * Ensures that when both hands have a Full House,
     * the player's Full House outranks the dealer's (e.g. 9s over 8s).
     */
    @Test
    public void result_FullHouse_vs_FullHouse_PlayerWins(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("3", "D"));
        communityCards.add(new UthCard("3", "S"));
        communityCards.add(new UthCard("8", "D"));
        communityCards.add(new UthCard("9", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("9", "H"));
        playerHand.dealCard(new UthCard("9", "C"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("8", "H"));
        dealerHand.dealCard(new UthCard("8", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.FULL_HOUSE, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.FULL_HOUSE, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Checks that Quads (four of a kind) vs. Quads can still produce
     * a single winner (player's quads rank is higher).
     */
    @Test
    public void result_Quads_vs_Quads_PlayerWins(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("8", "D"));
        communityCards.add(new UthCard("8", "S"));
        communityCards.add(new UthCard("9", "S"));
        communityCards.add(new UthCard("9", "D"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("9", "H"));
        playerHand.dealCard(new UthCard("9", "C"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("8", "H"));
        dealerHand.dealCard(new UthCard("8", "C"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.QUADS, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.QUADS, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Verifies that when both hands have a Straight Flush,
     * the player's higher Straight Flush wins.
     */
    @Test
    public void result_StraightFlush_vs_StraightFlush_PlayerWins(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "D"));
        communityCards.add(new UthCard("3", "H"));
        communityCards.add(new UthCard("5", "H"));
        communityCards.add(new UthCard("4", "H"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("6", "H"));
        playerHand.dealCard(new UthCard("2", "H"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("A", "H"));
        dealerHand.dealCard(new UthCard("2", "H"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.STRAIGHT_FLUSH, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.STRAIGHT_FLUSH, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Confirms that a Royal Flush (player) beats a lower Straight Flush (dealer).
     */
    @Test
    public void result_RoyalFlush_vs_StraightFlush_PlayerWins(){
        List<UthCard> communityCards = new ArrayList<>();
        communityCards.add(new UthCard("10", "S"));
        communityCards.add(new UthCard("Q", "H"));
        communityCards.add(new UthCard("10", "H"));
        communityCards.add(new UthCard("J", "H"));
        communityCards.add(new UthCard("4", "H"));

        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.dealCard(new UthCard("K", "H"));
        playerHand.dealCard(new UthCard("A", "H"));

        UthHand dealerHand = new UthHand();
        dealerHand.dealCard(new UthCard("9", "H"));
        dealerHand.dealCard(new UthCard("8", "H"));

        UthGameEngine.evaluateHand(communityCards, playerHand);
        UthGameEngine.evaluateHand(communityCards, dealerHand);

        UthGameEngine.determineGameResults(playerHand, dealerHand);

        Assertions.assertEquals(UthHandCombination.ROYAL_FLUSH, playerHand.getEvaluatedHand().handCombination());
        Assertions.assertEquals(UthHandCombination.STRAIGHT_FLUSH, dealerHand.getEvaluatedHand().handCombination());

        Assertions.assertEquals(UthHandState.WON, playerHand.getState());
        Assertions.assertEquals(UthHandState.LOST, dealerHand.getState());
    }

    /**
     * Ensures that if the player's hand ranks below Trips (e.g., Two Pair),
     * the Trips bet is lost and set to 0, with {@code UthTripsState.LOST}.
     */
    @Test
    public void evaluateTrips_LessThanTrips() {
        PokerEvaluatedHandModel model = new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.TWO_PAIR);
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setEvaluatedHand(model);
        playerHand.setTrips(100);

        UthGameEngine.evaluateTrips(playerHand);

        Assertions.assertEquals(0, playerHand.getTrips());
        Assertions.assertEquals(UthTripsState.LOST, playerHand.getTripsState());
    }

    /**
     * Confirms that a Trips hand (three of a kind) pays out 3to1
     * (so 100 becomes 400), with {@code UthTripsState.TRIPS}.
     */
    @Test
    public void evaluateTrips_Trips() {
        PokerEvaluatedHandModel model = new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.TRIPS);
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setEvaluatedHand(model);
        playerHand.setTrips(100);

        UthGameEngine.evaluateTrips(playerHand);

        Assertions.assertEquals(400, playerHand.getTrips());
        Assertions.assertEquals(UthTripsState.TRIPS, playerHand.getTripsState());
    }

    /**
     * Confirms that a Straight pays out 4to1
     * (so 100 becomes 500), with {@code UthTripsState.STRAIGHT}.
     */
    @Test
    public void evaluateTrips_Straight() {
        PokerEvaluatedHandModel model = new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.STRAIGHT);
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setEvaluatedHand(model);
        playerHand.setTrips(100);

        UthGameEngine.evaluateTrips(playerHand);

        Assertions.assertEquals(500, playerHand.getTrips());
        Assertions.assertEquals(UthTripsState.STRAIGHT, playerHand.getTripsState());
    }

    /**
     * Confirms that a Flush pays out 7to1
     * (so 100 becomes 800), with {@code UthTripsState.FLUSH}.
     */
    @Test
    public void evaluateTrips_Flush() {
        PokerEvaluatedHandModel model = new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.FLUSH);
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setEvaluatedHand(model);
        playerHand.setTrips(100);

        UthGameEngine.evaluateTrips(playerHand);

        Assertions.assertEquals(800, playerHand.getTrips());
        Assertions.assertEquals(UthTripsState.FLUSH, playerHand.getTripsState());
    }

    /**
     * Confirms that a Full House pays out 8to1
     * (so 100 becomes 900), with {@code UthTripsState.FULL_HOUSE}.
     */
    @Test
    public void evaluateTrips_FullHouse() {
        PokerEvaluatedHandModel model = new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.FULL_HOUSE);
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setEvaluatedHand(model);
        playerHand.setTrips(100);

        UthGameEngine.evaluateTrips(playerHand);

        Assertions.assertEquals(900, playerHand.getTrips());
        Assertions.assertEquals(UthTripsState.FULL_HOUSE, playerHand.getTripsState());
    }

    /**
     * Confirms that Four of a Kind (Quads) pays out 30to1
     * (so 100 becomes 3100), with {@code UthTripsState.QUADS}.
     */
    @Test
    public void evaluateTrips_Quads() {
        PokerEvaluatedHandModel model = new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.QUADS);
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setEvaluatedHand(model);
        playerHand.setTrips(100);

        UthGameEngine.evaluateTrips(playerHand);

        Assertions.assertEquals(3100, playerHand.getTrips());
        Assertions.assertEquals(UthTripsState.QUADS, playerHand.getTripsState());
    }

    /**
     * Confirms that a Straight Flush pays out 40to1
     * (so 100 becomes 4100), with {@code UthTripsState.STRAIGHT_FLUSH}.
     */
    @Test
    public void evaluateTrips_StraightFlush() {
        PokerEvaluatedHandModel model = new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.STRAIGHT_FLUSH);
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setEvaluatedHand(model);
        playerHand.setTrips(100);

        UthGameEngine.evaluateTrips(playerHand);

        Assertions.assertEquals(4100, playerHand.getTrips());
        Assertions.assertEquals(UthTripsState.STRAIGHT_FLUSH, playerHand.getTripsState());
    }

    /**
     * Confirms that a Royal Flush pays out 50to1
     * (so 100 becomes 5100), with {@code UthTripsState.ROYAL_FLUSH}.
     */
    @Test
    public void evaluateTrips_RoyalFlush() {
        PokerEvaluatedHandModel model = new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.ROYAL_FLUSH);
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setEvaluatedHand(model);
        playerHand.setTrips(100);

        UthGameEngine.evaluateTrips(playerHand);

        Assertions.assertEquals(5100, playerHand.getTrips());
        Assertions.assertEquals(UthTripsState.ROYAL_FLUSH, playerHand.getTripsState());
    }

    /**
     * Verifies that if the dealer wins and the dealer's hand qualifies
     * (pair or better), the player loses ante, blind, and play bets.
     */
    @Test
    public void processPayout_DealerWinsQualified() {
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setBet(100);
        playerHand.setPlay(400);
        playerHand.setState(UthHandState.LOST);

        UthHand dealerHand = new UthHand();
        dealerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.PAIR));
        dealerHand.setState(UthHandState.WON);

        UthGameEngine.processResults(playerHand, dealerHand);

        Assertions.assertEquals(0, playerHand.getAnte());
        Assertions.assertEquals(0, playerHand.getBlind());
        Assertions.assertEquals(0, playerHand.getPlay());
    }

    /**
     * Verifies that if the dealer wins but does NOT qualify
     * (high card only), the player's ante is returned,
     * but blind and play bets are lost.
     */
    @Test
    public void processPayout_DealerWinsUnqualified() {
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setBet(100);
        playerHand.setPlay(400);
        playerHand.setState(UthHandState.LOST);

        UthHand dealerHand = new UthHand();
        dealerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.HIGH_CARD));
        dealerHand.setState(UthHandState.WON);

        UthGameEngine.processResults(playerHand, dealerHand);

        Assertions.assertEquals(100, playerHand.getAnte());
        Assertions.assertEquals(0, playerHand.getBlind());
        Assertions.assertEquals(0, playerHand.getPlay());
    }

    /**
     * Tests the scenario where the player has a high-card hand,
     * the dealer does not qualify, and the player wins.
     * Ante pushes (returned) if dealer doesn't qualify,
     * blind pays at 1:1, and play pays 1:1 (i.e., 400 -> 800).
     */
    @Test
    public void processPayout_PlayerWinsHighCard_dealerUnqualified() {
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setBet(100);
        playerHand.setPlay(400);
        playerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.HIGH_CARD));
        playerHand.setState(UthHandState.WON);

        UthHand dealerHand = new UthHand();
        dealerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.HIGH_CARD));
        dealerHand.setState(UthHandState.LOST);

        UthGameEngine.processResults(playerHand, dealerHand);

        Assertions.assertEquals(100, playerHand.getAnte());
        Assertions.assertEquals(100, playerHand.getBlind());
        Assertions.assertEquals(800, playerHand.getPlay());
    }

    /**
     * Confirms that if the player has a Pair or better and the dealer qualifies,
     * the ante pays 1:1, blind pays a minimum of 1:1, and play also pays 1:1.
     */
    @Test
    public void processPayout_PlayerWinsPair_dealerQualifies() {
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setBet(100);
        playerHand.setPlay(400);
        playerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.PAIR));
        playerHand.setState(UthHandState.WON);

        UthHand dealerHand = new UthHand();
        dealerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.PAIR));
        dealerHand.setState(UthHandState.LOST);

        UthGameEngine.processResults(playerHand, dealerHand);

        Assertions.assertEquals(200, playerHand.getAnte());
        Assertions.assertEquals(100, playerHand.getBlind());
        Assertions.assertEquals(800, playerHand.getPlay());
    }

    /**
     * Ensures that a Straight yields an ante 1:1, blind 1:1,
     * and play 1:1 when the dealer qualifies.
     */
    @Test
    public void processPayout_PlayerWinsStraight_dealerQualifies() {
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setBet(100);
        playerHand.setPlay(400);
        playerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.STRAIGHT));
        playerHand.setState(UthHandState.WON);

        UthHand dealerHand = new UthHand();
        dealerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.PAIR));
        dealerHand.setState(UthHandState.LOST);

        UthGameEngine.processResults(playerHand, dealerHand);

        Assertions.assertEquals(200, playerHand.getAnte());
        Assertions.assertEquals(200, playerHand.getBlind());
        Assertions.assertEquals(800, playerHand.getPlay());
    }

    /**
     * Tests that a Flush yields an ante 1:1, blind 3:2 (which is 250 for a 100 bet),
     * and play 1:1, if the dealer qualifies.
     */
    @Test
    public void processPayout_PlayerWinsFlush_dealerQualifies() {
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setBet(100);
        playerHand.setPlay(400);
        playerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.FLUSH));
        playerHand.setState(UthHandState.WON);

        UthHand dealerHand = new UthHand();
        dealerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.PAIR));
        dealerHand.setState(UthHandState.LOST);

        UthGameEngine.processResults(playerHand, dealerHand);

        Assertions.assertEquals(200, playerHand.getAnte());
        Assertions.assertEquals(250, playerHand.getBlind());
        Assertions.assertEquals(800, playerHand.getPlay());
    }

    /**
     * Checks that a Full House pays ante 1:1, blind 3:1, and play 1:1,
     * assuming the dealer qualifies.
     */
    @Test
    public void processPayout_PlayerWinsFullHouse_dealerQualifies() {
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setBet(100);
        playerHand.setPlay(400);
        playerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.FULL_HOUSE));
        playerHand.setState(UthHandState.WON);

        UthHand dealerHand = new UthHand();
        dealerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.PAIR));
        dealerHand.setState(UthHandState.LOST);

        UthGameEngine.processResults(playerHand, dealerHand);

        Assertions.assertEquals(200, playerHand.getAnte());
        Assertions.assertEquals(400, playerHand.getBlind());
        Assertions.assertEquals(800, playerHand.getPlay());
    }

    /**
     * Tests that Quads (four of a kind) pays ante 1:1, blind 10:1,
     * and play 1:1, if the dealer qualifies.
     */
    @Test
    public void processPayout_PlayerWinsQuads_dealerQualifies() {
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setBet(100);
        playerHand.setPlay(400);
        playerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.QUADS));
        playerHand.setState(UthHandState.WON);

        UthHand dealerHand = new UthHand();
        dealerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.PAIR));
        dealerHand.setState(UthHandState.LOST);

        UthGameEngine.processResults(playerHand, dealerHand);

        Assertions.assertEquals(200, playerHand.getAnte());
        Assertions.assertEquals(1100, playerHand.getBlind());
        Assertions.assertEquals(800, playerHand.getPlay());
    }

    /**
     * Ensures that a Straight Flush pays ante 1:1, blind 50:1,
     * and play 1:1 when the dealer qualifies.
     */
    @Test
    public void processPayout_PlayerWinsStraightFlush_dealerQualifies() {
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setBet(100);
        playerHand.setPlay(400);
        playerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.STRAIGHT_FLUSH));
        playerHand.setState(UthHandState.WON);

        UthHand dealerHand = new UthHand();
        dealerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.PAIR));
        dealerHand.setState(UthHandState.LOST);

        UthGameEngine.processResults(playerHand, dealerHand);

        Assertions.assertEquals(200, playerHand.getAnte());
        Assertions.assertEquals(5100, playerHand.getBlind());
        Assertions.assertEquals(800, playerHand.getPlay());
    }

    /**
     * Confirms that a Royal Flush pays ante 1:1, blind 500:1,
     * and play 1:1, if the dealer qualifies.
     */
    @Test
    public void processPayout_PlayerWinsRoyalFlush_dealerQualifies() {
        UthPlayerHand playerHand = new UthPlayerHand();
        playerHand.setBet(100);
        playerHand.setPlay(400);
        playerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.ROYAL_FLUSH));
        playerHand.setState(UthHandState.WON);

        UthHand dealerHand = new UthHand();
        dealerHand.setEvaluatedHand(new PokerEvaluatedHandModel(
                new ArrayList<>(), null, UthHandCombination.PAIR));
        dealerHand.setState(UthHandState.LOST);

        UthGameEngine.processResults(playerHand, dealerHand);

        Assertions.assertEquals(200, playerHand.getAnte());
        Assertions.assertEquals(50100, playerHand.getBlind());
        Assertions.assertEquals(800, playerHand.getPlay());
    }
}

