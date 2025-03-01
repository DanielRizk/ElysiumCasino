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
import org.daniel.elysium.screens.blackjack.constants.GameActions;
import org.daniel.elysium.screens.blackjack.center.models.PlayerHandUI;

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

        gbc.gridy = 4;
        gbc.weighty = 0.50;
        playerHandPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        playerHandPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        playerHandPanel.setOpaque(false);
        PlayerHandUI playerHandUI = new PlayerHandUI();
        playerHandPanel.add(playerHandUI);
        add(playerHandPanel, gbc);


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

    public void updateBetDisplay(int bet) {
        cardLayout.show(buttonSwitcherPanel, bet > 0 ? "clear" : "hide");
    }

    public void clearActions() {
        cardLayout.show(buttonSwitcherPanel, "hide");
    }

    // Call this method to update the action buttons dynamically.
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


    public void showDealButton(boolean visible) {
        dealButton.setVisible(visible);
    }

    public boolean addDealerCard(UICard card) {
        DealerHandUI dealerHandUI = getDealerHand();
        if (dealerHandUI.addCard(card)){
            dealerHandUI.revalidate();
            dealerHandUI.repaint();
            return true;
        }
        return false;
    }

    public boolean addPlayerCard(int index, UICard card) {
        PlayerHandUI playerHandUI = (PlayerHandUI) playerHandPanel.getComponent(index);
        if(playerHandUI.addCard(card)){
            playerHandUI.getPlayerCards().revalidate();
            playerHandUI.getPlayerCards().repaint();
            return true;
        }
        return false;
    }

    public PlayerHandUI getPlayerHand(int index){
        return ((PlayerHandUI) playerHandPanel.getComponent(index));
    }

    public DealerHandUI getDealerHand(){
        return (DealerHandUI) dealerHandPanel.getComponent(0);
    }

    public List<PlayerHandUI> getPlayerHands() {
        List<PlayerHandUI> result = new ArrayList<>();
        for (Component comp : playerHandPanel.getComponents()) {
            if (comp instanceof PlayerHandUI) {
                result.add((PlayerHandUI) comp);
            }
        }
        return result;
    }

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
}


