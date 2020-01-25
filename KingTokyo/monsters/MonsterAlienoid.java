package KingTokyo.monsters;

import KingTokyo.effects.*;

import java.util.Collections;

import KingTokyo.cards.*;

public class MonsterAlienoid extends Monster{
    public MonsterAlienoid() {
        super("Aliendoid", MonsterType.ALIEN);
    }

    @Override
    public void setupEvolutionCards() {
        // Add evolution cards here.
        Effect[] alienScourge = {new ReceiveStars(2)};
        EvolutionCard alienScourgeCard = new EvolutionCard("Alien Scourge", false, alienScourge, Round.IMMEDIATELY, "Receive 2 stars.\n");
    
        // Add to deck here
        evoCards.add(alienScourgeCard);

        // Always shuffle the card in the end
        Collections.shuffle(evoCards);
    }
}