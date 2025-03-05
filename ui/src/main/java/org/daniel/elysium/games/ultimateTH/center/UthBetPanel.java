package org.daniel.elysium.games.ultimateTH.center;

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

public class UthBetPanel extends JPanel implements BetCircle.SelectionListener{
    private final UthBetUnit ante;
    private final UthBetUnit blind;
    private final UthBetUnit play;
    private final UthBetUnit trips;
    private BetCircle selectedCircle;
    private Image overlayImage;

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

    public void updateBetDisplay(int bet){
        ante.updateBetDisplay(bet);
        blind.updateBetDisplay(bet);
    }

    public void updateTripsDisplay(int bet){
        trips.updateBetDisplay(bet);
    }

    public void updatePlayDisplay(int bet){
        play.updateBetDisplay(bet);
    }

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

    public void clearAnteChips(){
        ante.clearChips();
        updateBetDisplay(0);
    }

    public void clearBlindChips(){
        blind.clearChips();
        updateBetDisplay(0);
    }

    public void clearTripsChips(){
        trips.clearChips();
        updateTripsDisplay(0);
    }

    public void clearPlayChips(){
        play.clearChips();
        updatePlayDisplay(0);
    }


    /**
     * Handles the event when a BetBox is selected. Ensures only one BetBox is selected at a time if no chips have been placed yet.
     *
     * @param selectedCircle The BetBox that was just selected.
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

    public void payTripsWin(UthPlayerHand hand){
        if (hand.getTripsState().getValue() > 0){
            List<Chip> tripsChips = new ArrayList<>(Chip.getChipCombination((hand.getTrips())));
            trips.clearChips();
            for (Chip chip : tripsChips) {
                trips.addChip(chip);
            }
        }
    }

    public void displayBlindMultiplier(UthHandCombination combination){
        switch (combination){
            case ROYAL_FLUSH -> blind.showOverlay(AssetManager.getScaledImage(ResultAsset.X500, new Dimension(50, 50)));
            case STRAIGHT_FLUSH -> blind.showOverlay(AssetManager.getScaledImage(ResultAsset.X50, new Dimension(50, 50)));
            case QUADS -> blind.showOverlay(AssetManager.getScaledImage(ResultAsset.X10, new Dimension(50, 50)));
            case FULL_HOUSE -> blind.showOverlay(AssetManager.getScaledImage(ResultAsset.X3, new Dimension(50, 50)));
            case FLUSH -> blind.showOverlay(AssetManager.getScaledImage(ResultAsset.X1_5, new Dimension(50, 50)));
            case STRAIGHT -> blind.showOverlay(AssetManager.getScaledImage(ResultAsset.X1, new Dimension(50, 50)));
        }
    }

    public void displayTripsMultiplier(UthTripsState state){
        switch (state){
            case ROYAL_FLUSH -> trips.showOverlay(AssetManager.getScaledImage(ResultAsset.X50, new Dimension(50, 50)));
            case STRAIGHT_FLUSH -> trips.showOverlay(AssetManager.getScaledImage(ResultAsset.X40, new Dimension(50, 50)));
            case QUADS -> trips.showOverlay(AssetManager.getScaledImage(ResultAsset.X30, new Dimension(50, 50)));
            case FULL_HOUSE -> trips.showOverlay(AssetManager.getScaledImage(ResultAsset.X8, new Dimension(50, 50)));
            case FLUSH -> trips.showOverlay(AssetManager.getScaledImage(ResultAsset.X7, new Dimension(50, 50)));
            case STRAIGHT -> trips.showOverlay(AssetManager.getScaledImage(ResultAsset.X4, new Dimension(500, 50)));
            case Trips -> trips.showOverlay(AssetManager.getScaledImage(ResultAsset.X3, new Dimension(50, 50)));
        }
    }


    /**
     * Displays the hand result image based on the hand combination.
     */
    public void displayHandResult(UthHandState state){
        switch (state){
            case WON -> showOverlay(AssetManager.getScaledImage(ResultAsset.WIN, new Dimension(300, 200)));
            case TIE -> showOverlay(AssetManager.getScaledImage(ResultAsset.TIE, new Dimension(300, 200)));
            case LOST -> showOverlay(AssetManager.getScaledImage(ResultAsset.LOST, new Dimension(300, 200)));
            case FOLD -> showOverlay(AssetManager.getScaledImage(ResultAsset.FOLD, new Dimension(300, 200)));
        }
    }

    /**
     * Displays an overlay image (e.g., a trophy or result notification) for 3 seconds.
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
