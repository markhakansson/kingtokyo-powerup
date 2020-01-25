package KingTokyo.effects;

import KingTokyo.game.*;
import KingTokyo.cards.*;

public class StarsWhenAttacking extends Effect {
    public StarsWhenAttacking(int stars) {
        super(stars);
    }

    /**
     * Adds stars to the `starsWhenAttacking` variable in the game state.
     * @param state The state to affect.
     * @param card The card this effect belongs to.
     */
    public void execute(GameState state, Card card) {
        state.starsWhenAttacking += getEffectValue();
    }

}