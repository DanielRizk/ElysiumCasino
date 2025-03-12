package org.daniel.elysium.menus.games;

import org.daniel.elysium.baccarat.BaccaratGameEngine;
import org.daniel.elysium.baccarat.constants.BacHandAction;
import org.daniel.elysium.baccarat.constants.BacHandType;
import org.daniel.elysium.baccarat.models.BacBetHand;
import org.daniel.elysium.baccarat.models.BacCard;
import org.daniel.elysium.baccarat.models.BacHand;
import org.daniel.elysium.cliUtils.CmdHelper;
import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.interfaces.MenuOptionCLI;
import org.daniel.elysium.models.Card;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.models.SymbolicDeck;
import org.daniel.elysium.user.profile.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A command-line implementation of a Baccarat game.
 * <p>
 * Players can bet on the Banker, Player, or Tie and play multiple hands.
 * The game follows standard Baccarat rules, including automatic drawing actions.
 * </p>
 */
public class BaccaratCLI implements MenuOptionCLI {

    /** The minimum bet allowed in the game. */
    public static int MIN_BET = 100;

    private UserProfile profile = null;
    private final Scanner scanner;
    private List<Card> cards;

    /**
     * Constructs a BaccaratCLI instance.
     */
    public BaccaratCLI() {
        this.scanner = new Scanner(System.in);
    }

    /** Returns Menu's exit code, default 0 */
    @Override
    public int exitCode() {
        return 0;
    }

    /**
     * Starts the Baccarat game session.
     *
     * @param profile the player's profile containing balance information
     */
    @Override
    public void start(UserProfile profile) {
        this.profile = profile;
        CmdHelper.clearCMD();

        Shoe<Card> shoe = Shoe.createShoe(6, SymbolicDeck::new);
        cards = shoe.cards();

        DebugPrint.println("Welcome to Baccarat!");
        DebugPrint.println("Current balance: " + profile.getBalance());
        DebugPrint.println();

        while (true) {
            if (isShoeEmpty(cards)) break;

            int numberOfHands = getNumberOfHands();
            if (numberOfHands == 0) break;

            List<BacBetHand> betHands = initializeBetHands(numberOfHands);
            List<BacHand> gameHands = initializeGameHands();

            displayCards(gameHands, betHands);
            evaluateHand(gameHands, betHands);
            displayCards(gameHands, betHands);

            proceedToPayouts(betHands);
        }
        CmdHelper.clearCMD();
    }

    /*======================
        Initialization
    ======================*/

    /**
     * Gets the number of hands the player wants to play.
     *
     * @return the number of hands selected by the player
     */
    private int getNumberOfHands() {
        CmdHelper.haltCMD();
        CmdHelper.clearCMD();

        DebugPrint.print("Enter how many hands you want to play (or 0 to quit): ");
        int numberOfHands = scanner.nextInt();

        if (numberOfHands > 5) {
            DebugPrint.println("The maximum hands you can play is 5");
            return getNumberOfHands();
        }

        return numberOfHands;
    }

    /**
     * Initializes the bet hands for the current game round.
     *
     * @param numberOfHands the number of hands the player wishes to play
     * @return a list of bet hands
     */
    private List<BacBetHand> initializeBetHands(int numberOfHands) {
        List<BacBetHand> betHands = new ArrayList<>();
        for (int i = 1; i <= numberOfHands; i++) {
            betHands.add(createBetHand(i));
        }
        return betHands;
    }

    /**
     * Creates a betting hand for a specific round.
     *
     * @param handNumber the current hand number
     * @return the created bet hand
     */
    private BacBetHand createBetHand(int handNumber) {
        while (true) {
            DebugPrint.print("Banker (1), Player (2), or Tie (3) for hand " + handNumber + ": ");
            int choice = scanner.nextInt();

            BacHandType type;
            if (choice == 1) {
                type = BacHandType.BANKER;
            } else if (choice == 2) {
                type = BacHandType.PLAYER;
            } else if (choice == 3) {
                type = BacHandType.TIE;
            } else {
                DebugPrint.println("Invalid option, try again!");
                continue;
            }

            DebugPrint.print("Please set your bet for hand " + handNumber + ": ");
            int bet = scanner.nextInt();

            if (isValidBet(bet, profile.getBalance())) {
                profile.decreaseBalanceBy(bet);
                BacBetHand betHand = new BacBetHand();
                betHand.setBet(bet);
                betHand.setHandType(type);
                return betHand;
            }
            DebugPrint.println("Invalid bet amount. Try again.");
        }
    }

    /**
     * Initializes the game hands for the current round.
     *
     * @return a list containing the player and banker hands
     */
    private List<BacHand> initializeGameHands() {
        List<BacHand> gameHands = new ArrayList<>();
        BacHand playerHand = new BacHand();
        BacHand bankerHand = new BacHand();

        for (int i = 0; i < 2; i++) {
            playerHand.dealCard(getCardFromShoe());
            bankerHand.dealCard(getCardFromShoe());
        }

        gameHands.add(playerHand);
        gameHands.add(bankerHand);
        return gameHands;
    }

    /*======================
     Evaluation and payouts
    ======================*/

    /**
     * Evaluates the game hands and determines whether additional cards are drawn.
     *
     * @param gameHands the list of hands in play
     * @param betHands  the list of player bets
     */
    private void evaluateHand(List<BacHand> gameHands, List<BacBetHand> betHands) {
        BacHand playerHand = gameHands.get(0);
        BacHand bankerHand = gameHands.get(1);

        BaccaratGameEngine.evaluatePlayer(bankerHand, playerHand);
        executePlayerAction(playerHand);
        BaccaratGameEngine.evaluateBanker(bankerHand, playerHand);
        executeBankerAction(bankerHand);
        BaccaratGameEngine.evaluateHands(bankerHand, playerHand);

        for (BacBetHand betHand : betHands) {
            BaccaratGameEngine.calculateResult(bankerHand, playerHand, betHand);
        }
    }

    /**
     * Executes the player's automatic drawing action if required.
     *
     * @param hand the player's hand
     */
    private void executePlayerAction(BacHand hand) {
        if (hand.getAction() == BacHandAction.DRAW) {
            hand.dealCard(getCardFromShoe());
        }
    }

    /**
     * Executes the banker's automatic drawing action if required.
     *
     * @param hand the banker's hand
     */
    private void executeBankerAction(BacHand hand) {
        if (hand.getAction() == BacHandAction.DRAW) {
            hand.dealCard(getCardFromShoe());
        }
    }

    /**
     * Processes payouts based on game results.
     *
     * @param betHands the list of bet hands
     */
    private void proceedToPayouts(List<BacBetHand> betHands) {
        DebugPrint.println("-----------Payouts------------");
        for (int i = 0; i < betHands.size(); i++) {
            BacBetHand hand = betHands.get(i);
            profile.increaseBalanceBy(hand.getBet());
            DebugPrint.println("Hand" + (i + 1) + "-> Type: " + hand.getHandType() + ", Bet: " + hand.getBet() + ", State: " + hand.getState());
        }
        DebugPrint.println("Balance: " + profile.getBalance());
        DebugPrint.println("------------------------------");
        DebugPrint.println();
    }

    /**
     * Displays the game state, including the hands and bets.
     *
     * @param gameHands the list of hands in play
     * @param betHands  the list of player bets
     */
    private void displayCards(List<BacHand> gameHands, List<BacBetHand> betHands) {
        CmdHelper.clearCMD();
        printGameHands(gameHands);
        printBetHands(betHands);
    }

    /**
     * Prints the player's and banker's hands.
     *
     * @param gameHands the list of game hands
     */
    private void printGameHands(List<BacHand> gameHands) {
        String playerHand = gameHands.get(0).getHand().toString();
        DebugPrint.println("    (" + gameHands.get(0).getHandValue() + ") Player  |  Banker (" + gameHands.get(1).getHandValue() + ")   ");
        DebugPrint.print(" ".repeat(14 - playerHand.length()) + playerHand + "  |  ");
        DebugPrint.println(gameHands.get(1).getHand());
        DebugPrint.println("---------------------------------");
    }

    /**
     * Prints the player's betting hands.
     *
     * @param betHands the list of betting hands
     */
    private void printBetHands(List<BacBetHand> betHands) {
        for (int i = 0; i < betHands.size(); i++) {
            BacBetHand hand = betHands.get(i);
            DebugPrint.println("Hand" + (i + 1) + ": " + hand.getHandType() + " -> " + hand.getBet());
        }
        DebugPrint.println();
        DebugPrint.println();
    }

    /*======================
        Helper methods
    ======================*/

    /**
     * Checks if a given bet is valid.
     *
     * @param bet the bet amount
     * @param balance the user's available balance
     * @return true if the bet is within valid limits, false otherwise
     */
    private boolean isValidBet(int bet, double balance) {
        return bet >= MIN_BET && bet <= balance;
    }

    /**
     * Checks if the shoe has enough cards for gameplay.
     *
     * @param cards the deck of remaining cards
     * @return true if the shoe has less than 15 cards, false otherwise
     */
    private boolean isShoeEmpty(List<Card> cards) {
        if (cards.size() < 15) {
            DebugPrint.println("Shoe ended, please start a new game");
            return true;
        }
        return false;
    }

    /**
     * Retrieves the next card from the shoe.
     *
     * @return the next card as a {@code BacCard}
     */
    private BacCard getCardFromShoe() {
        Card card = cards.remove(0);
        return new BacCard(card.getRank(), card.getSuit());
    }
}
