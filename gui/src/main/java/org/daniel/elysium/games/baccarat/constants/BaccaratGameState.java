package org.daniel.elysium.games.baccarat.constants;

/**
 * Enum representing the different states of a Baccarat game.
 */
public enum BaccaratGameState {
    /** The phase where players place their bets before the game starts. */
    BET_PHASE,

    /** The game has started, and initial preparations are complete. */
    GAME_STARTED,

    /** The phase where cards are being dealt to the players and banker. */
    DEALING_CARDS,

    /** The phase where the results of the hands are evaluated. */
    EVALUATION_PHASE,

    /** The phase where the game displays the result of the round. */
    DISPLAY_RESULT,

    /** The phase where winnings and losses are processed. */
    PAYOUT,

    /** The game has ended, and a new round can be started. */
    GAME_ENDED
}
