package org.daniel.elysium.ultimateTH.model;

import org.daniel.elysium.ultimateTH.constants.UthTripsState;

/**
 * Represents a player's hand in Ultimate Texas Hold'em.
 * <p>
 * This class extends {@code UthHand} and manages the player's bets, including Ante, Blind,
 * Play, and Trips bets, as well as the Trips bet state.
 * </p>
 */
public class UthPlayerHand extends UthHand {
    private int ante = 0;
    private int blind = 0;
    private int play = 0;
    private int trips = 0;

    private UthTripsState tripsState = UthTripsState.UNDEFINED;

    /**
     * Sets the Ante and Blind bets to the specified amount.
     *
     * @param bet the bet amount for both Ante and Blind
     */
    public void setBet(int bet) {
        this.ante = bet;
        this.blind = bet;
    }

    /**
     * Retrieves the Ante bet amount.
     *
     * @return the Ante bet amount
     */
    public int getAnte() {
        return ante;
    }

    /**
     * Retrieves the Blind bet amount.
     *
     * @return the Blind bet amount
     */
    public int getBlind() {
        return blind;
    }

    /**
     * Sets the Ante bet amount.
     *
     * @param ante the new Ante bet amount
     */
    public void setAnte(int ante) {
        this.ante = ante;
    }

    /**
     * Sets the Blind bet amount.
     *
     * @param blind the new Blind bet amount
     */
    public void setBlind(int blind) {
        this.blind = blind;
    }

    /**
     * Retrieves the Play bet amount.
     *
     * @return the Play bet amount
     */
    public int getPlay() {
        return play;
    }

    /**
     * Retrieves the Trips bet amount.
     *
     * @return the Trips bet amount
     */
    public int getTrips() {
        return trips;
    }

    /**
     * Sets the Play bet amount.
     *
     * @param play the new Play bet amount
     */
    public void setPlay(int play) {
        this.play = play;
    }

    /**
     * Sets the Trips bet amount.
     *
     * @param trips the new Trips bet amount
     */
    public void setTrips(int trips) {
        this.trips = trips;
    }

    /**
     * Retrieves the current state of the Trips bet.
     *
     * @return the {@code UthTripsState} representing the Trips bet outcome
     */
    public UthTripsState getTripsState() {
        return tripsState;
    }

    /**
     * Sets the state of the Trips bet.
     *
     * @param tripsState the new state of the Trips bet
     */
    public void setTripsState(UthTripsState tripsState) {
        this.tripsState = tripsState;
    }
}

