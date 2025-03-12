import org.daniel.elysium.blackjack.BlackjackEngine;
import org.daniel.elysium.blackjack.constants.BJHandState;
import org.daniel.elysium.blackjack.models.BJCard;
import org.daniel.elysium.blackjack.models.BJDealerHand;
import org.daniel.elysium.blackjack.models.BJPlayerHand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlackjackTest {

    /**
     * Test that a player's hand is recognized as a Blackjack
     * (an Ace + 10-valued card) immediately.
     */
    @Test
    public void isPlayerBlackjack(){
        // Create a new player hand and deal an Ace + 10
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.dealCard(new BJCard("A", "S"));
        playerHand.dealCard(new BJCard("10", "S"));

        // Check if the hand is recognized as Blackjack
        boolean isBlackjack = playerHand.isBlackJack();

        // Verify the hand state is BLACKJACK and the boolean reflects that
        Assertions.assertEquals(BJHandState.BLACKJACK, playerHand.getState());
        Assertions.assertTrue(isBlackjack);
    }

    /**
     * Test that a player's hand is considered splittable (two 10-value cards).
     * Also ensures it's recognized that these are not Aces.
     */
    @Test
    public void isPlayerSplittable(){
        BJPlayerHand playerHand = new BJPlayerHand();
        // Deal a K + 10 (both valued at 10)
        playerHand.dealCard(new BJCard("K", "S"));
        playerHand.dealCard(new BJCard("10", "S"));

        boolean isSplittable = playerHand.isSplittable();
        boolean isSplitAces = playerHand.isSplitAces();

        // K and 10 means the player can split (both have same 10-value)
        Assertions.assertTrue(isSplittable);
        // But they are not Aces, so isSplitAces should be false
        Assertions.assertFalse(isSplitAces);
    }

    /**
     * Test the scenario where a player's hand is two Aces,
     * which is splittable specifically as Aces.
     */
    @Test
    public void isPlayerSplitAces(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.dealCard(new BJCard("A", "S"));
        playerHand.dealCard(new BJCard("A", "S"));

        boolean isSplittable = playerHand.isSplittable();
        boolean isSplitAces = playerHand.isSplitAces();

        // Should be splittable because both cards are Aces
        Assertions.assertTrue(isSplittable);
        // Also should specifically detect they are Aces
        Assertions.assertTrue(isSplitAces);
    }

    /**
     * Test that a player can still be dealt another card
     * if the hand is not over 21 or otherwise restricted.
     */
    @Test
    public void canDealPlayerCard(){
        BJPlayerHand playerHand = new BJPlayerHand();
        // Total so far is 6+9=15
        playerHand.dealCard(new BJCard("6", "S"));
        playerHand.dealCard(new BJCard("9", "S"));

        // Check if the player can take another card (should be true at 15)
        boolean canDeal = playerHand.canDealCard(new BJCard("8", "S"));
        Assertions.assertTrue(canDeal);
    }

    /**
     * Test that a player can receive cards after splitting (non-Aces).
     */
    @Test
    public void canDealPlayerCardWhenSplit(){
        BJPlayerHand playerHand = new BJPlayerHand();
        // Deal two Ks, which is splittable
        playerHand.dealCard(new BJCard("K", "S"));
        playerHand.dealCard(new BJCard("K", "S"));

        // Confirm it's splittable but not split Aces
        boolean isSplittable = playerHand.isSplittable();
        boolean isSplitAces = playerHand.isSplitAces();

        Assertions.assertTrue(isSplittable);
        Assertions.assertFalse(isSplitAces);

        // Mark the hand as split and remove the second K from this hand
        playerHand.setHandSplit(true);
        playerHand.getHand().remove(1);

        // Now deal two cards to this new "split" hand
        Assertions.assertTrue(playerHand.dealCard(new BJCard("8", "S")));
        Assertions.assertTrue(playerHand.dealCard(new BJCard("8", "S")));
    }

    /**
     * Test that a player can only receive one additional card
     * when splitting Aces (if that's your house rule).
     * The second dealCard() should fail here.
     */
    @Test
    public void canDealPlayerCardWhenSplitAces(){
        BJPlayerHand playerHand = new BJPlayerHand();
        // Two Aces, so definitely splittable
        playerHand.dealCard(new BJCard("A", "S"));
        playerHand.dealCard(new BJCard("A", "S"));

        boolean isSplittable = playerHand.isSplittable();
        boolean isSplitAces = playerHand.isSplitAces();

        Assertions.assertTrue(isSplittable);
        Assertions.assertTrue(isSplitAces);

        // Indicate it's a split hand and specifically split Aces
        playerHand.setHandSplit(true);
        playerHand.setSplitAces(true);
        // Remove the second Ace, so this hand only keeps the first
        playerHand.getHand().remove(1);

        // Player can take exactly one card
        Assertions.assertTrue(playerHand.dealCard(new BJCard("8", "S")));
        // Attempting a second card should be denied (false)
        Assertions.assertFalse(playerHand.dealCard(new BJCard("8", "S")));
    }

    /**
     * Test that a player can't be dealt another card
     * if they've already busted (over 21).
     */
    @Test
    public void canNotDealPlayerCard(){
        BJPlayerHand playerHand = new BJPlayerHand();
        // 6 + 9 + 9 = 24, which is a bust
        playerHand.dealCard(new BJCard("6", "S"));
        playerHand.dealCard(new BJCard("9", "S"));
        playerHand.dealCard(new BJCard("9", "S"));

        // At this point, canDeal should return false
        boolean canDeal = playerHand.canDealCard(new BJCard("8", "S"));
        Assertions.assertFalse(canDeal);
    }

    /**
     * DealerHand testing
     */

    /**
     * Test that the dealer can draw another card under normal circumstances
     * (e.g. dealer has 15).
     */
    @Test
    public void canDealDealerCard(){
        BJDealerHand dealerHand = new BJDealerHand();
        dealerHand.dealCard(new BJCard("6", "S"));
        dealerHand.dealCard(new BJCard("9", "S"));

        // Dealer total is 15, so they can deal another card
        boolean canDeal = dealerHand.canDealCard(new BJCard("8", "S"));
        Assertions.assertTrue(canDeal);
    }

    /**
     * Test that the dealer can draw another card on a "soft 17"
     * (Ace + 6 is typically a soft 17 in Blackjack).
     */
    @Test
    public void canDealDealerCardOnSoft17(){
        BJDealerHand dealerHand = new BJDealerHand();
        // 6 + A is a soft 17
        dealerHand.dealCard(new BJCard("6", "S"));
        dealerHand.dealCard(new BJCard("A", "S"));

        boolean canDeal = dealerHand.canDealCard(new BJCard("8", "S"));
        // Depending on your rule set, the dealer might still hit on soft 17
        Assertions.assertTrue(canDeal);
    }

    /**
     * Test that the dealer cannot draw a card when they have a "hard" 17 or more,
     * e.g. 10 + 7 = 17 in this scenario.
     */
    @Test
    public void canNotDealDealerCard(){
        BJDealerHand dealerHand = new BJDealerHand();
        // 10 + 7 = 17, which is typically a stand for the dealer
        dealerHand.dealCard(new BJCard("10", "S"));
        dealerHand.dealCard(new BJCard("7", "S"));

        boolean canDeal = dealerHand.canDealCard(new BJCard("8", "S"));
        // Should be false because dealer stands on 17 or higher
        Assertions.assertFalse(canDeal);
    }

    /**
     * Test that a dealer's hand is recognized as Blackjack
     * (Ace + 10-valued card).
     */
    @Test
    public void isDealerBlackjack(){
        BJDealerHand dealerHand = new BJDealerHand();
        dealerHand.dealCard(new BJCard("A", "S"));
        dealerHand.dealCard(new BJCard("10", "S"));

        boolean isBlackjack = dealerHand.isBlackJack();
        // Confirm state is BLACKJACK and the boolean check is true
        Assertions.assertEquals(BJHandState.BLACKJACK, dealerHand.getState());
        Assertions.assertTrue(isBlackjack);
    }


    /**
     * GameEngine testing
     */

    /**
     * Test that insurance is offered if the dealer's up-card is an Ace.
     */
    @Test
    public void isInsuranceTest(){
        BJDealerHand dealerHand = new BJDealerHand();
        // Dealer's cards: Ace + 9
        dealerHand.dealCard(new BJCard("A", "S"));
        dealerHand.dealCard(new BJCard("9", "S"));

        boolean insurance = BlackjackEngine.isInsurance(dealerHand);
        Assertions.assertTrue(insurance);
    }

    /**
     * Test that insurance is not offered if the dealer's up-card is not an Ace.
     */
    @Test
    public void isNotInsuranceTest(){
        BJDealerHand dealerHand = new BJDealerHand();
        // Dealer's cards: K + 9
        dealerHand.dealCard(new BJCard("K", "S"));
        dealerHand.dealCard(new BJCard("9", "S"));

        boolean insurance = BlackjackEngine.isInsurance(dealerHand);
        Assertions.assertFalse(insurance);
    }

    /**
     * Test that when the player's hand is two 10-valued cards (K & Q),
     * the available options include HIT, STAND, DOUBLE, and SPLIT.
     */
    @Test
    public void getAvailableOptions_HIT_STAND_DOUBLE_SPLIT(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.dealCard(new BJCard("K", "S"));
        playerHand.dealCard(new BJCard("Q", "S"));

        List<String> expectedOptions = Arrays.asList("HIT", "STAND", "DOUBLE", "SPLIT");
        List<String> options = BlackjackEngine.getAvailableHandOptions(playerHand);

        Assertions.assertEquals(expectedOptions, options);
    }

    /**
     * Test that when the player has a total of 13 (K + 3),
     * the available options are HIT, STAND, and DOUBLE (but not SPLIT).
     */
    @Test
    public void getAvailableOptions_HIT_STAND_DOUBLE(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.dealCard(new BJCard("K", "S"));
        playerHand.dealCard(new BJCard("3", "S"));

        List<String> expectedOptions = Arrays.asList("HIT", "STAND", "DOUBLE");
        List<String> options = BlackjackEngine.getAvailableHandOptions(playerHand);

        Assertions.assertEquals(expectedOptions, options);
    }

    /**
     * Test that when the player has 3 cards but not busted (8 + K + A = 19),
     * the options are HIT or STAND only (no DOUBLE, no SPLIT).
     */
    @Test
    public void getAvailableOptions_HIT_STAND(){
        BJPlayerHand playerHand = new BJPlayerHand();
        // 8 + K + A -> 8 + 10 + 1 = 19 (soft or hard depending on logic)
        playerHand.dealCard(new BJCard("8", "S"));
        playerHand.dealCard(new BJCard("K", "S"));
        playerHand.dealCard(new BJCard("A", "S"));

        List<String> expectedOptions = Arrays.asList("HIT", "STAND");
        List<String> options = BlackjackEngine.getAvailableHandOptions(playerHand);

        Assertions.assertEquals(expectedOptions, options);
    }

    /**
     * Test that if the player's total is exactly 21,
     * they have no further action options.
     */
    @Test
    public void getAvailableOptions_HandValueEquals21(){
        BJPlayerHand playerHand = new BJPlayerHand();
        // A + 5 + 5 = 21
        playerHand.dealCard(new BJCard("A", "S"));
        playerHand.dealCard(new BJCard("5", "S"));
        playerHand.dealCard(new BJCard("5", "S"));

        // Expect no options if the hand is exactly 21
        List<String> expectedOptions = new ArrayList<>();
        List<String> options = BlackjackEngine.getAvailableHandOptions(playerHand);

        Assertions.assertEquals(21, playerHand.getHandValue());
        Assertions.assertEquals(expectedOptions, options);
    }

    /**
     * Test that if the player's total is over 21 (bust),
     * they have no further action options.
     */
    @Test
    public void getAvailableOptions_HandValueBust(){
        BJPlayerHand playerHand = new BJPlayerHand();
        // 10 + 5 + 7 = 22
        playerHand.dealCard(new BJCard("10", "S"));
        playerHand.dealCard(new BJCard("5", "S"));
        playerHand.dealCard(new BJCard("7", "S"));

        List<String> expectedOptions = new ArrayList<>();
        List<String> options = BlackjackEngine.getAvailableHandOptions(playerHand);

        // Confirm the hand is over 21
        Assertions.assertTrue(21 < playerHand.getHandValue(), "Expected hand value > 21");
        // No options should remain
        Assertions.assertEquals(expectedOptions, options);
    }

    /**
     * Test that if the player busts (over 21),
     * their final result is LOST and the bet is lost (set to zero).
     */
    @Test
    public void playerBust_PlayerLoses_BetLost(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.setBet(1000);

        // 10 + 5 + 7 = 22 (bust)
        playerHand.dealCard(new BJCard("10", "S"));
        playerHand.dealCard(new BJCard("5", "S"));
        playerHand.dealCard(new BJCard("7", "S"));

        BJDealerHand dealerHand = new BJDealerHand();
        dealerHand.dealCard(new BJCard("A", "S"));
        dealerHand.dealCard(new BJCard("9", "S"));

        // Resolve final result
        BlackjackEngine.resolvePlayerResult(playerHand, dealerHand);

        Assertions.assertEquals(BJHandState.LOST, playerHand.getState());
        Assertions.assertEquals(0, playerHand.getBet());
    }

    /**
     * Test that if the dealer busts while the player does not,
     * the player wins and the bet doubles (from 1000 to 2000).
     */
    @Test
    public void dealerBust_PlayerWins_BetDoubles(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.setBet(1000);

        // Player's total: 10 + 5 + 5 = 20
        playerHand.dealCard(new BJCard("10", "S"));
        playerHand.dealCard(new BJCard("5", "S"));
        playerHand.dealCard(new BJCard("5", "S"));

        BJDealerHand dealerHand = new BJDealerHand();
        // Dealer total: 7 + 9 + 9 = 25 (bust)
        dealerHand.dealCard(new BJCard("7", "S"));
        dealerHand.dealCard(new BJCard("9", "S"));
        dealerHand.dealCard(new BJCard("9", "S"));

        BlackjackEngine.resolvePlayerResult(playerHand, dealerHand);

        // Player should win
        Assertions.assertEquals(BJHandState.WON, playerHand.getState());
        // Bet doubles from 1000 to 2000
        Assertions.assertEquals(2000, playerHand.getBet());
    }

    /**
     * Test that if the player's final total is higher than the dealer's (without busting),
     * the player wins, and the bet doubles.
     */
    @Test
    public void playerHand_Higher_DealerHand_PlayerWins_BetDoubles(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.setBet(1000);

        // Player total: 10 + 5 + 5 = 20
        playerHand.dealCard(new BJCard("10", "S"));
        playerHand.dealCard(new BJCard("5", "S"));
        playerHand.dealCard(new BJCard("5", "S"));

        BJDealerHand dealerHand = new BJDealerHand();
        // Dealer total: 7 + 9 + 2 = 18
        dealerHand.dealCard(new BJCard("7", "S"));
        dealerHand.dealCard(new BJCard("9", "S"));
        dealerHand.dealCard(new BJCard("2", "S"));

        BlackjackEngine.resolvePlayerResult(playerHand, dealerHand);

        Assertions.assertEquals(BJHandState.WON, playerHand.getState());
        // Bet goes from 1000 to 2000 on a win
        Assertions.assertEquals(2000, playerHand.getBet());
    }

    /**
     * Test that if the dealer's total is higher than the player's (without busting),
     * the player loses and the bet is lost (0).
     */
    @Test
    public void dealerHand_Higher_PlayerHand_PlayerLoses_BetLost(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.setBet(1000);

        // Player total: 10 + 5 = 15
        playerHand.dealCard(new BJCard("10", "S"));
        playerHand.dealCard(new BJCard("5", "S"));

        BJDealerHand dealerHand = new BJDealerHand();
        // Dealer total: 7 + 9 + 2 = 18
        dealerHand.dealCard(new BJCard("7", "S"));
        dealerHand.dealCard(new BJCard("9", "S"));
        dealerHand.dealCard(new BJCard("2", "S"));

        BlackjackEngine.resolvePlayerResult(playerHand, dealerHand);

        Assertions.assertEquals(BJHandState.LOST, playerHand.getState());
        Assertions.assertEquals(0, playerHand.getBet());
    }

    /**
     * Test that if the dealer and player have the same total
     * (and are not busted), the result is PUSH, and the player's bet remains.
     */
    @Test
    public void dealerHand_And_PlayerHand_Push_BetRemains(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.setBet(1000);

        // Player total: 10 + 7 = 17
        playerHand.dealCard(new BJCard("10", "S"));
        playerHand.dealCard(new BJCard("7", "S"));

        BJDealerHand dealerHand = new BJDealerHand();
        // Dealer total: 7 + 9 + A => 7 + 9 + 1 = 17 (depending on your logic)
        dealerHand.dealCard(new BJCard("7", "S"));
        dealerHand.dealCard(new BJCard("9", "S"));
        dealerHand.dealCard(new BJCard("A", "S"));

        BlackjackEngine.resolvePlayerResult(playerHand, dealerHand);

        // Should be a push
        Assertions.assertEquals(BJHandState.PUSH, playerHand.getState());
        // Bet remains at 1000
        Assertions.assertEquals(1000, playerHand.getBet());
    }

    /**
     * Test that if both the dealer and player have Blackjack,
     * it results in a push and the player's bet remains unchanged.
     */
    @Test
    public void dealerHand_And_PlayerHand_PushWithBlackjack_BetRemains(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.setBet(1000);

        // Player: 10 + Ace => Blackjack
        playerHand.dealCard(new BJCard("10", "S"));
        playerHand.dealCard(new BJCard("A", "S"));

        BJDealerHand dealerHand = new BJDealerHand();
        // Dealer: K + Ace => Blackjack
        dealerHand.dealCard(new BJCard("K", "S"));
        dealerHand.dealCard(new BJCard("A", "S"));

        BlackjackEngine.resolvePlayerResult(playerHand, dealerHand);

        // Should be a push
        Assertions.assertEquals(BJHandState.PUSH, playerHand.getState());
        // Original bet remains
        Assertions.assertEquals(1000, playerHand.getBet());
    }

    /**
     * Test that if both the dealer and player are busted,
     * the player's result is LOST. (Depending on your game logic,
     * a "tie" with busts might not be a typical push.)
     */
    @Test
    public void dealerHand_And_PlayerHand_PushWithBust_BetLost(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.setBet(1000);

        // Player total: 10 + 7 + 7 = 24 (bust)
        playerHand.dealCard(new BJCard("10", "S"));
        playerHand.dealCard(new BJCard("7", "S"));
        playerHand.dealCard(new BJCard("7", "S"));

        BJDealerHand dealerHand = new BJDealerHand();
        // Dealer total: 7 + 9 + 8 = 24 (bust)
        dealerHand.dealCard(new BJCard("7", "S"));
        dealerHand.dealCard(new BJCard("9", "S"));
        dealerHand.dealCard(new BJCard("8", "S"));

        BlackjackEngine.resolvePlayerResult(playerHand, dealerHand);

        // Per the game logic, the player is marked as LOST
        // if both bust.
        Assertions.assertEquals(BJHandState.LOST, playerHand.getState());
        Assertions.assertEquals(0, playerHand.getBet());
    }

    /**
     * Test that if the player has a natural Blackjack,
     * the payoff is typically 1.5 times (or 3:2) the initial bet.
     * Here, 1000 becomes 2500.
     */
    @Test
    public void playerHand_Blackjack_BetTimes1_5(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.setBet(1000);

        // 10 + Ace => Blackjack
        playerHand.dealCard(new BJCard("10", "S"));
        playerHand.dealCard(new BJCard("A", "S"));

        BJDealerHand dealerHand = new BJDealerHand();
        // Dealer has 7 + 9 = 16
        dealerHand.dealCard(new BJCard("7", "S"));
        dealerHand.dealCard(new BJCard("9", "S"));

        BlackjackEngine.resolvePlayerResult(playerHand, dealerHand);

        // Player's state should be BLACKJACK
        Assertions.assertEquals(BJHandState.BLACKJACK, playerHand.getState());
        // Bet is 1.5 times original, so 1000 + (1000 * 1.5) = 2500
        Assertions.assertEquals(2500, playerHand.getBet());
    }

    /**
     * Test that if the player has insurance and the dealer
     * actually has Blackjack, the insurance bet is doubled
     * (2:1 payoff on the insurance side).
     */
    @Test
    public void playerHand_Insured_InsuranceBetTimes2(){
        BJPlayerHand playerHand = new BJPlayerHand();
        playerHand.setBet(1000);

        // Player has 10 + Ace => potential Blackjack for player as well
        playerHand.dealCard(new BJCard("10", "S"));
        playerHand.dealCard(new BJCard("A", "S"));

        BJDealerHand dealerHand = new BJDealerHand();
        // Dealer also has Ace + 10 => dealer's Blackjack
        dealerHand.dealCard(new BJCard("A", "S"));
        dealerHand.dealCard(new BJCard("10", "S"));

        // Mark the player's state as INSURED and set an insurance bet
        playerHand.setState(BJHandState.INSURED);
        playerHand.setInsuranceBet(500);

        BlackjackEngine.resolvePlayerResult(playerHand, dealerHand);

        // Player remains in INSURED state
        Assertions.assertEquals(BJHandState.INSURED, playerHand.getState());
        // Insurance bet pays 2:1 => 500 + (500 * 2) = 1500
        Assertions.assertEquals(1500, playerHand.getInsuranceBet());
    }
}
