package KingTokyo.effects;

import KingTokyo.game.*;
import KingTokyo.cards.*;

public class ReceiveStars extends Effect {
    public ReceiveStars(int stars) {
        super(stars);
    }

    /**
     * Adds stars to the current monster in the game state.
     * @param state The state to affect.
     * @param card The card this effect belongs to.
     */
    @Override
    public void execute(GameState state, Card card) {
        state.currentMonster.addStars(getEffectValue());
    }

}