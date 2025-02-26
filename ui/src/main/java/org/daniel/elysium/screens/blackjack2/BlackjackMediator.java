package org.daniel.elysium.screens.blackjack2;

import org.daniel.elysium.models.Chip;

// Mediator interface for coordinating UI events.
public interface BlackjackMediator {
    void onChipSelected(Chip chip);
    void onClearBet();
    void onDealRequested();
    void updateBalanceDisplay();
    void dealInitialCards();
    void onActionSelected(GameActions action);
    void returnToMainMenu();
}

