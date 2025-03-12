package org.daniel.elysium.games;

import org.daniel.elysium.assets.CardAsset;
import org.daniel.elysium.games.baccarat.models.BacCardUI;
import org.daniel.elysium.games.blackjack.models.BJCardUI;
import org.daniel.elysium.games.ultimateTH.models.UthCardUI;
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
     * @return a {@code List<UICard>} representing a predefined Blackjack deck
     */
    public static List<UICard> getCustomBJDeck() {
        List<UICard> cards = new ArrayList<>();
        cards.add(new BJCardUI("8", "S", CardAsset.S8));
        cards.add(new BJCardUI("10", "H", CardAsset.H10));
        cards.add(new BJCardUI("8", "S", CardAsset.S8));
        cards.add(new BJCardUI("A", "S", CardAsset.SA));
        cards.add(new BJCardUI("10", "C", CardAsset.C10));
        cards.add(new BJCardUI("9", "H", CardAsset.H9));
        return cards;
    }

    /**
     * Returns a custom Baccarat deck containing a specific set of cards.
     * This deck can be used to test or debug gameplay involving
     * predetermined hands in a Baccarat game.
     *
     * @return a {@code List<UICard>} representing a predefined Baccarat deck
     */
    public static List<UICard> getCustomBacDeck() {
        List<UICard> cards = new ArrayList<>();
        cards.add(new BacCardUI("10", "S", CardAsset.S10));
        cards.add(new BacCardUI("7", "S", CardAsset.S7));
        cards.add(new BacCardUI("7", "H", CardAsset.H7));
        cards.add(new BacCardUI("Q", "S", CardAsset.SQ));
        cards.add(new BacCardUI("10", "C", CardAsset.C10));
        cards.add(new BacCardUI("2", "H", CardAsset.H2));
        return cards;
    }

    /**
     * Returns a custom Ultimate Texas Hold'em (UTH) deck containing
     * a specific set of cards. This deck is useful for reproducing
     * test scenarios that require a known sequence of UTH cards.
     *
     * @return a {@code List<UICard>} representing a predefined UTH deck
     */
    public static List<UICard> getCustomUTHDeck() {
        List<UICard> cards = new ArrayList<>();
        cards.add(new UthCardUI("K", "H", CardAsset.HK));
        cards.add(new UthCardUI("3", "H", CardAsset.H3));
        cards.add(new UthCardUI("4", "D", CardAsset.D4));
        cards.add(new UthCardUI("J", "S", CardAsset.SJ));
        cards.add(new UthCardUI("7", "S", CardAsset.S7));
        cards.add(new UthCardUI("7", "D", CardAsset.D7));
        cards.add(new UthCardUI("5", "D", CardAsset.D5));
        return cards;
    }
}
