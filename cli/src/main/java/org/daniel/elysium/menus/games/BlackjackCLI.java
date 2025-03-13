package org.daniel.elysium.menus.games;

import org.daniel.elysium.blackjack.BlackjackEngine;
import org.daniel.elysium.blackjack.constants.BJHandState;
import org.daniel.elysium.blackjack.models.BJCard;
import org.daniel.elysium.blackjack.models.BJDealerHand;
import org.daniel.elysium.blackjack.models.BJHand;
import org.daniel.elysium.blackjack.models.BJPlayerHand;
import org.daniel.elysium.cliUtils.CmdHelper;
import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.interfaces.MenuOptionCLI;
import org.daniel.elysium.models.Card;
import org.daniel.elysium.models.LetterDeck;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.models.SymbolicDeck;
import org.daniel.elysium.user.profile.UserProfile;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Command-line interface (CLI) implementation for a Blackjack game.
 * <p>
 * This class manages game flow, including player hands, dealer actions,
 * betting, insurance, and payouts.
 * </p>
 */
public class BlackjackCLI implements MenuOptionCLI {

    /** The minimum bet allowed in the game. */
    public static int MIN_BET = 10;

    private UserProfile profile = null;
    private final Scanner scanner;
    private List<Card> cards;
    private final String encoding;

    /**
     * Initializes the Blackjack CLI with a new Scanner instance.
     */
    public BlackjackCLI() {
        this.scanner = new Scanner(System.in);
        this.encoding = Charset.defaultCharset().displayName();
    }

    /** Returns Menu's exit code, default 0 */
    @Override
    public int exitCode() {
        return 0;
    }

    /**
     * Starts the Blackjack game.
     *
     * @param profile the user's profile, containing balance and other details
     */
    @Override
    public void start(UserProfile profile) {
        this.profile = profile;
        CmdHelper.clearCMD();

        Shoe<Card> shoe;
        if ("UTF-8".equalsIgnoreCase(encoding)){
            shoe = Shoe.createShoe(4, SymbolicDeck::new);
        } else {
            shoe = Shoe.createShoe(4, LetterDeck::new);
        }
        cards = shoe.cards();

        DebugPrint.println("Welcome to BlackJack!");
        DebugPrint.println("Current balance: " + profile.getBalance());
        DebugPrint.println();

        while (true) {
            if (isShoeEmpty(cards)) break;

            int numberOfHands = getNumberOfHands();
            if (numberOfHands == 0) break;

            List<BJHand> gameHands = initializeGameHands(numberOfHands);
            BJDealerHand dealerHand = (BJDealerHand) gameHands.get(gameHands.size() - 1);

            dealInitialCards(gameHands);
            displayCards(gameHands, false);

            if (!checkInsurance(gameHands, dealerHand) && !handleInitialBlackjackCheck(gameHands, dealerHand)) {
                handlePlayerTurns(gameHands);

                if (isAnyPlayerStillInGame(gameHands)) {
                    handleDealerTurn(dealerHand, gameHands);
                }
            }

            displayCards(gameHands, true);
            resolveGameResults(gameHands, dealerHand);
            proceedToPayouts(gameHands);
        }

        CmdHelper.clearCMD();
    }

    /*======================
        Initialization
    ======================*/

    /**
     * Prompts the user to enter the number of hands they wish to play.
     *
     * @return the number of hands, or 0 to quit
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
     * Initializes player and dealer hands for the game.
     *
     * @param numberOfHands the number of player hands
     * @return a list of Blackjack hands, including the dealer's hand
     */
    private List<BJHand> initializeGameHands(int numberOfHands) {
        List<BJHand> gameHands = new ArrayList<>();
        for (int i = 1; i <= numberOfHands; i++) {
            gameHands.add(createPlayerHand(i));
        }
        gameHands.add(new BJDealerHand());
        return gameHands;
    }

    /**
     * Deals initial cards to all hands.
     *
     * @param gameHands the list of Blackjack hands
     */
    private void dealInitialCards(List<BJHand> gameHands) {
        for (int i = 0; i < 2; i++) {
            for (BJHand hand : gameHands) {
                hand.dealCard(getCardFromShoe());
            }
        }
    }

    /**
     * Creates a new player hand after verifying a valid bet.
     *
     * @param handNumber the player's hand number
     * @return a new {@code PlayerHand} with the placed bet
     */
    private BJPlayerHand createPlayerHand(int handNumber) {
        while (true) {
            DebugPrint.print("Please set your bet for hand " + handNumber + ": ");
            int bet = scanner.nextInt();
            if (isValidBet(bet, profile.getBalance())) {
                profile.decreaseBalanceBy(bet);
                BJPlayerHand hand = new BJPlayerHand();
                hand.setBet(bet);
                return hand;
            }
            DebugPrint.println("Invalid bet amount. Try again.");
        }
    }

    /*======================
        Insurance methods
    ======================*/

    /**
     * Checks if insurance is available based on the dealer's face-up card.
     * <p>
     * If the dealer's first card is an Ace (value of 11), insurance is offered.
     * </p>
     *
     * @param gameHands   the list of player and dealer hands
     * @param dealerHand  the dealer's hand
     * @return {@code true} if insurance was taken and dealer has blackjack, otherwise {@code false}
     */
    private boolean checkInsurance(List<BJHand> gameHands, BJDealerHand dealerHand) {
        if (dealerHand.getHand().get(0).getValue() == 11) {
            return handleInsurance(gameHands, dealerHand);
        }
        return false;
    }

    /**
     * Handles the insurance betting process.
     * <p>
     * Players can choose to place an insurance bet (half of their original bet) if the dealer shows an Ace.
     * If the dealer has blackjack, insured bets are paid out; otherwise, they are lost.
     * </p>
     *
     * @param gameHands   the list of player hands
     * @param dealerHand  the dealer's hand
     * @return {@code true} if the dealer has blackjack, otherwise {@code false}
     */
    private boolean handleInsurance(List<BJHand> gameHands, BJDealerHand dealerHand) {
        DebugPrint.println("Insurance Opened");
        for (int i = 0; i < gameHands.size() - 1; i++) {
            if (!gameHands.get(i).isBlackJack()) {
                if (isValidBet((((BJPlayerHand) gameHands.get(i)).getBet() / 2), profile.getBalance())) {
                    processInsuranceTurn(gameHands.get(i), i);
                }
            }
        }
        DebugPrint.println("Insurance Closed");

        boolean result = evaluateInsurance(gameHands, dealerHand);

        // Notify players if they lost the insurance bet
        for (int i = 0; i < gameHands.size(); i++) {
            if (!dealerHand.isBlackJack() && gameHands.get(i).getState() == BJHandState.INSURED) {
                DebugPrint.println("Hand" + (i + 1) + ": Insurance Lost");
            }
        }
        return result;
    }

    /**
     * Evaluates insurance bets after the dealer reveals their second card.
     * <p>
     * If the dealer has blackjack, insured bets are won, and other hands are marked as lost.
     * If the dealer does not have blackjack, insured bets are lost.
     * </p>
     *
     * @param gameHands   the list of player hands
     * @param dealerHand  the dealer's hand
     * @return {@code true} if the dealer has blackjack, otherwise {@code false}
     */
    private boolean evaluateInsurance(List<BJHand> gameHands, BJDealerHand dealerHand) {
        for (BJHand hand : gameHands) {
            if (hand instanceof BJPlayerHand) {
                if (dealerHand.isBlackJack()) {
                    if (hand.getState() != BJHandState.INSURED) {
                        hand.setState(BJHandState.LOST);
                    }
                } else {
                    hand.setState(BJHandState.UNDEFINED);
                }
            }
        }
        return dealerHand.isBlackJack();
    }

    /**
     * Handles the insurance betting turn for a player.
     * <p>
     * Prompts the player to decide whether to take insurance and processes their response.
     * </p>
     *
     * @param hand   the player's hand
     * @param index  the index of the hand in the game
     */
    private void processInsuranceTurn(BJHand hand, int index) {
        while (true) {
            int choice = getPlayerInsuranceActionChoice(index);
            if (executeInsuranceAction(choice, hand)) break;
        }
    }

    /**
     * Prompts the player to decide whether to take insurance.
     *
     * @param index the index of the player's hand
     * @return the player's choice (1 for Yes, 2 for No)
     */
    private int getPlayerInsuranceActionChoice(int index) {
        DebugPrint.print("Hand" + (index + 1) + ": Insurance? Yes (1), No (2): ");
        return scanner.nextInt();
    }

    /**
     * Executes the player's insurance decision.
     * <p>
     * If the player chooses insurance, half of their original bet is deducted from their balance.
     * </p>
     *
     * @param choice the player's choice (1 for Yes, 2 for No)
     * @param hand   the player's hand
     * @return {@code true} if the choice was valid, otherwise {@code false}
     */
    private boolean executeInsuranceAction(int choice, BJHand hand) {
        BJPlayerHand playerHand = (BJPlayerHand) hand;
        switch (choice) {
            case 1 -> {
                playerHand.setInsuranceBet((playerHand.getBet() / 2));
                profile.decreaseBalanceBy((double) playerHand.getBet() / 2);
                playerHand.setState(BJHandState.INSURED);
                return true;
            }
            case 2 -> {
                return true;
            }
            default -> DebugPrint.println("Invalid choice. Please choose 1 or 2.");
        }
        return false;
    }

    /*======================
        Blackjack methods
    ======================*/

    /**
     * Checks for an initial blackjack in both player and dealer hands.
     * <p>
     * If a hand has blackjack, it is marked as such. If the dealer has blackjack,
     * the game round will end immediately.
     * </p>
     *
     * @param gameHands  the list of player hands
     * @param dealerHand the dealer's hand
     * @return {@code true} if the dealer has blackjack, otherwise {@code false}
     */
    private boolean handleInitialBlackjackCheck(List<BJHand> gameHands, BJDealerHand dealerHand) {
        for (BJHand hand : gameHands) {
            if (hand.isBlackJack()) {
                hand.setState(BJHandState.BLACKJACK);
            }
        }

        if (dealerHand.isBlackJack()) {
            DebugPrint.println("Dealer has a BlackJack");
            return true;
        }
        return false;
    }

    /*======================
        Player actions
    ======================*/

    /**
     * Handles the turn sequence for each player.
     * <p>
     * Players take turns making actions unless they have blackjack.
     * </p>
     *
     * @param gameHands the list of player hands
     */
    private void handlePlayerTurns(List<BJHand> gameHands) {
        for (int i = 0; i < gameHands.size(); i++) {
            BJHand hand = gameHands.get(i);
            if (hand instanceof BJPlayerHand && hand.getState() != BJHandState.BLACKJACK) {
                processPlayerTurn((BJPlayerHand) hand, i, gameHands);
            }
        }
    }

    /**
     * Processes the actions of a player during their turn.
     * <p>
     * The player makes decisions such as hitting, standing, doubling down, or splitting.
     * </p>
     *
     * @param hand      the player's hand
     * @param index     the index of the player's hand
     * @param gameHands the list of all hands in play
     */
    private void processPlayerTurn(BJPlayerHand hand, int index, List<BJHand> gameHands) {
        if (!hand.isSplitAces()) {
            while (true) {
                if (hand.getHand().size() < 2) {
                    hand.dealCard(getCardFromShoe());
                    displayCards(gameHands, false);
                }
                int choice = getPlayerActionChoice(hand, index);
                if (executePlayerAction(choice, hand, gameHands)) break;
            }
        }
    }

    /**
     * Prompts the player for an action choice.
     *
     * @param hand  the player's hand
     * @param index the index of the player's hand
     * @return the chosen action (1 = Hit, 2 = Stand, 3 = Double Down, 4 = Split)
     */
    private int getPlayerActionChoice(BJPlayerHand hand, int index) {
        DebugPrint.print("Hand" + (index + 1) + ": (1) Hit, (2) Stand");
        if (hand.getHand().size() <= 2 && isValidBet(hand.getBet(), profile.getBalance())) DebugPrint.print(", (3) Double Down");
        if (hand.isSplittable() && isValidBet(hand.getBet(), profile.getBalance())) DebugPrint.print(", (4) Split");
        DebugPrint.print("?: ");
        return scanner.nextInt();
    }

    /**
     * Executes the player's chosen action.
     *
     * @param choice    the selected action
     * @param hand      the player's hand
     * @param gameHands the list of all hands in play
     * @return {@code true} if the turn should end, otherwise {@code false}
     */
    private boolean executePlayerAction(int choice, BJPlayerHand hand, List<BJHand> gameHands) {
        switch (choice) {
            case 1 -> { // Hit
                if (hand.canDealCard(peekCardFromShoe())) {
                    hand.dealCard(getCardFromShoe());
                    displayCards(gameHands, false);
                    if (hand.getHandValue() >= 21) return true; // End turn if bust or 21
                } else return true;
            }
            case 2 -> { // Stand
                return true;
            }
            case 3 -> { // Double Down
                return handleDoubleDown(hand, gameHands);
            }
            case 4 -> { // Split
                boolean splitting = hand.isSplitAces();
                handleSplit(hand, gameHands);
                if (splitting) {
                    return true;
                }
            }
            default -> DebugPrint.println("Invalid choice. Please choose 1, 2, 3, or 4.");
        }
        return false;
    }

    /**
     * Handles the player's decision to double down.
     * <p>
     * The player doubles their bet and receives only one additional card.
     * </p>
     *
     * @param hand      the player's hand
     * @param gameHands the list of all hands in play
     * @return {@code true} if the turn should end, otherwise {@code false}
     */
    private boolean handleDoubleDown(BJPlayerHand hand, List<BJHand> gameHands) {
        if (hand.getHand().size() == 2) {
            if (isValidBet(hand.getBet(), profile.getBalance())) {
                profile.decreaseBalanceBy(hand.getBet());
                hand.setBet((hand.getBet() * 2));
                hand.dealCard(getCardFromShoe());
                displayCards(gameHands, false);
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the player's decision to split their hand.
     * <p>
     * If the hand is splittable, it is divided into two separate hands,
     * and the player places an additional bet equal to their original bet.
     * </p>
     *
     * @param hand      the player's hand to be split
     * @param gameHands the list of all hands in play
     */
    private void handleSplit(BJPlayerHand hand, List<BJHand> gameHands) {
        if (isValidBet(hand.getBet(), profile.getBalance())) {
            if (!hand.isSplittable()) {
                DebugPrint.println("You cannot split this hand");
                return;
            }

            BJPlayerHand splitHand = new BJPlayerHand();
            splitHand.setBet(hand.getBet());
            profile.decreaseBalanceBy(hand.getBet());

            if (hand.isSplitAces()) {
                hand.setSplitAces(true);
                splitHand.setSplitAces(true);
            }

            BJCard secondCard = hand.getHand().remove(1);
            splitHand.dealCard(secondCard);

            ListIterator<BJHand> iterator = gameHands.listIterator(gameHands.indexOf(hand) + 1);
            iterator.add(splitHand);

            displayCards(gameHands, false);
        }
    }

    /**
     * Checks if at least one player hand is still in the game.
     *
     * @param gameHands the list of all hands in play
     * @return {@code true} if any player hand is still active, otherwise {@code false}
     */
    private boolean isAnyPlayerStillInGame(List<BJHand> gameHands) {
        return gameHands.stream().anyMatch(hand -> hand instanceof BJPlayerHand && hand.getState() != BJHandState.LOST);
    }

    /*======================
        Dealer actions
    ======================*/

    /**
     * Handles dealer actions based on game rules.
     *
     * @param dealerHand the dealer's hand
     * @param gameHands the list of player and dealer hands
     */
    private void handleDealerTurn(BJDealerHand dealerHand, List<BJHand> gameHands) {
        while (dealerHand.canDealCard(peekCardFromShoe())) {
            dealerHand.dealCard(getCardFromShoe());
            displayCards(gameHands, true);
        }
    }

    /*======================
     Evaluation and payouts
    ======================*/

    /**
     * Resolves the game results after all player and dealer actions.
     *
     * @param gameHands the list of player and dealer hands
     * @param dealerHand the dealer's hand
     */
    void resolveGameResults(List<BJHand> gameHands, BJDealerHand dealerHand) {
        for (BJHand hand : gameHands) {
            if (hand instanceof BJPlayerHand playerHand) {
                BlackjackEngine.resolvePlayerResult(playerHand, dealerHand);
            }
        }
    }

    /**
     * Displays the current hands in the game.
     *
     * @param gameHands the list of player and dealer hands
     * @param exposeDealer if true, shows the dealer's full hand
     */
    private void displayCards(List<BJHand> gameHands, boolean exposeDealer) {
        CmdHelper.clearCMD();
        DebugPrint.println("Your balance is: " + profile.getBalance());
        printDealerHand(gameHands.get(gameHands.size() - 1), exposeDealer);
        printPlayerHands(gameHands);
    }

    /**
     * Prints the dealer's hand.
     *
     * @param dealerHand the dealer's hand
     * @param exposeDealer if true, shows all dealer cards
     */
    private void printDealerHand(BJHand dealerHand, boolean exposeDealer) {
        StringBuilder dealerCard = new StringBuilder(dealerHand.getHand().get(0).toString());
        if (exposeDealer) {
            dealerHand.getHand().stream().skip(1).forEach(card -> dealerCard.append(" ").append(card));
            dealerCard.append(" (").append(dealerHand.getHandValue()).append(")");
        } else {
            dealerCard.append(" [ ]");
        }
        DebugPrint.println("\nDealer's Hand: " + dealerCard + "\n");
    }

    /**
     * Prints all player hands.
     *
     * @param gameHands the list of hands
     */
    private void printPlayerHands(List<BJHand> gameHands) {
        AtomicInteger handNumber = new AtomicInteger(1);
        gameHands.stream().filter(hand -> hand instanceof BJPlayerHand).forEach(hand -> {
            DebugPrint.println("Hand " + handNumber.getAndIncrement() + ": " + hand.getHand() + " (" + hand.getHandValue() + ") ," + " Bet: " + ((BJPlayerHand) hand).getBet());
        });
        DebugPrint.println();
    }

    /**
     * Handles the payout process based on game results.
     *
     * @param gameHands the list of player and dealer hands
     */
    private void proceedToPayouts(List<BJHand> gameHands) {
        DebugPrint.println("-----------Payouts------------");
        for (int i = 0; i < gameHands.size(); i++) {
            BJHand hand = gameHands.get(i);
            if (hand instanceof BJPlayerHand playerHand) {
                if (playerHand.getState() != BJHandState.INSURED) {
                    profile.increaseBalanceBy(playerHand.getBet());
                    DebugPrint.println("Hand" + (i + 1) + "-> Bet: " + playerHand.getBet() + ", State: " + hand.getState());
                } else {
                    profile.increaseBalanceBy(playerHand.getInsuranceBet());
                    DebugPrint.println("Hand" + (i + 1) + "-> Insurance: " + playerHand.getInsuranceBet() + ", State: " + playerHand.getState());
                }

            }
        }
        DebugPrint.println("Balance: " + profile.getBalance());
        DebugPrint.println("------------------------------");
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
     * @return the next card as a {@code BJCard}
     */
    private BJCard getCardFromShoe() {
        Card card = cards.remove(0);
        return new BJCard(card.getRank(), card.getSuit());
    }

    /**
     * Peeks at the next card in the shoe without removing it.
     *
     * @return the next card as a {@code BJCard}
     */
    private BJCard peekCardFromShoe() {
        Card card = cards.get(0);
        return new BJCard(card.getRank(), card.getSuit());
    }

    /**
     * Creates a custom shoe of predefined cards for testing.
     *
     * @return a list of pre-selected {@code Card} objects
     */
    public static List<Card> getCustomShoe(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("7", "H"));
        cards.add(new Card("A", "S"));
        cards.add(new Card("A", "H"));

        cards.add(new Card("7", "H"));
        cards.add(new Card("9", "S"));
        cards.add(new Card("A", "D"));

        cards.add(new Card("8", "H"));
        cards.add(new Card("8", "D"));

        cards.add(new Card("9", "D"));
        cards.add(new Card("10", "S"));

        cards.add(new Card("5", "H"));
        cards.add(new Card("10", "D"));
        cards.add(new Card("6", "C"));
        cards.add(new Card("K", "C"));
        cards.add(new Card("Q", "C"));
        cards.add(new Card("4", "C"));
        cards.add(new Card("4", "C"));
        cards.add(new Card("4", "C"));
        return cards;
    }
}
