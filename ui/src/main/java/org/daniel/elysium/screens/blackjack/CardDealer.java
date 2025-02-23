package org.daniel.elysium.screens.blackjack;

import org.daniel.elysium.assets.AssetTemp;
import org.daniel.elysium.assets.AssetManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardDealer {
    private GameAreaPanel gameAreaPanel;

    public CardDealer(GameAreaPanel gameAreaPanel) {
        this.gameAreaPanel = gameAreaPanel;
    }

    public void dealCards() {
        // Clear previous cards if any.
        gameAreaPanel.clearCards();
        Timer timer = new Timer(500, null); // 500ms delay between cards
        final int[] step = {0};
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                switch (step[0]) {
                    case 0:
                        // Dealer card 1: face down
                        gameAreaPanel.addDealerCard(AssetManager.getScaledIcon(AssetTemp.CARD_BACK, 100, 150));
                        break;
                    case 1:
                        // Player card 1: face up
                        gameAreaPanel.addPlayerCard(AssetManager.getScaledIcon(AssetTemp.CARD_FRONT, 100, 150));
                        break;
                    case 2:
                        // Dealer card 2: face up
                        gameAreaPanel.addDealerCard(AssetManager.getScaledIcon(AssetTemp.CARD_FRONT, 100, 150));
                        break;
                    case 3:
                        // Player card 2: face up
                        gameAreaPanel.addPlayerCard(AssetManager.getScaledIcon(AssetTemp.CARD_FRONT, 100, 150));
                        break;
                    default:
                        ((Timer) evt.getSource()).stop();
                        break;
                }
                step[0]++;
            }
        });
        timer.start();
    }
}
