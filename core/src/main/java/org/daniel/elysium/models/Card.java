package org.daniel.elysium.models;

public class Card {
    private final String rank;
    private final String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;

    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return 0;
    }

    public int getValue(CardValue cardType) {
        return cardType.getValue(this);
    }

    @Override
    public String toString() {
        return rank + suit;
    }
}
