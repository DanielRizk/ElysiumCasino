package org.daniel.elysium.models;

public class BJCard extends Card implements CardValue{

    public BJCard(String rank, String suit) {
        super(rank, suit);
    }

    public int getValue() {
        String rank = getRank();
        if (rank.equals("A")) {
            return 11;
        } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
            return 10;
        } else {
            return Integer.parseInt(rank);
        }
    }

    @Override
    public int getValue(Card card) {
        String rank = card.getRank();
        if (rank.equals("A")) {
            return 11;
        } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
            return 10;
        } else {
            return Integer.parseInt(rank);
        }
    }
}
