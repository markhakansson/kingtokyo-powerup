package KingTokyo.monsters;

import java.util.Collections;

import KingTokyo.cards.EvolutionCard;
import KingTokyo.effects.*;

public class MonsterKong extends Monster {
    public MonsterKong() {
        super("Kong", MonsterType.MUTANT);
    }

    public void setupEvolutionCards() {
        // Add evolution cards here
        Effect[] redDawn = {new AttackEveryone(2)};
        EvolutionCard redDawnCard = new EvolutionCard("Red Dawn", false, redDawn, Round.IMMEDIATELY, "Deal 2 damage to all others.\n");
    
        // Add to evolution card deck here
        evoCards.add(redDawnCard);

        // Always shuffle the cards in the end
        Collections.shuffle(evoCards);
    }
}