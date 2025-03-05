package org.daniel.elysium.games.ultimateTH.center;

import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
import org.daniel.elysium.ultimateTH.model.UthCard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UthCommunityCardsPanel extends JPanel {
    private final UthCardHolderPanel flop1;
    private final UthCardHolderPanel flop2;
    private final UthCardHolderPanel flop3;
    private final UthCardHolderPanel turn;
    private final UthCardHolderPanel river;

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

    public void addFlop1(UthCardUI uthCardUI){
        uthCardUI.setFaceDown();
        flop1.add(uthCardUI);
    }

    public void addFlop2(UthCardUI uthCardUI){
        uthCardUI.setFaceDown();
        flop2.add(uthCardUI);
    }

    public void addFlop3(UthCardUI uthCardUI){
        uthCardUI.setFaceDown();
        flop3.add(uthCardUI);
    }

    public void addTurn(UthCardUI uthCardUI){
        uthCardUI.setFaceDown();
        turn.add(uthCardUI);
    }

    public void addRiver(UthCardUI uthCardUI){
        uthCardUI.setFaceDown();
        river.add(uthCardUI);
    }

    public void exposeFlop(){
        if (getComponents().length < 3){
            return;
        }

        for (int i = 0; i < 3; i++){
            if (getComponent(i) instanceof UthCardHolderPanel holderPanel){
                holderPanel.getCard().setFaceUp();
            }
        }
    }

    public void exposeTurnAndRiver(){
        if (getComponents().length < 5){
            return;
        }

        if (getComponent(3) instanceof UthCardHolderPanel holderPanel){
            holderPanel.getCard().setFaceUp();
        }

        if (getComponent(4) instanceof UthCardHolderPanel holderPanel){
            holderPanel.getCard().setFaceUp();
        }
    }

    public void exposeAll(){
        exposeFlop();
        exposeTurnAndRiver();
    }

    public List<UthCard> getCards(){
        List<UthCard> cards = new ArrayList<>();
        for (Component component : getComponents()){
            if (component instanceof UthCardHolderPanel holderPanel){
                cards.add(holderPanel.getCard().getCard());
            }
        }
        return cards;
    }

    public void removeCards(){
        for (Component component : getComponents()){
            if (component instanceof UthCardHolderPanel holderPanel){
                holderPanel.removeCard();
            }
        }
    }
}
