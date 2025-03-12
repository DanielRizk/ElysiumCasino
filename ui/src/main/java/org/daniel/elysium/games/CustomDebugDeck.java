package org.daniel.elysium.games;

import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.games.baccarat.models.BacCardUI;
import org.daniel.elysium.games.blackjack.models.BJCardUI;
import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
import org.daniel.elysium.models.Card;
import org.daniel.elysium.models.cards.UICard;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class providing custom debug decks for various card games.
 * Each method returns a predetermined list of cards (UICard objects)
 * meant to facilitate testing or debugging specific scenarios.
 */
public class CustomDebugDeck {

    /**
     * Returns a custom Blackjack deck containing a specific set of cards.
     * This deck can be used to reproduce certain test scenarios
     * (e.g., specific hands) in a Blackjack game.
     *
     * @return a {@code List<Card>} representing a predefined Blackjack deck
     */
    public static List<Card> getCustomBJDeck() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("8", "S"));
        cards.add(new Card("2", "H"));
        cards.add(new Card("8", "S"));
        cards.add(new Card("A", "S"));
        cards.add(new Card("2", "C"));
        cards.add(new Card("9", "H"));
        return cards;
    }

    /**
     * Returns a custom Baccarat deck containing a specific set of cards.
     * This deck can be used to test or debug gameplay involving
     * predetermined hands in a Baccarat game.
     *
     * @return a {@code List<Card>} representing a predefined Baccarat deck
     */
    public static List<Card> getCustomBacDeck() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("9", "S"));
        cards.add(new Card("7", "S"));
        cards.add(new Card("7", "H"));
        cards.add(new Card("Q", "S"));
        cards.add(new Card("9", "C"));
        cards.add(new Card("2", "H"));
        return cards;
    }

    /**
     * Returns a custom Ultimate Texas Hold'em (UTH) deck containing
     * a specific set of cards. This deck is useful for reproducing
     * test scenarios that require a known sequence of UTH cards.
     *
     * @return a {@code List<Card>} representing a predefined UTH deck
     */
    public static List<Card> getCustomUTHDeck() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("K", "C"));
        cards.add(new Card("A", "D"));
        cards.add(new Card("A", "H"));
        cards.add(new Card("3", "S"));
        cards.add(new Card("6", "S"));
        cards.add(new Card("A", "C"));
        cards.add(new Card("9", "C"));
        cards.add(new Card("J", "H"));
        cards.add(new Card("K", "H"));
        return cards;
    }
}
