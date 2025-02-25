package org.daniel.elysium.screens.blackjack2;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.*;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.models.Card;
import org.daniel.elysium.models.Chip;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.screens.blackjack.BetCircle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class BlackjackPanel extends JPanel {
    private final StateManager stateManager;
    private int currentBet = 0;
    private GameState state = GameState.BET_PHASE;

    // For chip panel visibility control
    private JPanel chipPanel;
    private JPanel topPanel;
    private JPanel gameAreaPanel;
    private JPanel bettingPanel;

    StyledTextField balanceLabel;
    StyledButton dealButton;
    BetCircle betCircle;
    StyledTextField currentBetLabel;
    CardLayout playerButtons;
    JPanel buttonSwitcherPanel;

    JPanel dealerHandPanel;
    JPanel playerHandPanel;

    List<Card> cards;

    @Override
    public void addNotify() {
        super.addNotify();
        // When this panel is added to the hierarchy, re-add the chip panel.
        SwingUtilities.invokeLater(() -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                JLayeredPane layeredPane = frame.getRootPane().getLayeredPane();
                // Only add if not already added.
                if (chipPanel.getParent() != layeredPane) {
                    layeredPane.add(chipPanel, JLayeredPane.POPUP_LAYER);
                }
                repositionChipPanel();
                chipPanel.setVisible(true);
                frame.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        repositionChipPanel();
                    }
                });
            }
        });
    }

    @Override
    public void removeNotify() {
        // When this panel is removed, also remove the chip panel from the layered pane.
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            JLayeredPane layeredPane = frame.getRootPane().getLayeredPane();
            layeredPane.remove(chipPanel);
            layeredPane.repaint();
        }
        super.removeNotify();
    }

    public BlackjackPanel(StateManager stateManager) {
        this.stateManager = stateManager;
        setLayout(new BorderLayout());
        cards = Shoe.getShoe(4);

        BackgroundPanel background = new BackgroundPanel(BackgroundAsset.BACKGROUND);
        background.setLayout(new BorderLayout());

        // -------------------
        // Create Top Panel
        // -------------------
        topPanel = createTopPanel();

        // -------------------
        // Create Game Area Panel (center)
        // -------------------
        gameAreaPanel = createGameAreaPanel();

        // -------------------
        // Create Betting Panel (bottom)
        // -------------------
        bettingPanel = createBettingPanel();

        // -------------------
        // Create Chip Panel (left)
        // -------------------
        chipPanel = createChipPanel();

        // -------------------
        // Add all subpanels to the background panel
        // -------------------
        background.add(topPanel, BorderLayout.NORTH);
        background.add(gameAreaPanel, BorderLayout.CENTER);
        background.add(bettingPanel, BorderLayout.SOUTH);

        // Finally, add the background panel to this panel.
        add(background, BorderLayout.CENTER);






        // Optional: Hide chip panel when clicking outside it.
        /*this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (chipPanel != null && chipPanel.isVisible()) {
                    Rectangle bounds = SwingUtilities.convertRectangle(chipPanel.getParent(), chipPanel.getBounds(), BlackjackPanel.this);
                    if (!bounds.contains(e.getPoint())) {
                        chipPanel.setVisible(false);
                    }
                }
            }
        });*/
    }

    private JPanel createTopPanel(){
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        StyledButton returnButton = new StyledButton("Return to Main Menu", 250, 50);
        topPanel.add(returnButton, BorderLayout.WEST);
        // Balance label shows the current balance from the state manager's profile.
        balanceLabel = new StyledTextField("Balance: " + stateManager.getProfile().getBalance(), false);
        topPanel.add(balanceLabel, BorderLayout.EAST);
        returnButton.addActionListener(e ->{
            //chipPanel.setVisible(false);
            stateManager.switchPanel("MainMenu");
        });
        return topPanel;
    }

    private JPanel createGameAreaPanel(){
        JPanel gameAreaPanel = new JPanel(new GridBagLayout());
        gameAreaPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Dealer Hand Panel
        gbc.gridy = 0;
        gbc.weighty = 0.3;
        dealerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dealerHandPanel.setOpaque(false);
        dealerHandPanel.setMinimumSize(new Dimension(600, 150));
        dealerHandPanel.setPreferredSize(new Dimension(600, 150));
        gameAreaPanel.add(dealerHandPanel, gbc);

        // Filler panel between dealer and logo
        gbc.gridy = 1;
        gbc.weighty = 0.05;
        JPanel filler1 = new JPanel();
        filler1.setOpaque(false);
        filler1.setPreferredSize(new Dimension(600, 75));
        gameAreaPanel.add(filler1, gbc);

        // Logo / Rules Label
        gbc.gridy = 2;
        gbc.weighty = 0.2;
        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(BackgroundAsset.BLACKJACK_RULES, 600, 230));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameAreaPanel.add(logoLabel, gbc);

        // Deal Button Container
        gbc.gridy = 3;
        gbc.weighty = 0.1;
        JPanel dealButtonContainer = new JPanel(new BorderLayout());
        dealButtonContainer.setOpaque(false);
        dealButtonContainer.setMinimumSize(new Dimension(600, 60));
        //dealButtonContainer.setPreferredSize(new Dimension(600, 60)); // fixed height for the container
        dealButton = new StyledButton("DEAL", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        dealButton.setHorizontalAlignment(SwingConstants.CENTER);
        dealButton.setVisible(false);
        dealButtonContainer.add(dealButton, BorderLayout.CENTER);
        gameAreaPanel.add(dealButtonContainer, gbc);

        // Filler panel between deal button and player hand
        gbc.gridy = 4;
        gbc.weighty = 0.1;
        JPanel filler2 = new JPanel();
        filler2.setOpaque(false);
        filler2.setPreferredSize(new Dimension(600, 40));
        gameAreaPanel.add(filler2, gbc);

        // Player Hand Panel
        gbc.gridy = 5;
        gbc.weighty = 0.35;
        playerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        playerHandPanel.setOpaque(false);
        playerHandPanel.setMinimumSize(new Dimension(600, 150));
        playerHandPanel.setPreferredSize(new Dimension(600, 300));
        gameAreaPanel.add(playerHandPanel, gbc);
        return gameAreaPanel;
    }

    private JPanel createBettingPanel(){
        JPanel bettingPanel = new JPanel();
        bettingPanel.setLayout(new BoxLayout(bettingPanel, BoxLayout.Y_AXIS));
        bettingPanel.setOpaque(false);
        bettingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Bet panel: contains bet circle and current bet label.
        JPanel betPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        betPanel.setOpaque(false);
        betCircle = new BetCircle();
        betPanel.add(betCircle);
        currentBetLabel = new StyledTextField(String.valueOf(currentBet), 150, 50, 9, false);
        betPanel.add(currentBetLabel);
        bettingPanel.add(betPanel);
        bettingPanel.add(Box.createVerticalStrut(20));

        // Create a container that uses CardLayout to switch between clear button and action buttons.
        buttonSwitcherPanel = new JPanel(new CardLayout());
        buttonSwitcherPanel.setOpaque(false);
        // Fix the container size so layout remains constant (adjust dimensions as needed).
        Dimension fixedSize = new Dimension(1000, 60);
        buttonSwitcherPanel.setPreferredSize(fixedSize);
        buttonSwitcherPanel.setMinimumSize(fixedSize);
        buttonSwitcherPanel.setMaximumSize(fixedSize);

        // Create clear button panel.
        JPanel clearButtonPanel = new JPanel(new BorderLayout());
        clearButtonPanel.setOpaque(false);
        StyledButton clearBetButton = new StyledButton("Clear bet");
        clearBetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearBetButton.addActionListener(e -> {
            stateManager.getProfile().increaseBalanceBy(currentBet);
            currentBet = 0;
            dealButton.setVisible(false);
            playerButtons.show(buttonSwitcherPanel, "hide");
            currentBetLabel.setText(String.valueOf(currentBet));
            betCircle.clearChips();
            balanceLabel.setText("Balance: " + stateManager.getProfile().getBalance());
        });
        clearButtonPanel.add(clearBetButton, BorderLayout.CENTER);

        // Create action buttons panel.
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionButtonsPanel.setOpaque(false);
        StyledButton hitButton = new StyledButton("Hit");
        StyledButton standButton = new StyledButton("Stand");
        StyledButton doubleButton = new StyledButton("Double");
        StyledButton splitButton = new StyledButton("Split");
        actionButtonsPanel.add(hitButton);
        actionButtonsPanel.add(standButton);
        actionButtonsPanel.add(doubleButton);
        actionButtonsPanel.add(splitButton);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);

        // Add both panels to the CardLayout container with distinct card names.
        buttonSwitcherPanel.add(clearButtonPanel, "clear");
        buttonSwitcherPanel.add(actionButtonsPanel, "action");
        buttonSwitcherPanel.add(emptyPanel, "hide");

        // Initially, you might want to show the clear button (or whichever you prefer).
        playerButtons = (CardLayout) buttonSwitcherPanel.getLayout();
        playerButtons.show(buttonSwitcherPanel, "hide");

        // Add the card-switcher container to the betting panel.
        bettingPanel.add(buttonSwitcherPanel);

        // Example: When the deal button is pressed, switch to the action buttons.
        dealButton.addActionListener(e -> {
            if (currentBet == 0) {
                new Toast((JFrame) SwingUtilities.getWindowAncestor(this), "No bet placed", 3000).setVisible(true);
                return;
            }
            state = GameState.GAME_STARTED;
            dealButton.setVisible(false);
            chipPanel.setVisible(false);
            dealInitialCards();
            // Hide chip panel, etc., and then switch to the action buttons:
            playerButtons.show(buttonSwitcherPanel, "action");
            // ... additional game logic here.
        });

        return bettingPanel;
    }

    private void dealInitialCards() {
        addDealerCard(cards.remove(0));
        addPlayerCard(cards.remove(0));
        addDealerCard(new Card("BACK", "BACK", CardAsset.BC));
        addPlayerCard(cards.remove(0));
    }

    public void addDealerCard(Card card) {
        dealerHandPanel.add(card);
        dealerHandPanel.revalidate();
        dealerHandPanel.repaint();
    }

    public void addPlayerCard(Card card) {
        playerHandPanel.add(card);
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }



    private JPanel createChipPanel(){
        JPanel chipPanel = new JPanel();
        chipPanel.setLayout(new BoxLayout(chipPanel, BoxLayout.Y_AXIS));
        chipPanel.setOpaque(false);
        for (ChipAsset asset : ChipAsset.values()) {
            if (asset.getValue() <= stateManager.getProfile().getBalance()){
                Chip chip = new Chip(asset);
                chip.addActionListener(e -> {
                    if (chip.getValue() <= stateManager.getProfile().getBalance() && betCircle.getChipsCount() < betCircle.getMaxChips()){
                        betCircle.addChip(chip);
                        currentBet += chip.getValue();
                        currentBetLabel.setText(String.valueOf(currentBet));
                        playerButtons.show(buttonSwitcherPanel, "clear");
                        dealButton.setVisible(true);
                        stateManager.getProfile().decreaseBalanceBy(chip.getValue());
                        balanceLabel.setText("Balance: " + stateManager.getProfile().getBalance());
                    } else {
                        new Toast((JFrame) SwingUtilities.getWindowAncestor(this),
                                "Maximum number of chips reached.", 3000).setVisible(true);
                    }
                });
                chipPanel.add(chip);
            }
        }
        chipPanel.setVisible(true);
        return chipPanel;
    }

    private void repositionChipPanel() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            Dimension pref = chipPanel.getPreferredSize();
            int chipPanelWidth = pref.width;
            int chipPanelHeight = pref.height;
            int yPos = frame.getHeight() - chipPanelHeight - 60;  // 10px margin from bottom
            int xPos = 20; // 10px margin from left
            chipPanel.setBounds(xPos, yPos, chipPanelWidth, chipPanelHeight);
            chipPanel.revalidate();
            chipPanel.repaint();
        }
    }


    enum GameState {
        BET_PHASE,
        GAME_STARTED,
        DEALING_CARDS,
        PLAYER_TURN,
        DEALER_TURN,
        EVALUATION_PHASE,
        DISPLAY_RESULT,
        PAYOUT,
        GAME_ENDED
    }
}
