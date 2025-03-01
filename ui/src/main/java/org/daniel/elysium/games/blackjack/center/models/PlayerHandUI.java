package org.daniel.elysium.games.blackjack.center.models;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BJResultAsset;
import org.daniel.elysium.blackjack.constants.HandState;
import org.daniel.elysium.blackjack.models.PlayerHand;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.models.UICard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player's hand in the Blackjack UI.
 * Manages both the visual representation and backend logic of the player's hand.
 */
public class PlayerHandUI extends JPanel {
    private final PlayerCardsPanel playerCards;
    private final BetPanel betPanel;
    private final PlayerHand hand;

    public static final int FIRST_HAND = 0;

    /**
     * Constructs a new player hand UI.
     * Initializes the UI components and the backend player hand logic.
     */
    public PlayerHandUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Configure the player's hand panel
        playerCards = new PlayerCardsPanel();
        playerCards.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(playerCards);

        // Configure the bet panel
        betPanel = new BetPanel();
        betPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(betPanel);

        // Create the logical backend hand
        hand = new PlayerHand();
    }

    /*========================
      Fields getter methods
     ========================*/

    /**
     * Gets the player cards panel (UI representation).
     *
     * @return The {@link PlayerCardsPanel} instance.
     */
    public PlayerCardsPanel getPlayerCards() {
        return playerCards;
    }

    /**
     * Gets the player bet panel (UI representation).
     *
     * @return The {@link BetPanel} instance.
     */
    public BetPanel getBetPanel() {
        return betPanel;
    }

    /**
     * Gets the backend implementation of the player's hand.
     *
     * @return The {@link PlayerHand} instance.
     */
    public PlayerHand getHand() {
        return hand;
    }

    /*========================
      Bet setters and getters
     ========================*/

    /**
     * Gets the player's main bet amount.
     *
     * @return The bet amount.
     */
    public int getBet() {
        return hand.getBet();
    }

    /**
     * Gets the player's insurance bet amount.
     *
     * @return The insurance bet amount.
     */
    public int getInsuranceBet() {
        return hand.getInsuranceBet();
    }

    /**
     * Sets the player's main bet.
     *
     * @param bet The bet amount.
     */
    public void setBet(int bet) {
        hand.setBet(bet);
    }

    /**
     * Sets the player's insurance bet.
     *
     * @param insuranceBet The insurance bet amount.
     */
    public void setInsuranceBet(int insuranceBet) {
        hand.setInsuranceBet(insuranceBet);
    }

    /*========================
      Cards methods
     ========================*/

    /**
     * Adds a card to the player's UI and backend hand.
     *
     * @param uiCard The card to add.
     * @return {@code true} if the card was successfully added, {@code false} otherwise.
     */
    public boolean addCard(UICard uiCard) {
        if (hand.canDealCard(uiCard.getCard())) {
            hand.dealCard(uiCard.getCard());
            playerCards.addCard(uiCard);
            return true;
        }
        return false;
    }

    /**
     * Toggles the highlight effect on the player's hand.
     *
     * @param highlight {@code true} to enable glow, {@code false} to disable.
     */
    public void setHighlight(boolean highlight) {
        playerCards.setHighlight(highlight);
    }

    /*========================
      Chips and bet methods
     ========================*/

    /**
     * Checks if the bet panel has reached the maximum limit of chips.
     *
     * @return {@code true} if the player can add more chips, {@code false} otherwise.
     */
    public boolean canAddChip() {
        return betPanel.canAddChip();
    }

    /**
     * Adds a chip to the main bet and updates the display.
     *
     * @param chip The chip to add.
     */
    public void addChip(Chip chip) {
        betPanel.addChipMain(chip);
        setBet(getBet() + chip.getValue());
        updateBetDisplay(getBet());
    }

    /**
     * Duplicates the chips on the hand bet panel as part of a double-down bet.
     */
    public void addDoubleChip() {
        List<Chip> chips = new ArrayList<>(getBetPanel().getChipsMain());
        for (Chip chip : chips) {
            getBetPanel().addChipExtra(chip);
        }
        setBet(getBet() * 2);
        updateBetDisplay(getBet());
    }

    /**
     * Adds an insurance bet equivalent to half of the original bet.
     */
    public void addInsuranceBet() {
        List<Chip> chips = getBetPanel().getChipCombination((int) (getBet() * 0.5));
        for (Chip chip : chips) {
            getBetPanel().addChipExtra(chip);
        }
        setInsuranceBet((int) (getBet() * 0.5));
    }

    /**
     * Pays out winnings for a blackjack hand.
     */
    public void payBlackjackWin() {
        List<Chip> chips = getBetPanel().getChipCombination((getBet() * 3 / 5));
        for (Chip chip : chips) {
            getBetPanel().addChipMain(chip);
        }
    }

    /**
     * Pays out winnings for an insurance bet.
     */
    public void payInsurance() {
        List<Chip> chips = getBetPanel().getChipCombination((getInsuranceBet() * 2 / 3));
        for (Chip chip : chips) {
            getBetPanel().addChipExtra(chip);
        }
    }

    /**
     * Pays out winnings for a standard win.
     */
    public void payWin() {
        List<Chip> chips = new ArrayList<>(getBetPanel().getChipsMain());
        for (Chip chip : chips) {
            getBetPanel().addChipMain(chip);
        }

        chips = new ArrayList<>(getBetPanel().getChipsExtra());
        for (Chip chip : chips) {
            getBetPanel().addChipExtra(chip);
        }
    }

    /**
     * Clears the main bet and removes UI chips from the bet panel.
     */
    public void clearMainBet() {
        hand.setBet(0);
        betPanel.clearMainChips();
        updateBetDisplay(getBet());
    }

    /**
     * Clears the insurance bet and removes UI chips from the extra bet panel.
     */
    public void clearInsuranceBet() {
        hand.setInsuranceBet(0);
        betPanel.clearExtraChips();
    }

    /**
     * Clears all bets and removes UI chips from both main and extra bet panels.
     */
    public void clearChips() {
        betPanel.clearChips();
        hand.setBet(0);
        updateBetDisplay(getBet());
    }

    /*========================
      Helper methods
     ========================*/

    /**
     * Displays the hand result image based on the game state.
     */
    public void displayHandResult(){
        if (hand.getState() == HandState.BLACKJACK){
            playerCards.showOverlay(AssetManager.getScaledImage(BJResultAsset.WIN, new Dimension(300, 200)));
        } else if (hand.getState() == HandState.INSURED){
            playerCards.showOverlay(AssetManager.getScaledImage(BJResultAsset.WIN, new Dimension(300, 200)));
        } else if (hand.getState() == HandState.WON) {
            playerCards.showOverlay(AssetManager.getScaledImage(BJResultAsset.WIN, new Dimension(300, 200)));
        } else if (hand.getState() == HandState.PUSH) {
            playerCards.showOverlay(AssetManager.getScaledImage(BJResultAsset.WIN, new Dimension(300, 200)));
        } else {
            playerCards.showOverlay(AssetManager.getScaledImage(BJResultAsset.WIN, new Dimension(300, 200)));
        }
    }

    /**
     * Updates the bet display with the current bet amount.
     *
     * @param bet The updated bet amount.
     */
    private void updateBetDisplay(int bet) {
        betPanel.updateBetDisplay(bet);
    }
}
