package org.daniel.elysium.ultimateTH.pokerCore.models;

import org.daniel.elysium.ultimateTH.constants.UthHandCombination;
import org.daniel.elysium.ultimateTH.model.UthCard;

import java.util.List;

public record PokerEvaluatedHandModel(List<UthCard> cardCombination, UthCard kicker, UthHandCombination handCombination) {
}
