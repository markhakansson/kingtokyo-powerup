package KingTokyo.effects;

import KingTokyo.game.*;
import KingTokyo.cards.*;

public class CardsCostLess extends Effect {
    public CardsCostLess(int lessEnergy) {
        super(lessEnergy);
    }

    /**
     * Increments the ´cardsCostLess´ variable in the current GameState.
     * @param state The state to affect.
     * @param card The card this effect belongs.
     */
    @Override
    public void execute(GameState state, Card card) {
        state.cardsCostLess += getEffectValue();
    }

}