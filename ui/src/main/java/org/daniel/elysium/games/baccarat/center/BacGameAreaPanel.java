package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.interfaces.Mediator;
import org.daniel.elysium.models.cards.UICard;
import org.daniel.elysium.models.chips.Chip;

import javax.print.attribute.standard.Media;
import javax.swing.*;
import java.awt.*;

public class BacGameAreaPanel extends JPanel {

    private final CardsAreaPanel cardsAreaPanel;
    private final BettingAreaPanel bettingAreaPanel;
    private final JPanel buttonSwitcherPanel;
    private final CardLayout cardLayout;
    private final StyledButton dealButton;
    private final Mediator mediator;

    public BacGameAreaPanel(Mediator mediator, StateManager stateManager) {
        this.mediator = mediator;
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Define layout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Cards area
        gbc.gridy = 1;
        gbc.weighty = 0.30;
        cardsAreaPanel = new CardsAreaPanel();
        add(cardsAreaPanel, gbc);

        // Deal Button
        gbc.gridy = 2;
        gbc.weighty = 0.15;
        JPanel dealButtonContainer = new JPanel(new BorderLayout());
        dealButtonContainer.setOpaque(false);
        dealButton = new StyledButton("DEAL", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        dealButton.setHorizontalAlignment(SwingConstants.CENTER);
        dealButton.setVisible(false);
        dealButton.addActionListener(e -> mediator.onDealRequested());
        dealButtonContainer.add(dealButton, BorderLayout.CENTER);
        add(dealButtonContainer, gbc);

        // Betting Area Panel (3 Rectangles: Player, Banker, Tie)
        gbc.gridy = 3;
        gbc.weighty = 0.40; // Give it a reasonable height
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        bettingAreaPanel = new BettingAreaPanel();
        add(bettingAreaPanel, gbc);


        // Action Buttons Panel
        gbc.gridy = 4;
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

        // Empty Panel (Hidden State)
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);
        buttonSwitcherPanel.add(emptyPanel, "hide");
        cardLayout.show(buttonSwitcherPanel, "hide");

        add(buttonSwitcherPanel, gbc);
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

    public void addChip(Chip chip){
        bettingAreaPanel.addChip(chip);
    }

    public void clearChips(){
        bettingAreaPanel.clearChips();
    }

    public boolean isAnyBetSelected(){
        return bettingAreaPanel.getSelectedBet() != null;
    }

    public BetBox getSelectedBox(){
        return bettingAreaPanel.getSelectedBet();
    }

    public void resetSelection(){
        bettingAreaPanel.resetAllSelections();
    }

    public void addPlayerCard(UICard card){
        cardsAreaPanel.addPlayerCard(card);
    }

    public void addBankerCard(UICard card){
        cardsAreaPanel.addBankerCard(card);
    }
}
