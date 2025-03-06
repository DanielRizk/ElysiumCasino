package org.daniel.elysium.games.ultimateTH.center.models;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ResultAsset;
import org.daniel.elysium.games.ultimateTH.constants.UthActions;
import org.daniel.elysium.models.chips.BetCircle;
import org.daniel.elysium.models.chips.Chip;
import org.daniel.elysium.ultimateTH.constants.UthHandCombination;
import org.daniel.elysium.ultimateTH.constants.UthHandState;
import org.daniel.elysium.ultimateTH.constants.UthTripsState;
import org.daniel.elysium.ultimateTH.model.UthPlayerHand;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the betting panel for Ultimate Texas Hold'em, handling bet placements,
 * chip management, and result displays.
 * <p>
 * This panel includes Ante, Blind, Play, and Trips bet areas and supports interactions
 * such as chip placement, payout processing, and overlay displays for results.
 * </p>
 */
public class UthBetPanel extends JPanel implements BetCircle.SelectionListener{
    private final UthBetUnit ante;
    private final UthBetUnit blind;
    private final UthBetUnit play;
    private final UthBetUnit trips;
    private BetCircle selectedCircle;
    private Image overlayImage;

    /**
     * Constructs the betting panel, initializing betting areas and UI components.
     */
    public UthBetPanel() {
        setLayout(new GridLayout(2, 3, 0 , 0));
        setOpaque(false);

        Dimension dimension = new Dimension(900, 400);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);

        ante = new UthBetUnit(ComponentOrientation.LEFT_TO_RIGHT, "ANTE");
        blind = new UthBetUnit(ComponentOrientation.RIGHT_TO_LEFT, "BLIND");
        play = new UthBetUnit(ComponentOrientation.LEFT_TO_RIGHT, "PLAY");
        trips = new UthBetUnit(ComponentOrientation.RIGHT_TO_LEFT, "TRIPS");

        ante.getCircle().setSelectionListener(this);
        blind.getCircle().setSelectionListener(this);
        trips.getCircle().setSelectionListener(this);

        add(ante);
        add(createSpacing("="));
        add(blind);
        add(play);
        add(createSpacing(" "));
        add(trips);
    }

    /**
     * Creates a spacing label for visual separation.
     *
     * @param text the text to display in the label
     * @return the created JLabel instance
     */
    private JLabel createSpacing(String text){
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Roboto", Font.BOLD, 100));
        label.setForeground(Color.WHITE);
        Dimension dimension = new Dimension(20, 50);
        label.setMinimumSize(dimension);
        label.setPreferredSize(dimension);
        label.setMaximumSize(dimension);
        return label;
    }

    /* ======================
       Bet Display Updates
       ====================== */

    /**
     * Updates the displayed bet amount for both the Ante and Blind bets.
     *
     * @param bet the updated bet amount to display
     */
    public void updateBetDisplay(int bet){
        ante.updateBetDisplay(bet);
        blind.updateBetDisplay(bet);
    }

    /**
     * Updates the displayed Trips bet amount.
     *
     * @param bet the updated Trips bet amount to display
     */
    public void updateTripsDisplay(int bet){
        trips.updateBetDisplay(bet);
    }

    /**
     * Updates the displayed Play bet amount.
     *
     * @param bet the updated Play bet amount to display
     */
    public void updatePlayDisplay(int bet){
        play.updateBetDisplay(bet);
    }

    /* ======================
       Chip Management
       ====================== */

    /**
     * Adds a chip to the currently selected BetBox.
     *
     * @param chip The chip to be added to the selected bet box.
     */
    public void addChip(Chip chip) {
        if (selectedCircle != null) {
            if (selectedCircle.getLabel().equals("ANTE") || selectedCircle.getLabel().equals("BLIND")){
                ante.addChip(chip);
                blind.addChip(chip);
            } else {
                selectedCircle.addChip(chip);
            }
        }
    }

    /**
     * Adds play chips based on the specified action.
     *
     * @param actions the action determining the multiplier for play chips
     */
    public void addPlayChip(UthActions actions){
        List<Chip> chipList = new ArrayList<>(ante.getChips());
        switch (actions){
            case X4 -> {
                for (int i = 0; i < 4; i++){
                    play.addChips(chipList);
                }
            }
            case X3 -> {
                for (int i = 0; i < 3; i++){
                    play.addChips(chipList);
                }
            }
            case X2 -> {
                for (int i = 0; i < 2; i++){
                    play.addChips(chipList);
                }
            }
            case X1 -> {
                play.addChips(chipList);
            }
        }
    }

    /* ======================
       Clearing Chips
       ====================== */

    /**
     * Clears all Ante bet chips and updates the bet display.
     */
    public void clearAnteChips(){
        ante.clearChips();
        updateBetDisplay(0);
    }

    /**
     * Clears all Blind bet chips and updates the bet display.
     */
    public void clearBlindChips(){
        blind.clearChips();
        updateBetDisplay(0);
    }

    /**
     * Clears all Trips bet chips and updates the Trips bet display.
     */
    public void clearTripsChips(){
        trips.clearChips();
        updateTripsDisplay(0);
    }

    /**
     * Clears all Play bet chips and updates the Play bet display.
     */
    public void clearPlayChips(){
        play.clearChips();
        updatePlayDisplay(0);
    }

    /* ======================
       Selection Handling
       ====================== */

    /**
     * Handles selection of a bet circle, ensuring only one is selected at a time.
     *
     * @param selectedCircle the newly selected bet circle
     */
    @Override
    public void onSelected(BetCircle selectedCircle) {
        // Deselect the previous selection if no chips are placed
        if (this.selectedCircle != null) {
            this.selectedCircle.setSelected(false);
        }

        // Select the new box
        this.selectedCircle = selectedCircle;
        this.selectedCircle.setSelected(true);
    }

    /**
     * Returns the currently selected BetCircle.
     *
     * @return The selected BetCircle or null if no selection has been made.
     */
    public BetCircle getSelectedBet() {
        return selectedCircle;
    }

    /**
     * Resets all selections in the betting area.
     */
    public void resetAllSelections() {
        if (selectedCircle != null) {
            selectedCircle.setSelected(false);
            selectedCircle = null;
        }
    }

    /* ======================
       Selection Handling
       ====================== */

    /**
     * Processes the player's winnings and distributes chips accordingly.
     *
     * @param hand the player's hand containing the bet details
     * @param dealerQualifies {@code true} if the dealer qualifies for the ante payout, otherwise {@code false}
     */
    public void payWin(UthPlayerHand hand, boolean dealerQualifies) {
        List<Chip> playChips = new ArrayList<>(play.getChips());
        for (Chip chip : playChips) {
            play.addChip(chip);
        }

        if (dealerQualifies){
            List<Chip> anteChips = new ArrayList<>(ante.getChips());
            for (Chip chip : anteChips) {
                ante.addChip(chip);
            }
        } else {
            clearAnteChips();
        }

        if (hand.getEvaluatedHand().handCombination().getValue() > 0){
            List<Chip> blindChips = new ArrayList<>(Chip.getChipCombination(hand.getBlind()));
            blind.clearChips();
            for (Chip chip : blindChips) {
                blind.addChip(chip);
            }
        }
    }

    /**
     * Processes and distributes winnings for the Trips side bet.
     *
     * @param hand the player's hand containing the Trips bet details
     */
    public void payTripsWin(UthPlayerHand hand){
        if (hand.getTripsState().getValue() > 0){
            // TODO: double check the trips payout, ie should have been 4 chips, got 2, example on phone
            List<Chip> tripsChips = new ArrayList<>(Chip.getChipCombination((hand.getTrips())));
            trips.clearChips();
            for (Chip chip : tripsChips) {
                trips.addChip(chip);
            }
        }
    }

    /* ======================
       Results display
       ====================== */

    /**
     * Displays the blind bet multiplier based on the player's hand combination.
     *
     * @param combination the {@code UthHandCombination} determining the blind multiplier
     */
    public void displayBlindMultiplier(UthHandCombination combination){
        Dimension imageSize = new Dimension(50, 50);
        ResultAsset asset = switch (combination) {
            case ROYAL_FLUSH -> ResultAsset.X500;
            case STRAIGHT_FLUSH -> ResultAsset.X50;
            case QUADS -> ResultAsset.X10;
            case FULL_HOUSE -> ResultAsset.X3;
            case FLUSH -> ResultAsset.X1_5;
            case STRAIGHT -> ResultAsset.X1;
            default -> null;
        };
        blind.showOverlay(AssetManager.getScaledImage(asset, imageSize));
    }

    /**
     * Displays the Trips bet multiplier based on the player's Trips hand state.
     *
     * @param state the {@code UthTripsState} representing the Trips bet result
     */
    public void displayTripsMultiplier(UthTripsState state){
        Dimension imageSize = new Dimension(50, 50);
        ResultAsset asset = switch (state) {
            case ROYAL_FLUSH -> ResultAsset.X50;
            case STRAIGHT_FLUSH -> ResultAsset.X40;
            case QUADS -> ResultAsset.X30;
            case FULL_HOUSE -> ResultAsset.X8;
            case FLUSH -> ResultAsset.X7;
            case STRAIGHT -> ResultAsset.X4;
            case Trips -> ResultAsset.X3;
            default -> null;
        };
        trips.showOverlay(AssetManager.getScaledImage(asset, imageSize));
    }

    /**
     * Displays the Trips bet ante state based on the dealer's hand combination.
     *
     * @param combination the {@code UthHandCombination} representing the Trips bet result
     */
    public void displayAnteState(UthHandCombination combination){
        Dimension imageSize = new Dimension(100, 70);
        if (combination == UthHandCombination.HIGH_CARD){
            ante.showOverlay(AssetManager.getScaledImage(ResultAsset.PUSH, imageSize));
        }
    }

    /**
     * Displays the hand result image based on the hand combination.
     */
    public void displayHandResult(UthHandState state){
        Dimension imageSize = new Dimension(300, 200);
        ResultAsset asset = switch (state){
            case WON ->  ResultAsset.WIN;
            case TIE -> ResultAsset.TIE;
            case LOST -> ResultAsset.LOST;
            case FOLD -> ResultAsset.FOLD;
            case UNDEFINED -> null;
        };
        showOverlay(AssetManager.getScaledImage(asset, imageSize));
    }

    /**
     * Displays an overlay image for 3 seconds.
     * The overlay appears on top of the cards and disappears automatically.
     *
     * @param image The image to overlay on the panel.
     */
    private void showOverlay(Image image) {
        this.overlayImage = image;
        repaint();

        Timer timer = new Timer(3000, e -> {
            overlayImage = null;
            repaint();
            ((Timer) e.getSource()).stop(); // Stop the timer after execution
        });

        timer.setRepeats(false);
        timer.start();
    }

    /* ======================
           Rendering Methods
       ====================== */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        // Draw overlay image on top of the cards
        if (overlayImage != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelHeight = getHeight();
            int iw = overlayImage.getWidth(this);
            int ih = overlayImage.getHeight(this);
            int x = (getWidth() - iw) / 2;

            // Use a more precise way to center vertically
            int y = (panelHeight - ih) / 2;

            g2.drawImage(overlayImage, x, y, this);
            g2.dispose();
        }
    }
}
