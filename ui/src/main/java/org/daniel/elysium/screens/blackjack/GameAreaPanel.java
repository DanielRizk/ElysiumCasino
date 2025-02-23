package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.constants.Asset;
import org.daniel.elysium.managers.AssetManager;
import org.daniel.elysium.elements.StyledButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class GameAreaPanel extends JPanel {
    private JPanel dealerHandPanel;
    private JPanel playerHandPanel;
    private StyledButton dealButton;
    private Consumer<ActionEvent> onDealAction;

    public GameAreaPanel(Consumer<ActionEvent> onDealAction) {
        this.onDealAction = onDealAction;
        setLayout(new GridBagLayout());
        setOpaque(false);
        initializeComponents();
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Dealer Hand Panel – assign 30% of vertical space
        gbc.gridy = 0;
        gbc.weighty = 0.3;
        dealerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dealerHandPanel.setOpaque(false);
        // Give an initial preferred size, but do not set a strict minimum
        dealerHandPanel.setPreferredSize(new Dimension(600, 150));
        add(dealerHandPanel, gbc);

        // Filler panel between dealer and logo – assign 10% of vertical space
        gbc.gridy = 1;
        gbc.weighty = 0.05;
        JPanel filler1 = new JPanel();
        filler1.setOpaque(false);
        // Set a preferred size that will be proportionally scaled down if needed
        filler1.setPreferredSize(new Dimension(600, 75));
        add(filler1, gbc);

        // Logo – assign 20% of vertical space
        gbc.gridy = 2;
        gbc.weighty = 0.2;
        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(Asset.BLACKJACK, 600, 230));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(logoLabel, gbc);

        // Deal Button – assign 10% of vertical space
        gbc.gridy = 3;
        gbc.weighty = 0.1;
        dealButton = new StyledButton("DEAL", Asset.BUTTON_DB_SHARP);
        dealButton.addActionListener(onDealAction::accept);
        dealButton.setHorizontalAlignment(SwingConstants.CENTER);
        add(dealButton, gbc);

        // Filler panel between deal button and player panel – assign 5% of vertical space
        gbc.gridy = 4;
        gbc.weighty = 0.1;
        JPanel filler2 = new JPanel();
        filler2.setOpaque(false);
        filler2.setPreferredSize(new Dimension(600, 40));
        add(filler2, gbc);

        // Player Hand Panel – assign 35% of vertical space (increased weight to push it lower)
        gbc.gridy = 5;
        gbc.weighty = 0.35;
        playerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        playerHandPanel.setOpaque(false);
        playerHandPanel.setPreferredSize(new Dimension(600, 300));
        add(playerHandPanel, gbc);
    }



    // Expose methods to update card areas.
    public void clearCards() {
        dealerHandPanel.removeAll();
        playerHandPanel.removeAll();
        revalidate();
        repaint();
    }

    public void addDealerCard(ImageIcon cardIcon) {
        JLabel card = new JLabel(cardIcon);
        dealerHandPanel.add(card);
        dealerHandPanel.revalidate();
        dealerHandPanel.repaint();
    }

    public void addPlayerCard(ImageIcon cardIcon) {
        JLabel card = new JLabel(cardIcon);
        playerHandPanel.add(card);
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    public void hideDealButton() {
        dealButton.setVisible(false);
    }

    public void showDealButton(boolean visible) {
        dealButton.setVisible(visible);
    }
}
