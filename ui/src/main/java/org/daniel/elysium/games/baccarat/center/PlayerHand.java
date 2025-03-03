package org.daniel.elysium.games.baccarat.center;

import org.daniel.elysium.baccarat.HandType;

public class PlayerHand {
    private int bet = 0;
    private HandType type;

    public PlayerHand() {
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet += bet;
    }

    public HandType getType() {
        return type;
    }

    public void setType(HandType type) {
        this.type = type;
    }
}
