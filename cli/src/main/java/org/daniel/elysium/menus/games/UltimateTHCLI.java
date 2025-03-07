package org.daniel.elysium.menus.games;

import org.daniel.elysium.cliUtils.CmdHelper;
import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.interfaces.MenuOptionCLI;
import org.daniel.elysium.models.CLIDeck;
import org.daniel.elysium.models.Card;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.ultimateTH.UthGameEngine;
import org.daniel.elysium.ultimateTH.constants.UthGameStage;
import org.daniel.elysium.ultimateTH.constants.UthHandState;
import org.daniel.elysium.ultimateTH.model.UthCard;
import org.daniel.elysium.ultimateTH.model.UthHand;
import org.daniel.elysium.ultimateTH.model.UthPlayerHand;
import org.daniel.elysium.user.profile.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A command-line implementation of Ultimate Texas Hold'em.
 * <p>
 * Players can play multiple hands, place bets, and follow game stages
 * including dealing community cards and handling player actions.
 * </p>
 */
public class UltimateTHCLI implements MenuOptionCLI {

    /** The minimum bet allowed in the game. */
    public static int MIN_BET = 10;

    private UserProfile profile = null;
    private final Scanner scanner;
    private List<Card> cards;

    /**
     * Constructs an instance of the Ultimate Texas Hold'em CLI.
     */
    public UltimateTHCLI() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the Ultimate Texas Hold'em game session.
     *
     * @param profile the player's profile containing balance information
     */
    @Override
    public void start(UserProfile profile) {
        this.profile = profile;
        CmdHelper.clearCMD();

        DebugPrint.println("Welcome to Ultimate Texas Hold'em!");
        DebugPrint.println("Current balance: " + profile.getBalance());
        DebugPrint.println();

        while (true) {
            UthGameStage stage = UthGameStage.START;
            Shoe<Card> shoe = Shoe.createShoe(1, CLIDeck::new);
            cards = shoe.cards();

            int numberOfHands = getNumberOfHands();
            if (numberOfHands == 0) break;

            List<UthCard> communityCards = dealCommunityCards();
            List<UthPlayerHand> gameHands = initializeGameHands(numberOfHands);
            UthHand dealerHand = dealDealerHand();

            while (stage != UthGameStage.FINAL) {
                displayCards(communityCards, gameHands, dealerHand, stage);
                handlePlayerTurns(gameHands, stage);
                switch (stage) {
                    case START -> stage = UthGameStage.FLOP;
                    case FLOP -> stage = UthGameStage.RIVER;
                    case RIVER -> {
                        stage = UthGameStage.FINAL;
                        evaluateHands(communityCards, gameHands, dealerHand);
                    }
                }
            }

            for (UthPlayerHand hand : gameHands) {
                UthGameEngine.determineGameResults(hand, dealerHand);
            }

            displayCards(communityCards, gameHands, dealerHand, stage);

            for (UthPlayerHand hand : gameHands) {
                UthGameEngine.processResults(hand, dealerHand);
                UthGameEngine.evaluateTrips(hand);
            }

            proceedToPayouts(gameHands);
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
     * Deals the community cards for the game.
     *
     * @return a list of five community cards
     */
    private List<UthCard> dealCommunityCards() {
        List<UthCard> comm = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            comm.add(getCardFromShoe());
        }
        return comm;
    }

    /**
     * Initializes the game hands for the current round.
     *
     * @param numberOfHands the number of hands the player wishes to play
     * @return a list of player hands
     */
    private List<UthPlayerHand> initializeGameHands(int numberOfHands) {
        List<UthPlayerHand> gameHands = new ArrayList<>();
        for (int i = 1; i <= numberOfHands; i++) {
            gameHands.add(createPlayerHand(i));
        }
        return gameHands;
    }

    /**
     * Creates a player's hand and places the required ante and blind bets.
     *
     * @param handNumber the current hand number
     * @return the created player hand
     */
    private UthPlayerHand createPlayerHand(int handNumber) {
        while (true) {
            DebugPrint.print("Please set your bet for hand " + handNumber + ": ");
            int bet = scanner.nextInt();
            if (isValidBet(bet * 5, profile.getBalance())) {
                profile.decreaseBalanceBy(bet * 2);
                UthPlayerHand hand = new UthPlayerHand();
                hand.setBet(bet);
                hand.dealCard(getCardFromShoe());
                hand.dealCard(getCardFromShoe());

                while (true) {
                    DebugPrint.print("Do you want to bet Trips? Yes (place your bet), No (0): ");
                    int tripsBet = scanner.nextInt();
                    if (isValidBet(tripsBet + (bet * 5), profile.getBalance())) {
                        hand.setTrips(tripsBet);
                        profile.decreaseBalanceBy(tripsBet);
                        break;
                    } else if (tripsBet == 0) {
                        break;
                    } else {
                        DebugPrint.println("Invalid bet amount. Try again.");
                    }
                }
                return hand;
            }
            DebugPrint.println("Invalid bet amount. Try again.");
        }
    }

    /**
     * Deals the dealer's hand with two initial cards.
     *
     * @return A new {@code UthHand} instance containing the dealer's two starting cards.
     */
    private UthHand dealDealerHand() {
        UthHand hand = new UthHand();
        for (int i = 0; i < 2; i++) {
            hand.dealCard(getCardFromShoe());
        }
        return hand;
    }

    /*======================
        Player actions
    ======================*/

    /**
     * Handles the turn for all player hands during the game.
     *
     * @param gameHands The list of player hands currently in play.
     * @param stage     The current game stage (START, FLOP, RIVER, or FINAL).
     */
    private void handlePlayerTurns(List<UthPlayerHand> gameHands, UthGameStage stage) {
        for (int i = 0; i < gameHands.size(); i++) {
            UthPlayerHand hand = gameHands.get(i);
            processPlayerTurn(hand, i, stage);
        }
    }

    /**
     * Processes an individual player's turn, allowing them to choose an action if they haven't bet yet.
     *
     * @param hand  The player's current hand.
     * @param i     The index of the player's hand.
     * @param stage The current game stage.
     */
    private void processPlayerTurn(UthPlayerHand hand, int i, UthGameStage stage) {
        if (hand.getPlay() == 0) {
            while (true) {
                int choice = getPlayerChoice(hand, i, stage);
                if (executePlayerAction(choice, hand, stage)) break;
            }
        }
    }

    /**
     * Prompts the player to choose an action based on the game stage and their available balance.
     *
     * @param hand  The player's current hand.
     * @param i     The index of the player's hand.
     * @param stage The current game stage.
     * @return The integer value representing the player's choice.
     */
    private int getPlayerChoice(UthPlayerHand hand, int i, UthGameStage stage) {
        DebugPrint.print("Hand" + (i + 1) + ": ");
        if (stage == UthGameStage.START) {
            if (isValidBet(hand.getAnte() * 4, profile.getBalance())) {
                DebugPrint.print("4X (1), 3X (2), Check (3), Fold (4): ");
            } else {
                DebugPrint.print("3X (1), [    ], Check (3), Fold (4): ");
            }
            return scanner.nextInt();
        } else if (stage == UthGameStage.FLOP) {
            DebugPrint.print("2X (1), [    ], Check (3), Fold (4): ");
            return scanner.nextInt();
        } else if (stage == UthGameStage.RIVER) {
            DebugPrint.print("1X (1), [    ], [       ], Fold (4): ");
            return scanner.nextInt();
        }
        return -1;
    }

    /**
     * Executes the player's chosen action based on the selected option.
     *
     * @param choice The player's choice input.
     * @param hand   The player's hand being acted upon.
     * @param stage  The current game stage.
     * @return {@code true} if the player's action was successfully executed, otherwise {@code false}.
     */
    private boolean executePlayerAction(int choice, UthPlayerHand hand, UthGameStage stage) {
        switch (choice) {
            case 1 -> {
                return handleOptionOne(hand, stage, profile);
            }
            case 2 -> {
                return handleOptionTwo(hand, stage, profile);
            }
            case 3 -> {
                return handleOptionThree(stage);
            }
            case 4 -> {
                return handleOptionFour(hand);
            }
            default -> {
                DebugPrint.println("Invalid Option, Try again!.");
                return false;
            }
        }
    }

    /**
     * Handles the player's betting action for option 1 (X4, X3, X2, X1).
     *
     * @param hand    The player's hand being acted upon.
     * @param stage   The current game stage.
     * @param profile The player's profile containing balance information.
     * @return {@code true} if the action is successfully executed, otherwise {@code false}.
     */
    private boolean handleOptionOne(UthPlayerHand hand, UthGameStage stage, UserProfile profile) {
        if (stage == UthGameStage.START) {
            if (isValidBet(hand.getAnte() * 4, profile.getBalance())) {
                hand.setPlay(hand.getAnte() * 4);
                profile.decreaseBalanceBy(hand.getAnte() * 4);
            } else {
                hand.setPlay(hand.getAnte() * 3);
                profile.decreaseBalanceBy(hand.getAnte() * 3);
            }
            return true;
        } else if (stage == UthGameStage.FLOP) {
            hand.setPlay(hand.getAnte() * 2);
            profile.decreaseBalanceBy(hand.getAnte() * 2);
            return true;
        } else if (stage == UthGameStage.RIVER) {
            hand.setPlay(hand.getAnte());
            profile.decreaseBalanceBy(hand.getAnte());
            return true;
        }
        return false;
    }

    /**
     * Handles the player's betting action for option 2 (X3 if valid).
     *
     * @param hand    The player's hand being acted upon.
     * @param stage   The current game stage.
     * @param profile The player's profile containing balance information.
     * @return {@code true} if the action is successfully executed, otherwise {@code false}.
     */
    private boolean handleOptionTwo(UthPlayerHand hand, UthGameStage stage, UserProfile profile) {
        if (stage == UthGameStage.START) {
            if (isValidBet(hand.getAnte() * 3, profile.getBalance())) {
                hand.setPlay(hand.getAnte() * 3);
                profile.decreaseBalanceBy(hand.getAnte() * 3);
            } else {
                return true;
            }
            return true;
        }
        return false;
    }

    /**
     * Handles the player's "Check" option.
     *
     * @param stage The current game stage.
     * @return {@code true} if the player can check, otherwise {@code false}.
     */
    private boolean handleOptionThree(UthGameStage stage) {
        return stage == UthGameStage.START || stage == UthGameStage.FLOP;
    }

    /**
     * Handles the player's "Fold" option, setting their hand state to FOLD.
     *
     * @param hand The player's hand being acted upon.
     * @return {@code true} since folding is always a valid action.
     */
    private boolean handleOptionFour(UthPlayerHand hand) {
        hand.setState(UthHandState.FOLD);
        hand.setPlay(-1);
        return true;
    }

    /*======================
     Evaluation and payouts
    ======================*/

    /**
     * Evaluates all player and dealer hands at the end of the game.
     *
     * @param communityCards The shared community cards in play.
     * @param gameHands      The list of all player hands.
     * @param dealerHand     The dealer's hand.
     */
    private void evaluateHands(List<UthCard> communityCards, List<UthPlayerHand> gameHands, UthHand dealerHand) {
        for (UthPlayerHand hand : gameHands) {
            if (hand.getState() != UthHandState.FOLD) {
                UthGameEngine.evaluateHand(communityCards, hand);
            }
        }
        UthGameEngine.evaluateHand(communityCards, dealerHand);
    }

    /**
     * Proceeds to the payout phase, calculating and displaying results for each player hand.
     *
     * @param gameHands The list of all player hands in the game.
     */
    private void proceedToPayouts(List<UthPlayerHand> gameHands) {
        DebugPrint.println("-----------Payouts------------");
        for (int i = 0; i < gameHands.size(); i++) {
            UthPlayerHand hand = gameHands.get(i);
            if (hand.getState() != UthHandState.FOLD) {
                profile.increaseBalanceBy(hand.getTrips());
                profile.increaseBalanceBy(hand.getAnte());
                profile.increaseBalanceBy(hand.getBlind());
                profile.increaseBalanceBy(hand.getPlay());

                DebugPrint.println("Hand" + (i + 1) + "-> Ante: " + hand.getAnte() +
                        ", Blind: " + hand.getBlind() +
                        ", Play: " + hand.getPlay() +
                        ", Trips: " + hand.getTrips() +
                        ", State: " + hand.getState());
            }
        }
        DebugPrint.println("Balance: " + profile.getBalance());
        DebugPrint.println("------------------------------");
        DebugPrint.println();
    }

    /**
     * Displays the current game state, including the dealer's hand, community cards, and player hands.
     *
     * @param communityCards The shared community cards in play.
     * @param gameHands      The list of all player hands.
     * @param dealerHand     The dealer's hand.
     * @param stage          The current game stage (START, FLOP, TURN, RIVER, or FINAL).
     */
    private void displayCards(List<UthCard> communityCards, List<UthPlayerHand> gameHands, UthHand dealerHand, UthGameStage stage) {
        CmdHelper.clearCMD();
        printDealer(dealerHand, stage);
        printCommunityCards(communityCards, stage);
        printPlayerHands(gameHands, stage);
    }

    /**
     * Prints the dealer's hand. If the game stage is FINAL, the hand is revealed; otherwise, it remains hidden.
     *
     * @param dealerHand The dealer's hand.
     * @param stage      The current game stage.
     */
    private void printDealer(UthHand dealerHand, UthGameStage stage) {
        String hand = dealerHand.getHand().toString();
        DebugPrint.println("        Dealer Cards        ");
        if (stage == UthGameStage.FINAL) {
            DebugPrint.println(" ".repeat(14 - (hand.length() / 2)) + hand);
            String combination = "Combination: " + dealerHand.getEvaluatedHand().handCombination();
            DebugPrint.println(" ".repeat(14 - (combination.length() / 2)) + combination);
        } else {
            DebugPrint.println("           [ ][ ]           ");
        }
    }

    /**
     * Prints the community cards based on the current game stage.
     * Hidden cards are displayed as [ ] until they are revealed in later stages.
     *
     * @param communityCards The shared community cards in play.
     * @param stage          The current game stage.
     */
    private void printCommunityCards(List<UthCard> communityCards, UthGameStage stage) {
        String comm;
        DebugPrint.println("------------------------------");
        if (stage == UthGameStage.START) {
            DebugPrint.println("     [ ] [ ] [ ] [ ] [ ]      ");
        } else if (stage == UthGameStage.FLOP) {
            comm = communityCards.stream().limit(3).toList().toString();
            DebugPrint.println(" ".repeat(14 - ((comm.length() + " [ ] [ ]".length()) / 2)) + comm + " [ ] [ ]");
        } else if (stage == UthGameStage.TURN) {
            comm = communityCards.stream().limit(4).toList().toString();
            DebugPrint.println(" ".repeat(14 - ((comm.length() + " [ ]".length()) / 2)) + comm + " [ ]");
        } else if (stage == UthGameStage.RIVER || stage == UthGameStage.FINAL) {
            comm = communityCards.toString();
            DebugPrint.println(" ".repeat(14 - (comm.length() / 2)) + comm);
        }
        DebugPrint.println("------------------------------");
    }

    /**
     * Prints all player hands, including their bets and the hand combinations if revealed.
     *
     * @param gameHands The list of all player hands in play.
     * @param stage     The current game stage.
     */
    private void printPlayerHands(List<UthPlayerHand> gameHands, UthGameStage stage) {
        for (int i = 0; i < gameHands.size(); i++) {
            UthPlayerHand hand = gameHands.get(i);
            DebugPrint.print("Hand" + (i + 1) + ": ");

            if (hand.getPlay() == -1) { // Folded hand
                DebugPrint.print("State: " + hand.getState());
                if (hand.getTrips() != 0) {
                    DebugPrint.print(", Trips: " + hand.getTrips());
                }
            } else { // Active hand
                DebugPrint.print(hand.getHand() + " -> Ante = " + hand.getAnte() +
                        " , Blind = " + hand.getBlind() + " , Play = " + hand.getPlay());
                if (hand.getTrips() != 0) {
                    DebugPrint.print(" , Trips = " + hand.getTrips());
                }
                if (stage.ordinal() >= UthGameStage.RIVER.ordinal() && hand.getEvaluatedHand() != null) {
                    DebugPrint.print(", Combination: " + hand.getEvaluatedHand().handCombination());
                }
            }
            DebugPrint.println();
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
     * Retrieves the next card from the shoe.
     *
     * @return the next card as a {@code UthCard}
     */
    private UthCard getCardFromShoe() {
        Card card = cards.remove(0);
        return new UthCard(card.getRank(), card.getSuit());
    }
}
