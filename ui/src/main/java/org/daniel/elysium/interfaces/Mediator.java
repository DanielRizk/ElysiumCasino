package org.daniel.elysium.interfaces;

import org.daniel.elysium.models.chips.Chip;

/**
 * The {@code Mediator} interface defines a contract for coordinating interactions
 * between the Game UI and the game logic.
 */
public interface Mediator {

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
     * Handles a player action.
     *
     * @param action The selected {@link GameActions} to be performed.
     * @param index  The index of the player's hand (for cases like splits).
     */
    void onActionSelected(GameActions action, int index);

    /**
     * Resets and starts a new game.
     */
    void startNewGame();

    /**
     * Returns the player to the main menu.
     */
    void returnToMainMenu();
}
