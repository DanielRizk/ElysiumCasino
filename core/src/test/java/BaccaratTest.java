
import org.daniel.elysium.baccarat.BaccaratGameEngine;
import org.daniel.elysium.baccarat.constants.BacHandAction;
import org.daniel.elysium.baccarat.constants.BacHandState;
import org.daniel.elysium.baccarat.constants.BacHandType;
import org.daniel.elysium.baccarat.models.BacBetHand;
import org.daniel.elysium.baccarat.models.BacCard;
import org.daniel.elysium.baccarat.models.BacHand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BaccaratTest {

    /**
     * Tests the scenario where the banker has a natural 8 or 9, 
     * which should prevent the player from drawing any card 
     * (player stands automatically).
     */
    @Test
    public void playerEvaluation_BankerHas9or8() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player has a total of 4 (2 + 2).
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("2", "S"));

        // Banker has a natural 8 (K is effectively 0 in Baccarat, 
        // so K + 8 = 8 mod 10).
        bankerHand.dealCard(new BacCard("K", "S"));
        bankerHand.dealCard(new BacCard("8", "S"));

        // Evaluate the player's action when banker has 8
        BaccaratGameEngine.evaluatePlayer(bankerHand, playerHand);

        // Expect player to STAND (no draw) because banker has a natural.
        Assertions.assertEquals(BacHandAction.STAND, playerHand.getAction());
    }

    /**
     * Tests that if the banker does NOT have 8 or 9, 
     * and the player's hand total is > 5, 
     * the player stands.
     */
    @Test
    public void playerEvaluation_BankerDoesNotHave9or8_PlayerHasGreaterThan5() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player's hand: 2 + 4 = 6 total
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("4", "S"));

        // Banker has 7, which is not 8 or 9
        bankerHand.dealCard(new BacCard("K", "S")); // K is 0
        bankerHand.dealCard(new BacCard("7", "S"));

        // Player total is 6, which is > 5
        BaccaratGameEngine.evaluatePlayer(bankerHand, playerHand);

        // Player should STAND
        Assertions.assertEquals(BacHandAction.STAND, playerHand.getAction());
    }

    /**
     * Tests that if the banker does NOT have 8 or 9,
     * and the player's total is <= 5, 
     * the player draws a card.
     */
    @Test
    public void playerEvaluation_BankerDoesNotHave9or8_PlayerHasLessThanOrEquals5() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player's hand: 2 + 3 = 5
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("3", "S"));

        // Banker is 0 + 7 = 7, not 8 or 9
        bankerHand.dealCard(new BacCard("K", "S"));
        bankerHand.dealCard(new BacCard("7", "S"));

        // Player total <= 5 => draws
        BaccaratGameEngine.evaluatePlayer(bankerHand, playerHand);

        Assertions.assertEquals(BacHandAction.DRAW, playerHand.getAction());
    }

    /**
     * Tests that the banker does nothing if the player has only 2 cards. 
     * (Banker logic triggers when player has a 3rd card.)
     */
    @Test
    public void bankerEvaluation_PlayerHas2Cards() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player's hand: 2 + 5 = 7
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("5", "S"));

        // Banker: 3 + 1 = 4
        bankerHand.dealCard(new BacCard("3", "S"));
        bankerHand.dealCard(new BacCard("1", "S"));

        // With only 2 player cards, banker does not evaluate draw logic
        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);

        // Default or existing banker action is STAND
        Assertions.assertEquals(BacHandAction.STAND, bankerHand.getAction());
    }

    /**
     * Tests that if the player has 3 cards and the banker has 
     * a total <= 2, the banker draws.
     */
    @Test
    public void bankerEvaluation_PlayerHas3Cards_BankerHasLessThan3() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player's hand: 7 + 4 + 2 => 13 mod 10 => 3 (Baccarat style)
        playerHand.dealCard(new BacCard("7", "S"));
        playerHand.dealCard(new BacCard("4", "S"));
        playerHand.dealCard(new BacCard("2", "S"));

        // Banker: K + 2 => 2 total
        bankerHand.dealCard(new BacCard("K", "S")); // 0
        bankerHand.dealCard(new BacCard("2", "S")); // 2
        // => Banker total = 2

        // With banker <= 2, they draw
        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);

        Assertions.assertEquals(BacHandAction.DRAW, bankerHand.getAction());
    }

    /**
     * Tests that if the banker has 3, but the player's third card equals 8, 
     * the banker stands (per the baccarat rules).
     */
    @Test
    public void bankerEvaluation_PlayerHas3CardsAndEqual8_BankerEquals3() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player's 3 cards: 3 + 3 + 8 => 14 mod 10 => 4 or something, 
        // but the third card is specifically '8'
        playerHand.dealCard(new BacCard("3", "S"));
        playerHand.dealCard(new BacCard("3", "S"));
        playerHand.dealCard(new BacCard("8", "S"));

        // Banker: K (0) + 3 => 3
        bankerHand.dealCard(new BacCard("K", "S"));
        bankerHand.dealCard(new BacCard("3", "S"));

        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);

        // With banker=3 and player's third card=8, banker stands
        Assertions.assertEquals(BacHandAction.STAND, bankerHand.getAction());
    }

    /**
     * Tests that if the banker has 3 and the player's third card 
     * is NOT 8, the banker draws.
     */
    @Test
    public void bankerEvaluation_PlayerHas3CardsAndNotEqual8_BankerEquals3() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player's third card = A(1), not 8
        playerHand.dealCard(new BacCard("3", "S"));
        playerHand.dealCard(new BacCard("3", "S"));
        playerHand.dealCard(new BacCard("A", "S")); // value=1

        // Banker: 3 total
        bankerHand.dealCard(new BacCard("K", "S")); // 0
        bankerHand.dealCard(new BacCard("3", "S")); // 3

        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);

        // With banker=3 and player's 3rd != 8 => banker draws
        Assertions.assertEquals(BacHandAction.DRAW, bankerHand.getAction());
    }

    /**
     * Tests that if the banker has 4 and the player's third card 
     * is in [2..7], the banker draws.
     */
    @Test
    public void bankerEvaluation_PlayerThirdCardAndIn_2to7_BankerEquals4() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player's third card = 3, which is between 2 and 7
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("3", "S"));

        // Banker = 4
        bankerHand.dealCard(new BacCard("K", "S")); // 0
        bankerHand.dealCard(new BacCard("4", "S")); // 4

        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);

        // Should draw
        Assertions.assertEquals(BacHandAction.DRAW, bankerHand.getAction());
    }

    /**
     * Tests that if the banker has 4 but the player's third card 
     * is NOT in [2..7], the banker stands.
     */
    @Test
    public void bankerEvaluation_PlayerThirdCardAndNotIn_2to7_BankerEquals4() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player's third card = 1, which is outside 2..7
        playerHand.dealCard(new BacCard("K", "S"));
        playerHand.dealCard(new BacCard("K", "S"));
        playerHand.dealCard(new BacCard("1", "S"));

        // Banker = 4
        bankerHand.dealCard(new BacCard("K", "S"));
        bankerHand.dealCard(new BacCard("4", "S"));

        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);

        // Should stand
        Assertions.assertEquals(BacHandAction.STAND, bankerHand.getAction());
    }

    /**
     * Tests that if the banker has 5 and the player's third card 
     * is in [4..7], the banker draws.
     */
    @Test
    public void bankerEvaluation_PlayerThirdCardAndIn_4to7_BankerEquals5() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Third card = 4, which is in [4..7]
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("4", "S"));

        // Banker = 5
        bankerHand.dealCard(new BacCard("K", "S")); // 0
        bankerHand.dealCard(new BacCard("5", "S")); // 5

        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);

        // Banker draws
        Assertions.assertEquals(BacHandAction.DRAW, bankerHand.getAction());
    }

    /**
     * Tests that if the banker has 5 and the player's third card 
     * is NOT in [4..7], the banker stands.
     */
    @Test
    public void bankerEvaluation_PlayerThirdCardAndNotIn_4to7_BankerEquals5() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Third card = 3, which is outside [4..7]
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("3", "S"));

        // Banker = 5
        bankerHand.dealCard(new BacCard("K", "S"));
        bankerHand.dealCard(new BacCard("5", "S"));

        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);

        // Banker stands
        Assertions.assertEquals(BacHandAction.STAND, bankerHand.getAction());
    }

    /**
     * Tests that if the banker has 6 and the player's third card 
     * is in [6..7], the banker draws.
     */
    @Test
    public void bankerEvaluation_PlayerThirdCardAndIn_6to7_BankerEquals6() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Third card = 6, in [6..7]
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("6", "S"));

        // Banker = 6
        bankerHand.dealCard(new BacCard("K", "S"));
        bankerHand.dealCard(new BacCard("6", "S"));

        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);

        // Banker draws
        Assertions.assertEquals(BacHandAction.DRAW, bankerHand.getAction());
    }

    /**
     * Tests that if the banker has 6 and the player's third card
     * is NOT in [6..7], the banker stands.
     */
    @Test
    public void bankerEvaluation_PlayerThirdCardAndNotIn_6to7_BankerEquals6() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Third card = 1, not in [6..7]
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("1", "S"));

        // Banker = 6
        bankerHand.dealCard(new BacCard("K", "S"));
        bankerHand.dealCard(new BacCard("6", "S"));

        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);

        // Banker stands
        Assertions.assertEquals(BacHandAction.STAND, bankerHand.getAction());
    }

    /**
     * Tests that if the banker has 7 or higher, 
     * they stand no matter what the player's third card is.
     */
    @Test
    public void bankerEvaluation_PlayerThirdCardAny_BankerEquals7orHigher() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player draws third card
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("1", "S"));

        // Banker = 7 => stands
        bankerHand.dealCard(new BacCard("K", "S")); // 0
        bankerHand.dealCard(new BacCard("7", "S")); // 7

        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);

        Assertions.assertEquals(BacHandAction.STAND, bankerHand.getAction());
    }

    /**
     * Tests that if the player's final total is higher than the banker's, 
     * the player is marked WON and banker is LOST.
     */
    @Test
    public void handsEvaluation_PlayerWins() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player total might be (2 + 9 + 7) mod 10 = 18 mod 10 => 8
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("9", "S"));
        playerHand.dealCard(new BacCard("7", "S"));

        // Banker total might be K(0) + 6 => 6
        bankerHand.dealCard(new BacCard("K", "S"));
        bankerHand.dealCard(new BacCard("6", "S"));

        // Evaluate final result
        BaccaratGameEngine.evaluateHands(bankerHand, playerHand);

        // Player should be WON, banker LOST
        Assertions.assertEquals(BacHandState.WON, playerHand.getState());
        Assertions.assertEquals(BacHandState.LOST, bankerHand.getState());
    }

    /**
     * Tests that if the banker's total is higher than the player's, 
     * the banker is WON and the player is LOST.
     */
    @Test
    public void handsEvaluation_BankerWins() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player could be (2 + 9 + 3) => 14 => 4
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("9", "S"));
        playerHand.dealCard(new BacCard("3", "S"));

        // Banker could be K(0) + 5 => 5
        bankerHand.dealCard(new BacCard("K", "S"));
        bankerHand.dealCard(new BacCard("5", "S"));

        BaccaratGameEngine.evaluateHands(bankerHand, playerHand);

        Assertions.assertEquals(BacHandState.LOST, playerHand.getState());
        Assertions.assertEquals(BacHandState.WON, bankerHand.getState());
    }

    /**
     * Tests that if both totals are equal, 
     * the result is a TIE for both the player and banker.
     */
    @Test
    public void handsEvaluation_BankerAndPlayerTie() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        // Player: 2 + 9 + 8 => 19 => 9
        playerHand.dealCard(new BacCard("2", "S"));
        playerHand.dealCard(new BacCard("9", "S"));
        playerHand.dealCard(new BacCard("8", "S"));

        // Banker: K(0) + 2(2) + 7(7) => 9
        bankerHand.dealCard(new BacCard("K", "S"));
        bankerHand.dealCard(new BacCard("2", "S"));
        bankerHand.dealCard(new BacCard("7", "S"));

        BaccaratGameEngine.evaluateHands(bankerHand, playerHand);

        // Both TIE
        Assertions.assertEquals(BacHandState.TIE, playerHand.getState());
        Assertions.assertEquals(BacHandState.TIE, bankerHand.getState());
    }

    /**
     * Tests the payout calculation when the player hand wins 
     * and the bet is placed on the PLAYER. The bet should double 
     * or follow the multiplier (in this case: 1x).
     */
    @Test
    public void calculateResults_PlayerWon_BetHandOnPlayer() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();
        BacBetHand betHand = new BacBetHand();

        // Player WON, Banker LOST
        playerHand.setState(BacHandState.WON);
        bankerHand.setState(BacHandState.LOST);

        // The user bet on PLAYER with an initial bet of 1000
        betHand.setHandType(BacHandType.PLAYER);
        betHand.setBet(1000);

        // Evaluate the final bet result
        BaccaratGameEngine.calculateResult(bankerHand, playerHand, betHand);

        // If the multiplier is 1:1 for player, 
        // then final bet = 1000 + (1000 * 1) = 2000
        Assertions.assertEquals(2000, betHand.getBet());
        Assertions.assertEquals(BacHandState.WON, betHand.getState());
    }

    /**
     * Tests when the player hand loses (Banker wins) 
     * and the bet is on PLAYER. That bet should lose, 
     * bet = 0, final state = LOST.
     */
    @Test
    public void calculateResults_PlayerLost_BetHandOnPlayer() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();
        BacBetHand betHand = new BacBetHand();

        // Player LOST, Banker WON
        playerHand.setState(BacHandState.LOST);
        bankerHand.setState(BacHandState.WON);

        // The user bet on PLAYER with an initial 1000
        betHand.setHandType(BacHandType.PLAYER);
        betHand.setBet(1000);

        BaccaratGameEngine.calculateResult(bankerHand, playerHand, betHand);

        // Because the player lost, bet = 0
        Assertions.assertEquals(0, betHand.getBet());
        Assertions.assertEquals(BacHandState.LOST, betHand.getState());
    }

    /**
     * Tests when the banker hand wins and the bet is on BANKER. 
     * The bet is won, but often there's a 5% commission on Banker bets. 
     * This test indicates the final bet is 1950, so that suggests 
     * a 5% commission was taken (1000 + 950).
     */
    @Test
    public void calculateResults_BankerWon_BetHandOnBanker() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();
        BacBetHand betHand = new BacBetHand();

        // Banker WON, Player LOST
        playerHand.setState(BacHandState.LOST);
        bankerHand.setState(BacHandState.WON);

        // The user bet on BANKER with an initial 1000
        betHand.setHandType(BacHandType.BANKER);
        betHand.setBet(1000);

        BaccaratGameEngine.calculateResult(bankerHand, playerHand, betHand);

        // Final bet = 1950 => implies 1:1 minus 5% commission (50)
        Assertions.assertEquals(1950, betHand.getBet());
        Assertions.assertEquals(BacHandState.WON, betHand.getState());
    }

    /**
     * Tests the scenario where the Banker loses 
     * and the bet was on the BANKER. That bet should lose.
     */
    @Test
    public void calculateResults_BankerLost_BetHandOnBanker() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();
        BacBetHand betHand = new BacBetHand();

        // Player WON, Banker LOST
        playerHand.setState(BacHandState.WON);
        bankerHand.setState(BacHandState.LOST);

        // The user bet on BANKER, 1000
        betHand.setHandType(BacHandType.BANKER);
        betHand.setBet(1000);

        BaccaratGameEngine.calculateResult(bankerHand, playerHand, betHand);

        // Because Banker lost, bet goes to 0
        Assertions.assertEquals(0, betHand.getBet());
        Assertions.assertEquals(BacHandState.LOST, betHand.getState());
    }

    /**
     * Tests the scenario where it's a TIE and the user bet on TIE, 
     * which typically pays 8:1 or 9:1. Here it's 8:1 => final bet=9000 
     * from a 1000 initial bet.
     */
    @Test
    public void calculateResults_TieWon_BetHandOnTie() {
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();
        BacBetHand betHand = new BacBetHand();

        // Player TIE, Banker TIE
        playerHand.setState(BacHandState.TIE);
        bankerHand.setState(BacHandState.TIE);

        // The user bet on TIE, 1000
        betHand.setHandType(BacHandType.TIE);
        betHand.setBet(1000);

        BaccaratGameEngine.calculateResult(bankerHand, playerHand, betHand);

        // Final bet = 9000 => implies 1000 + (1000 * 8) if TIE = 8:1 payout
        Assertions.assertEquals(9000, betHand.getBet());
        Assertions.assertEquals(BacHandState.WON, betHand.getState());
    }
}

