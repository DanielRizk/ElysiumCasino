package org.daniel.elysium.games.blackjack;

import org.daniel.elysium.models.Chip;
import org.daniel.elysium.games.blackjack.constants.GameActions;

/**
 * The {@code BlackjackMediator} interface defines a contract for coordinating interactions
 * between the Blackjack UI and the game logic.
 */
public interface BlackjackMediator {

    /**
     * Handles the selection of a betting chip.
     *
     * @param chip The selected {@link Chip} to be added to the bet.
     */
    void onChipSelected(Chip chip);

    /**
     * Clears the current bet, removing all placed chips.
     */
    void onClearBet();

    /**
     * Initiates a new round of Blackjack by requesting the deal of initial cards.
     */
    void onDealRequested();

    /**
     * Updates the displayed balance to reflect the latest player funds.
     */
    void updateBalanceDisplay();

    /**
     * Deals the initial two cards to the dealer and player hands.
     */
    void dealInitialCards();

    /**
     * Handles a player action (e.g., Hit, Stand, Double, Split).
     *
     * @param action The selected {@link GameActions} to be performed.
     * @param index  The index of the player's hand (for cases like splits).
     */
    void onActionSelected(GameActions action, int index);

    /**
     * Returns the player to the main menu.
     */
    void returnToMainMenu();
}
