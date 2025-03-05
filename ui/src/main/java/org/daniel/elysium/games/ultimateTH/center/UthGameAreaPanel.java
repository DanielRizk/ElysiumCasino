package org.daniel.elysium.games.ultimateTH.center;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.games.blackjack.constants.BlackjackActions;
import org.daniel.elysium.games.ultimateTH.constants.UthActions;
import org.daniel.elysium.interfaces.Mediator;
import org.daniel.elysium.models.chips.BetCircle;
import org.daniel.elysium.models.chips.Chip;

import javax.swing.*;
import java.awt.*;
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

/*
        dealButton.setVisible(true);
        cardLayout.show(buttonSwitcherPanel, "clear");


        dealerHandPanel.addCard(new UthCardUI("A", "H", CardAsset.HA));
        communityCardsPanel.addFlop1(new UthCardUI("A", "H", CardAsset.HA));
        playerHandPanel.addCard(new UthCardUI("A", "H", CardAsset.HA));*/
    }

    /* ======================
       Button Visibility
       ====================== */


    /**
     * Updates the available action buttons dynamically based on the provided actions.
     *
     * @param availableActions A map containing available {@link BlackjackActions} and their respective hand indices.
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

    public void updateBetDisplay(int bet){
        betPanel.updateBetDisplay(bet);
    }

    public void updateTripsDisplay(int bet){
        betPanel.updateTripsDisplay(bet);
    }

    public void updatePlayDisplay(int bet){
        betPanel.updatePlayDisplay(bet);
    }

    public UthPlayerHandPanel getPlayerHand(){
        return playerHandPanel;
    }

    public UthDealerHandPanel getDealerHand(){
        return dealerHandPanel;
    }

    public void resetSelection(){
        betPanel.resetAllSelections();
    }

    public void clearAllChips(){
        betPanel.clearAllChips();
    }

    public UthCommunityCardsPanel getCommunityCards(){
        return communityCardsPanel;
    }

    /**
     * Checks if any bet is currently selected in the betting area.
     * This method is useful for validating whether a player has made a selection before allowing actions like placing a bet.
     *
     * @return true if a bet is currently selected; otherwise, false.
     */
    public boolean isAnyBetSelected(){
        return betPanel.getSelectedBet() != null;
    }

    /**
     * Retrieves the currently selected bet circle from the betting area.
     * This method provides access to the bet circle that the player has selected for placing bets.
     *
     * @return The {@link BetCircle} representing the selected betting area, or null if no bet circle is selected.
     */
    public BetCircle getSelectedCircle(){
        return betPanel.getSelectedBet();
    }

    /**
     * Adds a chip to the betting area on the game interface.
     * This method is responsible for visually representing the addition of a bet in the form of a chip
     * to the currently selected bet circle within the betting area.
     *
     * @param chip The {@link Chip} object to be added to the betting area.
     */
    public void addChip(Chip chip){
        betPanel.addChip(chip);
    }


    public void addPlayChips(UthActions actions){
        betPanel.addPlayChip(actions);
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
     * Hides all action buttons by switching to an empty panel.
     */
    public void clearActions() {
        cardLayout.show(buttonSwitcherPanel, "hide");
    }
}
