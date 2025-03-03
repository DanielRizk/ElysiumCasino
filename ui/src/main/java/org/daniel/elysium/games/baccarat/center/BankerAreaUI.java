package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.ResultAsset;
import org.daniel.elysium.baccarat.BacHand;
import org.daniel.elysium.baccarat.BacHandState;
import org.daniel.elysium.models.cards.UICard;

import javax.swing.*;
import java.awt.*;

public class BankerAreaUI extends JPanel {
    private final BankerCardsUI cardsUI;
    public BacHand hand;

    public BankerAreaUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // Ensure components are centered
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setPreferredSize(new Dimension(500, 400));

        BankerTextUI textUI = new BankerTextUI();
        textUI.setAlignmentX(CENTER_ALIGNMENT);

        cardsUI = new BankerCardsUI();
        cardsUI.setAlignmentX(CENTER_ALIGNMENT);

        add(Box.createVerticalGlue()); // Push content towards center
        add(textUI);
        add(Box.createRigidArea(new Dimension(0, 30))); // Space between text & cards
        add(cardsUI);
        add(Box.createVerticalGlue()); // Push content towards center

        this.hand = new BacHand();
    }

    public void addCard(BacCardUI card){
        cardsUI.addCard(card);
        hand.dealCard(card.getCard());
    }

    public BacHand getHand(){
        return hand;
    }

    public void removeCards(){
        cardsUI.removeCards();
        hand = new BacHand();
    }

    public void showOverlay(){
        if (hand.getState() == BacHandState.WON){
            cardsUI.showOverlay(AssetManager.getScaledImage(ResultAsset.WON, new Dimension(300, 200)));
        } else if (hand.getState() == BacHandState.LOST){
            cardsUI.showOverlay(AssetManager.getScaledImage(ResultAsset.LOST, new Dimension(300, 200)));
        } else if (hand.getState() == BacHandState.TIE){
            cardsUI.showOverlay(AssetManager.getScaledImage(ResultAsset.TIE, new Dimension(300, 200)));
        }
    }
}
