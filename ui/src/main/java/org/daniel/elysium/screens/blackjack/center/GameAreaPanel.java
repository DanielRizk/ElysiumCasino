package org.daniel.elysium.screens.blackjack.center;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.models.UICard;
import org.daniel.elysium.screens.blackjack.BlackjackMediator;
import org.daniel.elysium.screens.blackjack.center.models.DealerHandUI;
import org.daniel.elysium.screens.blackjack.center.models.PlayerHandUI;
import org.daniel.elysium.screens.blackjack.constants.GameActions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameAreaPanel extends JPanel {
    private final JPanel dealerHandPanel;
    private final JPanel playerHandPanel;
    private final StyledButton dealButton;
    private final BlackjackMediator mediator;

    private final JPanel buttonSwitcherPanel;
    private final CardLayout cardLayout;
    private final JPanel actionButtonsPanel;

    public GameAreaPanel(BlackjackMediator mediator, StateManager stateManager) {
        this.mediator = mediator;
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Create a grid to organize elements
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Dealer Hand Panel.
        gbc.gridy = 0;
        gbc.weighty = 0.10;
        dealerHandPanel = new JPanel(new BorderLayout());
        dealerHandPanel.setOpaque(false);
        DealerHandUI dealerHandUI = new DealerHandUI();
        dealerHandPanel.add(dealerHandUI);
        add(dealerHandPanel, gbc);

        // Logo / Rules Label.
        gbc.gridy = 1;
        gbc.weighty = 0.10;
        Dimension logoDimension = new Dimension(600, 230);
        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(BackgroundAsset.BLACKJACK_RULES, logoDimension));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(logoLabel, gbc);

        // Deal Button Container.
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

        //Filler panel between deal button and player hand.
        gbc.gridy = 3;
        gbc.weighty = 0.1;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        add(filler, gbc);

        // Player hand panel (Player hand + Bet panel).
        gbc.gridy = 4;
        gbc.weighty = 0.50;
        playerHandPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        playerHandPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        playerHandPanel.setOpaque(false);
        PlayerHandUI playerHandUI = new PlayerHandUI();
        playerHandPanel.add(playerHandUI);
        add(playerHandPanel, gbc);

        // Action buttons.
        gbc.gridy = 5;
        gbc.weighty = 0.15;
        cardLayout = new CardLayout();
        buttonSwitcherPanel = new JPanel(cardLayout);
        buttonSwitcherPanel.setOpaque(false);
        JPanel clearButtonPanel = new JPanel(new BorderLayout());
        clearButtonPanel.setOpaque(false);
        StyledButton clearBetButton = new StyledButton("Clear bet");
        clearBetButton.addActionListener(e -> mediator.onClearBet());
        clearButtonPanel.add(clearBetButton, BorderLayout.CENTER);
        buttonSwitcherPanel.add(clearButtonPanel, "clear");
        actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionButtonsPanel.setOpaque(false);
        buttonSwitcherPanel.add(actionButtonsPanel, "action");
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);
        buttonSwitcherPanel.add(emptyPanel, "hide");
        cardLayout.show(buttonSwitcherPanel, "hide");

        add(buttonSwitcherPanel, gbc);
    }

    /*======================
        Hands methods
    ======================*/

    /** Get the player hand at a specific index */
    public PlayerHandUI getPlayerHand(int index){
        return ((PlayerHandUI) playerHandPanel.getComponent(index));
    }

    /** Get the dealer hand */
    public DealerHandUI getDealerHand(){
        return (DealerHandUI) dealerHandPanel.getComponent(0);
    }

    /** Returns a list of all player's hand, should not be more than two */
    public List<PlayerHandUI> getPlayerHands() {
        List<PlayerHandUI> result = new ArrayList<>();
        for (Component comp : playerHandPanel.getComponents()) {
            if (comp instanceof PlayerHandUI) {
                result.add((PlayerHandUI) comp);
            }
        }
        return result;
    }

    /** Clears and resets the player and the dealer hands*/
    public void clearHands() {
        playerHandPanel.removeAll();
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
        playerHandPanel.add(new PlayerHandUI());

        dealerHandPanel.removeAll();
        dealerHandPanel.add(new DealerHandUI());
        dealerHandPanel.revalidate();
        dealerHandPanel.repaint();

        revalidate();
        repaint();
    }

    /*======================
        Game actions
    ======================*/

    /** Adds a card to the dealer's hand */
    public boolean addDealerCard(UICard card) {
        DealerHandUI dealerHandUI = getDealerHand();
        if (dealerHandUI.addCard(card)){
            dealerHandUI.revalidate();
            dealerHandUI.repaint();
            return true;
        }
        return false;
    }

    /** Adds a card to the player's hand */
    public boolean addPlayerCard(int index, UICard card) {
        PlayerHandUI playerHandUI = (PlayerHandUI) playerHandPanel.getComponent(index);
        if(playerHandUI.addCard(card)){
            playerHandUI.getPlayerCards().revalidate();
            playerHandUI.getPlayerCards().repaint();
            return true;
        }
        return false;
    }

    /** Splits the player's hand into two */
    public void splitHand(){
        PlayerHandUI original = getPlayerHand(0);
        PlayerHandUI split = new PlayerHandUI();
        playerHandPanel.removeAll();

        UICard secondCard = (UICard) original.getPlayerCards().getComponent(1);
        original.getPlayerCards().remove(1);
        original.getHand().getHand().remove(1);

        split.addCard(secondCard);
        List<Chip> chips = new ArrayList<>(original.getBetPanel().getChipsMain());
        for (Chip chip : chips){
            split.addChip(chip);
        }

        playerHandPanel.add(original);
        playerHandPanel.add(split);

        playerHandPanel.revalidate();
        playerHandPanel.repaint();
        revalidate();
        repaint();
    }

    /*======================
        Action buttons
    ======================*/

    /** Generates and updates the available action buttons dynamically depending on the stage the user at */
    public void updateActionButtons(Map<GameActions, Integer> availableActions) {
        actionButtonsPanel.removeAll();
        for (Map.Entry<GameActions, Integer> action : availableActions.entrySet()){
            StyledButton button = new StyledButton(action.getKey().toString());
            button.addActionListener(e -> mediator.onActionSelected(action.getKey(), action.getValue()));
            actionButtonsPanel.add(button);
        }
        actionButtonsPanel.revalidate();
        actionButtonsPanel.repaint();
        // Show the action card.
        cardLayout.show(buttonSwitcherPanel, "action");
    }

    /** Toggles the visibility of the deal button */
    public void showDealButton(boolean visible) {
        dealButton.setVisible(visible);
    }

    /** Toggles the clear bet button on if the bet is higher than 0, and off if the bet is 0 or less */
    public void showClearBetButton(boolean visible) {
        cardLayout.show(buttonSwitcherPanel, visible ? "clear" : "hide");
    }

    /** Hides all action buttons */
    public void clearActions() {
        cardLayout.show(buttonSwitcherPanel, "hide");
    }













}


