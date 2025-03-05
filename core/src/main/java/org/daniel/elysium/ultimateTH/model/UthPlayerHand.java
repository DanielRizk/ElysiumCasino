package org.daniel.elysium.ultimateTH.model;

import org.daniel.elysium.ultimateTH.constants.UthTripsState;

public class UthPlayerHand extends UthHand{
    private int ante = 0;
    private int blind = 0;
    private int play = 0;
    private int trips = 0;

    private UthTripsState tripsState = UthTripsState.UNDEFINED;

    public void setBet(int bet){
        this.ante = bet;
        this.blind = bet;
    }

    public int getAnte() {
        return ante;
    }

    public int getBlind() {
        return blind;
    }

    public void setAnte(int ante) {
        this.ante = ante;
    }

    public void setBlind(int blind) {
        this.blind = blind;
    }

    public int getPlay() {
        return play;
    }

    public int getTrips() {
        return trips;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public void setTrips(int trips) {
        this.trips = trips;
    }

    public UthTripsState getTripsState() {
        return tripsState;
    }

    public void setTripsState(UthTripsState tripsState) {
        this.tripsState = tripsState;
    }
}
