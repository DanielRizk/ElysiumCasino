package org.daniel.elysium.screens.blackjack2;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.models.UICard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

//TODO: fix deal button shifting other elements
public class GameAreaPanel extends JPanel {
    private final JPanel dealerHandPanel;
    private final JPanel playerHandPanel;
    private final JPanel dealButtonContainer;
    private final StyledButton dealButton;
    private final BlackjackMediator mediator;

    // Original card dimensions (adjust as needed)
    private final int cardOriginalWidth = 120;
    private final int cardOriginalHeight = 150;
    // The horizontal gap used in the FlowLayout
    private final int cardGap = 10;

    public GameAreaPanel(BlackjackMediator mediator) {
        this.mediator = mediator;
        setLayout(new GridBagLayout());
        setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Dealer Hand Panel.
        gbc.gridy = 0;
        gbc.weighty = 0.3;
        dealerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, cardGap, cardGap));
        dealerHandPanel.setOpaque(false);
        // Set preferred size but DO NOT force a minimum size so it can shrink
        dealerHandPanel.setPreferredSize(new Dimension(600, 150));
        dealerHandPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scaleCardsInPanel(dealerHandPanel);
            }
        });
        add(dealerHandPanel, gbc);

        // Filler panel between dealer and logo.
        gbc.gridy = 1;
        gbc.weighty = 0.05;
        JPanel filler1 = new JPanel();
        filler1.setOpaque(false);
        // Use preferred size; allow it to shrink if needed
        filler1.setPreferredSize(new Dimension(600, 75));
        add(filler1, gbc);

        // Logo / Rules Label.
        gbc.gridy = 2;
        gbc.weighty = 0.2;
        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(BackgroundAsset.BLACKJACK_RULES, 600, 230));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(logoLabel, gbc);

        // Deal Button Container.
        gbc.gridy = 3;
        gbc.weighty = 0.1;
        dealButtonContainer = new JPanel(new BorderLayout());
        dealButtonContainer.setOpaque(false);
        // Fix the height of the container so it doesn't shift other elements
        dealButtonContainer.setPreferredSize(new Dimension(600, 60));
        dealButton = new StyledButton("DEAL", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        dealButton.setHorizontalAlignment(SwingConstants.CENTER);
        // When hidden, the container still reserves space
        dealButton.setVisible(false);
        dealButton.addActionListener(e -> mediator.onDealRequested());
        dealButtonContainer.add(dealButton, BorderLayout.CENTER);
        add(dealButtonContainer, gbc);

        // Filler panel between deal button and player hand.
        gbc.gridy = 4;
        gbc.weighty = 0.1;
        JPanel filler2 = new JPanel();
        filler2.setOpaque(false);
        filler2.setPreferredSize(new Dimension(600, 40));
        add(filler2, gbc);

        // Player Hand Panel.
        gbc.gridy = 5;
        gbc.weighty = 0.35;
        playerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, cardGap, cardGap));
        playerHandPanel.setOpaque(false);
        playerHandPanel.setPreferredSize(new Dimension(600, 300));
        playerHandPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scaleCardsInPanel(playerHandPanel);
            }
        });
        add(playerHandPanel, gbc);
    }

    public void showDealButton(boolean visible) {
        dealButton.setVisible(visible);
    }

    public void addDealerCard(Component card) {
        dealerHandPanel.add(card);
        dealerHandPanel.revalidate();
        dealerHandPanel.repaint();
        scaleCardsInPanel(dealerHandPanel);
    }

    public void addPlayerCard(Component card) {
        playerHandPanel.add(card);
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
        scaleCardsInPanel(playerHandPanel);
    }

    public void exposeDealer(){
        Component[] comps = dealerHandPanel.getComponents();
        if (comps.length > 1 && comps[1] instanceof UICard secondCard) {
            secondCard.setFaceUp();
        }
    }

    /**
     * Dynamically scales card components in the given panel based on its current width.
     * Assumes each card is a JLabel with an ImageIcon.
     */
    private void scaleCardsInPanel(JPanel panel) {
        int cardCount = panel.getComponentCount();
        if (cardCount == 0) return;
        // Total horizontal gap between cards.
        int totalGap = cardGap * (cardCount - 1);
        // Available width for all cards.
        int availableWidth = panel.getWidth() - totalGap;
        if (availableWidth <= 0) return;
        // Compute new card width (do not upscale beyond original width)
        int newCardWidth = Math.min(availableWidth / cardCount, cardOriginalWidth);
        // Maintain aspect ratio.
        int newCardHeight = (int) (((double) newCardWidth / cardOriginalWidth) * cardOriginalHeight);

        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel cardLabel) {
                Icon icon = cardLabel.getIcon();
                if (icon instanceof ImageIcon imageIcon) {
                    Image scaledImage = imageIcon.getImage().getScaledInstance(newCardWidth, newCardHeight, Image.SCALE_SMOOTH);
                    cardLabel.setIcon(new ImageIcon(scaledImage));
                }
                cardLabel.setPreferredSize(new Dimension(newCardWidth, newCardHeight));
            } else {
                comp.setPreferredSize(new Dimension(newCardWidth, newCardHeight));
            }
        }
        panel.revalidate();
        panel.repaint();
    }
}



