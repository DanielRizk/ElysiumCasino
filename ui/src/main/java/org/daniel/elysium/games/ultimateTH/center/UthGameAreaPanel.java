package org.daniel.elysium.games.ultimateTH.center;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.games.ultimateTH.center.models.UthBetPanel;
import org.daniel.elysium.games.ultimateTH.center.models.UthCommunityCardsPanel;
import org.daniel.elysium.games.ultimateTH.center.models.UthDealerHandPanel;
import org.daniel.elysium.games.ultimateTH.center.models.UthPlayerHandPanel;
import org.daniel.elysium.games.ultimateTH.constants.UthActions;
import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
import org.daniel.elysium.interfaces.Mediator;
import org.daniel.elysium.models.chips.BetCircle;
import org.daniel.elysium.models.chips.Chip;
import org.daniel.elysium.ultimateTH.constants.UthHandCombination;
import org.daniel.elysium.ultimateTH.constants.UthHandState;
import org.daniel.elysium.ultimateTH.constants.UthTripsState;
import org.daniel.elysium.ultimateTH.model.UthCard;
import org.daniel.elysium.ultimateTH.model.UthHand;
import org.daniel.elysium.ultimateTH.model.UthPlayerHand;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * GameAreaPanel represents the main play area where dealer, player and community cards are displayed.
 * It also handles buttons for game actions like betting, dealing, and folding.
 */
public class UthGameAreaPanel extends JPanel {
    private final Mediator mediator;
    private final UthDealerHandPanel dealerHandPanel;
    private final UthCommunityCardsPanel communityCardsPanel;
    private final UthPlayerHandPanel playerHandPanel;
    private final UthBetPanel betPanel;
    private final StyledButton dealButton;
    private final JPanel buttonSwitcherPanel;
    private final CardLayout cardLayout;
    private final JPanel actionButtonsPanel;

    /**
     * Constructs the main Ultimate_TH play area, initializing UI elements.
     *
     * @param mediator   The mediator handling communication between UI and logic.
     * @param stateManager The state manager tracking the game's state.
     */
    public UthGameAreaPanel(Mediator mediator, StateManager stateManager) {
        this.mediator = mediator;
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Define layout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Dealer Hand Panel
        gbc.gridy = 0;
        gbc.weighty = 0.10;
        dealerHandPanel = new UthDealerHandPanel();
        dealerHandPanel.setOpaque(false);
        add(dealerHandPanel, gbc);

        // Community cards Panel
        gbc.gridy = 1;
        gbc.weighty = 0.10;
        communityCardsPanel = new UthCommunityCardsPanel();
        add(communityCardsPanel, gbc);

        // Deal Button
        gbc.gridy = 2;
        gbc.weighty = 0.10;
        JPanel dealButtonContainer = new JPanel(new BorderLayout());
        dealButtonContainer.setOpaque(false);
        dealButton = new StyledButton("DEAL", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        dealButton.setHorizontalAlignment(SwingConstants.CENTER);
        dealButton.setVisible(false);
        dealButton.addActionListener(e -> mediator.onDealRequested());
        dealButtonContainer.add(dealButton, BorderLayout.CENTER);
        add(dealButtonContainer, gbc);

        // Filler Panel (Spacer)
        gbc.gridy = 3;
        gbc.weighty = 0.10;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        add(filler, gbc);

        // Player Hand Panel
        gbc.gridy = 4;
        gbc.weighty = 0.10;
        playerHandPanel = new UthPlayerHandPanel();
        add(playerHandPanel, gbc);

        // Betting Panel
        gbc.gridy = 5;
        gbc.weighty = 0.40;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        betPanel = new UthBetPanel();
        add(betPanel, gbc);

        // Action Buttons Panel
        gbc.gridy = 6;
        gbc.weighty = 0.20;
        cardLayout = new CardLayout();
        buttonSwitcherPanel = new JPanel(cardLayout);
        buttonSwitcherPanel.setOpaque(false);

        // Clear Bet Button Panel
        JPanel clearButtonPanel = new JPanel(new BorderLayout());
        clearButtonPanel.setOpaque(false);
        StyledButton clearBetButton = new StyledButton("Clear bet");
        clearBetButton.addActionListener(e -> mediator.onClearBet());
        clearButtonPanel.add(clearBetButton, BorderLayout.CENTER);
        buttonSwitcherPanel.add(clearButtonPanel, "clear");

        // New game Button Panel
        JPanel newGameButtonPanel = new JPanel(new BorderLayout());
        newGameButtonPanel.setOpaque(false);
        StyledButton newGameButton = new StyledButton("Continue");
        newGameButton.addActionListener(e -> mediator.startNewGame());
        newGameButtonPanel.add(newGameButton, BorderLayout.CENTER);
        buttonSwitcherPanel.add(newGameButtonPanel, "continue");

        // Action Buttons Panel
        actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionButtonsPanel.setOpaque(false);
        buttonSwitcherPanel.add(actionButtonsPanel, "action");

        // Empty Panel (Hidden State)
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);
        buttonSwitcherPanel.add(emptyPanel, "hide");
        cardLayout.show(buttonSwitcherPanel, "hide");

        add(buttonSwitcherPanel, gbc);
    }

    /* ======================
       Hand Management Methods
       ====================== */

    /**
     * Returns the panel displaying the player's hand.
     *
     * @return the {@code UthPlayerHandPanel} instance
     */
    public UthPlayerHandPanel getPlayerHandPanel(){
        return playerHandPanel;
    }

    /**
     * Returns the player's hand.
     *
     * @return the {@code UthPlayerHand} instance
     */
    public UthPlayerHand getPlayerHand(){
        return playerHandPanel.getHand();
    }

    /**
     * Returns the panel displaying the dealer's hand.
     *
     * @return the {@code UthDealerHandPanel} instance
     */
    public UthDealerHandPanel getDealerHandPanel(){
        return dealerHandPanel;
    }

    /**
     * Returns the dealer's hand.
     *
     * @return the {@code UthHand} instance representing the dealer's hand
     */
    public UthHand getDealerHand(){
        return dealerHandPanel.getHand();
    }

    /**
     * Returns the panel displaying the community cards.
     *
     * @return the {@code UthCommunityCardsPanel} instance
     */
    public UthCommunityCardsPanel getCommunityCardsPanel(){
        return communityCardsPanel;
    }

    /**
     * Returns the list of community cards.
     *
     * @return a {@code List<UthCard>} containing the community cards
     */
    public List<UthCard> getCommunityCards(){
        return communityCardsPanel.getCards();
    }

    /* ======================
       Game Actions
       ====================== */

    /**
     * Adds a card to the player's hand.
     *
     * @param cardUI the card to be added to the player's hand
     */
    public void addPlayerCard(UthCardUI cardUI){
        playerHandPanel.addCard(cardUI);
    }

    /**
     * Adds a card to the dealer's hand.
     *
     * @param cardUI the card to be added to the dealer's hand
     */
    public void addDealerCard(UthCardUI cardUI){
        dealerHandPanel.addCard(cardUI);
    }

    /**
     * Adds community cards to the game area.
     * <p>
     * Ensures that exactly five cards are provided and assigns them to the
     * respective community card positions (Flop, Turn, and River).
     * </p>
     *
     * @param cards a list of {@code UthCardUI} representing the community cards
     * @throws AssertionError if the list does not contain at least five cards
     */
    public void addCommunityCard(List<UthCardUI> cards){
        assert cards.size() >= 5;
        communityCardsPanel.addFlop1(cards.get(0));
        communityCardsPanel.addFlop2(cards.get(1));
        communityCardsPanel.addFlop3(cards.get(2));
        communityCardsPanel.addTurn(cards.get(3));
        communityCardsPanel.addRiver(cards.get(4));
    }

    /**
     * Clears all cards from the player's hand, dealer's hand, and community cards.
     */
    public void clearCards(){
        dealerHandPanel.removeCards();
        communityCardsPanel.removeCards();
        playerHandPanel.removeCards();
    }

    /* ======================
       Button Visibility
       ====================== */

    /**
     * Updates the available action buttons dynamically based on the provided actions.
     *
     * @param availableActions A map containing available {@link UthActions} and their respective hand indices.
     */
    public void updateActionButtons(Map<UthActions, Integer> availableActions) {
        // Clear existing buttons
        actionButtonsPanel.removeAll();

        // Add new buttons based on the available actions
        if (availableActions != null && !availableActions.isEmpty()) {
            availableActions.forEach((action, index) -> {
                StyledButton button = new StyledButton(action.toString());
                button.addActionListener(e -> mediator.onActionSelected(action, index));
                actionButtonsPanel.add(button);
            });

            // Ensure the panel layout updates after adding new buttons
            actionButtonsPanel.revalidate();
            actionButtonsPanel.repaint();

            // Show the action button panel
            cardLayout.show(buttonSwitcherPanel, "action");
        } else {
            // Hide actions if none are available
            clearActions();
        }
    }

    /**
     * Toggles the visibility of the deal button.
     *
     * @param visible {@code true} to show the button, {@code false} to hide it.
     */
    public void showDealButton(boolean visible) {
        dealButton.setVisible(visible);
    }

    /**
     * Toggles the visibility of the clear bet button.
     *
     * @param visible {@code true} to show the clear bet button, {@code false} to hide it.
     */
    public void showClearBetButton(boolean visible) {
        cardLayout.show(buttonSwitcherPanel, visible ? "clear" : "hide");
    }

    /**
     * Toggles the visibility of the new game button.
     *
     * @param visible {@code true} to show the new game button, {@code false} to hide it.
     */
    public void showNewGameButton(boolean visible) {
        cardLayout.show(buttonSwitcherPanel, visible ? "continue" : "hide");
    }

    /**
     * Hides all action buttons by switching to an empty panel.
     */
    public void clearActions() {
        cardLayout.show(buttonSwitcherPanel, "hide");
    }

    /* ======================
        Chips handling
       ====================== */

    /**
     * Adds a chip to the betting panel.
     *
     * @param chip the {@code Chip} to be added
     */
    public void addChip(Chip chip){
        betPanel.addChip(chip);
    }

    /**
     * Adds play chips based on the specified action, X4, X3 ... etc.
     *
     * @param actions the {@code UthActions} representing the player's action
     */
    public void addPlayChips(UthActions actions){
        betPanel.addPlayChip(actions);
    }

    /**
     * Clears all chips from the betting panel, including Trips, Ante, Blind, and Play chips.
     */
    public void clearAllChips(){
        betPanel.clearTripsChips();
        betPanel.clearAnteChips();
        betPanel.clearBlindChips();
        betPanel.clearPlayChips();
    }

    /* ======================
         UI updates
       ====================== */

    /**
     * Updates the displayed total bet amount.
     *
     * @param bet the updated bet amount to display
     */
    public void updateBetDisplay(int bet){
        betPanel.updateBetDisplay(bet);
    }

    /**
     * Updates the displayed Trips bet amount.
     *
     * @param bet the updated Trips bet amount to display
     */
    public void updateTripsDisplay(int bet){
        betPanel.updateTripsDisplay(bet);
    }

    /**
     * Updates the displayed Play bet amount.
     *
     * @param bet the updated Play bet amount to display
     */
    public void updatePlayDisplay(int bet){
        betPanel.updatePlayDisplay(bet);
    }

    /* ======================
         Result display
       ====================== */

    /**
     * Displays the result of the player's hand based on its final state.
     *
     * @param state the {@code UthHandState} representing the outcome of the player's hand
     */
    public void displayResults(UthHandState state){
        betPanel.displayHandResult(state);
    }

    /**
     * Displays the blind bet multiplier based on the player's hand combination.
     *
     * @param combination the {@code UthHandCombination} determining the blind multiplier
     */
    public void displayBlindMultiplier(UthHandCombination combination){
        betPanel.displayBlindMultiplier(combination);
    }

    /**
     * Displays the Trips bet multiplier based on the player's Trips hand state.
     *
     * @param state the {@code UthTripsState} representing the Trips bet result
     */
    public void displayTripsMultiplier(UthTripsState state){
        betPanel.displayTripsMultiplier(state);
    }

    /**
     * Displays the Trips bet ante state based on the dealer's hand combination.
     *
     * @param combination the {@code UthHandCombination} representing the Trips bet result
     */
    public void displayAnteState(UthHandCombination combination){
        betPanel.displayAnteState(combination);
    }

    /* ======================
        Bet circle selection
       ====================== */

    /**
     * Checks if any bet has been selected.
     *
     * @return {@code true} if a bet is selected, {@code false} otherwise
     */
    public boolean isAnyBetSelected(){
        return betPanel.getSelectedBet() != null;
    }

    /**
     * Returns the currently selected bet circle.
     *
     * @return the {@code BetCircle} representing the selected bet, or {@code null} if none is selected
     */
    public BetCircle getSelectedCircle(){
        return betPanel.getSelectedBet();
    }

    /**
     * Resets all bet selections, clearing any active selections.
     */
    public void resetSelection(){
        betPanel.resetAllSelections();
    }

    /* ======================
        Getters
       ====================== */

    /**
     * Returns the betting panel.
     *
     * @return the {@code UthBetPanel} instance
     */

    public UthBetPanel getBetPanel(){
        return betPanel;
    }
}
