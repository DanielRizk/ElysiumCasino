package org.daniel.elysium.ultimateTH.pokerCore.models;

import org.daniel.elysium.ultimateTH.constants.UthHandCombination;
import org.daniel.elysium.ultimateTH.model.UthCard;

import java.util.List;

/**
 * Represents the evaluated poker hand model in Ultimate Texas Hold'em.
 * <p>
 * This record stores the best five-card combination, the kicker card (if applicable),
 * and the final hand ranking.
 * </p>
 *
 * @param cardCombination the best five-card hand selected from the player's and community cards
 * @param kicker the highest-ranked kicker card used in tie-breaking situations
 * @param handCombination the evaluated hand combination (e.g., Straight, Flush, Full House)
 */
public record PokerEvaluatedHandModel(
        List<UthCard> cardCombination,
        UthCard kicker,
        UthHandCombination handCombination
) {
}