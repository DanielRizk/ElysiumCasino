package org.daniel.elysium.models;

/**
 * Represents a strategy for determining the value of a card.
 * This allows different games to define their own card value rules.
 */
public interface CardValue {

    /**
     * Calculates and returns the value of a given card based on specific game rules.
     *
     * @param card The card whose value needs to be determined.
     * @return The numerical value of the card according to the implemented strategy.
     */
    int getValue(Card card);
}

