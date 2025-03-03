package org.daniel.elysium.games.baccarat;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.blackjack.BlackjackEngine;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.games.baccarat.center.BacGameAreaPanel;
import org.daniel.elysium.games.baccarat.constants.BaccaratGameState;
import org.daniel.elysium.games.blackjack.BlackjackPanel;
import org.daniel.elysium.games.blackjack.center.BJGameAreaPanel;
import org.daniel.elysium.games.blackjack.constants.BlackjackGameState;
import org.daniel.elysium.interfaces.ChipPanelConsumer;
import org.daniel.elysium.interfaces.GameActions;
import org.daniel.elysium.interfaces.Mediator;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.models.cards.UICard;
import org.daniel.elysium.models.cards.UIDeck;
import org.daniel.elysium.models.chips.Chip;
import org.daniel.elysium.models.panels.ChipPanel;
import org.daniel.elysium.models.panels.ChipPanelUtil;
import org.daniel.elysium.models.panels.TopPanel;

import java.util.List;

public class BaccaratController implements Mediator, ChipPanelConsumer {
    // State managers
    private final StateManager stateManager;
    private BaccaratGameState state = BaccaratGameState.BET_PHASE;

    // References to subcomponents.
    private final TopPanel topPanel;
    private ChipPanel chipPanel;
    private final BacGameAreaPanel gameAreaPanel;

    // Define the game logic engine
    //private BlackjackEngine gameEngine;

    /** The minimum bet allowed in the game. */
    public static final int MIN_BET = 100;

    // Game cards creation
    Shoe<UICard> shoe = Shoe.createShoe(4, UIDeck::new);
    private List<UICard> cards = shoe.cards();

    public BaccaratController(StateManager stateManager) {
        this.stateManager = stateManager;
        // gameEngine
        this.topPanel = new TopPanel(this, stateManager);
        this.chipPanel = new ChipPanel(this, stateManager);
        this.gameAreaPanel = new BacGameAreaPanel(this, stateManager);
    }


    @Override
    public void onChipSelected(Chip chip) {

    }

    @Override
    public void onClearBet() {

    }

    @Override
    public void onDealRequested() {

    }

    @Override
    public void updateBalanceDisplay() {
        topPanel.setBalance(stateManager.getProfile().getBalance());
    }

    @Override
    public void dealInitialCards() {

    }

    @Override
    public void onActionSelected(GameActions action, int index) {

    }

    @Override
    public void returnToMainMenu() {
        if (state.ordinal() > BaccaratGameState.GAME_STARTED.ordinal()) {
            StyledConfirmDialog dialog = new StyledConfirmDialog(stateManager.getFrame(),
                    "If you exit now you will lose your bet, Continue?");
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                stateManager.switchPanel("MainMenu");
            }
        } else {
            onClearBet();
            stateManager.switchPanel("MainMenu");
        }
    }

    /*======================
        Reset
    ======================*/

    /** Protected API for the {@link BaccaratPanel} to revert to initial state when exiting */
    protected void resetScreen(){
        state = BaccaratGameState.BET_PHASE;
        chipPanel.setVisible(false);
        //gameAreaPanel.clearActions();
        //gameAreaPanel.clearHands();
        //gameEngine = new BlackjackEngine();
        ChipPanelUtil.removeChipPanel(this, stateManager);
        cards = Shoe.createShoe(4, UIDeck::new).cards();
    }

    /** Protected API for the {@link BaccaratPanel} to restart fresh and updated screen */
    protected void restartScreen(){
        ChipPanelUtil.revealChipPanel(this, stateManager);
        updateBalanceDisplay();
    }

    /*======================
        Helper methods
    ======================*/

    /**
     * Returns the top panel containing game status and return button.
     *
     * @return The {@link TopPanel} UI component.
     */
    public TopPanel getTopPanel() {
        return topPanel;
    }

    /**
     * Updates the chip selection panel.
     * @param panel new panel.
     */
    @Override
    public void setChipPanel(ChipPanel panel) {
        chipPanel = panel;
    }

    /**
     * Returns the chip selection panel.
     *
     * @return The {@link ChipPanel} UI component.
     */
    @Override
    public ChipPanel getChipPanel() {
        return chipPanel;
    }

    /**
     * Returns the game area panel containing the dealer and player hands.
     *
     * @return The {@link BacGameAreaPanel} UI component.
     */
    public BacGameAreaPanel getGameAreaPanel() {
        return gameAreaPanel;
    }
}
