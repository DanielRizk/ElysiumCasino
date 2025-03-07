package org.daniel.elysium.menus.games;

import org.daniel.elysium.blackjack.BlackjackEngine;
import org.daniel.elysium.blackjack.constants.HandState;
import org.daniel.elysium.blackjack.models.BJCard;
import org.daniel.elysium.blackjack.models.BJHand;
import org.daniel.elysium.blackjack.models.DealerHand;
import org.daniel.elysium.blackjack.models.PlayerHand;
import org.daniel.elysium.cliUtils.CmdHelper;
import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.interfaces.MenuOptionCLI;
import org.daniel.elysium.models.CLIDeck;
import org.daniel.elysium.models.Card;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.user.profile.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class BlackjackCLI implements MenuOptionCLI {

    public static int MIN_BET = 10;

    private UserProfile profile = null;
    private final Scanner scanner;

    Shoe<Card> shoes;
    List<Card> cards;

    public BlackjackCLI() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void start(UserProfile profile) {
        this.profile = profile;
        CmdHelper.clearCMD();

        shoes = Shoe.createShoe(4, CLIDeck::new);
        cards = getCustomShoe(); //shoes.cards();

        DebugPrint.println("Welcome to BlackJack!");
        DebugPrint.println("Current balance: " + profile.getBalance());
        DebugPrint.println();
        CmdHelper.haltCMD();

        while (true) {
            if (isShoeEmpty(cards)) break;

            int numberOfHands = getNumberOfHands();
            if (numberOfHands == 0) break;

            List<BJHand> gameHands = initializeGameHands(numberOfHands);
            DealerHand dealerHand = (DealerHand) gameHands.get(gameHands.size() - 1);

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

    private boolean isShoeEmpty(List<Card> cards) {
        if (cards.size() < 15) {
            DebugPrint.println("Shoe ended, please start a new game");
            return true;
        }
        return false;
    }

    private int getNumberOfHands() {
        CmdHelper.clearCMD();

        DebugPrint.print("Enter how many hands you want to play (or 0 to quit): ");
        int numberOfHands = scanner.nextInt();

        if (numberOfHands > 5) {
            DebugPrint.println("The maximum hands you can play is 5");
            return getNumberOfHands();
        }

        return numberOfHands;
    }

    private List<BJHand> initializeGameHands(int numberOfHands) {
        List<BJHand> gameHands = new ArrayList<>();
        for (int i = 1; i <= numberOfHands; i++) {
            gameHands.add(createPlayerHand(i));
        }
        gameHands.add(new DealerHand());
        return gameHands;
    }

    private void dealInitialCards(List<BJHand> gameHands) {
        for (int i = 0; i < 2; i++) {
            for (BJHand hand : gameHands) {
                hand.dealCard(getCardFromShoe());
            }
        }
    }


    private PlayerHand createPlayerHand(int handNumber) {
        while (true) {
            DebugPrint.print("Please set your bet for hand " + handNumber + ": ");
            int bet = scanner.nextInt();
            if (isValidBet(bet, profile.getBalance())) {
                profile.decreaseBalanceBy(bet);
                PlayerHand hand = new PlayerHand();
                hand.setBet(bet);
                return hand;
            }
            DebugPrint.println("Invalid bet amount. Try again.");
        }
    }

    private boolean isValidBet(int bet, double balance) {
        return bet >= MIN_BET && bet <= balance;
    }

    private boolean checkInsurance(List<BJHand> gameHands, DealerHand dealerHand) {
        if (dealerHand.getHand().get(0).getValue() == 11) {
            return handleInsurance(gameHands, dealerHand);
        }
        return false;
    }

    private boolean handleInsurance(List<BJHand> gameHands, DealerHand dealerHand) {
        DebugPrint.println("Insurance Opened");
        for (int i = 0; i < gameHands.size() - 1; i++) {
            if (!gameHands.get(i).isBlackJack()) {
                if (isValidBet((((PlayerHand) gameHands.get(i)).getBet() / 2), profile.getBalance())) {
                    processInsuranceTurn(gameHands.get(i), i);
                }
            }
        }
        DebugPrint.println("Insurance Closed");
        boolean result = evaluateInsurance(gameHands, dealerHand);
        for (int i = 0; i < gameHands.size(); i++){
            if (!dealerHand.isBlackJack() && gameHands.get(i).getState() == HandState.INSURED) {
                DebugPrint.println("Hand" + (i + 1) + ": Insurance Lost");
            }
        }
        return result;
    }

    private boolean evaluateInsurance(List<BJHand> gameHands, DealerHand dealerHand) {
        for (BJHand hand : gameHands) {
            if (hand instanceof PlayerHand) {
                if (dealerHand.isBlackJack()) {
                    if (hand.getState() != HandState.INSURED) {
                        hand.setState(HandState.LOST);
                    }
                } else {
                    hand.setState(HandState.UNDEFINED);
                }
            }
        }
        return dealerHand.isBlackJack();
    }

    private void processInsuranceTurn(BJHand hand, int index) {
        while (true) {
            int choice = getPlayerInsuranceActionChoice(index);
            if (executeInsuranceAction(choice, hand)) break;
        }
    }

    private int getPlayerInsuranceActionChoice(int index) {
        DebugPrint.print("Hand" + (index + 1) + ": Insurance ? Yes (1), No (2): ");
        return scanner.nextInt();
    }

    private boolean executeInsuranceAction(int choice, BJHand hand) {
        PlayerHand playerHand = (PlayerHand) hand;
        switch (choice) {
            case 1 -> {
                playerHand.setInsuranceBet((playerHand.getBet() / 2));
                profile.decreaseBalanceBy((double) playerHand.getBet() / 2);
                playerHand.setState(HandState.INSURED);
                return true;
            }
            case 2 -> {
                return true;
            }
            default -> DebugPrint.println("Invalid choice. Please choose 1, 2.");
        }
        return false;
    }

    private boolean handleInitialBlackjackCheck(List<BJHand> gameHands, DealerHand dealerHand) {
        for (BJHand hand : gameHands) {
            if (hand.isBlackJack()) {
                hand.setState(HandState.BLACKJACK);
            }
        }

        if (dealerHand.isBlackJack()) {
            DebugPrint.println("Dealer has a BlackJack");
            return true;
        }
        return false;
    }


    private void handlePlayerTurns(List<BJHand> gameHands) {
        for (int i = 0; i < gameHands.size(); i++) {
            BJHand hand = gameHands.get(i);
            if (hand instanceof PlayerHand && hand.getState() != HandState.BLACKJACK) {
                processPlayerTurn((PlayerHand) hand, i, gameHands);
            }
        }
    }

    private void processPlayerTurn(PlayerHand hand, int index, List<BJHand> gameHands)  {
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

    private int getPlayerActionChoice(PlayerHand hand, int index) {
        DebugPrint.print("Hand" + (index + 1) + ": (1) Hit, (2) Stand");
        if (hand.getHand().size() <= 2 && isValidBet(hand.getBet(), profile.getBalance())) DebugPrint.print(", (3) Double Down");
        if (hand.isSplittable() && isValidBet(hand.getBet(), profile.getBalance())) DebugPrint.print(", (4) Split");
        DebugPrint.print("?: ");
        return scanner.nextInt();
    }

    private boolean executePlayerAction(int choice, PlayerHand hand, List<BJHand> gameHands) {
        switch (choice) {
            case 1 -> {
                if (hand.canDealCard(peekCardFromShoe())) {
                    hand.dealCard(getCardFromShoe());
                    displayCards(gameHands, false);
                    if (hand.getHandValue() >= 21) return true;
                } else return true;
            }
            case 2 -> {
                return true;
            }
            case 3 -> {
                return handleDoubleDown(hand, gameHands);
            }
            case 4 -> {
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

    private boolean handleDoubleDown(PlayerHand hand, List<BJHand> gameHands)  {
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

    private void handleSplit(PlayerHand hand, List<BJHand> gameHands) {
        if (isValidBet(hand.getBet(), profile.getBalance())) {
            if (!hand.isSplittable()) {
                DebugPrint.println("You cannot split this hand");
                return;
            }

            PlayerHand splitHand = new PlayerHand();
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

    private boolean isAnyPlayerStillInGame(List<BJHand> gameHands) {
        return gameHands.stream().anyMatch(hand -> hand instanceof PlayerHand && hand.getState() != HandState.LOST);
    }

    private void handleDealerTurn(DealerHand dealerHand, List<BJHand> gameHands) {
        while (dealerHand.canDealCard(peekCardFromShoe())) {
            dealerHand.dealCard(getCardFromShoe());
            displayCards(gameHands, true);
        }
    }

    void resolveGameResults(List<BJHand> gameHands, DealerHand dealerHand) {
        for (BJHand hand : gameHands) {
            if (hand instanceof PlayerHand playerHand) {
                BlackjackEngine.resolvePlayerResult(playerHand, dealerHand);
            }
        }
    }

    private void displayCards(List<BJHand> gameHands, boolean exposeDealer) {
        CmdHelper.clearCMD();
        DebugPrint.println("Your balance is: " + profile.getBalance());
        printDealerHand(gameHands.get(gameHands.size() - 1), exposeDealer);
        printPlayerHands(gameHands);
    }

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

    private void printPlayerHands(List<BJHand> gameHands) {
        AtomicInteger handNumber = new AtomicInteger(1);
        gameHands.stream().filter(hand -> hand instanceof PlayerHand).forEach(hand -> {
            DebugPrint.println("Hand " + handNumber.getAndIncrement() + ": " + hand.getHand() + " (" + hand.getHandValue() + ") ," + " Bet: " + ((PlayerHand) hand).getBet());
        });
        DebugPrint.println();
    }

    private void proceedToPayouts(List<BJHand> gameHands) {
        DebugPrint.println("-----------Payouts------------");
        for (int i = 0; i < gameHands.size(); i++) {
            BJHand hand = gameHands.get(i);
            if (hand instanceof PlayerHand playerHand) {
                if (playerHand.getState() != HandState.INSURED) {
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

    private BJCard getCardFromShoe() {
        Card card = cards.remove(0);
        return new BJCard(card.getRank(), card.getSuit());
    }

    private BJCard peekCardFromShoe() {
        Card card = cards.get(0);
        return new BJCard(card.getRank(), card.getSuit());
    }


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
