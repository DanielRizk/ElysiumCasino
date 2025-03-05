package org.daniel.elysium.games.blackjack.center;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.assets.LogoAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.games.blackjack.center.models.BJDealerHandUI;
import org.daniel.elysium.games.blackjack.center.models.BJPlayerHandUI;
import org.daniel.elysium.games.blackjack.constants.BlackjackActions;
import org.daniel.elysium.games.blackjack.models.BJCardUI;
import org.daniel.elysium.interfaces.Mediator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GameAreaPanel represents the main play area where dealer and player hands are displayed.
 * It also handles buttons for game actions like betting, dealing, and hitting.
 */
public class BJGameAreaPanel extends JPanel {
    private final JPanel dealerHandPanel;
    private final JPanel playerHandPanel;
    private final StyledButton dealButton;
    private final Mediator mediator;
    private final JPanel buttonSwitcherPanel;
    private final CardLayout cardLayout;
    private final JPanel actionButtonsPanel;

    /**
     * Constructs the main blackjack play area, initializing UI elements.
     *
     * @param mediator   The mediator handling communication between UI and logic.
     * @param stateManager The state manager tracking the game's state.
     */
    public BJGameAreaPanel(Mediator mediator, StateManager stateManager) {
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
        dealerHandPanel = new JPanel(new BorderLayout());
        dealerHandPanel.setOpaque(false);
        dealerHandPanel.add(new BJDealerHandUI());
        add(dealerHandPanel, gbc);

        // Logo / Rules Label
        gbc.gridy = 1;
        gbc.weighty = 0.10;
        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(LogoAsset.BLACKJACK_RULES, new Dimension(600, 230)));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(logoLabel, gbc);

        // Deal Button
        gbc.gridy = 2;
        gbc.weighty = 0.05;
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
        gbc.weighty = 0.1;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        add(filler, gbc);

        // Player Hand Panel
        gbc.gridy = 4;
        gbc.weighty = 0.50;
        playerHandPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        playerHandPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        playerHandPanel.setOpaque(false);
        playerHandPanel.add(new BJPlayerHandUI());
        add(playerHandPanel, gbc);

        // Action Buttons Panel
        gbc.gridy = 5;
        gbc.weighty = 0.15;
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
    }

    /* ======================
       Hand Management Methods
       ====================== */

    /**
     * Retrieves the player hand at a specific index.
     *
     * @param index The index of the player hand.
     * @return The {@link BJPlayerHandUI} at the given index.
     */
    public BJPlayerHandUI getPlayerHand(int index) {
        return (BJPlayerHandUI) playerHandPanel.getComponent(index);
    }

    /**
     * Retrieves the dealer's hand.
     *
     * @return The {@link BJDealerHandUI} instance.
     */
    public BJDealerHandUI getDealerHand() {
        return (BJDealerHandUI) dealerHandPanel.getComponent(0);
    }

    /**
     * Returns a list of all player hands.
     *
     * @return List of {@link BJPlayerHandUI}.
     */
    public List<BJPlayerHandUI> getPlayerHands() {
        List<BJPlayerHandUI> result = new ArrayList<>();
        for (Component comp : playerHandPanel.getComponents()) {
            if (comp instanceof BJPlayerHandUI) {
                result.add((BJPlayerHandUI) comp);
            }
        }
        return result;
    }

    /**
     * Clears and resets both the dealer's and player's hands.
     */
    public void clearHands() {
        playerHandPanel.removeAll();
        playerHandPanel.add(new BJPlayerHandUI());

        dealerHandPanel.removeAll();
        dealerHandPanel.add(new BJDealerHandUI());

        revalidate();
        repaint();
    }

    /* ======================
       Game Actions
       ====================== */

    /**
     * Adds a card to the dealer's hand.
     *
     * @param card The {@link BJCardUI} to be added.
     * @return {@code true} if successfully added, otherwise {@code false}.
     */
    public boolean addDealerCard(BJCardUI card) {
        BJDealerHandUI dealerHandUI = getDealerHand();
        if (dealerHandUI.addCard(card)) {
            dealerHandUI.revalidate();
            dealerHandUI.repaint();
            return true;
        }
        return false;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param index The index of the player hand.
     * @param card  The {@link BJCardUI} to be added.
     * @return {@code true} if successfully added, otherwise {@code false}.
     */
    public boolean addPlayerCard(int index, BJCardUI card) {
        BJPlayerHandUI playerHandUI = getPlayerHand(index);
        if (playerHandUI.addCard(card)) {
            playerHandUI.revalidate();
            playerHandUI.repaint();
            return true;
        }
        return false;
    }

    /**
     * Splits the player's hand into two and handles the UI elements and the internal hand.
     * @param index The index of the player hand.
     */
    public void splitHand(int index) {
        BJPlayerHandUI original = getPlayerHand(index);
        BJPlayerHandUI split = new BJPlayerHandUI();

        boolean isSplitAces = original.getHand().isSplitAces();

        BJCardUI secondCard = (BJCardUI) original.getPlayerCards().getComponent(1);
        original.getPlayerCards().remove(1);
        original.getHand().getHand().remove(1);

        split.addCard(secondCard);
        split.getBetPanel().getChipsMain().addAll(original.getBetPanel().getChipsMain());
        split.getHand().setBet(original.getBet());
        split.getBetPanel().updateBetDisplay(split.getBet());

        original.getHand().setSplitAces(isSplitAces);
        split.getHand().setSplitAces(isSplitAces);

        playerHandPanel.add(split, index + 1);

        revalidate();
        repaint();
    }

    /* ======================
       Button Visibility
       ====================== */

    /**
     * Updates the available action buttons dynamically based on the provided actions.
     *
     * @param availableActions A map containing available {@link BlackjackActions} and their respective hand indices.
     */
    public void updateActionButtons(Map<BlackjackActions, Integer> availableActions) {
        // Clear existing buttons
        actionButtonsPanel.removeAll();

        // Add new buttons based on the available actions
        if (availableActions != null && !availableActions.isEmpty()) {
            availableActions.forEach((action, index) -> {
                if (!(action == BlackjackActions.SPLIT && getPlayerHands().size() >= 4)) {
                    StyledButton button = new StyledButton(action.toString());
                    button.addActionListener(e -> mediator.onActionSelected(action, index));
                    actionButtonsPanel.add(button);
                }
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
     * Hides all action buttons by switching to an empty panel.
     */
    public void clearActions() {
        cardLayout.show(buttonSwitcherPanel, "hide");
    }
}
