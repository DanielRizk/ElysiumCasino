package org.daniel.elysium.games.blackjack.constants;

/**
 * Enum representing the different states of a Blackjack game.
 */
public enum BJGameState {
    /** The phase where players place their bets before the game starts. */
    BET_PHASE,

    /** The game has started, and initial preparations are complete. */
    GAME_STARTED,

    /** The phase where cards are being dealt to the players and dealer. */
    DEALING_CARDS,

    /** The phase where the player takes actions such as Hit, Stand, Double, or Split. */
    PLAYER_TURN,

    /** The phase where the dealer plays according to the game rules. */
    DEALER_TURN,

    /** The phase where the results of the hands are evaluated. */
    EVALUATION_PHASE,

    /** The phase where the game displays the result of the round. */
    DISPLAY_RESULT,

    /** The phase where winnings and losses are processed. */
    PAYOUT,

    /** The game has ended, and a new round can be started. */
    GAME_ENDED
}
