package KingTokyo.monsters;

import java.util.Collections;

import KingTokyo.effects.*;
import KingTokyo.cards.*;

public class MonsterGigazaur extends Monster {
    public MonsterGigazaur() {
        super("Gigazaur", MonsterType.MUTANT);
    }

    public void setupEvolutionCards() {
        // Add evolution cards here
        Effect[] radioactive = {new ReceiveHealth(1), new ReceiveEnergy(2)};
        EvolutionCard radioactiveCard = new EvolutionCard("Radioactive Waste", false, radioactive, Round.IMMEDIATELY, "Gain 2 ENERGY and 1 HEART.\n");
        
        // Add cards to deck here
        evoCards.add(radioactiveCard);

        // Always shuffle the card in the end
        Collections.shuffle(evoCards);
    }
}