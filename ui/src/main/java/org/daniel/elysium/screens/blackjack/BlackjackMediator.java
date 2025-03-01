package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.models.Chip;
import org.daniel.elysium.screens.blackjack.constants.GameActions;

/** Mediator interface for coordinating UI events. */
public interface BlackjackMediator {
    void onChipSelected(Chip chip);
    void onClearBet();
    void onDealRequested();
    void updateBalanceDisplay();
    void dealInitialCards();
    void onActionSelected(GameActions action, int index);
    void returnToMainMenu();
}

