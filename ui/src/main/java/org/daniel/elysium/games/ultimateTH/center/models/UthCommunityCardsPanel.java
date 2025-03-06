package org.daniel.elysium.games.ultimateTH.center.models;

import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
import org.daniel.elysium.ultimateTH.model.UthCard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the community card area in Ultimate Texas Hold'em.
 * <p>
 * This panel manages five community card slots (Flop, Turn, and River),
 * allowing cards to be added, revealed, retrieved, and removed.
 * </p>
 */
public class UthCommunityCardsPanel extends JPanel {
    private final UthCardHolderPanel flop1;
    private final UthCardHolderPanel flop2;
    private final UthCardHolderPanel flop3;
    private final UthCardHolderPanel turn;
    private final UthCardHolderPanel river;

    /**
     * Constructs a community card panel with five card slots.
     */
    public UthCommunityCardsPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setOpaque(false);

        flop1 = new UthCardHolderPanel();
        flop2 = new UthCardHolderPanel();
        flop3 = new UthCardHolderPanel();
        turn = new UthCardHolderPanel();
        river = new UthCardHolderPanel();

        add(flop1);
        add(flop2);
        add(flop3);
        add(turn);
        add(river);
    }

    /* ======================
       Cards management
       ====================== */

    /**
     * Adds the first flop card in a face-down position.
     *
     * @param uthCardUI the card to be added
     */
    public void addFlop1(UthCardUI uthCardUI) {
        uthCardUI.setFaceDown();
        flop1.add(uthCardUI);
    }

    /**
     * Adds the second flop card in a face-down position.
     *
     * @param uthCardUI the card to be added
     */
    public void addFlop2(UthCardUI uthCardUI) {
        uthCardUI.setFaceDown();
        flop2.add(uthCardUI);
    }

    /**
     * Adds the third flop card in a face-down position.
     *
     * @param uthCardUI the card to be added
     */
    public void addFlop3(UthCardUI uthCardUI) {
        uthCardUI.setFaceDown();
        flop3.add(uthCardUI);
    }

    /**
     * Adds the turn card in a face-down position.
     *
     * @param uthCardUI the card to be added
     */
    public void addTurn(UthCardUI uthCardUI) {
        uthCardUI.setFaceDown();
        turn.add(uthCardUI);
    }

    /**
     * Adds the river card in a face-down position.
     *
     * @param uthCardUI the card to be added
     */
    public void addRiver(UthCardUI uthCardUI) {
        uthCardUI.setFaceDown();
        river.add(uthCardUI);
    }
    /**
     * Retrieves all community cards as a list.
     *
     * @return a list of {@code UthCard} representing the community cards
     */
    public List<UthCard> getCards() {
        List<UthCard> cards = new ArrayList<>();
        for (Component component : getComponents()) {
            if (component instanceof UthCardHolderPanel holderPanel) {
                cards.add(holderPanel.getCard().getCard());
            }
        }
        return cards;
    }

    /**
     * Removes all cards from the community card panel.
     */
    public void removeCards() {
        for (Component component : getComponents()) {
            if (component instanceof UthCardHolderPanel holderPanel) {
                holderPanel.removeCard();
            }
        }
    }

    /* ======================
       Exposing Cards
       ====================== */

    /**
     * Reveals the three flop cards by turning them face-up.
     */
    public void exposeFlop() {
        if (getComponents().length < 3) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            if (getComponent(i) instanceof UthCardHolderPanel holderPanel) {
                holderPanel.getCard().setFaceUp();
            }
        }
    }

    /**
     * Reveals the turn and river cards by turning them face-up.
     */
    public void exposeTurnAndRiver() {
        if (getComponents().length < 5) {
            return;
        }

        if (getComponent(3) instanceof UthCardHolderPanel holderPanel) {
            holderPanel.getCard().setFaceUp();
        }

        if (getComponent(4) instanceof UthCardHolderPanel holderPanel) {
            holderPanel.getCard().setFaceUp();
        }
    }

    /**
     * Reveals all community cards (Flop, Turn, and River).
     */
    public void exposeAll() {
        exposeFlop();
        exposeTurnAndRiver();
    }
}
