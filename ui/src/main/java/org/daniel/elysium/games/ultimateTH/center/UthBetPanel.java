package org.daniel.elysium.games.ultimateTH.center;

import org.daniel.elysium.games.ultimateTH.constants.UthActions;
import org.daniel.elysium.models.chips.BetCircle;
import org.daniel.elysium.models.chips.Chip;

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

    public void clearAllChips(){
        clearBetChips();
        clearPlayChips();
        clearTripsChips();
    }

    public void clearBetChips(){
        clearAnteChips();
        clearBlindChips();
    }

    public void clearAnteChips(){
        ante.clearChips();
    }

    public void clearBlindChips(){
        blind.clearChips();
    }

    public void clearTripsChips(){
        trips.clearChips();
    }

    public void clearPlayChips(){
        play.clearChips();
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
}
