package KingTokyo.cards;

import KingTokyo.effects.*;
import KingTokyo.game.*;
import KingTokyo.server.Server;

public class EvolutionCard extends Card {
    public boolean permanent;

    public EvolutionCard(String name, boolean permanent, Effect[] effect, Round whenToUse, String description) {
        super(name, 0, true, effect, whenToUse, description);
        this.permanent = permanent;
    }

    @Override
    public void useCard(GameState state) {
        if(!inUse) {
            if(discard) {
                inUse = true;
            }
            for(Effect effect : this.getEffects()) {
                effect.execute(state, this);
            }
        }
    }
}